package me.byadolife.prisonbreak;

import me.byadolife.prisonbreak.PrisonBreak;
import me.byadolife.prisonbreak.models.GamePlayer;
import me.byadolife.prisonbreak.models.GameRole;
import me.byadolife.prisonbreak.models.GameState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.*;

/**
 * Ana oyun mantığını yöneten sınıf.
 * Jailbreak mekaniği: Mahkumlar → Kaçış Noktası | Polisler → Yakala
 */
public class GameManager {

    private final PrisonBreak plugin;
    private final LocationManager locationManager;

    // ─── State ───────────────────────────────────────────────────────────────

    private GameState state = GameState.WAITING;
    private final Map<UUID, GamePlayer> players = new HashMap<>();
    private int totalEscapes = 0;

    // ─── Tasks ───────────────────────────────────────────────────────────────

    private BukkitTask countdownTask;
    private BukkitTask gameTimerTask;
    private BukkitTask hudTask;
    private BukkitTask escapeCheckTask;
    private int countdownSeconds;
    private int remainingSeconds;

    // ─── Config cache ────────────────────────────────────────────────────────

    private final int minPlayers;
    private final int maxPlayers;
    private final int gameDuration;
    private final int countdownDuration;
    private final int escapesToWin;
    private final int escapeReward;
    private final int arrestReward;
    private final double arrestRadius;
    private final long arrestCooldownMs;
    private final int respawnDelay;

