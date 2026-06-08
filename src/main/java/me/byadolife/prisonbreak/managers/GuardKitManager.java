package me.byadolife.prisonbreak.managers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuardKitManager {

    public static void giveKit(Player player) {

        player.getInventory().clear();

        ItemStack baton = new ItemStack(Material.WOODEN_SWORD);
        ItemMeta batonMeta = baton.getItemMeta();
        batonMeta.setDisplayName("§6Gardiyan Copu");
        baton.setItemMeta(batonMeta);

        ItemStack taser = new ItemStack(Material.BLAZE_ROD);
        ItemMeta taserMeta = taser.getItemMeta();
        taserMeta.setDisplayName("§eŞok Tabancası");
        taser.setItemMeta(taserMeta);

        ItemStack cuffs = new ItemStack(Material.LEAD);
        ItemMeta cuffsMeta = cuffs.getItemMeta();
        cuffsMeta.setDisplayName("§bKelepçe");
        cuffs.setItemMeta(cuffsMeta);

        player.getInventory().setItem(0, baton);
        player.getInventory().setItem(1, taser);
        player.getInventory().setItem(2, cuffs);
    }
}
