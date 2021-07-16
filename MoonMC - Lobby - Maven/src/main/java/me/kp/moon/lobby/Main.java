package me.kp.moon.lobby;

import me.kp.moon.lobby.commands.Ping;
import net.md_5.bungee.api.plugin.Plugin;

public final class Main extends Plugin {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        Main.instance = this;
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands() {
        getProxy().getPluginManager().registerCommand(this, new Ping());
    }
}
