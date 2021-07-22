package me.kp.moon.moonlobby;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main getInstance() {
        return getPlugin(Main.class);
    }

    @Override
    public void onEnable() {
        registerEvents();
        registerCommands();
        Bukkit.getConsoleSender().sendMessage("§9[MoonWE] §aPlugin habilitado.");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§9[MoonWE] §cPlugin desabilitado.");
    }

    private void registerEvents() {
        PluginManager pluginManager = Bukkit.getPluginManager();

    }

    private void registerCommands() {

    }

}
