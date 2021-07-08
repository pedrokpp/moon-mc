package me.kp.moon.moonpvp.cache;

import org.bukkit.entity.Player;

import java.util.*;

public class SysCache {

    public static final HashMap<String, List<String>> cacheReports = new HashMap<>();
    public static void addReportToPlayer(Player player, String report) {
        if (!cacheReports.containsKey(player.getName()))
            cacheReports.put(player.getName(), Collections.singletonList(report));
        else {
            List<String> reports = new ArrayList<>(cacheReports.get(player.getName()));
            reports.add(report);
            cacheReports.replace(player.getName(), reports);
        }
    }

}
