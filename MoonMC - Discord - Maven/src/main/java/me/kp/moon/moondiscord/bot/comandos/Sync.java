package me.kp.moon.moondiscord.bot.comandos;

import me.kp.moon.moondiscord.bot.utils.BotUtils;
import me.kp.moon.moondiscord.mysql.MySQL;
import me.kp.moon.moondiscord.system.SysUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import org.apache.commons.lang.RandomStringUtils;

import java.util.concurrent.TimeUnit;

public class Sync extends ListenerAdapter {

    public MySQL mySQL = new MySQL();

    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        String args = e.getMessage().getContentRaw();
        TextChannel channel = e.getChannel();

        if (e.getMember() == null) return;

        Member member = e.getMember();
        User user = member.getUser();

        boolean dIDExists;

        if (args.startsWith(BotUtils.PREFIX + "sync") || args.startsWith("+sync") || args.startsWith("/sync")) {
            String key = RandomStringUtils.randomAlphanumeric(35);
            String discordID = user.getId();
            dIDExists = mySQL.discordIDExiste(discordID);
            String message = dIDExists ? "Essa conta já foi vinculada a um Player no servidor. Caso " +
                    "isso não deveria ter acontecido, contate <@!286652790326034432>." : "**NÃO DIVULGUE SUA KEY PARA NINGUÉM** \n\n" +
                    "Sua key de sincronização é: ||" + key + "||";

            RestAction<Void> sendDm = user.openPrivateChannel()
                    .flatMap(dm -> dm.sendMessage(message))
                    .delay(60, TimeUnit.SECONDS)
                    .flatMap(Message::delete);

            sendDm.queue();

            if (!SysUtils.keys.containsValue(discordID))
                SysUtils.keys.put(key, discordID);
            else {
                if (dIDExists)
                    return;
                SysUtils.keys.forEach((key1, value) -> {
                    if (value.equals(discordID)) {
                        SysUtils.keys.remove(key1);
                        SysUtils.keys.put(key, value);
                    }
                });
            }


        }
    }

}
