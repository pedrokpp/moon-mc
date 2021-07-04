package me.kp.moon.moondiscord.bot.comandos;

import me.kp.moon.moondiscord.bot.utils.BotUtils;
import me.kp.moon.moondiscord.plugin.utils.PlayerUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class CheckStaff extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        String args = e.getMessage().getContentRaw();
        MessageChannel channel = e.getChannel();

        if (e.getMember() == null) {
            return;
        }

        Member member = e.getMember();

        if (args.startsWith(BotUtils.PREFIX + "checkstaff") || args.startsWith("+checkstaff") || args.startsWith("!checkstaff")) {
            int onlinePlayers = Bukkit.getOnlinePlayers().size();

            List<String> staffOn = new ArrayList<>();

            Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("command.staffchat")).forEach(s -> {
                String str = PlayerUtils.getPlayerTag(s);
                assert str != null;
                String tag = str.substring(4, str.length()-3);
                staffOn.add(tag + " " + s.getName());
            });


            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(":rocket: MoonMC - Informações sobre a staff")
                    .setDescription("Lista dos staffers e seus respectivos cargos:\n\n``" +
                            (staffOn.size() == 1 ? staffOn.get(0) + "``" : StringUtils.join(staffOn, "``,\n ``")))
                    .setThumbnail(e.getGuild().getIconUrl())
                    .setFooter(BotUtils.FOOTER)
                    .setColor(BotUtils.MAIN_COLOR);
            channel.sendMessage(embed.build()).queue();
        }
    }

}
