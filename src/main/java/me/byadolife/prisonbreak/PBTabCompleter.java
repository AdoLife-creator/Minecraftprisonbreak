package me.byadolife.prisonbreak.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class PBTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender,
                                      Command command,
                                      String alias,
                                      String[] args) {

        List<String> list = new ArrayList<>();

        if (args.length == 1) {
            list.add("join");
            list.add("leave");
            list.add("setspawn");
            list.add("reload");
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("setspawn")) {
            list.add("lobby");
            list.add("mahkum");
            list.add("gardiyan");
        }

        return list;
    }
}
