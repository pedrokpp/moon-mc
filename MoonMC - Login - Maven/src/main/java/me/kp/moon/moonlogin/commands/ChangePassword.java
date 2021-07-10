package me.kp.moon.moonlogin.commands;

import me.kp.moon.moonlogin.data.PlayerData;
import me.kp.moon.moonlogin.data.PlayerDataManager;
import me.kp.moon.moonlogin.enums.Strings;
import me.kp.moon.moonlogin.mysql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChangePassword implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Strings.apenasPlayers());
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return true;
        if (command.getName().equalsIgnoreCase("changepassword")) {
            player.sendMessage("§cArgumentos insuficientes. §7/" + label + " <antiga> <nova>");
            return false;
        }
        if (args.length < 2) {
            player.sendMessage("§cArgumentos insuficientes. §7/changepassword <antiga> <nova>");
            return true;
        }
        if (!args[0].equals(playerData.getPassword())) {
            player.sendMessage("§cSenha atual não corresponde com a apresentada.");
            return true;
        }
        if (args[1].length() < 6) {
            player.sendMessage("§cSua nova senha precisa ter no mínimo 6 caracteres.");
            return true;
        }
        MySQL.updatePassword(player, args[1], false);
        Bukkit.getConsoleSender().sendMessage("§7" + player.getName() + " alterou sua senha.");
        playerData.setKickable(true);
        return false;
    }
}