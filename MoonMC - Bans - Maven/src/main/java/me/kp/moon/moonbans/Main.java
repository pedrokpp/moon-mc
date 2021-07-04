package me.kp.moon.moonbans;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import me.kp.moon.moonbans.commands.*;
import me.kp.moon.moonbans.listeners.PacketListener;
import me.kp.moon.moonbans.listeners.PlayerListener;
import me.kp.moon.moonbans.mysql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    MySQL mySQL = new MySQL();

    public static Main getInstance() {
        return getPlugin(Main.class);
    }

    @Override
    public void onLoad() {
        Bukkit.getConsoleSender().sendMessage("§9[MoonBans] §aCarregando plugin...");
        mySQL.connectToDBS();
        PacketEvents.create(this);
        PacketEventsSettings settings = PacketEvents.get().getSettings();
        settings.fallbackServerVersion(ServerVersion.v_1_8_8)
                .compatInjector(false)
                .checkForUpdates(false)
                .bStats(true);
        PacketEvents.get().loadAsyncNewThread();
    }

    @Override
    public void onEnable() {
        PacketEvents.get().registerListener(new PacketListener());
        PacketEvents.get().init();
        registerEvents();
        registerCommands();
        Bukkit.getConsoleSender().sendMessage("§9[MoonBans] §aPlugin carregado com sucesso.");
    }

    @Override
    public void onDisable() {
        PacketEvents.get().terminate();
        Bukkit.getConsoleSender().sendMessage("§9[MoonBans] §cPlugin desligado.");
    }

    private void registerEvents() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerListener(), this);
    }

    private void registerCommands() {
        getCommand("testt").setExecutor(new Test());
        getCommand("ipban").setExecutor(new Ipban());
        getCommand("checkban").setExecutor(new Checkban());
        getCommand("tempipban").setExecutor(new TempIpBan());
        getCommand("unban").setExecutor(new Unban());
        getCommand("mute").setExecutor(new Mute());
        getCommand("tempmute").setExecutor(new TempMute());
        getCommand("unmute").setExecutor(new Unmute());
        getCommand("checkmute").setExecutor(new Checkmute());
        getCommand("alts").setExecutor(new Alts());
        getCommand("checkip").setExecutor(new Checkip());
        getCommand("kick").setExecutor(new Kick());
    }
}
