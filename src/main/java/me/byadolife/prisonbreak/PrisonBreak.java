package me.byadolife.prisonbreak;

import me.byadolife.prisonbreak.commands.PBCommand;
import me.byadolife.prisonbreak.listeners.*;
import me.byadolife.prisonbreak.managers.*;
import org.bukkit.plugin.java.JavaPlugin;

public class PrisonBreak extends JavaPlugin {

    private static PrisonBreak instance;

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();

        getCommand("pb").setExecutor(new PBCommand());

        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        getServer().getPluginManager().registerEvents(new DamageListener(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);

        TabManager.setup();
    }

    public static PrisonBreak getInstance() {
        return instance;
    }
}
