package me.byadolife.prisonbreak.managers;

import me.byadolife.prisonbreak.team.TeamType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TeamManager {

    private static final HashMap<UUID, TeamType> cache = new HashMap<>();

    public static void setup() {
        // şimdilik boş (TabManager handle ediyor)
    }

    public static void setPrisoner(Player player) {

        TabManager.setPrisoner(player);
        cache.put(player.getUniqueId(), TeamType.PRISONER);

        player.sendMessage("§6Mahkum takımına katıldın!");

        // SkinRestorer komutu
        player.performCommand("skin set yoshio0302");
    }

    public static void setGuard(Player player) {

        TabManager.setGuard(player);
        cache.put(player.getUniqueId(), TeamType.GUARD);

        player.sendMessage("§9Gardiyan takımına katıldın!");

        // SkinRestorer komutu
        player.performCommand("skin set Clinkoo");
    }

    public static TeamType getTeam(Player player) {
        return cache.get(player.getUniqueId());
    }

    public static void clear(Player player) {
        cache.remove(player.getUniqueId());
    }
}
