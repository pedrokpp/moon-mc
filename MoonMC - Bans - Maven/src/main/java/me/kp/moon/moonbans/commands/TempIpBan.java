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

public class TempIpBan implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String name = sender instanceof Player ? sender.getName() : "CONSOLE";
        if (command.getName().equalsIgnoreCase("tempipban")) {
            if (!sender.hasPermission("command.tempipban")) {
                sender.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(label + " <player/ip> <tempo> <motivo>"));
                return true;
            }
            String ipOrName = null;
            long ms = System.currentTimeMillis();
            String reason = StringUtils.join(Arrays.copyOfRange(args, 2, args.length), " ");
            String tempo = args[1].toLowerCase();
            if (!(tempo.endsWith("d") || tempo.endsWith("h") || tempo.endsWith("m") || tempo.endsWith("s"))) {
                sender.sendMessage("§cFormato de tempo inválido. §7(Disponíveis: d, h, m, s)");
                return true;
            }
            try {
                if (tempo.endsWith("d")) ms = SysUtils.addDaysToMs(Integer.parseInt(tempo.replace("d", "")), ms);
                if (tempo.endsWith("h")) ms = SysUtils.addHoursToMs(Integer.parseInt(tempo.replace("h", "")), ms);
                if (tempo.endsWith("m")) ms = SysUtils.addMinutesToMs(Integer.parseInt(tempo.replace("m", "")), ms);
                if (tempo.endsWith("s")) ms = SysUtils.addSecondsToMs(Integer.parseInt(tempo.replace("s", "")), ms);
            } catch (NumberFormatException exception) {
                sender.sendMessage("§cFalha ao converter formato de tempo. §7(tenha certeza de ter digitado certo)");
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
                    sender.sendMessage("§cOcorreu um erro ao banir o " + ipOrName + " §e" + args[0] + "§c.");
                    return true;
                }
                else if (isBanned == 1) {
                    sender.sendMessage("§cEste " + ipOrName +" já está banido.");
                    return true;
                }

                long finalMs = ms;
                Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                    MySQL.applyBan(args[0], finalMs, reason, name, false);
                });
                boolean silent = reason.contains("-s");
                long finalMs1 = ms;
                Bukkit.getOnlinePlayers().forEach(player -> {
                    if (player.getName().equalsIgnoreCase(args[0]) || player.getAddress().getHostName().equalsIgnoreCase(args[0])) {
                        player.kickPlayer(Strings.getTempBanMessage(finalMs1));
                    }
                });
                sender.sendMessage("§aBanimento §eTEMPORÁRIO §aaplicado ao " + ipOrName + " §e" + args[0] + "§a.");
                if (!silent) SysUtils.alertPlayers();
                SysUtils.alertBanimentoStaff(args[0], reason.replace("-s", ""), name, tempo, silent);
            }
        }
        return false;
    }

}
