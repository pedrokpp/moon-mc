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
                int diff = PlayerRank.getRank(playerData).next().getXp() - PlayerRank.getRank(playerData).getXp();
                getProgressBar(pontos_necessarios, diff, player);
            }

        } else {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
        }
        return true;
    }

    private static void getProgressBar(int atual, int total, Player player) {
        int barSize = 10;
        int realPorcent = 100 - (int) (((double) atual / (double) total) * 100D);
        int barPorcent = realPorcent / 10;
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < barSize; i++) {
            if (i < barPorcent) {
                bar.append("§a§m-§r");
            } else if (i == barPorcent) {
                bar.append("§a§m>§r");
            } else {
                bar.append("§7§m-§r");
            }
        }
        player.sendMessage(bar + "§r §7(" + realPorcent + "% concluído)");
    }

}
