package me.byadolife.prisonbreak.listener;

import me.byadolife.prisonbreak.manager.GameManager;
import me.byadolife.prisonbreak.manager.LocationManager;
import me.byadolife.prisonbreak.models.GamePlayer;
import me.byadolife.prisonbreak.models.GameState;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * Oyuncu event'lerini yakalar.
 */
public class PlayerListener implements Listener {

    private final GameManager gameManager;

    public PlayerListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    // ───────────────────────────── QUIT ─────────────────────────────

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (gameManager.isInGame(player)) {
            gameManager.leaveGame(player);
        }
    }

    // ───────────────────────────── DEATH ─────────────────────────────

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (!gameManager.isInGame(player)) return;
        if (gameManager.getState() != GameState.ACTIVE) return;

        event.setDroppedExp(0);
        event.getDrops().clear();
        event.setDeathMessage(null);
    }

    // ───────────────────────────── RESPAWN ─────────────────────────────

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        if (!gameManager.isInGame(player)) return;
        if (gameManager.getState() != GameState.ACTIVE) return;

        GamePlayer gp = gameManager.getGamePlayer(player);
        if (gp == null) return;

        LocationManager lm = gameManager.getLocationManager();

        Location respawnLocation;

        if (gp.isPrisoner()) {
            respawnLocation = lm.getCellSpawn();
        } else {
            respawnLocation = lm.getPoliceSpawn();
        }

        if (respawnLocation == null) {
            respawnLocation = player.getWorld().getSpawnLocation();
        }

        event.setRespawnLocation(respawnLocation);
    }

    // ───────────────────────────── ITEM DROP ─────────────────────────────

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (!gameManager.isInGame(player)) return;
        if (gameManager.getState() != GameState.ACTIVE) return;

        event.setCancelled(true);
        player.sendMessage(GameManager.colorize("&cOyun sırasında eşya bırakamazsın!"));
    }
}
