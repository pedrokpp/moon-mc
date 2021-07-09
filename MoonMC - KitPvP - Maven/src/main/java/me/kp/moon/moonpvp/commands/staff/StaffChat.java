package me.kp.moon.moonpvp.commands.staff;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.enums.PlayerGroup;
import me.kp.moon.moonpvp.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChat implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null)
            return true;
        if (command.getName().equalsIgnoreCase("sc") || command.getName().equalsIgnoreCase("s")) {

            if (!player.hasPermission("command.staffchat")) {
                player.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }

            if (args.length == 0) {
                if (!playerData.seeStaffChat) {
                    player.sendMessage("§cVocê está com o staffchat desativado.");
                    return true;
                }
                if (!playerData.staffChat) {
                    playerData.setStaffChat(true);
                    player.sendMessage("§aVocê alterou seu chat para: §eSTAFF");
                } else {
                    playerData.setStaffChat(false);
                    player.sendMessage("§aVocê alterou seu chat para: §eGERAL");
                }
            } else {
                switch (args[0]) {
                    case "off":
                        if (!playerData.seeStaffChat) {
                            player.sendMessage("§cVocê já desativou o seu staffchat.");
                            return true;
                        }
                        playerData.setSeeStaffChat(false);
                        playerData.setStaffChat(false);
                        PlayerUtils.sendMessageToStaff("§c§l[SC] §fO player §e" + player.getName() +
                                " §4§ldesativou §fo staffchat dele.");
                        player.sendMessage("§4§lATENÇÃO: §cNão fique muito tempo com o staffchat desativado.");
                        break;
                    case "on":
                        if (playerData.seeStaffChat) {
                            player.sendMessage("§cVocê já ativou o seu staffchat.");
                            return true;
                        }
                        playerData.setSeeStaffChat(true);
                        PlayerUtils.sendMessageToStaff("§c§l[SC] §fO player §e" + player.getName() +
                                        " §a§lreativou §fo staffchat dele.");
                        break;
                    default:
                        if (!playerData.seeStaffChat) {
                            player.sendMessage("§cVocê está com o staffchat desativado.");
                            return true;
                        }
                        Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("command.staffchat")).forEach(staffer -> {
                            PlayerData stafferData = PlayerDataManager.getPlayerData(staffer);
                            if (stafferData == null) return;
                            String finalMessage = "§c§l[SC] §7[%server%] " + PlayerGroup.getPlayerNameWithGroup(player) + " §7» §f" + String.join(" ", args);
                            String server;
                            if (playerData.evento) server = "Evento";
                            else if (playerData.screenshare) server = "ScreenShare";
                            else server = "KitPvP";
                            if (stafferData.seeStaffChat) staffer.sendMessage(ChatColor.translateAlternateColorCodes('&', finalMessage.replace("%server%", server)));
                        });
                        break;
                }
            }


        }

        return false;
    }
}
