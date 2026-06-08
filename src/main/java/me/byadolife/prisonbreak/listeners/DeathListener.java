package me.byadolife.prisonbreak.listeners;

import me.byadolife.prisonbreak.managers.SpawnManager;
import me.byadolife.prisonbreak.managers.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        Player victim = event.getPlayer();

        if (!(victim.getKiller() instanceof Player killer))
            return;

        // Gardiyan mahkum öldürdü
        if (TeamManager.isGuard(killer)
                && TeamManager.isPrisoner(victim)) {

            // Mahkum suçlu değilse gardiyana ceza say
            if (!TeamManager.isCriminal(victim)) {

                TeamManager.addIllegalKill(killer);

            } else {

                TeamManager.clearCriminal(victim);
            }
        }

        // Ölünce takım spawnına geri gönder
        Bukkit.getScheduler().runTaskLater(
                me.byadolife.prisonbreak.PrisonBreak.getInstance(),
                () -> {

                    if (!victim.isOnline())
                        return;

                    Location spawn = null;

                    if (TeamManager.isPrisoner(victim)) {
                        spawn = SpawnManager.getSpawn("mahkum");
                    }

                    if (TeamManager.isGuard(victim)) {
                        spawn = SpawnManager.getSpawn("gardiyan");
                    }

                    if (spawn != null) {
                        victim.teleport(spawn);
                    }

                },
                2L
        );
    }
}
