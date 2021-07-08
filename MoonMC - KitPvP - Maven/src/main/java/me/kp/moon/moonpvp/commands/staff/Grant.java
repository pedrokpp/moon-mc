package me.kp.moon.moonpvp.commands.staff;

import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.utils.DiscordWebhook;
import me.kp.moon.moonpvp.utils.SysUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class Grant implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("grant")) {
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
            if (!player.hasPermission(args[1])) {
                player.sendMessage("§cVocê não consegue dar a permissão §e" + args[1] + "§c, pois você não possui essa permissão.");
                return true;
            }
            DiscordWebhook webhook = new DiscordWebhook(SysUtils.webhookURLLOGGRANT);
            webhook.setContent("O player **" + player.getName() + "** setou a permissão ``" + args[1] + "`` para **" + target.getName() + "**.");
            try {
                webhook.execute();
            } catch (IOException e) {
                player.sendMessage("§cErro ao registrar log, cancelando ação... §7(Contate a equipe de desenvolvimento)");
                e.printStackTrace();
                return true;
            }
            player.sendMessage("§aPermissão §e" + args[1] + " §aconcedida ao player §e" + target.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + target.getName() + " permission set " + args[1]);
        }
        return false;
    }
}
