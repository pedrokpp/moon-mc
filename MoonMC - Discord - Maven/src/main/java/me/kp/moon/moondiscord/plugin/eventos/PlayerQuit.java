package me.kp.moon.moondiscord.plugin.eventos;

import me.kp.moon.moondiscord.bot.utils.BotUtils;
import me.kp.moon.moondiscord.plugin.utils.PlayerUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (e.getQuitMessage() != null) {
            e.setQuitMessage(null);
        }
        Player p = e.getPlayer();
        if (PlayerUtils.staffJoin.containsKey(p)) {
            Date date = new Date();
            DateFormat df = new SimpleDateFormat("HH:mm");

            try {
                Date dateJoin = df.parse(PlayerUtils.staffJoin.get(p));
                long time_difference = date.getTime() - dateJoin.getTime();
                long minutes_difference = (time_difference / (1000*60)) % 60;
                long hours_difference = (time_difference / (1000*60*60)) % 24;

                String time = hours_difference == 0 ? minutes_difference + " minutos" : hours_difference + " horas e " + minutes_difference + " minutos";

                if (!df.format(date).equals(PlayerUtils.staffJoin.get(p))) {
                    df.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("**:clipboard: LOGS - PRESENÇA :clipboard:**")
                            .setThumbnail("https://minotar.net/avatar/" + p.getName())
                            .addField("Staffer", "``" + p.getName() + "``", false)
                            .addField("Entrou às", "``" + PlayerUtils.staffJoin.get(p) + "``", true)
                            .addField("Saiu às", "``" + df.format(date) + "``", true)
                            .addField("Tempo online", "``" + time + "``", true)
                            .setFooter(BotUtils.FOOTER)
                            .setColor(BotUtils.MAIN_COLOR);
                    BotUtils.sendMsg(BotUtils.ID_CHANNEL_JOIN_LOG, null, embed.build());
                    PlayerUtils.staffJoin.remove(p);
                }

            } catch (ParseException parseException) {
                if (!df.format(date).equals(PlayerUtils.staffJoin.get(p))) {
                    df.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("**:clipboard: LOGS - PRESENÇA :clipboard:**")
                            .setThumbnail("https://minotar.net/avatar/" + p.getName())
                            .addField("Staffer", "``" + p.getName() + "``", false)
                            .addField("Entrou às", "``" + PlayerUtils.staffJoin.get(p) + "``", true)
                            .addField("Saiu às", "``" + df.format(date) + "``", true)
                            .setFooter(BotUtils.FOOTER)
                            .setColor(BotUtils.MAIN_COLOR);
                    BotUtils.sendMsg(BotUtils.ID_CHANNEL_JOIN_LOG, null, embed.build());
                    PlayerUtils.staffJoin.remove(p);
                }
            }

        }
    }

}
