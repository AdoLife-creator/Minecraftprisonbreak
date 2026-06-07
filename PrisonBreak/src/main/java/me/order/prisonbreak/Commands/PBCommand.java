package dev.prisonbreak.commands;

import dev.prisonbreak.managers.GameManager;
import dev.prisonbreak.models.GamePlayer;
import dev.prisonbreak.models.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * /pb <join|leave|status> — Oyuncu komutları
 */
public class PBCommand implements CommandExecutor, TabCompleter {

    private final GameManager gameManager;

    public PBCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    private static final String PREFIX = "&8[&c&lPB&8] ";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Bu komut sadece oyuncular tarafından kullanılabilir.");
            return true;
        }

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "join" -> handleJoin(player);
            case "leave" -> handleLeave(player);
            case "status" -> handleStatus(player);
            default -> sendHelp(player);
        }

        return true;
    }

    private void handleJoin(Player player) {
        if (gameManager.getState() == GameState.ACTIVE) {
            send(player, "&cOyun zaten başladı! Bir sonrakini bekle.");
            return;
        }
        gameManager.joinGame(player);
    }

    private void handleLeave(Player player) {
        if (!gameManager.isInGame(player)) {
            send(player, "&cZaten bir oyunda değilsin!");
            return;
        }
        gameManager.leaveGame(player);
        send(player, "&aOyundan ayrıldın.");
    }

    private void handleStatus(Player player) {
        send(player, "&8&m──────────────────────────");
        send(player, "&6&l  🏛 PrisonBreak Durumu");
        send(player, "&8&m──────────────────────────");

        String stateStr = switch (gameManager.getState()) {
            case WAITING -> "&7Bekleniyor";
            case COUNTDOWN -> "&eGeri sayım";
            case ACTIVE -> "&aAktif";
            case ENDING -> "&cBitiyor";
        };
        send(player, "&7Durum: " + stateStr);
        send(player, "&7Oyuncular: &f" + gameManager.getPlayers().size());

        if (gameManager.getState() == GameState.ACTIVE) {
            send(player, "&7Kaçışlar: &a" + gameManager.getTotalEscapes());
            send(player, "&7Kalan süre: &e" + formatTime(gameManager.getRemainingSeconds()));

            if (gameManager.isInGame(player)) {
                GamePlayer gp = gameManager.getGamePlayer(player);
                send(player, "&7Rolün: " + gp.getRole().getColored());
                send(player, "&7Paran: &e" + gp.getMoney() + "₺");
            }
        }
        send(player, "&8&m──────────────────────────");
    }

    private void sendHelp(Player player) {
        send(player, "&8&m──────────────────────────");
        send(player, "&6&l  PrisonBreak Komutları");
        send(player, "&8&m──────────────────────────");
        send(player, "&e/pb join &7- Oyuna katıl");
        send(player, "&e/pb leave &7- Oyundan ayrıl");
        send(player, "&e/pb status &7- Oyun durumunu gör");
        if (player.hasPermission("prisonbreak.admin")) {
            send(player, "&c/pbadmin &7- Admin komutları");
        }
        send(player, "&8&m──────────────────────────");
    }

    private void send(Player player, String msg) {
        player.sendMessage(GameManager.colorize(PREFIX + msg));
    }

    private String formatTime(int seconds) {
        if (seconds <= 0) return "∞";
        return String.format("%d:%02d", seconds / 60, seconds % 60);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("join", "leave", "status");
        }
        return List.of();
    }
}
