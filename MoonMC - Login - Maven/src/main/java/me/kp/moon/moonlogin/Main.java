package me.kp.moon.moonlogin;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import me.kp.moon.moonlogin.commands.LoginTeste;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

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
        //TODO Register a packet listener here
        PacketEvents.get().init();
        registerCommands();
    }

    @Override
    public void onDisable() {
        PacketEvents.get().terminate();
    }

    private void registerCommands() {
        getCommand("loginteste").setExecutor(new LoginTeste());
    }

}
