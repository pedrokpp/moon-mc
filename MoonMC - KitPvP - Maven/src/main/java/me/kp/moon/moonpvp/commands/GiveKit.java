package me.kp.moon.moonpvp.commands;

import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.kit.KitType;
import me.kp.moon.moonpvp.utils.SysUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveKit implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("givekit")) {
            if (!sender.hasPermission("command.givekit")) {
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
            if (target.hasPermission("kit." + kit.getKitname().toLowerCase())) {
                sender.sendMessage("§cEsse player já possui esse kit.");
                return true;
            }
            sender.sendMessage("§aO player §e" + target.getName() + " §crecebeu o kit §e" + kit.getKitname() + "§a.");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + target.getName() + " permission set kit." + kit.getKitname().toLowerCase());
            SysUtils.sendTitle(target, "§a§lPARABÉNS", "§aVocê recebeu o kit §e" + kit.getKitname() + "§a!");
            SysUtils.sendTitle(target, "§a§lPARABÉNS", "§aVocê recebeu o kit §e" + kit.getKitname() + "§a!");
            target.playSound(target.getLocation(), Sound.FIREWORK_LARGE_BLAST2, 1f, 1f);
            target.playSound(target.getLocation(), Sound.LEVEL_UP, 1f, 1f);
        }
        return false;
    }

}
