package me.byadolife.prisonbreak.commands;

import me.byadolife.prisonbreak.gui.TeamMenu;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class PBCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {

        if(!(sender instanceof Player player))
            return true;

        if(args.length == 1 &&
                args[0].equalsIgnoreCase("join")) {

            TeamMenu.open(player);
            return true;
        }

        player.sendMessage("§6PrisonBreak");
        player.sendMessage("§7/pb join");
        return true;
    }
}
