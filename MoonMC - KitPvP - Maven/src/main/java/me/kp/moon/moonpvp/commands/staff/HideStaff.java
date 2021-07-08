package me.kp.moon.moonpvp.commands.staff;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HideStaff implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("hidestaff") || command.getName().equalsIgnoreCase("hs")) {
            if (!player.hasPermission("command.hidestaff")) {
                player.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }
            PlayerData playerData = PlayerDataManager.getPlayerData(player);
            if (playerData == null) return true;

            if (!playerData.hasHiddenStaff) {
                player.sendMessage("§aVocê agora não vê mais staffers no admin.");
                playerData.setHasHiddenStaff(true);
                Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("command.staffchat")).forEach(staffer -> {
                    PlayerData staffPlayerData = PlayerDataManager.getPlayerData(staffer);
                    if (staffPlayerData == null) return;
                    if (staffPlayerData.admin || staffPlayerData.vanish) {
                        player.hidePlayer(staffer);
                    }
                });
            } else {
                player.sendMessage("§cVocê agora vê staffers no admin.");
                playerData.setHasHiddenStaff(false);
                Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("command.staffchat")).forEach(staffer -> {
                    PlayerData staffPlayerData = PlayerDataManager.getPlayerData(staffer);
                    if (staffPlayerData == null) return;
                    if (staffPlayerData.admin || staffPlayerData.vanish) {
                        player.showPlayer(staffer);
                    }
                });
            }
        }
        return false;
    }
}
