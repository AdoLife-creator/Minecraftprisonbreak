package me.byadolife.prisonbreak.commands;

import me.byadolife.prisonbreak.gui.TeamMenu;
import me.byadolife.prisonbreak.managers.SpawnManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PBCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Bu komut sadece oyuncular içindir.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§6=== PrisonBreak ===");
            player.sendMessage("§e/pb join");
            player.sendMessage("§e/pb setspawn lobby");
            player.sendMessage("§e/pb setspawn mahkum");
            player.sendMessage("§e/pb setspawn gardiyan");
            return true;
        }

        if (args[0].equalsIgnoreCase("join")) {
            TeamMenu.open(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("setspawn")) {

            if (!player.hasPermission("prisonbreak.admin")) {
                player.sendMessage("§cYetkin yok.");
                return true;
            }

            if (args.length < 2) {
                player.sendMessage("§cKullanım:");
                player.sendMessage("§e/pb setspawn lobby");
                player.sendMessage("§e/pb setspawn mahkum");
                player.sendMessage("§e/pb setspawn gardiyan");
                return true;
            }

            String type = args[1].toLowerCase();

            if (!type.equals("lobby")
                    && !type.equals("mahkum")
                    && !type.equals("gardiyan")) {

                player.sendMessage("§cGeçersiz spawn tipi.");
                return true;
            }

            SpawnManager.saveSpawn(type, player.getLocation());

            player.sendMessage(
                    "§a" + type + " spawnı başarıyla kaydedildi."
            );

            return true;
        }

        player.sendMessage("§cBilinmeyen komut.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,
                                      Command command,
                                      String alias,
                                      String[] args) {

        List<String> list = new ArrayList<>();

        if (args.length == 1) {
            list.add("join");
            list.add("setspawn");
        }

        if (args.length == 2
                && args[0].equalsIgnoreCase("setspawn")) {

            list.add("lobby");
            list.add("mahkum");
            list.add("gardiyan");
        }

        return list;
    }
}
