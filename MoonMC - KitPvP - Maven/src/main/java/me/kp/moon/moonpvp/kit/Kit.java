package me.kp.moon.moonpvp.kit;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.gui.SeusKitsGUI;
import me.kp.moon.moonpvp.utils.SysUtils;
import me.kp.moon.moonpvp.warps.WarpType;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Kit implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return true;
        if (command.getName().equalsIgnoreCase("kit") || command.getName().equalsIgnoreCase("kits")) {
            if (playerData.kitType != null || playerData.warpType != null) {
                player.sendMessage("§cVocê já possui um kit selecionado.");
                return true;
            }
            if (command.getName().equalsIgnoreCase("kits") || args.length == 0) {
                SeusKitsGUI.openGUI(player);
                return false;
            }
            KitType kitType = KitType.getKitTypeByName(args[0]);
            if (kitType == null) {
                player.sendMessage("§cO kit \"§e" + args[0] + "§c\" não existe.");
                return true;
            }
            if (!player.hasPermission("kit." + kitType.getKitname().toLowerCase())) {
                player.sendMessage("§cVocê não possui permissão para utilizar este kit.");
                return true;
            }
            if (!kitType.isEnabled()) {
                player.sendMessage("§cO kit §e" + kitType.getKitname() + "§c está desabilitado no momento.");
                return true;
            }
            playerData.setKitType(kitType);
            playerData.setWarpType(WarpType.ARENA);
            player.setGameMode(GameMode.SURVIVAL);
            player.setAllowFlight(false);
            KitUtils.giveKitItems(playerData);
            player.sendMessage("§aVocê selecionou o kit §e" + kitType.getKitname() + "§a!");
            player.teleport(SysUtils.getRandomSpawnLocation());
        }

        return false;
    }
}
