package me.kp.moon.moonpvp.commands;

import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.enums.Strings;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Discord implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("discord")) {
            TextComponent textComponent = new TextComponent("§aParticipe da nossa comunidade do §9Discord §aclicando §6nesta §6mensagem!");
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClique aqui").create()));
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, Strings.getDiscord()));
            player.sendMessage(textComponent);
        }
        return false;
    }
}
