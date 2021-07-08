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

public class Admin implements CommandExecutor {
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
        if (command.getName().equalsIgnoreCase("admin")) {
            if (!player.hasPermission("command.admin")) {
                player.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }

            if (!playerData.admin) {
                PlayerUtils.giveAdminItems(player);
                if (playerData.superadmin) {
                    player.sendMessage("§cVocê saiu do modo super admin.");
                    playerData.setSuperadmin(false);
                }
                player.sendMessage("§aVocê entrou no modo admin.");
                player.setGameMode(GameMode.CREATIVE);
                playerData.setAdmin(true);
                 Bukkit.getOnlinePlayers().forEach(players -> {
                    if (!players.hasPermission("command.staffchat"))
                        players.hidePlayer(player);
                });
            } else {
                PlayerUtils.giveInitialItems(player);
                player.sendMessage("§cVocê saiu do modo admin.");
                player.setGameMode(GameMode.SURVIVAL);
                playerData.setAdmin(false);
                Bukkit.getOnlinePlayers().forEach(players -> {
                    players.showPlayer(player);
                });
            }
        }
        return false;
    }
}
