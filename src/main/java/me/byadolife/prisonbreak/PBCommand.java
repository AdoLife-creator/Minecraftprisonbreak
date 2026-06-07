package me.byadolife.prisonbreak.commands;

import me.byadolife.prisonbreak.gui.TeamMenu;
import me.byadolife.prisonbreak.managers.SpawnManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PBCommand implements CommandExecutor, TabCompleter {

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

        if(args.length == 2 &&
                args[0].equalsIgnoreCase("setspawn")) {

            if(!player.hasPermission("prisonbreak.admin")) {
                player.sendMessage("§cYetkin yok.");
                return true;
            }

            SpawnManager.save(
                    player.getLocation(),
                    "spawns." + args[1].toLowerCase()
            );

            player.sendMessage(
                    "§aSpawn kaydedildi: §e" + args[1]
            );

            return true;
        }

        player.sendMessage("§e/pb join");
        player.sendMessage("§e/pb setspawn lobby");
        player.sendMessage("§e/pb setspawn mahkum");
        player.sendMessage("§e/pb setspawn gardiyan");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,
                                      Command command,
                                      String alias,
                                      String[] args) {

        List<String> list = new ArrayList<>();

        if(args.length == 1) {
            list.add("join");
            list.add("setspawn");
        }

        if(args.length == 2 &&
                args[0].equalsIgnoreCase("setspawn")) {

            list.add("lobby");
            list.add("mahkum");
            list.add("gardiyan");
        }

        return list;
    }
}
