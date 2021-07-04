package me.kp.moon.moonbans.commands;

import me.kp.moon.moonbans.enums.Messages;
import me.kp.moon.moonbans.mysql.MySQL;
import me.kp.moon.moonbans.utils.SysUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.text.DateFormat;

public class Checkban implements CommandExecutor {

    private String ipOrName;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("checkban")) {
            if (!sender.hasPermission("command.checkban")) {
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
                long time = MySQL.getTempBanTime(args[0], false);
                sender.sendMessage("§aInformações sobre o banimento do " + ipOrName + " §e" + args[0] + "§a:\n" +
                        "§fAutor: §7" + MySQL.getBanAuthor(args[0], false) + "\n" +
                        "§fMotivo: §7" + MySQL.getBanReason(args[0], false) + "\n" +
                        "§fData do fim do banimento: §7" + (time == -1L ? "§cNUNCA" : SysUtils.timeConverter(time)) + "\n" +
                        "§fData do banimento: §7" + DateFormat.getInstance().format(MySQL.getBanDate(args[0], false)).replace("00:00", ""));
            }
        }
        return false;
    }
}
