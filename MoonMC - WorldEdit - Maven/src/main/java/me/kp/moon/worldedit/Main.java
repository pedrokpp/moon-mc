package me.kp.moon.worldedit;

import me.kp.moon.worldedit.commands.Set;
import me.kp.moon.worldedit.commands.Wand;
import me.kp.moon.worldedit.listeners.PlayerJoinQuit;
import me.kp.moon.worldedit.listeners.WandListener;
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
        pluginManager.registerEvents(new PlayerJoinQuit(), this);
        pluginManager.registerEvents(new WandListener(), this);
    }

    private void registerCommands() {
        getCommand("/wand").setExecutor(new Wand());
        getCommand("/set").setExecutor(new Set());
    }

}
