package me.kp.moon.moonpvp.commands;

import me.kp.moon.moonpvp.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Sudo implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("sudo")) {
            if (!player.hasPermission("command.sudo")) {
                player.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }
            if (args.length == 0) {
                player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(command.getName() + " <player>"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("§cNão foi possível encontrar o player §e" + args[0] + "§c.");
                return true;
            }
            String cc = String.join(" ", args).replace(args[0] + " ", "");
            target.chat(cc);
            String warn = cc.startsWith("/") ? "§aVocê forçou §e" + target.getName() + " §aa executar: §7" + cc :
                    "§aVocê forçou §e" + target.getName() + " §aa falar: §7" + cc;
            String notif = cc.startsWith("/") ? "§cO player §e" + player.getName() + " §cforçou você a executar: §7" + cc :
                    "§cO player §e" + player.getName() + " §cforçou você a falar: §7" + cc;
            player.sendMessage(warn);
            target.sendMessage(notif);
        }

        return false;
    }
}
