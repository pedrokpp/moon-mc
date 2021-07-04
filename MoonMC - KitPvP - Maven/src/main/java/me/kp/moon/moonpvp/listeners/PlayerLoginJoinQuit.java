package me.kp.moon.moonpvp.listeners;

import me.kp.moon.moonpvp.Main;
import me.kp.moon.moonpvp.api.TagAPI;
import me.kp.moon.moonpvp.clan.ClanSQL;
import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.kit.GladiatorUtils;
import me.kp.moon.moonpvp.kit.KitType;
import me.kp.moon.moonpvp.mysql.MySQL;
import me.kp.moon.moonpvp.utils.DeathUtils;
import me.kp.moon.moonpvp.utils.PlayerUtils;
import me.kp.moon.moonpvp.warps.WarpType;
import me.kp.moon.moonpvp.warps.WarpUtils;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerLoginJoinQuit implements Listener {

    @EventHandler
    public void PlayerLogin(PlayerLoginEvent event) {
        if (event.getResult() == PlayerLoginEvent.Result.KICK_WHITELIST) {
            event.setKickMessage("§cO servidor está em manutenção! Acompanhe os anúncios em nosso Discord.");
            return;
        }
        if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            event.setKickMessage("§cO servidor está lotado no momento. Caso deseje entrar com o servidor lotado," +
                    "adquira VIP em nosso Discord.");
        }
    }

    @EventHandler
    public void AsyncPlayerLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        PlayerDataManager.addPlayer(uuid);
        PlayerData playerData = PlayerDataManager.getPlayerData(uuid);
        if (playerData == null) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cOcorreu um erro.");
            return;
        }
        MySQL.createUser(uuid);
        MySQL.cachePlayer(playerData, false);
    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) {
            player.kickPlayer("§cOcorreu um erro.");
            return;
        }
        String clanTag = ClanSQL.getClanTag(player);
        if (clanTag != null)
            playerData.setCacheLastClan("§" + ClanSQL.getClanColor(clanTag) + "[" + clanTag + "]");
        else playerData.setCacheLastClan("");
        PlayerUtils.changePlayerTag(player, playerData.playerTag, playerData);
        PlayerUtils.sendPlayerToSpawn(player);
        PlayerUtils.giveInitialItems(player);
        Bukkit.getOnlinePlayers().forEach(online -> {
            PlayerData onlineData = PlayerDataManager.getPlayerData(online);
            if (onlineData == null) return;
            if (onlineData.inDuel) {
                online.hidePlayer(player);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void PlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) {
            PlayerDataManager.removePlayer(player);
            return;
        }
        if (playerData.combat) {
            Player last = playerData.lastCombatPlayer;
            PlayerData lastData = PlayerDataManager.getPlayerData(last);
            if (lastData == null) return;
            if (lastData.gladiatorLocation != null) {
                last.teleport(lastData.gladiatorLocation);
                GladiatorUtils.clearArena(lastData.gladiatorLocation);
            } else {
                if (playerData.gladiatorLocation != null) {
                    last.teleport(playerData.gladiatorLocation);
                    GladiatorUtils.clearArena(playerData.gladiatorLocation);
                }
            }
            if (lastData.warpType == WarpType._1v1) {
                lastData.setInDuel(false);
                lastData.setWarpType(WarpType._1v1);
                WarpUtils.teleportPlayerToWarp(last, lastData.warpType);
                WarpUtils.giveWarpItems(lastData);
            }
            if (lastData.warpType == WarpType.SUMO) {
                lastData.setInDuel(false);
                lastData.setWarpType(WarpType.SUMO);
                WarpUtils.teleportPlayerToWarp(last, lastData.warpType);
                WarpUtils.giveWarpItems(lastData);
            }
            playerData.setGladiatorLocation(null);
            lastData.setGladiatorLocation(null);
            PlayerUtils.removePlayerCombatLog(lastData);
            last.sendMessage("§aO player §e" + player.getName() + " §adeslogou em combate.");
            lastData.setCombat(false);
            DeathUtils.giveRandomXP(lastData);
            DeathUtils.giveRandomCoins(lastData);
            DeathUtils.removeRandomXP(playerData);
            DeathUtils.removeRandomCoins(playerData);
        }
        if (playerData.screenshare) {
            TextComponent textComponent = new TextComponent("§c§o(SCREENSHARE) O player §e§o" + player.getName() + " §c§odeslogou §c§oem §c§oScreenShare.");
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("" +
                    "§4#pazéosguri §5#vaidarban §6#sheesh").create()));
            Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("command.staffchat")).forEach(staffer -> staffer.sendMessage(textComponent));
        }
        playerData.setLastTag(player.getDisplayName().replace(player.getName(), "").trim());
        playerData.setUsername(player.getName());

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> MySQL.updatePlayer(playerData, false));

        TagAPI.deletePlayer(player);
        PlayerDataManager.removePlayer(player);
    }

}
