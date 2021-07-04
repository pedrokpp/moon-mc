package me.kp.moon.moonpvp.commands;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.kit.GladiatorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Teste implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("teste")) {
            if (!player.getName().equals("pedrokp")) {
                player.sendMessage("§cComando inexistente.");
                return true;
            }
            PlayerData playerData = PlayerDataManager.getPlayerData(player);
            if (playerData == null) return true;
            if (args[0].equalsIgnoreCase("l")) {
                GladiatorUtils.clearArena(player.getLocation());
                return false;
            }
            if (args[0].equalsIgnoreCase("g")) {
                Bukkit.broadcastMessage(playerData.gladiatorLocation + "");
                player.teleport(playerData.gladiatorLocation);
                return false;
            }
            Location location = player.getLocation();
            if (GladiatorUtils.checkArenasNearbyPlayer(player)) {
                player.sendMessage("arena");
                return true;
            }
            GladiatorUtils.spawnArena(location.add(0, 209-location.getY(), 0));
            GladiatorUtils.teleportPlayersToArena(player, null);
        }
        return false;
    }
}
