package me.kp.moon.moonpvp.commands;

import me.kp.moon.moonpvp.api.HologramAPI;
import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.kit.GladiatorUtils;
import me.kp.moon.moonpvp.mysql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

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
            if (args.length == 0) {
                HologramAPI.reloadHolograms();
                return false;
            }
            if (args[0].equalsIgnoreCase("l")) {
                GladiatorUtils.clearArena(player.getLocation());
                return false;
            }

        }
        return false;
    }
}
