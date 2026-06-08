package me.byadolife.prisonbreak.listeners;

import me.byadolife.prisonbreak.managers.CuffManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class CuffBlockListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        if (CuffManager.isCuffed(e.getPlayer())) {
            e.setTo(e.getFrom());
        }
    }
}
