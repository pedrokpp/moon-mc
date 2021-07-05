package me.kp.moon.moonpvp.commands;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.enums.PlayerRank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class Rank implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerData playerData = PlayerDataManager.getPlayerData(player);
            if (playerData == null) return true;
            player.sendMessage("§9§lMoon§1§lMC §7- §eSistema de Rank");
            PlayerRank[] values;
            for (int length = (values = PlayerRank.values()).length, i = 0; i < length; ++i) {
                PlayerRank rank = values[i];
                if (PlayerRank.getRank(playerData).getName().equals(rank.getName())) {
                    player.sendMessage("§7(" + rank.getColoredSymbol() + "§7) " + rank.getColoredName() + " §a" + new DecimalFormat().format(rank.getXp()) + " XP   §e< Seu rank atual");
                } else {
                    player.sendMessage("§7(" + rank.getColoredSymbol() + "§7) " + rank.getColoredName() + " §a" + new DecimalFormat().format(rank.getXp()) + " XP");
                }
            }
            player.sendMessage("§7Seu rank atual é: " + PlayerRank.getRank(playerData).getColoredName() + "§7.");
            if (PlayerRank.getRank(playerData) != PlayerRank.INFINITY) {
                player.sendMessage("§7O próximo rank é: " + PlayerRank.getRank(playerData).next().getColoredName() + "§7.");
                int pontos_necessarios = PlayerRank.getRank(playerData).next().getXp() - playerData.cacheXP;
                player.sendMessage("§7Você possui §a" + playerData.cacheXP + " XP §7e faltam §a" + pontos_necessarios + " XP §7para o próximo §6rank§7.");
                player.sendMessage(" ");
                player.sendMessage("§7Progresso para o próximo §6rank§7:");
                progressPercentage(playerData.cacheXP, PlayerRank.getRank(playerData).next().getXp(), player);
            }

        } else {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
        }
        return true;
    }

    public static void progressPercentage(int remain, int total, Player player) {
        if (remain > total) {
            throw new IllegalArgumentException();
        }
        int maxBareSize = 10;
        int remainProcent = 100 * remain / total / maxBareSize;
        char defaultChar = '-';
        String icon = "§a§m-";
        String bare = "§f§m" + new String(new char[maxBareSize]).replace('\0', defaultChar);
        StringBuilder bareDone = new StringBuilder();
        for (int i = 0; i < remainProcent; ++i) {
            bareDone.append(icon);
        }
        bareDone.append(">§f§m");
        String bareRemain = bare.substring(remainProcent);
        player.sendMessage(bareDone + bareRemain + "§f §7(" + remainProcent * 10 + "% conclu\u00eddo)");
        if (remain == total) {
            System.out.print("\n");
        }
    }

}
