package me.byadolife.prisonbreak;

import me.byadolife.prisonbreak.commands.PBCommand;
import me.byadolife.prisonbreak.listeners.JoinListener;
import me.byadolife.prisonbreak.listeners.QuitListener;
import me.byadolife.prisonbreak.listeners.TeamMenuListener;
import me.byadolife.prisonbreak.managers.SpawnManager;
import me.byadolife.prisonbreak.managers.TabManager;
import me.byadolife.prisonbreak.managers.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class PrisonBreak extends JavaPlugin {

    private static PrisonBreak instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        // MANAGERS INIT
        SpawnManager.init(this);

        TeamManager.setup();
        TabManager.setupBoard();

        // ONLINE PLAYERS FIX (reload-safe)
        for (Player p : Bukkit.getOnlinePlayers()) {
            TabManager.setupPlayer(p);
        }

        // COMMANDS
        if (getCommand("pb") != null) {
            getCommand("pb").setExecutor(new PBCommand());
        }

        // LISTENERS
        getServer().getPluginManager().registerEvents(new TeamMenuListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);

        getLogger().info("PrisonBreak enabled!");
    }

    @Override
    public void onDisable() {

        // cleanup tab
        for (Player p : Bukkit.getOnlinePlayers()) {
            TabManager.clear(p);
        }

        instance = null;
    }

    public static PrisonBreak getInstance() {
        return instance;
    }
}
