package me.byadolife.prisonbreak.commands;

import me.byadolife.prisonbreak.managers.TeamManager;
import me.byadolife.prisonbreak.gui.TeamMenu;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class PBCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player p)) return true;

        if (args.length == 1 && args[0].equalsIgnoreCase("join")) {

            if (TeamManager.hasTeam(p)) {
                p.sendMessage("§cZaten takımdasın!");
                return true;
            }

            TeamMenu.open(p);
        }

        return true;
    }
}
