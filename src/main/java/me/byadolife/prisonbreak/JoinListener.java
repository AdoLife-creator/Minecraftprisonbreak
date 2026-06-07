package me.byadolife.prisonbreak.listeners;

import me.byadolife.prisonbreak.managers.TabManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        TabManager.update(event.getPlayer());
    }
}
