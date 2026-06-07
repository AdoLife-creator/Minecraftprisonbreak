package me.byadolife.prisonbreak.managers;

import me.byadolife.prisonbreak.team.TeamType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.UUID;

public class TeamManager {

    private static final HashMap<UUID, TeamType> cache = new HashMap<>();

    private static Team prisoner;
    private static Team guard;

    public static void setup() {
        var board = Bukkit.getScoreboardManager().getMainScoreboard();

        prisoner = board.getTeam("pb_prisoner");
        if (prisoner == null) prisoner = board.registerNewTeam("pb_prisoner");

        guard = board.getTeam("pb_guard");
        if (guard == null) guard = board.registerNewTeam("pb_guard");

        prisoner.setPrefix("§6[M] §f");
        guard.setPrefix("§9[G] §f");
    }

    public static void setPrisoner(Player p) {
        guard.removeEntry(p.getName());
        prisoner.addEntry(p.getName());

        cache.put(p.getUniqueId(), TeamType.PRISONER);

        p.sendMessage("§aMahkum takımına katıldın!");

        SkinManager.setPrisonerSkin(p);
        p.teleport(SpawnManager.get("spawns.mahkum"));
    }

    public static void setGuard(Player p) {
        prisoner.removeEntry(p.getName());
        guard.addEntry(p.getName());

        cache.put(p.getUniqueId(), TeamType.GUARD);

        p.sendMessage("§9Gardiyan takımına katıldın!");

        SkinManager.setGuardSkin(p);
        p.teleport(SpawnManager.get("spawns.gardiyan"));
    }

    public static TeamType getTeam(Player p) {
        return cache.get(p.getUniqueId());
    }
}
