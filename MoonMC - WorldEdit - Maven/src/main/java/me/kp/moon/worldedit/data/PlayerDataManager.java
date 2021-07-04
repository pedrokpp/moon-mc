package me.kp.moon.worldedit.data;

import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {

    private static final ConcurrentHashMap<UUID, PlayerData> playerDataConcurrentHashMap = new ConcurrentHashMap<>();

    public static void addPlayer(Player player) {
        if (!playerDataConcurrentHashMap.containsKey(player.getUniqueId()))
            playerDataConcurrentHashMap.put(player.getUniqueId(), new PlayerData(player));
    }

    public static PlayerData getPlayerData(Player player) {
        return playerDataConcurrentHashMap.get(player.getUniqueId());
    }

    public static void removePlayer(Player player) {
        playerDataConcurrentHashMap.remove(player.getUniqueId());
    }

}
