package me.byadolife.prisonbreak.listeners;

import me.byadolife.prisonbreak.gui.TeamMenu;
import me.byadolife.prisonbreak.managers.SpawnManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        Location lobby =
                SpawnManager.getSpawn("lobby");

        if (lobby != null) {
            player.teleport(lobby);
        }

        Bukkit.getScheduler().runTaskLater(
                me.byadolife.prisonbreak.PrisonBreak.getInstance(),
                () -> TeamMenu.open(player),
                20L
        );
    }
}
