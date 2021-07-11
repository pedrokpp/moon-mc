package me.kp.moon.moonlogin.commands;

import me.kp.moon.moonlogin.auth.AuthAPI;
import me.kp.moon.moonlogin.cache.SysCache;
import me.kp.moon.moonlogin.data.PlayerData;
import me.kp.moon.moonlogin.data.PlayerDataManager;
import me.kp.moon.moonlogin.enums.Strings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Login implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Strings.apenasPlayers());
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return true;
        if (command.getName().equalsIgnoreCase("login")) {
            if (playerData.isLoggedIn()) {
                player.sendMessage("§cVocê já está logado.");
                return true;
            }
            if (playerData.getPassword() == null) {
                player.sendMessage("§cVocê ainda não registrou sua conta.");
                return true;
            }
            if (args.length == 0) {
                player.sendMessage("§eUtilize o comando §7/login <senha>§e.");
                return true;
            }
            String pass = args[0];
            if (!playerData.getPassword().equals(pass)) {
                player.sendMessage("§cSenha incorreta.");
                return true;
            } else {
                player.sendMessage("§aVocê se autenticou com sucesso.");
                AuthAPI.authPlayer(playerData);
                SysCache.bindPlayerToIP(player.getName(), player.getAddress().getHostName().trim());
            }
        }
        return false;
    }
}
