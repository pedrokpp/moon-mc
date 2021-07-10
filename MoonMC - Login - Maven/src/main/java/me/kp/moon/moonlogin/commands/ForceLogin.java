package me.kp.moon.moonlogin.commands;

import me.kp.moon.moonlogin.auth.AuthAPI;
import me.kp.moon.moonlogin.data.PlayerData;
import me.kp.moon.moonlogin.data.PlayerDataManager;
import me.kp.moon.moonlogin.enums.Strings;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceLogin implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("forcelogin")) {
            if (!sender.hasPermission("command.forcelogin")) {
                sender.sendMessage("§cVocê não tem permissão para executar esse comando.");
                return true;
            }
            if (args.length < 1) {
                sender.sendMessage("§cEscolha um alvo para forçar login.");
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage("§cEsse player não está online.");
                return true;
            }
            PlayerData targetData = PlayerDataManager.getPlayerData(target);
            if (targetData == null) {
                sender.sendMessage("§cErro interno ao encontrar este player.");
                return true;
            }
            AuthAPI.authPlayer(targetData);
            sender.sendMessage("§aVocê forçou o login de §e" + target.getName() + "§a.");
        }
        return false;
    }
}
