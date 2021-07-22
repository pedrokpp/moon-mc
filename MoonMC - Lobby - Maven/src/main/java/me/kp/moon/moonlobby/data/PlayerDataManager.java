package me.kp.moon.moonlobby.data;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager {

    private static final HashMap<UUID, PlayerData> playerDataHashMap = new HashMap<>();

    public static void addPlayer(Player player) {
        playerDataHashMap.put(player.getUniqueId(), new PlayerData(player));
    }

    public static void removePlayer(Player player) {
        playerDataHashMap.remove(player.getUniqueId());
    }

    public static PlayerData getPlayerData(Player player) {
        if (playerDataHashMap.containsKey(player.getUniqueId()))
            return playerDataHashMap.get(player.getUniqueId());
        return null;
    }


}
