package me.byadolife.prisonbreak.managers;

import me.byadolife.prisonbreak.team.TeamType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TeamManager {

    private static final HashMap<UUID, TeamType> teams = new HashMap<>();
    private static final HashMap<UUID, Integer> guardKills = new HashMap<>();
    private static final HashMap<UUID, Boolean> criminals = new HashMap<>();

    public static TeamType getTeam(Player player) {
        return teams.get(player.getUniqueId());
    }

    public static boolean isPrisoner(Player player) {
        return getTeam(player) == TeamType.PRISONER;
    }

    public static boolean isGuard(Player player) {
        return getTeam(player) == TeamType.GUARD;
    }

    public static void remove(Player player) {

        teams.remove(player.getUniqueId());
        guardKills.remove(player.getUniqueId());
        criminals.remove(player.getUniqueId());
    }

    public static void setPrisoner(Player player) {

        if (isPrisoner(player)) {
            player.sendMessage("§cZaten mahkumsun.");
            return;
        }

        teams.put(player.getUniqueId(), TeamType.PRISONER);

        SkinManager.setPrisonerSkin(player);

        Location spawn = SpawnManager.getSpawn("mahkum");

        if (spawn != null) {
            player.teleport(spawn);
        }

        player.sendMessage("§6Mahkum takımına katıldın!");
    }

    public static void setGuard(Player player) {

        if (isGuard(player)) {
            player.sendMessage("§cZaten gardiyansın.");
            return;
        }

        teams.put(player.getUniqueId(), TeamType.GUARD);

        SkinManager.setGuardSkin(player);

        GuardKitManager.giveKit(player);

        Location spawn = SpawnManager.getSpawn("gardiyan");

        if (spawn != null) {
            player.teleport(spawn);
        }

        player.sendMessage("§9Gardiyan takımına katıldın!");
    }

    public static void markCriminal(Player player) {

        criminals.put(
                player.getUniqueId(),
                true
        );

        player.sendMessage(
                "§cBir gardiyana saldırdın, artık aranıyorsun!"
        );
    }

    public static boolean isCriminal(Player player) {

        return criminals.getOrDefault(
                player.getUniqueId(),
                false
        );
    }

    public static void clearCriminal(Player player) {

        criminals.remove(
                player.getUniqueId()
        );
    }

    public static void addIllegalKill(Player guard) {

        int amount =
                guardKills.getOrDefault(
                        guard.getUniqueId(),
                        0
                ) + 1;

        guardKills.put(
                guard.getUniqueId(),
                amount
        );

        guard.sendMessage(
                "§cMasum mahkum öldürdün! (" +
                        amount +
                        "/3)"
        );

        if (amount >= 3) {

            guardKills.remove(
                    guard.getUniqueId()
            );

            guard.sendMessage(
                    "§4Masum mahkumları öldürdüğün için mahkum oldun!"
            );

            setPrisoner(guard);
        }
    }
}
