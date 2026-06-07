package me.byadolife.prisonbreak.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SkinManager {

    public static void setPrisonerSkin(Player p) {
        Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                "skin set " + p.getName() + " yoshio0302"
        );
    }

    public static void setGuardSkin(Player p) {
        Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                "skin set " + p.getName() + " Clinkoo"
        );
    }
}
