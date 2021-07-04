package me.kp.moon.moonpvp.commands;

import me.kp.moon.moonpvp.enums.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Aplicar implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player)sender;
            player.sendMessage("§aAcesse o nosso discord para se aplicar a equipe. §7(/discord)");
            player.chat("/discord");
        }
        else {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
        }
        return true;
    }

}