    public GameManager(PrisonBreak plugin, LocationManager locationManager) {
        this.plugin = plugin;
        this.locationManager = locationManager;

        // Load config values
        this.minPlayers       = plugin.getConfig().getInt("game.min-players", 2);
        this.maxPlayers       = plugin.getConfig().getInt("game.max-players", 20);
        this.gameDuration     = plugin.getConfig().getInt("game.game-duration", 600);
        this.countdownDuration= plugin.getConfig().getInt("game.countdown", 30);
        this.escapesToWin     = plugin.getConfig().getInt("game.escapes-to-win", 5);
        this.escapeReward     = plugin.getConfig().getInt("game.escape-reward", 500);
        this.arrestReward     = plugin.getConfig().getInt("game.arrest-reward", 200);
        this.arrestRadius     = plugin.getConfig().getDouble("arrest.arrest-radius", 2.5);
        this.arrestCooldownMs = plugin.getConfig().getInt("arrest.arrest-cooldown", 3) * 1000L;
        this.respawnDelay     = plugin.getConfig().getInt("respawn.respawn-delay", 3);
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  JOIN / LEAVE
    // ═══════════════════════════════════════════════════════════════════════

    public boolean joinGame(Player player) {
        if (players.containsKey(player.getUniqueId())) {
            player.sendMessage(colorize(plugin.getConfig().getString("messages.prefix")
                    + plugin.getConfig().getString("messages.already-in-game")));
            return false;
        }
        if (players.size() >= maxPlayers) {
            player.sendMessage(colorize(plugin.getConfig().getString("messages.prefix")
                    + plugin.getConfig().getString("messages.game-full")));
            return false;
        }
        if (state == GameState.ACTIVE || state == GameState.ENDING) {
            player.sendMessage(colorize("&cOyun zaten başladı!"));
            return false;
        }

        GameRole role = assignRole();
        GamePlayer gp = new GamePlayer(player, role);
        players.put(player.getUniqueId(), gp);

        String joinMsg = plugin.getConfig().getString("messages.joined", "&aOyuna katıldın! Rol: &e{role}")
                .replace("{role}", role.getColored());
        player.sendMessage(colorize(plugin.getConfig().getString("messages.prefix") + joinMsg));

        broadcastExcept(player, colorize("&7" + player.getName() + " oyuna katıldı. &8("
                + players.size() + "/" + maxPlayers + ")"));

        // Yeterli oyuncu var mı?
        if (state == GameState.WAITING && players.size() >= minPlayers) {
            startCountdown();
        }

        return true;
    }

    public void leaveGame(Player player) {
        GamePlayer gp = players.remove(player.getUniqueId());
        if (gp == null) return;

        restorePlayer(player);
        broadcastAll(colorize("&7" + player.getName() + " oyundan ayrıldı."));

        // Yeterli oyuncu kalmadı mı?
        if (state == GameState.COUNTDOWN && players.size() < minPlayers) {
            cancelCountdown();
            broadcastAll(colorize("&cYetersiz oyuncu! Geri sayım iptal edildi."));
        } else if (state == GameState.ACTIVE) {
            checkWinConditions();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  ROLE ASSIGNMENT
    // ═══════════════════════════════════════════════════════════════════════

    private GameRole assignRole() {
        long policeCount    = players.values().stream().filter(GamePlayer::isPolice).count();
        long prisonerCount  = players.values().stream().filter(GamePlayer::isPrisoner).count();

        // 1 polis her 3 mahkuma; en az 1 polis olsun
        if (policeCount == 0 || prisonerCount / Math.max(1, policeCount) >= 3) {
            return GameRole.POLICE;
        }
        return GameRole.PRISONER;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  COUNTDOWN
    // ═══════════════════════════════════════════════════════════════════════

    private void startCountdown() {
        state = GameState.COUNTDOWN;
        countdownSeconds = countdownDuration;

        countdownTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (players.size() < minPlayers) {
                    cancelCountdown();
                    broadcastAll(colorize("&cYetersiz oyuncu!"));
                    cancel();
                    return;
                }

                if (countdownSeconds <= 0) {
                    cancel();
                    startGame();
                    return;
                }

                if (countdownSeconds <= 5 || countdownSeconds % 10 == 0) {
                    broadcastAll(colorize("&eOyun &6" + countdownSeconds + " &esaniye içinde başlıyor!"));
                }

                countdownSeconds--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void cancelCountdown() {
        if (countdownTask != null) {
            countdownTask.cancel();
            countdownTask = null;
        }
        state = GameState.WAITING;
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  GAME START / END
    // ═══════════════════════════════════════════════════════════════════════

    public boolean forceStart() {
        if (!locationManager.isFullySetup()) {
            return false;
        }
        if (state == GameState.COUNTDOWN && countdownTask != null) {
            countdownTask.cancel();
        }
        startGame();
        return true;
    }

    private void startGame() {
        if (!locationManager.isFullySetup()) {
            broadcastAll(colorize("&cKonum kurulumu eksik! Admin /pbadmin setup yapmalı."));
            state = GameState.WAITING;
            return;
        }

        state = GameState.ACTIVE;
        totalEscapes = 0;
        remainingSeconds = gameDuration;

        // Teleport + kit
        for (GamePlayer gp : players.values()) {
            spawnPlayer(gp);
        }

        broadcastAll(colorize(plugin.getConfig().getString("messages.game-start",
                "&aOyun başladı! &7Mahkumlar: &cKaçın! &7Polisler: &9Yakalayın!")));

        // Title
        sendTitleAll(
                Component.text("OYUN BAŞLADI!", NamedTextColor.GOLD, TextDecoration.BOLD),
                Component.text("Rolünü öğrenmek için ekrana bak!", NamedTextColor.YELLOW)
        );

        startHUD();
        startEscapeCheck();

        // Game timer
        if (gameDuration > 0) {
            gameTimerTask = new BukkitRunnable() {
                @Override
                public void run() {
                    if (remainingSeconds <= 0) {
                        cancel();
                        endGame(false); // Police wins on timeout
                        return;
                    }
                    if (remainingSeconds == 60 || remainingSeconds == 30 || remainingSeconds == 10) {
                        broadcastAll(colorize("&eOyun &6" + remainingSeconds + " &esaniye içinde bitiyor!"));
                    }
                    remainingSeconds--;
                }
            }.runTaskTimer(plugin, 20L, 20L);
        }
    }

    public void endGame(boolean prisonersWin) {
        if (state == GameState.ENDING) return;
        state = GameState.ENDING;

        cancelTasks();

        String endMsg;
        if (prisonersWin) {
            endMsg = plugin.getConfig().getString("messages.game-end-prisoners",
                    "&6Mahkumlar kazandı! &e{escapes} kişi kaçtı!")
                    .replace("{escapes}", String.valueOf(totalEscapes));
            sendTitleAll(
                    Component.text("MAHKUMLAR KAZANDI!", NamedTextColor.GOLD, TextDecoration.BOLD),
                    Component.text(totalEscapes + " kişi özgürlüğe kavuştu!", NamedTextColor.YELLOW)
            );
        } else {
            endMsg = plugin.getConfig().getString("messages.game-end-police",
                    "&9Polisler kazandı! Tüm kaçışları durdurdu!");
            sendTitleAll(
                    Component.text("POLİSLER KAZANDI!", NamedTextColor.BLUE, TextDecoration.BOLD),
                    Component.text("Düzen sağlandı!", NamedTextColor.AQUA)
            );
        }

        broadcastAll(colorize(plugin.getConfig().getString("messages.prefix") + endMsg));

        // Show stats
        showEndStats();

        // Restore players after delay
        new BukkitRunnable() {
            @Override
            public void run() {
                for (GamePlayer gp : new ArrayList<>(players.values())) {
                    restorePlayer(gp.getPlayer());
                }
                players.clear();
                state = GameState.WAITING;
                totalEscapes = 0;
            }
        }.runTaskLater(plugin, 100L); // 5 seconds
    }

    public void forceStop() {
        if (state == GameState.WAITING) return;
        cancelTasks();
        for (GamePlayer gp : new ArrayList<>(players.values())) {
            restorePlayer(gp.getPlayer());
        }
        players.clear();
        state = GameState.WAITING;
        totalEscapes = 0;
        broadcastAll(colorize("&cOyun admin tarafından durduruldu."));
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  PLAYER SPAWN / KIT
    // ═══════════════════════════════════════════════════════════════════════

    private void spawnPlayer(GamePlayer gp) {
        Player p = gp.getPlayer();
        p.getInventory().clear();
        p.setHealth(20.0);
        p.setFoodLevel(20);
        p.setGameMode(GameMode.SURVIVAL);

        if (gp.isPrisoner()) {
            p.teleport(locationManager.getPrisonerSpawn());
            givePrisonerKit(p);
            // Role title
            p.showTitle(Title.title(
                    Component.text("MAHKUM", NamedTextColor.RED, TextDecoration.BOLD),
                    Component.text("Hapishaneden kaç!", NamedTextColor.YELLOW),
                    Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(3), Duration.ofMillis(500))
            ));
        } else {
            p.teleport(locationManager.getPoliceSpawn());
            givePoliceKit(p);
            p.showTitle(Title.title(
                    Component.text("POLİS", NamedTextColor.BLUE, TextDecoration.BOLD),
                    Component.text("Mahkumları yakala!", NamedTextColor.AQUA),
                    Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(3), Duration.ofMillis(500))
            ));
        }
    }

    private void givePrisonerKit(Player player) {
        // Mahkum: sadece bir kazma (kaçış yolu açmak için)
        ItemStack pickaxe = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta pm = pickaxe.getItemMeta();
        pm.displayName(Component.text("Hücre Kazması", NamedTextColor.RED));
        pickaxe.setItemMeta(pm);
        player.getInventory().addItem(pickaxe);

        // Biraz ekmek
        player.getInventory().addItem(new ItemStack(Material.BREAD, 5));
    }

    private void givePoliceKit(Player player) {
        // Polis: demir kılıç (arrest için), zırh
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        ItemMeta sm = sword.getItemMeta();
        sm.displayName(Component.text("Polis Coppu", NamedTextColor.BLUE));
        sword.setItemMeta(sm);
        player.getInventory().addItem(sword);

        player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        player.getInventory().addItem(new ItemStack(Material.BREAD, 8));
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  ESCAPE SYSTEM
    // ═══════════════════════════════════════════════════════════════════════

    private void startEscapeCheck() {
        escapeCheckTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (state != GameState.ACTIVE) { cancel(); return; }

                Location escapePoint = locationManager.getEscapePoint();
                if (escapePoint == null) return;

                for (GamePlayer gp : new ArrayList<>(players.values())) {
                    if (!gp.isPrisoner() || gp.isArrested()) continue;
                    Player p = gp.getPlayer();

                    // Kaçış noktasına yakın mı? (3 blok)
                    if (p.getLocation().distanceSquared(escapePoint) <= 9) {
                        handleEscape(gp);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 10L); // Her 0.5 saniyede bir kontrol
    }

    /**
     * Mahkum kaçış noktasına ulaştığında çağrılır.
     */
    public void handleEscape(GamePlayer gp) {
        if (!gp.isPrisoner() || state != GameState.ACTIVE) return;

        Player p = gp.getPlayer();
        gp.addMoney(escapeReward);
        gp.incrementEscapes();
        totalEscapes++;

        // Announce
        String msg = plugin.getConfig().getString("messages.escaped",
                "&a{player} &fhapishaneden kaçtı! &e(+{money}₺)")
                .replace("{player}", p.getName())
                .replace("{money}", String.valueOf(escapeReward));
        broadcastAll(colorize(plugin.getConfig().getString("messages.prefix") + msg));

        // Sound & effects
        p.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, p.getLocation(), 30, 1, 1, 1);
        p.getWorld().playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);

        // Title to escaped player
        p.showTitle(Title.title(
                Component.text("ÖZGÜRSÜN!", NamedTextColor.GOLD, TextDecoration.BOLD),
                Component.text("+" + escapeReward + "₺ kazandın!", NamedTextColor.GREEN),
                Title.Times.times(Duration.ofMillis(300), Duration.ofSeconds(3), Duration.ofMillis(500))
        ));

        // Geri mahkum hapishanesine spawn et (döngüsel oyun)
        new BukkitRunnable() {
            @Override
            public void run() {
                if (players.containsKey(p.getUniqueId())) {
                    p.teleport(locationManager.getCellSpawn());
                    p.sendMessage(colorize("&7Tekrar hücreye konuldun. Tekrar kaç!"));
                }
            }
        }.runTaskLater(plugin, 60L); // 3 saniye sonra

        // Win check
        checkWinConditions();
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  ARREST SYSTEM
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Polis, mahkumu kılıçla vurduğunda çağrılır.
     * EntityDamageByEntityEvent'ten tetiklenir.
     */
    public boolean tryArrest(Player policePlayer, Player prisonerPlayer) {
        if (state != GameState.ACTIVE) return false;

        GamePlayer police   = players.get(policePlayer.getUniqueId());
        GamePlayer prisoner = players.get(prisonerPlayer.getUniqueId());

        if (police == null || prisoner == null) return false;
        if (!police.isPolice() || !prisoner.isPrisoner()) return false;
        if (prisoner.isArrested()) return false;

        // Cooldown kontrolü
        if (!police.canArrest(arrestCooldownMs)) {
            long remaining = (arrestCooldownMs - (System.currentTimeMillis() - police.getLastArrestTime())) / 1000 + 1;
            policePlayer.sendMessage(colorize("&cTutuklamaları &e" + remaining + "s &csonra yapabilirsin."));
            return false;
        }

        // TUTUKLAMA!
        prisoner.setArrested(true);
        police.setLastArrestTime(System.currentTimeMillis());
        police.addMoney(arrestReward);
        police.incrementArrests();

        // Announce
        String msg = plugin.getConfig().getString("messages.arrested",
                "&c{player} &ftutuklandı! Polis: &b{police} &e(+{money}₺)")
                .replace("{player}", prisonerPlayer.getName())
                .replace("{police}", policePlayer.getName())
                .replace("{money}", String.valueOf(arrestReward));
        broadcastAll(colorize(plugin.getConfig().getString("messages.prefix") + msg));

        // Para sıfırla (ceza)
        prisoner.resetMoney();

        // Effects
        prisonerPlayer.getWorld().playSound(prisonerPlayer.getLocation(), Sound.ENTITY_PLAYER_HURT, 1f, 0.5f);
        prisonerPlayer.getWorld().spawnParticle(Particle.SMOKE, prisonerPlayer.getLocation(), 15, 0.5, 0.5, 0.5);

        // Prisoner title
        prisonerPlayer.showTitle(Title.title(
                Component.text("TUTUKLANDI!", NamedTextColor.RED, TextDecoration.BOLD),
                Component.text("Polis: " + policePlayer.getName(), NamedTextColor.GRAY),
                Title.Times.times(Duration.ofMillis(200), Duration.ofSeconds(2), Duration.ofMillis(500))
        ));

        // Police title
        policePlayer.showTitle(Title.title(
                Component.text("TUTUKLADINIZ!", NamedTextColor.BLUE, TextDecoration.BOLD),
                Component.text("+" + arrestReward + "₺ kazandınız!", NamedTextColor.GREEN),
                Title.Times.times(Duration.ofMillis(200), Duration.ofSeconds(2), Duration.ofMillis(500))
        ));

        // Hücreye gönder
        Location arrestPoint = locationManager.getArrestPoint();
        if (arrestPoint != null) {
            prisonerPlayer.teleport(arrestPoint);
        }

        // Respawn delay
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!players.containsKey(prisonerPlayer.getUniqueId())) return;
                prisoner.setArrested(false);
                prisonerPlayer.teleport(locationManager.getCellSpawn());
                prisonerPlayer.sendMessage(colorize("&7Serbest bırakıldın! Tekrar kaçmayı dene."));
            }
        }.runTaskLater(plugin, respawnDelay * 20L);

        return true; // Hasarı iptal et
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  WIN CONDITIONS
    // ═══════════════════════════════════════════════════════════════════════

    private void checkWinConditions() {
        if (state != GameState.ACTIVE) return;

        // Mahkumlar kazanır: yeterli kaçış
        if (totalEscapes >= escapesToWin) {
            endGame(true);
            return;
        }

        // Polisler kazanır: oyunda polis kalmadıysa mahkumlar kazanır
        long policeCount    = players.values().stream().filter(GamePlayer::isPolice).count();
        long prisonerCount  = players.values().stream().filter(GamePlayer::isPrisoner).count();

        if (policeCount == 0 && prisonerCount > 0) {
            endGame(true); // Polis yoksa mahkumlar kazanır
        } else if (prisonerCount == 0 && policeCount > 0) {
            endGame(false); // Mahkum yoksa polisler kazanır
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  HUD (Action Bar)
    // ═══════════════════════════════════════════════════════════════════════

    private void startHUD() {
        hudTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (state != GameState.ACTIVE) { cancel(); return; }

                for (GamePlayer gp : players.values()) {
                    Player p = gp.getPlayer();
                    String timeStr = formatTime(remainingSeconds);

                    Component hud;
                    if (gp.isPrisoner()) {
                        hud = Component.text("🔴 MAHKUM", NamedTextColor.RED, TextDecoration.BOLD)
                                .append(Component.text("  |  ", NamedTextColor.DARK_GRAY))
                                .append(Component.text("💰 " + gp.getMoney() + "₺", NamedTextColor.GOLD))
                                .append(Component.text("  |  ", NamedTextColor.DARK_GRAY))
                                .append(Component.text("🏃 " + totalEscapes + "/" + escapesToWin + " Kaçış", NamedTextColor.GREEN))
                                .append(Component.text("  |  ", NamedTextColor.DARK_GRAY))
                                .append(Component.text("⏱ " + timeStr, NamedTextColor.YELLOW));
                    } else {
                        hud = Component.text("🔵 POLİS", NamedTextColor.BLUE, TextDecoration.BOLD)
                                .append(Component.text("  |  ", NamedTextColor.DARK_GRAY))
                                .append(Component.text("💰 " + gp.getMoney() + "₺", NamedTextColor.GOLD))
                                .append(Component.text("  |  ", NamedTextColor.DARK_GRAY))
                                .append(Component.text("⚔ " + gp.getArrests() + " Tutuklama", NamedTextColor.AQUA))
                                .append(Component.text("  |  ", NamedTextColor.DARK_GRAY))
                                .append(Component.text("⏱ " + timeStr, NamedTextColor.YELLOW));
                    }
                    p.sendActionBar(hud);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  END STATS
    // ═══════════════════════════════════════════════════════════════════════

    private void showEndStats() {
        new BukkitRunnable() {
            @Override
            public void run() {
                broadcastAll(colorize("&8&m─────────────────────────────────"));
                broadcastAll(colorize("&6&l  📊 OYUN İSTATİSTİKLERİ"));
                broadcastAll(colorize("&8&m─────────────────────────────────"));

                // Top prisoners
                players.values().stream()
                        .filter(GamePlayer::isPrisoner)
                        .sorted(Comparator.comparingInt(GamePlayer::getEscapes).reversed())
                        .limit(3)
                        .forEach(gp -> broadcastAll(colorize(
                                "&c  🔴 " + gp.getPlayer().getName()
                                        + " &7→ &a" + gp.getEscapes() + " kaçış"
                                        + " &7| &e" + gp.getMoney() + "₺")));

                // Top police
                players.values().stream()
                        .filter(GamePlayer::isPolice)
                        .sorted(Comparator.comparingInt(GamePlayer::getArrests).reversed())
                        .limit(3)
                        .forEach(gp -> broadcastAll(colorize(
                                "&9  🔵 " + gp.getPlayer().getName()
                                        + " &7→ &b" + gp.getArrests() + " tutuklama"
                                        + " &7| &e" + gp.getMoney() + "₺")));

                broadcastAll(colorize("&8&m─────────────────────────────────"));
            }
        }.runTaskLater(plugin, 20L);
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  UTILITIES
    // ═══════════════════════════════════════════════════════════════════════

    private void restorePlayer(Player player) {
        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.sendActionBar(Component.empty());
    }

    private void cancelTasks() {
        if (countdownTask  != null) { countdownTask.cancel();  countdownTask  = null; }
        if (gameTimerTask  != null) { gameTimerTask.cancel();  gameTimerTask  = null; }
        if (hudTask        != null) { hudTask.cancel();        hudTask        = null; }
        if (escapeCheckTask!= null) { escapeCheckTask.cancel();escapeCheckTask= null; }
    }

    private void broadcastAll(String msg) {
        for (GamePlayer gp : players.values()) {
            gp.getPlayer().sendMessage(msg);
        }
    }

    private void broadcastExcept(Player except, String msg) {
        for (GamePlayer gp : players.values()) {
            if (!gp.getPlayer().equals(except)) gp.getPlayer().sendMessage(msg);
        }
    }

    private void sendTitleAll(Component title, Component subtitle) {
        Title.Times times = Title.Times.times(
                Duration.ofMillis(500), Duration.ofSeconds(3), Duration.ofMillis(500));
        Title t = Title.title(title, subtitle, times);
        for (GamePlayer gp : players.values()) {
            gp.getPlayer().showTitle(t);
        }
    }

    private String formatTime(int seconds) {
        if (seconds <= 0) return "∞";
        int m = seconds / 60;
        int s = seconds % 60;
        return String.format("%d:%02d", m, s);
    }

    public static String colorize(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    // ─── Public getters ───────────────────────────────────────────────────

    public GameState getState() { return state; }
    public Map<UUID, GamePlayer> getPlayers() { return players; }
    public int getTotalEscapes() { return totalEscapes; }
    public int getRemainingSeconds() { return remainingSeconds; }
    public boolean isInGame(Player p) { return players.containsKey(p.getUniqueId()); }
    public GamePlayer getGamePlayer(Player p) { return players.get(p.getUniqueId()); }
}
