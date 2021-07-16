package me.kp.moon.bot.utils;

import java.util.Random;

public class BotUtils {

    public static int getRandomInt(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }

}
