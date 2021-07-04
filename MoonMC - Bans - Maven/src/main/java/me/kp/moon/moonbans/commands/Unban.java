package me.kp.moon.moonbans.commands;

import me.kp.moon.moonbans.Main;
import me.kp.moon.moonbans.enums.Messages;
import me.kp.moon.moonbans.mysql.MySQL;
import me.kp.moon.moonbans.utils.SysUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Unban implements CommandExecutor {

    private String ipOrName;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String name = sender instanceof Player ? sender.getName() : "CONSOLE";
        if (command.getName().equalsIgnoreCase("unban")) {
            if (!sender.hasPermission("command.unban")) {
                sender.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(label + " <player/ip>"));
                return true;
            }
            boolean success;
            if (args[0].contains(".")) {
                success = MySQL.IPExiste(args[0]);
                if (success) ipOrName = "IP";
            } else {
                success = MySQL.UsernameExiste(args[0]);
                if (success) ipOrName = "player";
            }
            if (!success) {
                sender.sendMessage("§cNão foi possível encontrar esse player/ip no banco de dados.");
                return true;
            } else {
                int isBanned = MySQL.isBanned(args[0], false);
                if (isBanned == -1) {
                    sender.sendMessage("§cOcorreu um erro ao checar o " + ipOrName + " §e" + args[0] + "§c.");
                    return true;
                }
                else if (isBanned == 0) {
                    sender.sendMessage("§cEste " + ipOrName +" não está banido.");
                    return true;
                }
                Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                    MySQL.applyUnban(args[0], false);
                });
                sender.sendMessage("§aBanimento do " + ipOrName + " §e" + args[0] + " §arevogado.");
                SysUtils.alertUnbanStaff(args[0], name);
            }
        }
        return false;
    }

}
