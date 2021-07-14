package me.kp.moon.moonpvp.commands;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Money implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return true;
        if (command.getName().equalsIgnoreCase("money") || command.getName().equalsIgnoreCase("bal")) {
            if (args.length == 0) {
                int coins = playerData.cacheCoins;
                player.sendMessage("§aO seu saldo de moedas é de §7$" + coins + "§a.");
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                if (!args[0].equalsIgnoreCase("pay")) {
                    player.sendMessage("§cNão foi possível encontrar o player §e" + args[0] + "§c.");
                    return true;
                }
                if (args.length < 3) {
                    player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(label + " pay <player> <quantia>"));
                    return true;
                }
                Player target1 = Bukkit.getPlayer(args[1]);
                if (target1 == null) {
                    player.sendMessage("§cO player §e" + args[1] + " §cnão foi encontrado online.");
                    return true;
                }
                int quantia;
                try {
                    quantia = Integer.parseInt(args[2]);
                } catch (NumberFormatException exception) {
                    player.sendMessage("§cA quantia apresentada é inválida.");
                    return true;
                }
                if (playerData.cacheCoins < quantia) {
                    player.sendMessage("§cVocê não tem essa quantidade de moedas.");
                    return true;
                }
                PlayerData targetData1 = PlayerDataManager.getPlayerData(target1);
                if (targetData1 == null) return true;
                playerData.setCacheCoins(playerData.cacheCoins - quantia);
                targetData1.setCacheCoins(targetData1.cacheCoins + quantia);
                player.sendMessage("§aVocê enviou §7$" + quantia + " para §7" + target1.getName() + "§a.");
                target1.sendMessage("§aVocê recebeu §7$" + quantia + " de §7" + player.getName() + "§a.");
                return false;
            }
            PlayerData targetData = PlayerDataManager.getPlayerData(target);
            if (targetData == null) return true;
            int coins = targetData.cacheCoins;
            player.sendMessage("§aO saldo de §e" + target.getName() + " §aé de §7$" + coins + "§a.");
        }
        return false;
    }
}
