package me.kp.moon.moonpvp.warps;

import me.kp.moon.moonpvp.Main;
import me.kp.moon.moonpvp.warps.listeners.Damage;
import me.kp.moon.moonpvp.warps.listeners.LavaChallengeListener;
import me.kp.moon.moonpvp.warps.listeners.SumoListener;
import me.kp.moon.moonpvp.warps.listeners._1v1Listener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class WarpListeners {

    public static void registerWarpListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new _1v1Listener(), Main.getInstance());
        pluginManager.registerEvents(new SumoListener(), Main.getInstance());
        pluginManager.registerEvents(new LavaChallengeListener(), Main.getInstance());
        pluginManager.registerEvents(new Damage(), Main.getInstance());
    }

}
