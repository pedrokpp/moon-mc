package me.kp.moon.moonbans.commands;

import me.kp.moon.moonbans.Main;
import me.kp.moon.moonbans.enums.Messages;
import me.kp.moon.moonbans.enums.Strings;
import me.kp.moon.moonbans.mysql.MySQL;
import me.kp.moon.moonbans.utils.SysUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Ipban implements CommandExecutor {

    private String ipOrName;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String name = sender instanceof Player ? sender.getName() : "CONSOLE";
        if (command.getName().equalsIgnoreCase("ipban")) {
            if (!sender.hasPermission("command.ipban")) {
                sender.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(label + " <player/ip> <motivo>"));
                return true;
            }
            String reason = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
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
                    sender.sendMessage("§cOcorreu um erro ao banir o " + ipOrName + " §e" + args[0] + "§c.");
                    return true;
                }
                else if (isBanned == 1) {
                    sender.sendMessage("§cEste " + ipOrName +" já está banido.");
                    return true;
                }
                Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                        MySQL.applyBan(args[0], -1L, reason, name, false);
                });
                boolean silent = reason.contains("-s");
                Bukkit.getOnlinePlayers().forEach(player -> {
                    if (player.getName().equalsIgnoreCase(args[0]) || player.getAddress().getHostName().equalsIgnoreCase(args[0])) {
                        player.kickPlayer(Strings.getPermaBanMessage());
                    }
                });
                sender.sendMessage("§aBanimento §ePERMANENTE §aaplicado ao " + ipOrName + " §e" + args[0] + "§a.");
                if (!silent) SysUtils.alertPlayers();
                SysUtils.alertBanimentoStaff(args[0], reason.replace("-s", ""), name, "PERMANENTE", silent);
            }
        }
        return false;
    }
}
