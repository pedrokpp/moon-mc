package me.kp.moon.moonpvp.gui;

import me.kp.moon.moonpvp.Main;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class GuiListener {

    public static void registerGuiListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ShopGUI(), Main.getInstance());
        pluginManager.registerEvents(new SeusKitsGUI(), Main.getInstance());
        pluginManager.registerEvents(new ShopKDRGUI(), Main.getInstance());
        pluginManager.registerEvents(new ShopKitsGUI(), Main.getInstance());
        pluginManager.registerEvents(new SoupTypeGUI(), Main.getInstance());
        pluginManager.registerEvents(new WarpsGUI(), Main.getInstance());
        pluginManager.registerEvents(new StatusGUI(), Main.getInstance());
    }

}
