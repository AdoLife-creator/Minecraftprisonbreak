package me.byadolife.prisonbreak.managers;

import me.byadolife.prisonbreak.team.TeamType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamManager {

    private static final HashMap<UUID, TeamType> teams = new HashMap<>();
    private static final HashMap<UUID, Integer> guardKills = new HashMap<>();
    private static final HashMap<UUID, Boolean> criminals = new HashMap<>();

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

    public static void setPrisoner(Player p) {

        if (hasTeam(p)) return;

        teams.put(p.getUniqueId(), TeamType.PRISONER);

        p.performCommand("skin set yoshio0302 " + p.getName());

        p.setPlayerListName("§6[M] §f" + p.getName());

        Location loc = SpawnManager.get("mahkum");
        if (loc != null) p.teleport(loc);

        p.sendMessage("§6Mahkum oldun!");
    }

    public static void setGuard(Player p) {

        if (hasTeam(p)) return;

        teams.put(p.getUniqueId(), TeamType.GUARD);

        p.performCommand("skin set Clinkoo " + p.getName());

        GuardKitManager.giveKit(p);

        p.setPlayerListName("§9[G] §f" + p.getName());

        Location loc = SpawnManager.get("gardiyan");
        if (loc != null) p.teleport(loc);

        p.sendMessage("§9Gardiyan oldun!");
    }

    public static void markCriminal(Player p) {
        criminals.put(p.getUniqueId(), true);
    }

    public static boolean isCriminal(Player p) {
        return criminals.getOrDefault(p.getUniqueId(), false);
    }

    public static void addIllegalKill(Player g) {

        int c = guardKills.getOrDefault(g.getUniqueId(), 0) + 1;
        guardKills.put(g.getUniqueId(), c);

        if (c >= 3) {
            guardKills.remove(g.getUniqueId());
            setPrisoner(g);
        }
    }

    public static void respawn(Player p) {

        Location loc = isPrisoner(p)
                ? SpawnManager.get("mahkum")
                : SpawnManager.get("gardiyan");

        if (loc != null) {
            Bukkit.getScheduler().runTask(
                    me.byadolife.prisonbreak.PrisonBreak.getInstance(),
                    () -> p.teleport(loc)
            );
        }
    }
}
