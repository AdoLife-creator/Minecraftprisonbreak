package me.byadolife.prisonbreak.managers;

import me.byadolife.prisonbreak.team.TeamType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TabManager {

    private static final HashMap<UUID, TeamType> teams = new HashMap<>();

    public static void setPrisoner(Player player) {
        teams.put(player.getUniqueId(), TeamType.PRISONER);
        update(player);
    }

    public static void setGuard(Player player) {
        teams.put(player.getUniqueId(), TeamType.GUARD);
        update(player);
    }

    public static void update(Player player) {
        TeamType type = teams.get(player.getUniqueId());

        if (type == null) {
            player.setPlayerListName("§7" + player.getName());
            return;
        }

        if (type == TeamType.PRISONER) {
            player.setPlayerListName("§6[M] §f" + player.getName());
        }

        if (type == TeamType.GUARD) {
            player.setPlayerListName("§9[G] §f" + player.getName());
        }
    }

    public static void reset(Player player) {
        teams.remove(player.getUniqueId());
    }
}
