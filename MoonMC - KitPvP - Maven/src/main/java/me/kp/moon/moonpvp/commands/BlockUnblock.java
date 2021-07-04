package me.kp.moon.moonpvp.commands;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BlockUnblock implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return true;
        if (command.getName().equalsIgnoreCase("block")) {
            if (args.length == 0) {
                player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(command.getName() + " <player> || /" + command.getName() + " <list>"));
                return true;
            }
            if (args[0].equalsIgnoreCase("list")) {
                List<String> listaConvertida = new ArrayList<>();
                playerData.ignoredPlayers.forEach(uuid -> { listaConvertida.add(Bukkit.getPlayer(uuid).getName()); });
                String message = listaConvertida.size() == 0 ? "§aVocê não bloqueou nenhum player." :
                        "§aA lista de players que você bloqueou é: §7" + StringUtils.join(listaConvertida, "§a, §7") + "§a.";
                player.sendMessage(message);
                return false;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("§cNão foi possível encontrar o player §e" + args[0] + "§c.");
                return true;
            }
            if (target == player) {
                player.sendMessage("§cVocê não pode se bloquear.");
                return true;
            }
            player.sendMessage("§aVocê bloqueou §e" + target.getName() + "§a.");
            player.sendMessage("§7§oCaso queira desbloqueá-lo, digite: /unblock " + target.getName());
            playerData.addIgnoredPlayer(target);
            return false;
        }
        if (command.getName().equalsIgnoreCase("unblock")) {
            if (args.length == 0) {
                player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(command.getName() + " <player>"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("§cNão foi possível encontrar o player §e" + args[0] + "§c.");
                return true;
            }
            player.sendMessage("§aVocê desbloqueou §e" + target.getName() + "§a.");
            player.sendMessage("§7§oCaso queira bloqueá-lo novamente, digite: /block " + target.getName());
            playerData.removeIgnoredPlayer(target);
            return false;
        }
        return false;
    }
}
