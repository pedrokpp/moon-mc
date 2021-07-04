package me.kp.moon.moonbans.commands;

import me.kp.moon.moonbans.enums.Messages;
import me.kp.moon.moonbans.enums.Strings;
import me.kp.moon.moonbans.utils.SysUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Kick implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String name = sender instanceof Player ? sender.getName() : "CONSOLE";
        if (command.getName().equalsIgnoreCase("kick")) {
            if (!sender.hasPermission("command.kick")) {
                sender.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getMessage());
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(label + " <player> <motivo>"));
                return true;
            }
            String reason = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
            if (args[0].equalsIgnoreCase("*")) {
                sender.sendMessage("§aVocê kickou todos os players do servidor.");
                Bukkit.getOnlinePlayers().stream().filter(player -> player != sender).forEach(p->p.kickPlayer(Strings.getKickMessage(reason)));
                SysUtils.alertKickStaff(args[0], reason, name);
                return false;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage("§cNenhum player online possui o nick \"§e" + args[0] + "§c\".");
                return true;
            }
            target.kickPlayer(Strings.getKickMessage(reason));
            sender.sendMessage("§aO player §e" + target.getName() + " §afoi expulso do servidor.");
            SysUtils.alertKickStaff(target.getName(), reason, name);
        }
        return false;
    }

}
