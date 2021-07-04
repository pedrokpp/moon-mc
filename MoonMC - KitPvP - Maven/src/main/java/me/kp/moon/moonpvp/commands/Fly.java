package me.kp.moon.moonpvp.commands;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Fly implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return true;
        if (command.getName().equalsIgnoreCase("fly")) {
            if (!player.hasPermission("command.fly")) {
                player.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }
            if (playerData.kitType != null || playerData.evento) {
                player.sendMessage(Messages.NAO_PODE_COM_KIT.getMessage());
                return true;
            }
            if (args.length == 0) {
                if (!player.getAllowFlight()) {
                    player.setAllowFlight(true);
                    player.sendMessage("§aVocê ativou o modo fly.");
                } else {
                    player.setAllowFlight(false);
                    player.sendMessage("§cVocê desativou o modo fly.");
                }
                return false;
            }
            if (!player.hasPermission("command.fly.others")) {
                player.sendMessage(Messages.SEM_PERMISSAO.getMessage());
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("§cNão foi possível encontrar o player §e" + args[0] + "§c.");
                return true;
            }
            if (!target.getAllowFlight()) {
                target.setAllowFlight(true);
                target.sendMessage("§aSeu fly foi ativado por §e" + player.getName() + "§a.");
                player.sendMessage("§aVocê ativou o modo fly de §e" + target.getName() + "§a.");
            } else {
                target.setAllowFlight(false);
                target.sendMessage("§cSeu fly foi desativado por §e" + player.getName() + "§c.");
                player.sendMessage("§cVocê desativou o modo fly de §e" + target.getName() + "§c.");
                return false;
            }
        }
        return false;
    }
}
