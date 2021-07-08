package me.kp.moon.moonpvp.commands.staff;

import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.kit.KitType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveKit implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("removekit")) {
            if (!sender.hasPermission("command.removekit")) {
                sender.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(label + " <player> <kit>"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage("§cNão foi possível encontrar o player §e" + args[0] + "§c.");
                return true;
            }
            KitType kit = KitType.getKitTypeByName(args[1]);
            if (kit == null) {
                sender.sendMessage("§cNão foi possível encontrar o kit \"§e" + args[1] + "§c\".");
                return true;
            }
            if (!target.hasPermission("kit." + kit.getKitname().toLowerCase())) {
                sender.sendMessage("§cEsse player não possui esse kit.");
                return true;
            }
            sender.sendMessage("§aVocê removeu o kit §e" + kit.getKitname() + " §ade §e" + target.getName() + "§a.");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + target.getName() + " permission unset kit." + kit.getKitname().toLowerCase());
        }
        return false;
    }

}
