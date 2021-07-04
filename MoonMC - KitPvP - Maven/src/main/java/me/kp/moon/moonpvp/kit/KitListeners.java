package me.kp.moon.moonpvp.kit;

import me.kp.moon.moonpvp.Main;
import me.kp.moon.moonpvp.kit.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class KitListeners {

    public static void registerKitListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new AjninListener(), Main.getInstance());
        pluginManager.registerEvents(new ArcherListener(), Main.getInstance());
        pluginManager.registerEvents(new AnchorListener(), Main.getInstance());
        pluginManager.registerEvents(new BoxerListener(), Main.getInstance());
        pluginManager.registerEvents(new CamelListener(), Main.getInstance());
        pluginManager.registerEvents(new CriticalListener(), Main.getInstance());
        pluginManager.registerEvents(new FishermanListener(), Main.getInstance());
        pluginManager.registerEvents(new GladiatorListener(), Main.getInstance());
        pluginManager.registerEvents(new LeechListener(), Main.getInstance());
        pluginManager.registerEvents(new MagmaListener(), Main.getInstance());
        pluginManager.registerEvents(new MonkListener(), Main.getInstance());
        pluginManager.registerEvents(new NinjaListener(), Main.getInstance());
        pluginManager.registerEvents(new SnailListener(), Main.getInstance());
        pluginManager.registerEvents(new SpecialistListener(), Main.getInstance());
        pluginManager.registerEvents(new StomperListener(), Main.getInstance());
        pluginManager.registerEvents(new ThorListener(), Main.getInstance());
        pluginManager.registerEvents(new TurtleListener(), Main.getInstance());
        pluginManager.registerEvents(new VampireListener(), Main.getInstance());
        pluginManager.registerEvents(new ViperListener(), Main.getInstance());
    }

}
