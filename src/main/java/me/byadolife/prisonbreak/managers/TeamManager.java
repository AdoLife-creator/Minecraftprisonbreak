package me.byadolife.prisonbreak.managers;

import me.byadolife.prisonbreak.team.TeamType;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamManager {

    private static final Map<UUID, TeamType> teams = new HashMap<>();
    private static final Map<UUID, Integer> guardKills = new HashMap<>();
    private static final Set<UUID> criminals = new HashSet<>();

    public static void setPrisoner(Player p) {
        teams.put(p.getUniqueId(), TeamType.PRISONER);
    }

    public static void setGuard(Player p) {
        teams.put(p.getUniqueId(), TeamType.GUARD);
    }

    public static TeamType getTeam(Player p) {
        return teams.get(p.getUniqueId());
    }

    public static boolean isPrisoner(Player p) {
        return getTeam(p) == TeamType.PRISONER;
    }

    public static boolean isGuard(Player p) {
        return getTeam(p) == TeamType.GUARD;
    }

    public static void remove(Player p) {
        teams.remove(p.getUniqueId());
        guardKills.remove(p.getUniqueId());
        criminals.remove(p.getUniqueId());
    }

    public static void addIllegalKill(Player guard) {
        int count = guardKills.getOrDefault(guard.getUniqueId(), 0) + 1;
        guardKills.put(guard.getUniqueId(), count);

        if (count >= 3) {
            setPrisoner(guard);
            guardKills.remove(guard.getUniqueId());
        }
    }
}
