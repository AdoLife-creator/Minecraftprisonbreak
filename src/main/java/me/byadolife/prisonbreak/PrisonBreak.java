package me.byadolife.prisonbreak;

import me.byadolife.prisonbreak.manager.GameManager;
import me.byadolife.prisonbreak.manager.LocationManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class PrisonBreak extends JavaPlugin {

    private static PrisonBreak instance;

    private GameManager gameManager;
    private LocationManager locationManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        locationManager = new LocationManager(this);
        gameManager = new GameManager(this, locationManager);

        getLogger().info("PrisonBreak aktif!");
    }

    @Override
    public void onDisable() {
        getLogger().info("PrisonBreak kapandı!");
    }

    public static PrisonBreak getInstance() {
        return instance;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }
}
