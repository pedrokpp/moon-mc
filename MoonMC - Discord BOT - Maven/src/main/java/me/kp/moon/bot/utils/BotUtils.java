package me.kp.moon.bot.utils;

import me.kp.moon.bot.Main;
import me.kp.moon.bot.enums.GlobalVariables;
import net.dv8tion.jda.api.EmbedBuilder;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Random;

public class BotUtils {

    public static int getRandomInt(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }

    public static boolean clearingDM = false;

    public static void sendLog(String log) {
        Objects.requireNonNull(Main.jda.getTextChannelById(Config.logChannelID)).sendMessage(new EmbedBuilder()
                .setDescription(log)
                .setTimestamp(OffsetDateTime.now())
                .setColor(GlobalVariables.mainColor)
        .build()).queue();
    }

}
