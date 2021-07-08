package me.kp.moon.moonpvp.commands.staff;

import me.kp.moon.moonpvp.cache.SysCache;
import me.kp.moon.moonpvp.enums.Messages;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Reports implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("reports")) {
            if (!player.hasPermission("command.reports")) {
                player.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }
            if (args.length == 0) {
                if (SysCache.cacheReports.size() == 0) {
                    player.sendMessage("§cNão há nenhum report em cache para nenhum player.");
                    return false;
                }
                String layout = "§cLista dos últimos reports: \n \n";
                StringBuilder fullMessage = new StringBuilder();

                SysCache.cacheReports.forEach((nick, report) -> fullMessage.append("§c").append(nick).append(": §7")
                        .append(StringUtils.join(report, "§c, §7")).append(" §8[").append(report.size())
                        .append("x]").append("\n"));

                player.sendMessage(layout + fullMessage);
                return false;
            }
            if (SysCache.cacheReports.containsKey(args[0])) {
                List<String> cache = SysCache.cacheReports.get(args[0]);
                player.sendMessage("§cLista de reports do player §e" + args[0] + "§c:\n \n§7" +
                        StringUtils.join(cache, "§c, §7") + " §8[" + cache.size() + "x]");
            } else {
                player.sendMessage("§cNão foi possível encontrar nenhum report em cache para esse player.");
                return true;
            }
        }
        return false;
    }
}
