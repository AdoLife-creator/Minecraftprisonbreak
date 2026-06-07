package me.byadolife.prisonbreak.listeners;

import me.byadolife.prisonbreak.gui.TeamMenu;
import me.byadolife.prisonbreak.managers.TeamManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TeamMenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if(!event.getView().getTitle().equals(TeamMenu.TITLE))
            return;

        event.setCancelled(true);

        if(event.getCurrentItem() == null)
            return;

        Player player = (Player) event.getWhoClicked();

        if(event.getCurrentItem().getType() == Material.ORANGE_CONCRETE) {
            TeamManager.setPrisoner(player);
            player.closeInventory();
        }

        if(event.getCurrentItem().getType() == Material.BLUE_CONCRETE) {
            TeamManager.setGuard(player);
            player.closeInventory();
        }
    }
}
