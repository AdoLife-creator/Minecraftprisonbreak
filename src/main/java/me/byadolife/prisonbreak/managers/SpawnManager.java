package me.byadolife.prisonbreak.managers;

import me.byadolife.prisonbreak.PrisonBreak;
import org.bukkit.*;

public class SpawnManager {

    public static void save(Location loc, String path) {

        var cfg = PrisonBreak.getInstance().getConfig();

        cfg.set(path + ".world", loc.getWorld().getName());
        cfg.set(path + ".x", loc.getX());
        cfg.set(path + ".y", loc.getY());
        cfg.set(path + ".z", loc.getZ());
        cfg.set(path + ".yaw", loc.getYaw());
        cfg.set(path + ".pitch", loc.getPitch());

        PrisonBreak.getInstance().saveConfig();
    }

    public static Location get(String path) {

        var cfg = PrisonBreak.getInstance().getConfig();

        if(!cfg.contains(path + ".world"))
            return null;

        World world =
                Bukkit.getWorld(cfg.getString(path + ".world"));

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
