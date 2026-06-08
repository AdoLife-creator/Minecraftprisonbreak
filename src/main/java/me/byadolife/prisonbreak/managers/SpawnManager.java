package me.byadolife.prisonbreak.managers;

import me.byadolife.prisonbreak.PrisonBreak;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SpawnManager {

    public static Location getSpawn(String team) {

        var cfg = PrisonBreak.getInstance().getConfig();

        String path = "teams." + team + ".spawn";

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
