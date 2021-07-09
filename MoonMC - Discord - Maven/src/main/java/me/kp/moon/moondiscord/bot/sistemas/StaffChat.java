package me.kp.moon.moondiscord.bot.sistemas;

import com.vdurmont.emoji.EmojiParser;
import me.kp.moon.moondiscord.bot.utils.BotUtils;
import me.kp.moon.moondiscord.plugin.utils.PlayerUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StaffChat extends ListenerAdapter implements Listener {

    /*
    TODO bloquear mensagens de bots
    TODO cargos do Discord pro Mine
    DONE lidar com emojis
    TODO lidar com anexos
     */

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        TextChannel textChannel = event.getChannel();
        String message = event.getMessage().getContentDisplay();
        if (event.getMember() == null || event.getMember().getUser().isBot()) return;
        Member member = event.getMember();
        if (!textChannel.getId().equals(BotUtils.ID_STAFF_CHAT)) return;
        String finalMessage = EmojiParser.parseToAliases(message);
        BotUtils.sendStaffChatDiscordToMine(member, finalMessage);

    }

    @EventHandler
    public void PlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (PlayerUtils.inStaffChat.contains(player.getUniqueId())) {
            String message = event.getMessage();
            String finalMessage = message.replace("@everyone", "``@everyone``")
                    .replace("@here", "``@here``").replace("@modgc", "<@&819001409068335104>")
                    .replace("/sc", "").replace("/s", "");
            PlayerUtils.sendStaffChatMineToDiscord(player, finalMessage, BotUtils.webhookClient);
        }
    }

    @EventHandler
    public void PlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("command.staffchat")) return;
        List<String> command = Arrays.asList(event.getMessage().split(" "));

        if (command.size() < 1) return;
        if (command.size() == 1 && (command.get(0).equalsIgnoreCase("/s") ||
                command.get(0).equalsIgnoreCase("/sc"))) {
            if (PlayerUtils.inStaffChat.contains(player.getUniqueId()))
                PlayerUtils.inStaffChat.remove(player.getUniqueId());
            else
                PlayerUtils.inStaffChat.add(player.getUniqueId());

            return;
        }
        if (!(command.get(0).equalsIgnoreCase("/sc") || command.get(0).equalsIgnoreCase("/s"))) return;
        if (command.get(1).equalsIgnoreCase("on") || command.get(1).equalsIgnoreCase("off")) return;

        String message = String.join(" ", command).replace("/sc", "").replace("/s", "");
        String finalMessage = message.replace("@everyone", "``@everyone``")
                .replace("@here", "``@here``").replace("@modgc", "<@&818996656623321109>");

        PlayerUtils.sendStaffChatMineToDiscord(player, finalMessage, BotUtils.webhookClient);
    }

}
