package me.kp.moon.moonlogin;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import me.kp.moon.moonlogin.commands.*;
import me.kp.moon.moonlogin.data.PlayerData;
import me.kp.moon.moonlogin.data.PlayerDataManager;
import me.kp.moon.moonlogin.enums.Strings;
import me.kp.moon.moonlogin.listeners.bukkit.BukkitListener;
import me.kp.moon.moonlogin.listeners.packets.PacketListener;
import me.kp.moon.moonlogin.mysql.MySQL;
import me.kp.moon.moonlogin.utils.SysUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    MySQL mySQL = new MySQL();

    public static Plugin getInstance() {
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
        mySQL.connectToDBS();
        Bukkit.getConsoleSender().sendMessage("§9[MoonLogin] §aPlugin carregado");
    }

    @Override
    public void onEnable() {
        PacketEvents.get().registerListener(new PacketListener());
        PacketEvents.get().init();
        registerEvents();
        registerCommands();
        scheduleTasks();
        Bukkit.getConsoleSender().sendMessage("§9[MoonLogin] §aPlugin habilitado");
    }

    @Override
    public void onDisable() {
        PacketEvents.get().terminate();
        Bukkit.getConsoleSender().sendMessage("§9[MoonLogin] §cPlugin desabilitado");
    }

    private void registerCommands() {
        getCommand("loginteste").setExecutor(new LoginTeste());
        getCommand("login").setExecutor(new Login());
        getCommand("register").setExecutor(new Register());
        getCommand("unregister").setExecutor(new Unregister());
        getCommand("forcelogin").setExecutor(new ForceLogin());
        getCommand("changepassword").setExecutor(new ChangePassword());
    }

    private void registerEvents() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new BukkitListener(), this);
    }

    private void scheduleTasks() {
        Bukkit.getScheduler().runTaskTimer(this, () -> Bukkit.getOnlinePlayers().forEach(player -> {
            PlayerData playerData = PlayerDataManager.getPlayerData(player);
            if (playerData == null) return;
            if (!playerData.isLoggedIn()) {
                if (playerData.getTimer() == 0) {
                    player.kickPlayer(Strings.getKickMessage("§cVocê demorou muito para se autenticar."));
                    return;
                }
                if (playerData.getPassword() == null) {
                    SysUtils.sendActionBar(player, "§cVocê tem " + playerData.getTimer() + " segundos para se registrar.");
                    if (playerData.getTimer() % 3 == 0) player.sendMessage("§eUtilize o comando §7/register <senha> <senha>§e.");
                } else {
                    SysUtils.sendActionBar(player, "§cVocê tem " + playerData.getTimer() + " segundos para logar.");
                    if (playerData.getTimer() % 3 == 0) player.sendMessage("§eUtilize o comando §7/login <senha>§e.");
                }
                playerData.setTimer(playerData.getTimer() - 1);
            }
        }), 0L, 20L);

        Bukkit.getScheduler().runTaskTimer(this, () -> Bukkit.getOnlinePlayers().forEach(player -> {
            PlayerData playerData = PlayerDataManager.getPlayerData(player);
            if (playerData == null) return;
            if (playerData.isKickable()) player.kickPlayer("§aSua senha foi alterada.");
        }),0L, 0L);
    }

}
