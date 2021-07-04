package me.kp.moon.moonpvp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class GrantTabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("grant")) {
            if (args.length >= 1) {
                return Arrays.asList("command.", "kit.", "pax.alerts", "worldedit");
            }
        }
        return null;
    }

}
