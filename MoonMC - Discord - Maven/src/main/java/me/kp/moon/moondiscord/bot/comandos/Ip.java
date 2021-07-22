package me.kp.moon.moondiscord.bot.comandos;

import me.kp.moon.moondiscord.bot.utils.BotUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;

public class Ip extends ListenerAdapter {

    private int pingMedio;

    private int getPingMedio() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            try {
                Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
                pingMedio = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | NoSuchFieldException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        return pingMedio /= Bukkit.getOnlinePlayers().size();
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        String args = e.getMessage().getContentRaw();
        MessageChannel channel = e.getChannel();

        if (e.getMember() == null) {
            return;
        }

        if (args.startsWith(BotUtils.PREFIX + "ip") || args.startsWith("+ip") || args.startsWith("!ip")) {
            int onlinePlayers = Bukkit.getOnlinePlayers().size();
            int pingMedio = onlinePlayers == 0 ? 0 : getPingMedio();

            int staffCount = (int) Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("command.staffchat")).count();

            String manut = Bukkit.hasWhitelist() ? "Sim" : "Não";

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(":rocket: MoonMC - Informações")
                    .setDescription("Informações referentes ao ip ``moon-mc.net``\n\nEntre no servidor e participe das estatísticas!")
                    .addField("Quantos players online?", "``" +
                            onlinePlayers +
                            " players``", false)
                    .addField("Quantos staffers online?", "``" +
                            staffCount + " staffers``", false)
                    .addField("Qual é o ping médio do servidor?", "``" +
                            pingMedio + "``", false)
                    .addField("O server está em manutenção?", "``" +
                            manut + "``", false)
                    .setThumbnail(e.getGuild().getIconUrl())
                    .setFooter(BotUtils.FOOTER)
                    .setColor(BotUtils.MAIN_COLOR);
            channel.sendMessage(embed.build()).queue();
        }
    }
}
