package me.kp.moon.moonpvp.commands;

import me.kp.moon.moonpvp.Main;
import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Sorteio implements CommandExecutor {

    private int chance;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("sorteio")) {
            if (!player.hasPermission("command.sortearplayer")) {
                player.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }
            int onlinePlayers = (int) Bukkit.getOnlinePlayers().stream().filter(target -> !target.hasPermission("command.staffchat")).count();
            if (onlinePlayers <= 3) {
                player.sendMessage("§cO servidor não tem players suficientes para a realização de um sorteio.");
                return true;
            }
            if (args.length == 0) {
                player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(command.getName() + " <coins>"));
                return true;
            }
            int quantia;
            try {
                quantia = Integer.parseInt(args[0]);
            } catch (NumberFormatException exception) {
                player.sendMessage(Messages.APENAS_NUMEROS_ARGS.getMessage());
                return true;
            }
            this.chance = 100 / onlinePlayers;
            List<Player> playerList = Bukkit.getOnlinePlayers().stream().filter(target -> !target.hasPermission("command.staffchat")).collect(Collectors.toList());
            Random random = new Random();
            int sorteado = random.nextInt(onlinePlayers);
            Player sort = playerList.get(sorteado);
            PlayerData sortData = PlayerDataManager.getPlayerData(sort);
            if (sortData == null) return true;
            sortData.setCacheCoins(sortData.cacheCoins + quantia);

            Bukkit.broadcastMessage("§9§lMoon§1§lMC §7» §fUm sorteio acabou de começar!");
            Bukkit.broadcastMessage("§9§lMoon§1§lMC §7» §fVocê já está participando!");
            Bukkit.broadcastMessage("§9§lMoon§1§lMC §7» §fJogadores participando: §a" + onlinePlayers);
            PlayerUtils.sendMessageToStaff("§7§o(STAFF) Sorteio feito por §f" + player.getName() + "§7§o.");

            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                Bukkit.broadcastMessage("§9§lMoon§1§lMC §7» §fO ganhador do sorteio foi §e" + sort.getName() +
                        "§f! §7(" + chance + "% de chance)");
            }, 200L);
        }
        return false;
    }
}
