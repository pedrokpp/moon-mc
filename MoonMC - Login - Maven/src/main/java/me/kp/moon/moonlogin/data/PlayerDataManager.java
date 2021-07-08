package me.kp.moon.moonlogin.data;

import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {
    
    private static final ConcurrentHashMap<UUID, PlayerData> map = new ConcurrentHashMap<>();
    
    public static void addPlayer(Player player) {
        if (!map.containsKey(player.getUniqueId()))
            map.put(player.getUniqueId(), new PlayerData(player));
    }

    public static void removePlayer(Player player) {
        map.remove(player.getUniqueId());
    }

    public static PlayerData getPlayerData(Player player) {
        return map.get(player.getUniqueId());
    }
    public static PlayerData getPlayerData(UUID uuid) {
        return map.get(uuid);
    }
}
