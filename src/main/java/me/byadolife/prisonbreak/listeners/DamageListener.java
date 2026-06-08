package me.byadolife.prisonbreak.listeners;

import me.byadolife.prisonbreak.managers.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player victim))
            return;

        if (!(event.getDamager() instanceof Player attacker))
            return;

        // Mahkum -> Mahkum kapalı
        if (TeamManager.isPrisoner(attacker)
                && TeamManager.isPrisoner(victim)) {

            event.setCancelled(true);
            return;
        }

        // Gardiyan -> Gardiyan kapalı
        if (TeamManager.isGuard(attacker)
                && TeamManager.isGuard(victim)) {

            event.setCancelled(true);
            return;
        }

        // Mahkum -> Gardiyan
        if (TeamManager.isPrisoner(attacker)
                && TeamManager.isGuard(victim)) {

            TeamManager.markCriminal(attacker);
        }
    }
}
