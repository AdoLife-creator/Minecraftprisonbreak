package me.byadolife.prisonbreak.managers;

import me.byadolife.prisonbreak.team.TeamType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.UUID;

public class TeamManager {

    private static final HashMap<UUID, TeamType> teams = new HashMap<>();

    private static Team prisonerTeam;
    private static Team guardTeam;

    public static void setup() {

        Scoreboard board =
                Bukkit.getScoreboardManager().getMainScoreboard();

        prisonerTeam = board.getTeam("pb_prisoner");
        if(prisonerTeam == null)
            prisonerTeam = board.registerNewTeam("pb_prisoner");

        guardTeam = board.getTeam("pb_guard");
        if(guardTeam == null)
            guardTeam = board.registerNewTeam("pb_guard");

        prisonerTeam.setPrefix("§6[M] §f");
        guardTeam.setPrefix("§9[G] §f");
    }

    public static void setPrisoner(Player player) {

        guardTeam.removeEntry(player.getName());

        prisonerTeam.addEntry(player.getName());

        teams.put(player.getUniqueId(), TeamType.PRISONER);

        player.sendMessage("§6Mahkum takımına katıldın!");
    }

    public static void setGuard(Player player) {

        prisonerTeam.removeEntry(player.getName());

        guardTeam.addEntry(player.getName());

        teams.put(player.getUniqueId(), TeamType.GUARD);

        player.sendMessage("§9Gardiyan takımına katıldın!");
    }
}
