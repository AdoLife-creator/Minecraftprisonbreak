package me.byadolife.prisonbreak.listeners;

import me.byadolife.prisonbreak.managers.TabManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        TabManager.reset(event.getPlayer());
    }
}
