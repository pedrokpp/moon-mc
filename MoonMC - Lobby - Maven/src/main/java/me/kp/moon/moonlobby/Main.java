package me.kp.moon.moonlobby;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import me.kp.moon.moonlobby.listeners.PlayerJoinQuit;
import me.kp.moon.moonlobby.listeners.PlayerListener;
import me.kp.moon.moonlobby.listeners.ServerListener;
import me.kp.moon.moonlobby.npc.PacketListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main getInstance() {
        return getPlugin(Main.class);
    }

    @Override
    public void onLoad() {
        PacketEvents.create(this);
        PacketEventsSettings settings = PacketEvents.get().getSettings();
        settings
                .fallbackServerVersion(ServerVersion.v_1_8_8)
                .compatInjector(false)
                .checkForUpdates(false)
                .bStats(true);
        PacketEvents.get().loadAsyncNewThread();
    }

    @Override
    public void onEnable() {
        registerEvents();
        registerCommands();
        PacketEvents.get().registerListener(new PacketListener());
        PacketEvents.get().init();
        Bukkit.getConsoleSender().sendMessage("§9[MoonLobby] §aPlugin habilitado.");
    }

    @Override
    public void onDisable() {
        PacketEvents.get().terminate();
        Bukkit.getConsoleSender().sendMessage("§9[MoonLobby] §cPlugin desabilitado.");
    }

    private void registerEvents() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinQuit(), this);
        pluginManager.registerEvents(new ServerListener(), this);
        pluginManager.registerEvents(new PlayerListener(), this);
    }

    private void registerCommands() {

    }

}
