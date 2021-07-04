package me.kp.moon.moonbans.commands;

import me.kp.moon.moonbans.enums.Messages;
import me.kp.moon.moonbans.mysql.MySQL;
import me.kp.moon.moonbans.utils.SysUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.text.DateFormat;

public class Checkmute implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("checkmute")) {
            if (!sender.hasPermission("command.checkmute")) {
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
            } else {
                int isMuted = MySQL.isMuted(args[0], false);
                if (isMuted == -1) {
                    sender.sendMessage("§cOcorreu um erro ao checar o player §e" + args[0] + "§c.");
                    return true;
                } else if (isMuted == 0) {
                    sender.sendMessage("§cEste player não está mutado.");
                    return true;
                }
                long time = MySQL.getTempBanTime(args[0], false);
                sender.sendMessage("§aInformações sobre o mute do player §e" + args[0] + "§a:\n" +
                        "§fAutor: §7" + MySQL.getBanAuthor(args[0], false) + "\n" +
                        "§fMotivo: §7" + MySQL.getBanReason(args[0], false) + "\n" +
                        "§fData do fim do mute: §7" + (time == -1L ? "§cNUNCA" : SysUtils.timeConverter(time)) + "\n" +
                        "§fData do mute: §7" + DateFormat.getInstance().format(MySQL.getMuteDate(args[0], false)).replace("00:00", ""));
            }
        }
        return false;
    }
}
