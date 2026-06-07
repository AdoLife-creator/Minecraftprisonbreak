package me.byadolife.prisonbreak;

import me.byadolife.prisonbreak.listener.PlayerListener;
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

        getServer().getPluginManager().registerEvents(
                new PlayerListener(gameManager),
                this
        );

        getLogger().info("PrisonBreak enabled (Paper 1.21.4)");
    }

    @Override
    public void onDisable() {
        getLogger().info("PrisonBreak disabled");
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
