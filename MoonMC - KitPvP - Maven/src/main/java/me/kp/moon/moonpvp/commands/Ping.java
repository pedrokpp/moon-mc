package me.kp.moon.moonpvp.commands;

import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ping implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("ping")) {
            if (args.length == 0) {
                player.sendMessage("§aSeu ping é §e" + PlayerUtils.getPlayerPing(player) + "ms§a.");
                return false;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("§cNão foi possível encontrar o player §e" + args[0] + "§c.");
                return true;
            }
            player.sendMessage("§aO ping de §e" + target.getName() + " §aé §e" + PlayerUtils.getPlayerPing(target) + "ms§a.");
        }
        return false;
    }
}
