package me.byadolife.prisonbreak.managers;

import org.bukkit.entity.Player;

public class TeamManager {

    public static void setup() {
        // scoreboard kullanmıyoruz burada
    }

    public static void setPrisoner(Player p) {
        TabManager.setPrisoner(p);

        p.sendMessage("§6Mahkum takımına katıldın!");
        p.performCommand("skin set yoshio0302");
    }

    public static void setGuard(Player p) {
        TabManager.setGuard(p);

        p.sendMessage("§9Gardiyan takımına katıldın!");
        p.performCommand("skin set Clinkoo");
    }
}
