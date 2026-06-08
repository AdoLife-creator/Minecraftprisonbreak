package me.byadolife.prisonbreak.managers;

import me.byadolife.prisonbreak.team.TeamType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TeamManager {

    private static final Map<UUID, TeamType> teams = new HashMap<>();
    private static final Map<UUID, Integer> guardKills = new HashMap<>();
    private static final Set<UUID> criminals = new HashSet<>();

    // ================= TEAM =================

    public static boolean hasTeam(Player p) {
        return teams.containsKey(p.getUniqueId());
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

    // ================= SET TEAM =================

    public static void setPrisoner(Player p) {
        teams.put(p.getUniqueId(), TeamType.PRISONER);
    }

    public static void setGuard(Player p) {
        teams.put(p.getUniqueId(), TeamType.GUARD);
    }

    // ================= REMOVE =================

    public static void remove(Player p) {
        teams.remove(p.getUniqueId());
        guardKills.remove(p.getUniqueId());
        criminals.remove(p.getUniqueId());
    }

    // ================= CRIMINAL =================

    public static void markCriminal(Player p) {
        criminals.add(p.getUniqueId());
    }

    public static boolean isCriminal(Player p) {
        return criminals.contains(p.getUniqueId());
    }

    public static void clearCriminal(Player p) {
        criminals.remove(p.getUniqueId());
    }

    // ================= ILLEGAL KILL =================

    public static void addIllegalKill(Player guard) {

        int count = guardKills.getOrDefault(guard.getUniqueId(), 0) + 1;

        guardKills.put(guard.getUniqueId(), count);

        guard.sendMessage("§cMasum öldürdün! " + count + "/3");

        if (count >= 3) {
            guardKills.remove(guard.getUniqueId());
            setPrisoner(guard);
            guard.sendMessage("§4Fazla masum öldürdün, mahkum oldun!");
        }
    }
}
