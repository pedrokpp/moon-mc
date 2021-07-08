package me.kp.moon.moonpvp.commands.staff;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Vanish implements CommandExecutor {
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
        if (command.getName().equalsIgnoreCase("vanish") || command.getName().equalsIgnoreCase("v")) {
            if (!player.hasPermission("command.vanish")) {
                player.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }
            if (!playerData.vanish) {
                player.setGameMode(GameMode.SURVIVAL);
                if (playerData.admin || playerData.superadmin) {
                    String modo = playerData.admin ? "admin." : "super admin.";
                    player.sendMessage("§cVocê saiu do modo " + modo);
                    playerData.setAdmin(false);
                    playerData.setSuperadmin(false);
                }
                player.sendMessage("§aVocê entrou no modo vanish.");
                playerData.setVanish(true);
                player.setAllowFlight(true);
                Bukkit.getOnlinePlayers().forEach(players -> {
                    if (!players.hasPermission("command.staffchat"))
                        players.hidePlayer(player);
                });
            } else {
                player.sendMessage("§cVocê saiu do modo vanish.");
                playerData.setVanish(false);
                player.setAllowFlight(false);
                Bukkit.getOnlinePlayers().forEach(players -> {
                    players.showPlayer(player);
                });
            }
        }
        return false;
    }
}
