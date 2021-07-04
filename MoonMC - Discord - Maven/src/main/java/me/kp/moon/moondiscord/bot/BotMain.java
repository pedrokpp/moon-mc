package me.kp.moon.moondiscord.bot;

import me.kp.moon.moondiscord.bot.comandos.CheckStaff;
import me.kp.moon.moondiscord.bot.comandos.Console;
import me.kp.moon.moondiscord.bot.comandos.Ip;
import me.kp.moon.moondiscord.bot.comandos.Sync;
import me.kp.moon.moondiscord.bot.sistemas.StaffChat;
import me.kp.moon.moondiscord.bot.utils.BotUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class BotMain {

    public static JDA jda;

    public static void startBot() throws LoginException, InterruptedException {
        jda = JDABuilder.createDefault(BotUtils.TOKEN)
                .addEventListeners(new Console())
                .addEventListeners(new Ip())
                .addEventListeners(new StaffChat())
                .addEventListeners(new Sync())
                .addEventListeners(new CheckStaff())
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setActivity(Activity.playing("no moon-mc.com"))
                .build();

        jda.awaitReady();
    }

    public static void stopBot() {
        jda.shutdown();
    }

}
