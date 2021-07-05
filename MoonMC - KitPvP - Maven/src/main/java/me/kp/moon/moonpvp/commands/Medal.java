package me.kp.moon.moonpvp.commands;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Medals;
import me.kp.moon.moonpvp.enums.Messages;
import net.md_5.bungee.api.chat.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Medal implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return false;
        if (command.getName().equalsIgnoreCase("medal")) {
            if (args.length == 0) {
                player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(label + " <medalha>"));
                player.chat("/medals");
                return true;
            }
            Medals medal = Medals.getMedalByName(args[0]);
            if (medal == null) {
                player.sendMessage("§cNão foi possível encontrar a medalha §e" + args[0] + "§c.");
                return true;
            }
            if (!player.hasPermission("medal." + medal.getName().toLowerCase())) {
                player.sendMessage("§cVocê não tem permissão para utilizar essa medalha.");
                return true;
            }
            player.sendMessage("§aVocê selecionou a medalha " + medal.getMedal());
            playerData.setMedal(medal);
        }
        if (command.getName().equalsIgnoreCase("medals")) {
            ArrayList<BaseComponent> medals = new ArrayList<>();
            medals.add(new TextComponent("§aSuas medalhas são: "));
            Medals[] values;
            for (int length = (values = Medals.values()).length, i = 0; i < length; ++i) {
                Medals medal = values[i];
                if (player.hasPermission("medal." + medal.getName().toLowerCase())) {
                    TextComponent medalComponent = new TextComponent(medal.getMedal());
                    medalComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(medal.getColoredName() +
                            "\n" + medal.getDescription() + "\n\n§fClique para selecionar.").create()));
                    medalComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/medal " + medal.getName()));
                    medals.add(medalComponent);
                    if (i == length - 1) medals.add(new TextComponent("§f."));
                    else medals.add(new TextComponent("§f, "));
                }
            }
            BaseComponent[] fullMessage = new BaseComponent[medals.size()];
            fullMessage = medals.toArray(fullMessage);
            player.spigot().sendMessage(fullMessage);
        }
        return false;
    }

}
