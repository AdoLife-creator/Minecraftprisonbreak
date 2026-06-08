package me.byadolife.prisonbreak.managers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GuardKitManager {

    public static void giveKit(Player player) {

        player.getInventory().clear();

        ItemStack sword =
                new ItemStack(Material.WOODEN_SWORD);

        ItemStack cuffs =
                new ItemStack(Material.FISHING_ROD);

        ItemStack stun =
                new ItemStack(Material.BLAZE_ROD);

        player.getInventory().addItem(sword);
        player.getInventory().addItem(cuffs);
        player.getInventory().addItem(stun);
    }
}
