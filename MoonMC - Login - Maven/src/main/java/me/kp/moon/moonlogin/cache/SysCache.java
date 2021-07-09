package me.kp.moon.moonlogin.cache;

import me.kp.moon.moonlogin.Main;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class SysCache {

    private static final List<String> recentConnections = new ArrayList<>();

    public static void addRecentConnection(String ip) {
        if (!recentConnections.contains(ip)) {
            recentConnections.add(ip);
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> recentConnections.remove(ip), 15 * 20L);
        }
    }

    public static boolean isIpCached(String ip) {
        return recentConnections.contains(ip);
    }

}
