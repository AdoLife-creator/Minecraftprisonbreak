package me.byadolife.prisonbreak.managers;

import org.bukkit.entity.Player;

public class TabManager {

    public static void update(Player player) {

        player.setPlayerListHeaderFooter(
                "§6§lPrisonBreak",
                ""
        );
    }
}
