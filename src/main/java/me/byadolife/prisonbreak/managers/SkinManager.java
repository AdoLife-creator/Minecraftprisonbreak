package me.byadolife.prisonbreak.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SkinManager {

    public static void setPrisonerSkin(Player p) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "skin set yoshio0302 " + p.getName());
    }

    public static void setGuardSkin(Player p) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "skin set Clinkoo " + p.getName());
    }
}
