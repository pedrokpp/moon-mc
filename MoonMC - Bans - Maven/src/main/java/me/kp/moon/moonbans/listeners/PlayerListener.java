package me.kp.moon.moonbans.listeners;

import me.kp.moon.moonbans.Main;
import me.kp.moon.moonbans.data.PlayerData;
import me.kp.moon.moonbans.data.PlayerDataManager;
import me.kp.moon.moonbans.enums.Strings;
import me.kp.moon.moonbans.mysql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void AsyncPlayerLogin(AsyncPlayerPreLoginEvent event) {
        String ip = event.getAddress().getHostName().trim();
        System.out.println("'" + ip + "'");
        if (!MySQL.IPExiste(ip)) return;
        long tempbanTime = MySQL.getTempBanTime(ip, false);
        if (tempbanTime != -3) {
            if (MySQL.isBanned(ip, false) == 1) { // se o player estiver banido
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
                if (tempbanTime == -1) { // se for permanente
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, Strings.getPermaBanMessage());
                    System.out.println(event.getLoginResult().toString());
                } else if (System.currentTimeMillis() - tempbanTime < 0) { // agora - depois = negativo
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, Strings.getTempBanMessage(tempbanTime));
                    System.out.println(event.getLoginResult().toString());
                } else {
                    MySQL.applyUnban(ip, false);
                    event.allow();
                    System.out.println(event.getLoginResult().toString());
                }
            } else {
                event.allow();
                System.out.println(event.getLoginResult().toString());
            }
        } else {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cOcorreu um erro ao computar seus dados, por favor relogue.\n\n" +
                    "§cCaso os erros persistam, contate a staff em §9" + Strings.getDiscord() + "\n\n" +
                    "§eAtenciosamente, equipe do " + Strings.getName() + "§e.");
            System.out.println(event.getLoginResult().toString());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (!MySQL.UsernameExiste(player.getName())) return;
        long tempbanTime = MySQL.getTempBanTime(player.getName(), false);
        if (tempbanTime != -3) {
            if (MySQL.isBanned(player.getName(), false) == 1) { // se o player estiver banido
                if (tempbanTime == -1) { // se for permanente
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, Strings.getPermaBanMessage());
                    System.out.println(event.getResult().toString());
                } else if (System.currentTimeMillis() - tempbanTime < 0) { // agora - depois = negativo
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, Strings.getTempBanMessage(tempbanTime));
                    System.out.println(event.getResult().toString());
                } else {
                    MySQL.applyUnban(player.getName(), false);
                    event.allow();
                    System.out.println(event.getResult().toString());
                }
            } else {
                event.allow();
                System.out.println(event.getResult().toString());
            }
        } else {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cOcorreu um erro ao computar seus dados, por favor relogue.\n\n" +
                    "§cCaso os erros persistam, contate a staff em §9" + Strings.getDiscord() + "\n\n" +
                    "§eAtenciosamente, equipe do " + Strings.getName() + "§e.");
            System.out.println(event.getResult().toString());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void PlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        MySQL.createUser(player);
        PlayerDataManager.addPlayer(player);
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) {
            event.getPlayer().kickPlayer("§cErro ao computar seus dados. Relogue, por favor.");
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            /* mute */
            boolean muted = MySQL.isMuted(player.getName(), false) == 1;
            playerData.setMuted(muted);
            playerData.setMutedWhenJoined(muted);
            Long mutedTime = MySQL.getTempMuteTime(player.getName(), false);
            playerData.setCacheTempMuteTime(mutedTime);
            String muteReason = MySQL.getMuteReason(player.getName(), false);
            playerData.setMuteReason(muteReason);
            String muteAuthor = MySQL.getMuteAuthor(player.getName(), false);
            playerData.setMuteAuthor(muteAuthor);
            /* mute */
        });
    }

    @EventHandler
    public void PlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            if (playerData.isMutedWhenJoined != playerData.isMuted) {
                if (playerData.isMuted) {
                    MySQL.applyMute(player.getName(), playerData.cacheTempMuteTime, playerData.muteReason, playerData.muteAuthor, false);
                } else {
                    MySQL.applyUnmute(player.getName(), false);
                }
            }
        });
        PlayerDataManager.removePlayer(player);
    }

}
