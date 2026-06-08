package me.byadolife.prisonbreak.managers;

import me.byadolife.prisonbreak.PrisonBreak;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class SpawnManager {

    public static void saveSpawn(String name, Location loc) {

        FileConfiguration cfg =
                PrisonBreak.getInstance().getConfig();

        cfg.set("spawns." + name + ".world",
                loc.getWorld().getName());

        cfg.set("spawns." + name + ".x",
                loc.getX());

        cfg.set("spawns." + name + ".y",
                loc.getY());

        cfg.set("spawns." + name + ".z",
                loc.getZ());

        cfg.set("spawns." + name + ".yaw",
                loc.getYaw());

        cfg.set("spawns." + name + ".pitch",
                loc.getPitch());

        PrisonBreak.getInstance().saveConfig();
    }

    public static Location getSpawn(String name) {

        FileConfiguration cfg =
                PrisonBreak.getInstance().getConfig();

        if (!cfg.contains("spawns." + name + ".world"))
            return null;

        World world =
                Bukkit.getWorld(
                        cfg.getString("spawns." + name + ".world")
                );

        if (world == null)
            return null;

        return new Location(
                world,
                cfg.getDouble("spawns." + name + ".x"),
                cfg.getDouble("spawns." + name + ".y"),
                cfg.getDouble("spawns." + name + ".z"),
                (float) cfg.getDouble("spawns." + name + ".yaw"),
                (float) cfg.getDouble("spawns." + name + ".pitch")
        );
    }
}
