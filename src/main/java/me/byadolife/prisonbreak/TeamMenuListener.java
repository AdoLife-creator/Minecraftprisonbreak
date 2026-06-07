package me.byadolife.prisonbreak.listeners;

import me.byadolife.prisonbreak.gui.TeamMenu;
import me.byadolife.prisonbreak.managers.SpawnManager;
import me.byadolife.prisonbreak.managers.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TeamMenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (!event.getView().getTitle().equals(TeamMenu.TITLE))
            return;

        event.setCancelled(true);

        if (event.getCurrentItem() == null)
            return;

        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem().getType() == Material.ORANGE_CONCRETE) {

            TeamManager.setPrisoner(player);

            Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(),
                    "skin set " + player.getName() + " yoshio0302"
            );

            Location loc =
                    SpawnManager.get("spawns.mahkum");

            if (loc != null)
                player.teleport(loc);

            player.closeInventory();
        }

        if (event.getCurrentItem().getType() == Material.BLUE_CONCRETE) {

            TeamManager.setGuard(player);

            Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(),
                    "skin set " + player.getName() + " Clinkoo"
            );

            Location loc =
                    SpawnManager.get("spawns.gardiyan");

            if (loc != null)
                player.teleport(loc);

            player.closeInventory();
        }
    }
}
