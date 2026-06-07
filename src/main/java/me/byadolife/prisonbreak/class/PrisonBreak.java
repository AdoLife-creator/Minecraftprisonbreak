package me.byadolife.prisonbreak;

import me.byadolife.prisonbreak.commands.PBCommand;
import me.byadolife.prisonbreak.listeners.JoinListener;
import me.byadolife.prisonbreak.listeners.TeamMenuListener;
import me.byadolife.prisonbreak.managers.TeamManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class PrisonBreak extends JavaPlugin {

    private static PrisonBreak instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        TeamManager.setup();

        PBCommand pbCommand = new PBCommand();

        getCommand("pb").setExecutor(pbCommand);
        getCommand("pb").setTabCompleter(pbCommand);

        getServer().getPluginManager().registerEvents(
                new TeamMenuListener(),
                this
        );

        getServer().getPluginManager().registerEvents(
                new JoinListener(),
                this
        );

        getLogger().info("PrisonBreak enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("PrisonBreak disabled.");
    }

    public static PrisonBreak getInstance() {
        return instance;
    }
}
