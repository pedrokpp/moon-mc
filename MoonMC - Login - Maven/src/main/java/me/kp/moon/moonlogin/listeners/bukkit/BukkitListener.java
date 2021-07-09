package me.kp.moon.moonlogin.listeners.bukkit;

import me.kp.moon.moonlogin.cache.SysCache;
import me.kp.moon.moonlogin.data.PlayerData;
import me.kp.moon.moonlogin.data.PlayerDataManager;
import me.kp.moon.moonlogin.enums.Strings;
import me.kp.moon.moonlogin.lists.ProxyList;
import me.kp.moon.moonlogin.mysql.MySQL;
import me.kp.moon.moonlogin.utils.SysUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BukkitListener implements Listener {

    private final List<String> welcome = Arrays.asList("§aEstavamos te esperando, %name%!", "§aHá quanto tempo!", "§aSeja bem vindo novamente!", "Se divirta no servidor, %name%!");

    @EventHandler(priority = EventPriority.HIGHEST)
    public void AsyncPreLogin(AsyncPlayerPreLoginEvent event) {
        String ip = event.getAddress().getHostName().trim();

        if (ProxyList.proxyList.contains(ip)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Strings.getKickMessage("§cProxy detectado em sua conexão."));
        }
        else if (Bukkit.getOnlinePlayers().stream().filter(p -> p.getAddress().getHostName().equals(ip)).count() > 2) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Strings.getKickMessage("§cVocê excedeu o limite máximo de contas conectadas por IP no servidor."));
        }
        else if (SysCache.isIpCached(ip)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Strings.getKickMessage("§cVocê reconectou muito rápido e por isso, sua conexão foi bloqueada. Tente novamente em alguns segundos."));
        }

        SysCache.addRecentConnection(ip);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        PlayerDataManager.addPlayer(player);
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cOcorreu um erro.");
            return;
        }
        // tentar pegar o cache da senha, checando se o player existe no dbs
        // caso não exista, password == null. se não, password = decodedPassword
        MySQL.cacheDecodedPassword(playerData, true);

    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        String welcomeMessage = playerData.getPassword() != null ? welcome.get(new Random().nextInt(welcome.size()))
                .replace("%name%", player.getName()) : "§aSeja bem vindo ao nosso servidor!";
        SysUtils.sendTitle(player, "§9§lMoon§1§lMC", welcomeMessage);
    }

    @EventHandler
    public void PlayerQuit(PlayerQuitEvent event) {
        PlayerDataManager.removePlayer(event.getPlayer());
    }

}
