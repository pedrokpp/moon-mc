package me.kp.moon.moonpvp.commands;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.enums.PlayerTag;
import me.kp.moon.moonpvp.utils.PlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Tag implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return false;
        if (command.getName().equalsIgnoreCase("tag")) {
            if (args.length == 0) {
                player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(command.getName() + " <tag>"));
                player.chat("/tags");
                return true;
            }
            PlayerTag tag = PlayerTag.getTagByName(args[0].replace("+", "plus"));
            if (tag == null) {
                player.sendMessage("§cNão foi possível encontrar a tag §e" + args[0] + "§c.");
                return true;
            }
            if (!player.hasPermission("tag." + tag.getName().toLowerCase())) {
                player.sendMessage("§cVocê não tem permissão para utilizar essa tag.");
                return true;
            }
            player.sendMessage("§aSua tag foi alterada para " + tag.getColor() + tag.getName().replace("plus", "+"));
            PlayerUtils.changePlayerTag(player, tag, playerData);
        }
        if (command.getName().equalsIgnoreCase("tags")) {
            StringBuilder tags = new StringBuilder();
            PlayerTag[] values;
            for (int length = (values = PlayerTag.values()).length, i = 0; i < length; ++i) {
                final PlayerTag tag2 = values[i];
                if (player.hasPermission("tag." + tag2.getName().toLowerCase())) {
                    tags.append((tags.length() == 0) ? "" : "§f, ").append(tag2.getColor()).append(tag2.getName().replace("plus", "+"));
                }
            }
            player.sendMessage("§aSuas tags disponíveis são: " + tags);
        }
        return false;
    }

}
