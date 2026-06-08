package me.byadolife.prisonbreak.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TeamMenu {

    public static final String TITLE = "§6Takım Seç";

    public static void open(Player player) {

        Inventory inv = Bukkit.createInventory(null, 27, TITLE);

        ItemStack prisoner = new ItemStack(Material.ORANGE_CONCRETE);
        ItemMeta prisonerMeta = prisoner.getItemMeta();

        prisonerMeta.setDisplayName("§6Mahkum");
        prisonerMeta.setLore(List.of(
                "§7Kaçmaya çalış.",
                "",
                "§eKatılmak için tıkla."
        ));

        prisoner.setItemMeta(prisonerMeta);

        ItemStack guard = new ItemStack(Material.BLUE_CONCRETE);
        ItemMeta guardMeta = guard.getItemMeta();

        guardMeta.setDisplayName("§9Gardiyan");
        guardMeta.setLore(List.of(
                "§7Mahkumları yakala.",
                "",
                "§eKatılmak için tıkla."
        ));

        guard.setItemMeta(guardMeta);

        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, glass);
        }

        inv.setItem(11, prisoner);
        inv.setItem(15, guard);

        player.openInventory(inv);
    }
}
