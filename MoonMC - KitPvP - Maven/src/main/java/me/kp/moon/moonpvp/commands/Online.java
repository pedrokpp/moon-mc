package me.kp.moon.moonpvp.commands;

import me.kp.moon.moonpvp.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Online implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("online") || command.getName().equalsIgnoreCase("list")) {
            player.sendMessage("§aQuantidade de players online: §e" + Bukkit.getOnlinePlayers().size() + " players§a.");
        }
        return false;
    }
}
