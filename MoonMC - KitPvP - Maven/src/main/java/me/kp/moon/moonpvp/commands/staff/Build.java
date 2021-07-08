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

public class Build implements CommandExecutor {

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
        if (command.getName().equalsIgnoreCase("build") || command.getName().equalsIgnoreCase("b")) {
            if (!player.hasPermission("command.build")) {
                player.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }
            if (args.length == 0) {
                if (!playerData.build) {
                    playerData.setBuild(true);
                    player.setGameMode(GameMode.CREATIVE);
                    player.sendMessage("§aVocê ativou o seu build.");
                } else {
                    playerData.setBuild(false);
                    player.setGameMode(GameMode.SURVIVAL);
                    player.sendMessage("§cVocê desativou o seu build.");
                }
                return false;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("§cNão foi possível encontrar o player §e" + args[0] + "§c.");
                return true;
            }
            PlayerData targetData = PlayerDataManager.getPlayerData(target);
            if (targetData == null) return true;
            if (!targetData.build) {
                targetData.setBuild(true);
                target.setGameMode(GameMode.CREATIVE);
                target.sendMessage("§aSeu build foi ativado por §e" + player.getName() + "§a.");
                player.sendMessage("§aVocê ativou o build de §e" + target.getName() + "§a.");
            } else {
                targetData.setBuild(false);
                target.setGameMode(GameMode.SURVIVAL);
                target.sendMessage("§cSeu build foi desativado por §e" + player.getName() + "§a.");
                player.sendMessage("§cVocê desativou o build de §e" + target.getName() + "§c.");
            }
            return false;
        }
        return false;
    }
}
