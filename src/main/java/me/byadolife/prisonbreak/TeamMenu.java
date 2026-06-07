package me.byadolife.prisonbreak.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamMenu {

    public static final String TITLE = "§6Takım Seç";

    public static void open(Player player) {

        Inventory inv = Bukkit.createInventory(null, 27, TITLE);

        ItemStack prisoner = new ItemStack(Material.ORANGE_CONCRETE);
        ItemMeta pMeta = prisoner.getItemMeta();
        pMeta.setDisplayName("§6Mahkuma Katıl");
        prisoner.setItemMeta(pMeta);

        ItemStack guard = new ItemStack(Material.BLUE_CONCRETE);
        ItemMeta gMeta = guard.getItemMeta();
        gMeta.setDisplayName("§9Gardiyana Katıl");
        guard.setItemMeta(gMeta);

        inv.setItem(11, prisoner);
        inv.setItem(15, guard);

        player.openInventory(inv);
    }
}
