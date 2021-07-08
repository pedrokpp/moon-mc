package me.kp.moon.moonpvp.commands.staff;

import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.utils.ItemUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Head implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player)sender;
            if (player.hasPermission("command.head")) {
                if (args.length > 0) {
                    player.getInventory().addItem(ItemUtils.getPlayerSkull(args[0]));
                    player.sendMessage("§aA cabeça do player §e" + args[0] + " §afoi adicionada ao seu inventário.");
                }
                else {
                    player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(label + " <player>"));
                }
            }
            else {
                player.sendMessage(Messages.SEM_PERMISSAO.getMessage());
            }
        }
        else {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
        }
        return true;
    }

}
