package me.kp.moon.moonpvp.commands.staff;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.utils.DiscordWebhook;
import me.kp.moon.moonpvp.utils.SysUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class Ungrant implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return true;
        if (command.getName().equalsIgnoreCase("ungrant")) {
            if (!player.hasPermission("command.grant")) {
                player.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }
            if (args.length < 2) {
                player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(label + " <player> <permissão>"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("§cNão foi possível encontrar o player §e" + args[0] + "§c.");
                return true;
            }
            PlayerData targetData = PlayerDataManager.getPlayerData(target);
            if (targetData == null) return true;
            if (!player.hasPermission(args[1])) {
                player.sendMessage("§cVocê não consegue retirar a permissão §e" + args[1] + "§c, pois você não possui essa permissão.");
                return true;
            }
            DiscordWebhook webhook = new DiscordWebhook(SysUtils.webhookURLLOGGRANT);
            webhook.setContent("O player **" + playerData.username + "** deu unset na permissão ``" + args[1] + "`` para **" + targetData.username + "**.");
            try {
                webhook.execute();
            } catch (IOException e) {
                player.sendMessage("§cErro ao registrar log, cancelando ação... §7(Contate a equipe de desenvolvimento)");
                e.printStackTrace();
                return true;
            }
            player.sendMessage("§aPermissão §e" + args[1] + " §arevogada do player §e" + targetData.username);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + targetData.username + " permission unset " + args[1]);
        }
        return false;
    }

}
