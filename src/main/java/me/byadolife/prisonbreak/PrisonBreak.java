package me.byadolife.prisonbreak;

import me.byadolife.prisonbreak.commands.PBCommand;
import me.byadolife.prisonbreak.listeners.DeathListener;
import me.byadolife.prisonbreak.listeners.GuardItemListener;
import me.byadolife.prisonbreak.listeners.JoinListener;
import me.byadolife.prisonbreak.listeners.QuitListener;
import me.byadolife.prisonbreak.listeners.TeamMenuListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class PrisonBreak extends JavaPlugin {

    private static PrisonBreak instance;

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();

        PBCommand command = new PBCommand();

        getCommand("pb").setExecutor(command);
        getCommand("pb").setTabCompleter(command);

        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        getServer().getPluginManager().registerEvents(new TeamMenuListener(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new GuardItemListener(), this);

        getLogger().info("PrisonBreak aktif!");
    }

    @Override
    public void onDisable() {
        getLogger().info("PrisonBreak kapatildi!");
    }

    public static PrisonBreak getInstance() {
        return instance;
    }
}
