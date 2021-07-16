package me.kp.moon.bot.utils;

import java.util.ArrayList;
import java.util.List;

public class ThreadUtils {

    private static final List<String> peopleThreads = new ArrayList<>();

    public static boolean hasThread(String id) {
        return peopleThreads.contains(id);
    }
    public static void addID(String id) {
        if (!peopleThreads.contains(id))
            peopleThreads.add(id);
    }
    public static void removeID(String id) {
        peopleThreads.remove(id);
    }

}
