package me.kp.moon.moondiscord.plugin;

import me.kp.moon.moondiscord.bot.sistemas.StaffChat;
import me.kp.moon.moondiscord.bot.BotMain;
import me.kp.moon.moondiscord.mysql.MySQL;
import me.kp.moon.moondiscord.plugin.comandos.Sync;
import me.kp.moon.moondiscord.plugin.eventos.PlayerJoin;
import me.kp.moon.moondiscord.plugin.eventos.PlayerQuit;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;

public class Main extends JavaPlugin {

    private final MySQL mySQL = new MySQL();

    public static Main getInstance() {
        return getPlugin(Main.class);
    }

    @Override
    public void onLoad() {
        Bukkit.getConsoleSender().sendMessage("§9[MoonDiscord] §aIniciando instancia do bot...");
    }

    @Override
    public void onEnable() {
        registerEvents();
        registerCommands();
        mySQL.connectToDBS();
        try {
            BotMain.startBot();
            Bukkit.getConsoleSender().sendMessage("§9[MoonDiscord] §aBot iniciado!");
        } catch (LoginException | InterruptedException e) {
            Bukkit.getConsoleSender().sendMessage("§9[MoonDiscord] §cErro ao iniciar bot!");
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§9[MoonDiscord] §cBot desligado!");
        BotMain.stopBot();
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);
        Bukkit.getPluginManager().registerEvents(new StaffChat(), this);
    }

    private void registerCommands() {
        getCommand("sync").setExecutor(new Sync());
    }


}
