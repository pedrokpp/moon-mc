package me.kp.moon.moonpvp.commands;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Fake implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return true;
        if (command.getName().equalsIgnoreCase("fake")) {
            if (!player.hasPermission("command.fake")) {
                player.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }
            player.sendMessage("§cComando ainda em fase de testes.");
//            if (playerData.fakeCooldown) {
//                player.sendMessage("§cAguarde para alterar seu nick novamente.");
//                return true;
//            }
//            if (args.length == 0) {
//                player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(label + " <nick/reset>"));
//                return true;
//            }
//            if (args[0].equalsIgnoreCase("reset")) {
//                if (!FakeAPI.hasFake(playerData)) {
//                    player.sendMessage("§cVocê não tem nenhum fake ativo.");
//                    return true;
//                }
//                player.sendMessage("§9Alterando seu nick para §e" + playerData.username + "§a...");
//                FakeAPI.applyFake(playerData.username, playerData);
//                player.sendMessage("§aSeu nick foi alterado para §e" + playerData.username + "§a.\n" +
//                        "§7Você poderá escolher outro fake novamente em 90 segundos.");
//                return false;
//            }
//            if (Bukkit.getPlayer(args[0]) != null || Bukkit.getOnlinePlayers().stream().anyMatch(p -> p.getName().equalsIgnoreCase(args[0])) ||
//                    Arrays.stream(Bukkit.getOfflinePlayers()).anyMatch(p -> p.getName().equalsIgnoreCase(args[0]))) {
//                player.sendMessage("§cEsse player já está registrado no banco de dados.");
//                return true;
//            }
//            player.sendMessage("§9Alterando seu nick para §e" + args[0] + "§a...");
//            FakeAPI.applyFake(args[0], playerData);
//            player.sendMessage("§aSeu nick foi alterado para §e" + args[0] + "§a.\n" +
//                    "§7Você poderá escolher outro fake novamente em 90 segundos.");
        }
        return false;
    }
}
