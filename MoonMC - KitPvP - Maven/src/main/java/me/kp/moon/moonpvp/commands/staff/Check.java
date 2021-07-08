package me.kp.moon.moonpvp.commands.staff;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.enums.PlayerGroup;
import me.kp.moon.moonpvp.enums.PlayerTag;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Check implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player)sender;
            if (player.hasPermission("command.check")) {
                if (args.length > 0) {
                    final Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        PlayerData targetData = PlayerDataManager.getPlayerData(target);
                        if (targetData == null) return true;
                        player.sendMessage("§aInformaçoes administrativas de §e" + target.getName());
                        player.sendMessage("§fGrupo: §a" + PlayerGroup.getGroup(target).getColoredName());
                        player.sendMessage("§fTag: §a" + ((targetData.playerTag == PlayerTag.MEMBRO) ? "§7MEMBRO" : targetData.playerTag.getPrefix().toUpperCase()));
                        player.sendMessage("§fOP: §a" + (target.isOp() ? "Sim" : "§cNão"));
                        player.sendMessage("§fBuild: §a" + (targetData.build ? "Sim" : "§cNão"));
                        player.sendMessage("§fAdmin: §a" + (targetData.admin ? "Sim" : "§cNão"));
                        player.sendMessage("§fSuper Admin: §a" + (targetData.admin ? "Sim" : "§cNão"));
                        player.sendMessage("§fVanish: §a" + (targetData.vanish ? "Sim" : "§cNão"));
                        player.sendMessage("§fSpy: §a" + (targetData.tellSpy ? "Sim" : "§cNão"));
                    }
                    else {
                        player.sendMessage("§cNão foi possível encontrar o player §e" + args[0] + "§c.");
                    }
                }
                else {
                    player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(label + " <player>"));
                }
            }
            else {
                player.sendMessage(Messages.SEM_PERMISSAO.getMessage());
            }
        }
        else {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
        }
        return true;
    }

}
