package me.byadolife.prisonbreak.managers;

import me.byadolife.prisonbreak.PrisonBreak;
import me.byadolife.prisonbreak.team.TeamType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamManager {

    private static final HashMap<UUID, TeamType> teams = new HashMap<>();
    private static final HashMap<UUID, Integer> guardKills = new HashMap<>();
    private static final HashMap<UUID, Boolean> criminals = new HashMap<>();

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

    // ================= REMOVE (FIXED) =================

    public static void remove(Player p) {
        teams.remove(p.getUniqueId());
        guardKills.remove(p.getUniqueId());
        criminals.remove(p.getUniqueId());
    }

    // ================= CRIMINAL FIX =================

    public static void markCriminal(Player p) {
        criminals.put(p.getUniqueId(), true);
    }

    public static boolean isCriminal(Player p) {
        return criminals.getOrDefault(p.getUniqueId(), false);
    }

    public static void clearCriminal(Player p) {
        criminals.remove(p.getUniqueId());
    }

    // ================= SPAWN FIX =================

    public static Location getSpawn(String key) {

        var cfg = PrisonBreak.getInstance().getConfig();

        String path = "teams." + key + ".spawn";

        if (!cfg.contains(path + ".world")) return null;

        return new Location(
                Bukkit.getWorld(cfg.getString(path + ".world")),
                cfg.getDouble(path + ".x"),
                cfg.getDouble(path + ".y"),
                cfg.getDouble(path + ".z"),
                (float) cfg.getDouble(path + ".yaw"),
                (float) cfg.getDouble(path + ".pitch")
        );
    }
}
