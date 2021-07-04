package me.kp.moon.moonbans.commands;

import me.kp.moon.moonbans.Main;
import me.kp.moon.moonbans.enums.Messages;
import me.kp.moon.moonbans.mysql.MySQL;
import me.kp.moon.moonbans.utils.SysUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Alts implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String name = (sender instanceof Player) ? sender.getName() : "CONSOLE";
        if (command.getName().equalsIgnoreCase("alts")) {
            if (!sender.hasPermission("command.alts")) {
                sender.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(label + " <player>"));
                return true;
            }
            boolean success;
            if (args[0].contains(".") || args[0].length() < 3 || args[0].length() > 16) {
                sender.sendMessage("§cNick inválido.");
                return true;
            } else {
                success = MySQL.UsernameExiste(args[0]);
            }
            if (!success) {
                sender.sendMessage("§cNão foi possível encontrar esse player no banco de dados.");
                return true;
            }
            SysUtils.sendMessageToStaff("§aIniciando busca por alts do player §e" + args[0] + "§a...");
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                List<String> alts = MySQL.getAlts(args[0], false);
                if (alts == null) {
                    SysUtils.sendMessageToStaff("§cErro ao checar alts do player §e" + args[0] + "§c.");
                    return;
                }
                for (String alt : alts) {
                    if (Bukkit.getOnlinePlayers().stream().filter(player -> player.getName().equalsIgnoreCase(alt)).count() >= 1) {
                        alts.set(alts.indexOf(alt), "§a" + alt);
                    } else if (MySQL.isBanned(args[0], false) == 1) {
                        alts.set(alts.indexOf(alt), "§c" + alt);
                    } else {
                        alts.set(alts.indexOf(alt), "§8" + alt);
                    }
                }

                String message = "§9Registro de contas no IP do player §e" + args[0] + "§a: §7[§aOnline§7, §cBanido§7, §8Offline§7]\n" +
                        StringUtils.join(alts, "§7, ") + "§7.";
                SysUtils.sendMessageToStaff(message);

            });
        }
        return false;
    }
}
