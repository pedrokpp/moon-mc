package me.kp.moon.moonpvp.commands.staff;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SuperAdmin implements CommandExecutor {

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
        if (command.getName().equalsIgnoreCase("superadmin") || command.getName().equalsIgnoreCase("sa")) {
            if (!player.hasPermission("command.superadmin")) {
                player.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }

            if (!playerData.superadmin) {
                PlayerUtils.giveAdminItems(player);
                if (playerData.admin) {
                    player.sendMessage("§cVocê saiu do modo admin.");
                    playerData.setAdmin(false);
                }
                player.sendMessage("§aVocê entrou no modo super admin.");
                player.setGameMode(GameMode.CREATIVE);
                playerData.setSuperadmin(true);
                Bukkit.getOnlinePlayers().forEach(players -> {
                    players.hidePlayer(player);
                });
            } else {
                PlayerUtils.giveInitialItems(player);
                player.sendMessage("§cVocê saiu do modo super admin.");
                player.setGameMode(GameMode.SURVIVAL);
                playerData.setSuperadmin(false);
                Bukkit.getOnlinePlayers().forEach(players -> {
                    players.showPlayer(player);
                });
            }
        }
        return false;
    }
}
