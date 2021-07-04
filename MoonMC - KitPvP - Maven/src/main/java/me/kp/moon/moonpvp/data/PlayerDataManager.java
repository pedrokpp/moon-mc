package me.kp.moon.moonpvp.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {

    public static ConcurrentHashMap<UUID, PlayerData> playerDataHashMap = new ConcurrentHashMap<>();

    public static void addPlayer(UUID uuid) {
            playerDataHashMap.put(uuid, new PlayerData(uuid));
    }

    public static void removePlayer(Player player) {
        if (player != null) {
            playerDataHashMap.remove(player.getUniqueId());
            Bukkit.getConsoleSender().sendMessage("§cA PlayerData do jogador §e" + player.getName() + "§c foi removida.");
        }
    }

    public static PlayerData getPlayerData(Player player) {
        if (playerDataHashMap.containsKey(player.getUniqueId()))
            return playerDataHashMap.get(player.getUniqueId());
        return null;
    }

    public static PlayerData getPlayerData(UUID uuid) {
        if (playerDataHashMap.containsKey(uuid))
            return playerDataHashMap.get(uuid);
        return null;
    }

}
