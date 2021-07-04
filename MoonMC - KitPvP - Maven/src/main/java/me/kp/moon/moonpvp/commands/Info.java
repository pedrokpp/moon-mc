package me.kp.moon.moonpvp.commands;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Info implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (player.hasPermission("command.info")) {
                if (args.length > 0) {
                    final Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        PlayerData targetData = PlayerDataManager.getPlayerData(target);
                        if (targetData == null) return true;
                        player.sendMessage("§aInformações sobre o player §e" + target.getName() + "§a:");
                        player.sendMessage("§aNick: §f" + target.getName());
                        String kit = targetData.kitType == null ? "Nenhum" : targetData.kitType.getKitname();
                        player.sendMessage("§aKit: §f" + kit);
                        String warp = targetData.warpType == null ? "Nenhuma" : targetData.warpType.getWarpName();
                        player.sendMessage("§aWarp: §f" + warp);
                        player.sendMessage("§aPing: §f" + PlayerUtils.getPlayerPing(target));
                        player.sendMessage("§aGameMode: §f" + target.getGameMode().name());
                        player.sendMessage("§aFly: §f" + (target.getAllowFlight() ? "§fSim" : "§cNão"));
                    } else {
                        player.sendMessage("§cNão foi possível encontrar o player §e" + args[0] + "§c.");
                    }
                } else {
                    player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(label + " <player>"));
                }
            } else {
                player.sendMessage(Messages.SEM_PERMISSAO.getMessage());
            }
        } else {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
        }
        return true;
    }

}
