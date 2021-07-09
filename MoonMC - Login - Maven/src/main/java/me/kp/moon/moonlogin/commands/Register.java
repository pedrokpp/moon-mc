package me.kp.moon.moonlogin.commands;

import me.kp.moon.moonlogin.auth.AuthAPI;
import me.kp.moon.moonlogin.data.PlayerData;
import me.kp.moon.moonlogin.data.PlayerDataManager;
import me.kp.moon.moonlogin.enums.Strings;
import me.kp.moon.moonlogin.mysql.MySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Register implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Strings.apenasPlayers());
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return true;
        if (command.getName().equalsIgnoreCase("register")) {
            if (playerData.isLoggedIn()) {
                player.sendMessage("§cVocê já está logado.");
                return true;
            }
            if (playerData.getPassword() != null) {
                player.sendMessage("§cVocê já não registrou sua conta.");
                return true;
            }
            if (args.length == 0) {
                player.sendMessage("§eUtilize o comando §7/register <senha> <senha>§e.");
                return true;
            }
            String pass = args[0];
            String passConfirmation = args[1];
            if (!pass.equals(passConfirmation)) {
                player.sendMessage("§cAs senhas não são iguais. Tente novamente.");
                return true;
            }
            if (pass.length() < 6) {
                player.sendMessage("§cSua senha precisa ter mais que 6 caracteres. §7(Essa senha possui " + pass.length() + " caracteres)");
                return true;
            }
            playerData.setPassword(pass);
            MySQL.registerPlayer(playerData);
            player.sendMessage("§aVocê se autenticou com sucesso.");
            player.sendMessage("§7Guardamos sua senha com encriptação AES-GCM.");
            AuthAPI.authPlayer(playerData);
        }
        return false;
    }

}
