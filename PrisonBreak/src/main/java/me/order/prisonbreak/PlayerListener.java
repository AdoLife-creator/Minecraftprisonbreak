package dev.prisonbreak.listeners;

import dev.prisonbreak.managers.GameManager;
import dev.prisonbreak.models.GamePlayer;
import dev.prisonbreak.models.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Oyuncu event'lerini yakalar.
 */
public class PlayerListener implements Listener {

    private final GameManager gameManager;

    public PlayerListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * Oyundan çıkınca otomatik leave.
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (gameManager.isInGame(player)) {
            gameManager.leaveGame(player);
        }
    }

    /**
     * Oyun sırasında ölümü engelle — düşme hasarı zaten iptal,
     * ama yine de güvenlik için kontrol.
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (!gameManager.isInGame(player)) return;
        if (gameManager.getState() != GameState.ACTIVE) return;

        // Ölümü temizle, eşyaları düşürme
        event.setDroppedExp(0);
        event.getDrops().clear();
        event.setDeathMessage(null);

        // Respawn sonrası tekrar spawn et
    }

    /**
     * Oyun sırasında respawn — rol spawn'una ışınla.
     */
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (!gameManager.isInGame(player)) return;
        if (gameManager.getState() != GameState.ACTIVE) return;

        GamePlayer gp = gameManager.getGamePlayer(player);
        if (gp == null) return;

        // Cell spawn'a gönder
        if (gameManager.getPlayers().containsKey(player.getUniqueId())) {
            event.setRespawnLocation(gp.isPrisoner()
                    ? gameManager.getGamePlayer(player).isPrisoner()
                        ? getCellSpawnSafe(gameManager) : getPoliceSafe(gameManager)
                    : getPoliceSafe(gameManager));
        }
    }

    private org.bukkit.Location getCellSpawnSafe(GameManager gm) {
        // LocationManager erişimi için GameManager'den al
        return gm.getPlayers().values().iterator().next()
                .getPlayer().getWorld().getSpawnLocation(); // fallback
    }

    private org.bukkit.Location getPoliceSafe(GameManager gm) {
        return gm.getPlayers().values().iterator().next()
                .getPlayer().getWorld().getSpawnLocation(); // fallback
    }

    /**
     * Oyun sırasında eşya bırakmayı engelle.
     */
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!gameManager.isInGame(player)) return;
        if (gameManager.getState() != GameState.ACTIVE) return;

        // Kit eşyalarını bırakmayı engelle
        event.setCancelled(true);
        player.sendMessage(GameManager.colorize("&cOyun sırasında eşya bırakamazsın!"));
    }
}
