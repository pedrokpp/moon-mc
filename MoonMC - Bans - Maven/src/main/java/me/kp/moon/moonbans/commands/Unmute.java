package me.kp.moon.moonbans.commands;

import me.kp.moon.moonbans.Main;
import me.kp.moon.moonbans.data.PlayerData;
import me.kp.moon.moonbans.data.PlayerDataManager;
import me.kp.moon.moonbans.enums.Messages;
import me.kp.moon.moonbans.mysql.MySQL;
import me.kp.moon.moonbans.utils.SysUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Unmute implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String name = sender instanceof Player ? sender.getName() : "CONSOLE";
        if (command.getName().equalsIgnoreCase("unmute")) {
            if (!sender.hasPermission("command.unmute")) {
                sender.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }
            if (args.length < 1) {
                sender.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(label + " <player>"));
                return true;
            }
            String reason = "";
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
                        else if (isMuted != 1) {
                            sender.sendMessage("§cEste player não está mutado.");
                            return true;
                        }
                        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                            MySQL.applyUnmute(args[0], false);
                        });
                        sender.sendMessage("§aUnmute §aaplicado ao player §e" + args[0] + "§a.");
                        SysUtils.alertUnmuteStaff(args[0], name);
                    }
                } else {
                    PlayerData targetData = PlayerDataManager.getPlayerData(target);
                    if (targetData == null) {
                        sender.sendMessage("§cNão foi possível resgatar PlayerData deste player.");
                        return true;
                    }
                    if (!targetData.isMuted) {
                        sender.sendMessage("§cEste player não está mutado.");
                        return true;
                    }
                    targetData.setMuted(false);
                    targetData.setMuteReason(reason);
                    targetData.setMuteAuthor(name);
                    targetData.setCacheTempMuteTime(0);
                    sender.sendMessage("§aUnmute §aaplicado ao player §e" + args[0] + "§a.");
                    SysUtils.alertUnmuteStaff(args[0], name);
                }
            }
        }
        return false;
    }

}
