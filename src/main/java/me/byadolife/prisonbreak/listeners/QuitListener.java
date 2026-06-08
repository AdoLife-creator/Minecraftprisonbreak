package me.byadolife.prisonbreak.listeners;

import me.byadolife.prisonbreak.managers.SkinManager;
import me.byadolife.prisonbreak.managers.TeamManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        SkinManager.resetSkin(event.getPlayer());

        TeamManager.remove(event.getPlayer());
    }
}
