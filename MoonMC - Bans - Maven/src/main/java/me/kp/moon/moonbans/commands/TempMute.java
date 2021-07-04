package me.kp.moon.moonbans.commands;

import me.kp.moon.moonbans.Main;
import me.kp.moon.moonbans.data.PlayerData;
import me.kp.moon.moonbans.data.PlayerDataManager;
import me.kp.moon.moonbans.enums.Messages;
import me.kp.moon.moonbans.mysql.MySQL;
import me.kp.moon.moonbans.utils.SysUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class TempMute implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String name = sender instanceof Player ? sender.getName() : "CONSOLE";
        if (command.getName().equalsIgnoreCase("tempmute")) {
            if (!sender.hasPermission("command.tempmute")) {
                sender.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(label + " <player> <tempo> <motivo>"));
                return true;
            }
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
            boolean isOnline;
            if (args[0].contains(".") || args[0].length() < 3 || args[0].length() > 16) {
                sender.sendMessage("§cNick inválido.");
                return true;
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                isOnline = target != null;
                if (!isOnline) {
                    success = MySQL.UsernameExiste(args[0]);
                    if (!success) {
                        sender.sendMessage("§cNão foi possível encontrar esse player/ip no banco de dados.");
                        return true;
                    } else {
                        int isMuted = MySQL.isMuted(args[0], false);
                        if (isMuted == -1) {
                            sender.sendMessage("§cOcorreu um erro ao mutar o player §e" + args[0] + "§c.");
                            return true;
                        }
                        else if (isMuted == 1) {
                            sender.sendMessage("§cEste player já está mutado.");
                            return true;
                        }
                        long finalMs = ms;
                        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                            MySQL.applyMute(args[0], finalMs, reason, name, false);
                        });
                        sender.sendMessage("§aMute §eTEMPORÁRIO §aaplicado ao player §e" + args[0] + "§a.");
                        SysUtils.alertMutetoStaff(args[0], reason, name, tempo);
                    }
                } else {
                    PlayerData targetData = PlayerDataManager.getPlayerData(target);
                    if (targetData == null) {
                        sender.sendMessage("§cNão foi possível resgatar PlayerData deste player.");
                        return true;
                    }
                    if (targetData.isMuted) {
                        sender.sendMessage("§cEste player já está mutado.");
                        return true;
                    }
                    targetData.setMuted(true);
                    targetData.setMuteReason(reason);
                    targetData.setMuteAuthor(name);
                    targetData.setCacheTempMuteTime(ms);
                    sender.sendMessage("§aMute §eTEMPORÁRIO §aaplicado ao player §e" + args[0] + "§a.");
                    SysUtils.alertMutetoStaff(args[0], reason, name, tempo);
                }
            }
        }
        return false;
    }
    
}
