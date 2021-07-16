package me.kp.moon.lobby.commands;

import me.kp.moon.lobby.enums.Reply;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Ping extends Command {

    public Ping() {
        super("ping", "", "ms");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(Reply.APENAS_PLAYERS.getTextComponent());
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        player.sendMessage(new TextComponent("§aSeu ping é: §7" + player.getPing() + " ms"));
    }
}
