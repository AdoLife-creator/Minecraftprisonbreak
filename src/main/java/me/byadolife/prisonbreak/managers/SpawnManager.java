package me.byadolife.prisonbreak.managers;

import me.byadolife.prisonbreak.PrisonBreak;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class SpawnManager {

    private static PrisonBreak plugin;

    public static void init(PrisonBreak pl) {
        plugin = pl;
    }

    public static void save(Location loc, String path) {
        if (plugin == null || loc == null) return;

        var cfg = plugin.getConfig();

        cfg.set(path + ".world", loc.getWorld().getName());
        cfg.set(path + ".x", loc.getX());
        cfg.set(path + ".y", loc.getY());
        cfg.set(path + ".z", loc.getZ());
        cfg.set(path + ".yaw", loc.getYaw());
        cfg.set(path + ".pitch", loc.getPitch());

        plugin.saveConfig();
    }

    public static Location get(String path) {
        if (plugin == null) return null;

        var cfg = plugin.getConfig();

        if (!cfg.contains(path + ".world")) return null;

        World world = Bukkit.getWorld(cfg.getString(path + ".world"));
        if (world == null) return null;

        return new Location(
                world,
                cfg.getDouble(path + ".x"),
                cfg.getDouble(path + ".y"),
                cfg.getDouble(path + ".z"),
                (float) cfg.getDouble(path + ".yaw"),
                (float) cfg.getDouble(path + ".pitch")
        );
    }
}
