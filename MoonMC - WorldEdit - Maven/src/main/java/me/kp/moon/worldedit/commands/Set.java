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

public class Set implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Reply.APENAS_PLAYERS.getReply());
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return true;
        if (command.getName().equalsIgnoreCase("/set")) {
            if (!player.hasPermission("worldedit")) {
                player.sendMessage(Reply.SEM_PERMISSAO.getReply());
                return true;
            }
            if (playerData.pos1 == null || playerData.pos2 == null) {
                player.sendMessage("§cVocê precisa selecionar suas §eduas posições §cantes de usar esse comando.");
                return true;
            }
            if (args.length < 2) {
                player.sendMessage(Reply.ARGUMENTOS_INSUFICIENTES.getUsage(label + " <bloco> <id>"));
                return true;
            }
            Material material = Material.getMaterial(args[0]);
            if (material == null || !material.isBlock()) {
                player.sendMessage("§cBloco inválido.");
                return true;
            }

            double x = Math.abs(playerData.pos1.getX() - playerData.pos2.getX());
            double y = Math.abs(playerData.pos1.getY() - playerData.pos2.getY());
            double z = Math.abs(playerData.pos1.getZ() - playerData.pos2.getZ());
            double volume = x * y * z;
            if (volume > 15.000) {
                player.sendMessage("§cVocê não pode editar mais de 15.000 blocos. §7(Atual: " + volume + " blocos)");
                return true;
            }
            player.sendMessage("§aIniciando edição de §e" + (int) volume + " blocos§a.");
            WorldEditUtils.setBlocks(Material.GLASS, 0, playerData.pos1, playerData.pos2);
        }
        return false;
    }
}
