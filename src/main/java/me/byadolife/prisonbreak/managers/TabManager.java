package me.byadolife.prisonbreak.managers;

import me.byadolife.prisonbreak.team.TeamType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.UUID;

public class TabManager {

    private static final HashMap<UUID, TeamType> cache = new HashMap<>();

    private static Team prisoner;
    private static Team guard;

    // 🔥 TEK INIT METODU
    public static void setup() {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();

        prisoner = board.getTeam("pb_prisoner");
        if (prisoner == null) prisoner = board.registerNewTeam("pb_prisoner");

        guard = board.getTeam("pb_guard");
        if (guard == null) guard = board.registerNewTeam("pb_guard");

        prisoner.setPrefix("§6[M] §f");
        guard.setPrefix("§9[G] §f");
    }

    // 🔥 PLAYER JOIN RESET
    public static void setupPlayer(Player player) {
        if (player == null) return;

        if (prisoner == null || guard == null) setup();

        prisoner.removeEntry(player.getName());
        guard.removeEntry(player.getName());
    }

    public static void setPrisoner(Player player) {
        guard.removeEntry(player.getName());
        prisoner.addEntry(player.getName());
        cache.put(player.getUniqueId(), TeamType.PRISONER);
    }

    public static void setGuard(Player player) {
        prisoner.removeEntry(player.getName());
        guard.addEntry(player.getName());
        cache.put(player.getUniqueId(), TeamType.GUARD);
    }

    public static TeamType getTeam(Player player) {
        return cache.get(player.getUniqueId());
    }

    public static void clear(Player player) {
        if (player == null) return;

        prisoner.removeEntry(player.getName());
        guard.removeEntry(player.getName());
        cache.remove(player.getUniqueId());
    }
}
