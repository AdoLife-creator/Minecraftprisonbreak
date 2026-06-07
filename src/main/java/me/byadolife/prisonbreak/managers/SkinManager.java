package me.byadolife.prisonbreak.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SkinManager {

    public static void setPrisonerSkin(Player player) {
        Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                "skin set " + player.getName() + " yoshio0302"
        );
    }

    public static void setGuardSkin(Player player) {
        Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                "skin set " + player.getName() + " Clinkoo"
        );
    }
}
