package me.byadolife.prisonbreak.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SpawnManager {

    public static Location get(String key) {

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
