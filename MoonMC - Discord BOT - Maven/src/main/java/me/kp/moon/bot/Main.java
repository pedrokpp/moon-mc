package me.kp.moon.bot;

import me.kp.moon.bot.comandos.PingarTicket;
import me.kp.moon.bot.comandos.SetupTicket;
import me.kp.moon.bot.eventos.TicketReact;
import me.kp.moon.bot.utils.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class Main {

    public static JDA jda;

    private static boolean setJDA() {
        try {
            JDABuilder builder = JDABuilder.createDefault(Config.botToken);
            builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
            builder.setBulkDeleteSplittingEnabled(false);
            builder.setActivity(Activity.playing("no moon-mc.net"));

            jda = builder.build();
        } catch (LoginException exception) {
            System.out.println("NÃO FOI POSSÍVEL INICIALIZAR O BOT.");
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        if (!setJDA()) return;
        jda.addEventListener(new SetupTicket());
        jda.addEventListener(new TicketReact());
        jda.addEventListener(new PingarTicket());
    }

}
