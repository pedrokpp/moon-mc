package me.kp.moon.moonbans.data;

import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {

    public static ConcurrentHashMap<UUID, PlayerData> playerDataHashMap = new ConcurrentHashMap<>();

    public static void addPlayer(Player player) {
        if (player != null) {
            PlayerDataManager.playerDataHashMap.put(player.getUniqueId(), new PlayerData(player));
        }
    }

    public static void removePlayer(Player player) {
        if (player != null) {
            PlayerDataManager.playerDataHashMap.remove(player.getUniqueId());
        }
    }

    public static PlayerData getPlayerData(Player player) {
        if (PlayerDataManager.playerDataHashMap.containsKey(player.getUniqueId())) {
            return PlayerDataManager.playerDataHashMap.get(player.getUniqueId());
        }
        return null;
    }

}
