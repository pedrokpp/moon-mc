package me.kp.moon.moonpvp.warps;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.gui.SeusKitsGUI;
import me.kp.moon.moonpvp.gui.WarpsGUI;
import me.kp.moon.moonpvp.kit.KitType;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Warp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return true;
        if (command.getName().equalsIgnoreCase("warp") || command.getName().equalsIgnoreCase("warps")) {
            if (playerData.kitType != null || playerData.warpType != null) {
                player.sendMessage("§cVocê já possui um kit selecionado.");
                return true;
            }
            if (command.getName().equalsIgnoreCase("warps") || args.length == 0) {
                WarpsGUI.openGUI(player);
                return false;
            }
            WarpType warp = WarpType.getWarpTypeByName(args[0]);
            if (warp == null) {
                player.sendMessage("§cA warp \"§e" + args[0] + "§c\" não existe.");
                return true;
            }
            if (!warp.isEnabled()) {
                player.sendMessage("§cA warp §e" + warp.getWarpName() + "§c está desabilitada no momento.");
                return true;
            }
            if (warp == WarpType.ARENA) {
                SeusKitsGUI.openGUI(player);
                return false;
            }
            playerData.setKitType(KitType.PVP);
            if (warp == WarpType.FISHERMAN) playerData.setKitType(KitType.FISHERMAN);
            playerData.setWarpType(warp);
            player.setGameMode(GameMode.SURVIVAL);
            player.setAllowFlight(false);
            WarpUtils.teleportPlayerToWarp(player, warp);
            WarpUtils.giveWarpItems(playerData);
            player.sendMessage("§aVocê foi para a warp §e" + warp.getWarpName() + "§a!");
        }

        return false;
    }

}
