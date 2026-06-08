package me.byadolife.prisonbreak.managers;

import org.bukkit.entity.Player;

public class TabManager {

    public static void setup() {}

    public static void update(Player p) {

        if (TeamManager.isPrisoner(p)) {
            p.setPlayerListName("§6[M] §f" + p.getName());
        }

        if (TeamManager.isGuard(p)) {
            p.setPlayerListName("§9[G] §f" + p.getName());
        }
    }
}
