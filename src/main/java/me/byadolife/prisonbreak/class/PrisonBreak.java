package me.byadolife.prisonbreak;

import me.byadolife.prisonbreak.commands.PBCommand;
import me.byadolife.prisonbreak.listeners.JoinListener;
import me.byadolife.prisonbreak.listeners.QuitListener;
import me.byadolife.prisonbreak.listeners.TeamMenuListener;
import me.byadolife.prisonbreak.managers.TabManager;
import me.byadolife.prisonbreak.managers.TeamManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class PrisonBreak extends JavaPlugin {

    private static PrisonBreak instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        TeamManager.setup();
        TabManager.reset(null); // güvenli init

        getCommand("pb").setExecutor(new PBCommand());

        getServer().getPluginManager().registerEvents(new TeamMenuListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
    }

    @Override
    public void onDisable() {}

    public static PrisonBreak getInstance() {
        return instance;
    }
}
