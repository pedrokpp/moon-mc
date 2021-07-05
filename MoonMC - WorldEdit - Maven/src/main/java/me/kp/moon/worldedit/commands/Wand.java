package me.kp.moon.worldedit.commands;

import me.kp.moon.worldedit.data.PlayerData;
import me.kp.moon.worldedit.data.PlayerDataManager;
import me.kp.moon.worldedit.enums.Reply;
import me.kp.moon.worldedit.utils.WorldEditUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Wand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Reply.APENAS_PLAYERS.getReply());
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return true;
        if (command.getName().equalsIgnoreCase("/wand")) {
            if (!player.hasPermission("worldedit")) {
                player.sendMessage(Reply.SEM_PERMISSAO.getReply());
                return true;
            }
            player.setItemInHand(WorldEditUtils.getWand());
            player.sendMessage("§aVocê recebeu um §emachado mágico§a, espero que você já saiba como usa-lo.");
        }
        return false;
    }
}
