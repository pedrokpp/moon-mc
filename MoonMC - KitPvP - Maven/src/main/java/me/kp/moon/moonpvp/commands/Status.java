package me.kp.moon.moonpvp.commands;

import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.gui.StatusGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Status implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("status") || command.getName().equalsIgnoreCase("profile")) {
            if (args.length == 0) {
                StatusGUI.openGUI(player, player);
                return false;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("§cNão foi possível encontrar o player §e" + args[0] + "§c.");
                return true;
            }
            StatusGUI.openGUI(target, player);
        }
        return false;
    }
}
