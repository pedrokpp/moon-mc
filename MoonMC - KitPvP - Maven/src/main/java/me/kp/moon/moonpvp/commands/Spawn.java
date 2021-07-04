package me.kp.moon.moonpvp.commands;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.utils.PlayerUtils;
import me.kp.moon.moonpvp.warps.WarpType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return true;
        if (command.getName().equalsIgnoreCase("spawn") || command.getName().equalsIgnoreCase("lobby")) {
            if (args.length == 0) {
                if (playerData.evento) {
                    playerData.setEvento(false);
                    player.sendMessage("§cVocê saiu do evento.");
                }
                if (playerData.admin) {
                    player.chat("/admin");
                }
                if (playerData.superadmin) {
                    player.chat("/sa");
                }
                if (playerData.vanish) {
                    player.chat("/v");
                }
                player.sendMessage("§aVocê foi teleportado para o spawn.");
                PlayerUtils.sendPlayerToSpawn(player);
                return false;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("§cNão foi possível encontrar o player §e" + args[0] + "§c.");
                return true;
            }
            PlayerData targetData = PlayerDataManager.getPlayerData(target);
            if (targetData == null) return false;
            if (targetData.combat) {
                player.sendMessage("§cEste player está em combate.");
                return true;
            }
            if (targetData.evento) {
                target.sendMessage("§cVocê saiu do evento forçadamente.");
                targetData.setEvento(false);
            }
            if (targetData.admin) {
                target.chat("/admin");
            }
            if (targetData.superadmin) {
                target.chat("/sa");
            }
            if (targetData.vanish) {
                target.chat("/v");
            }
            player.sendMessage("§aVocê enviou §e" + target.getName() + " §apara o spawn.");
            target.sendMessage("§aVocê foi enviado para o spawn por §e" + player.getName() + "§a.");
            PlayerUtils.sendPlayerToSpawn(target);
        }
        return false;
    }
}
