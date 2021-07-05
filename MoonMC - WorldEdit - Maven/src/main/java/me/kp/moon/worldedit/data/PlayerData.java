package me.kp.moon.worldedit.data;

import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

@Setter
public class PlayerData {

    private Player player;
    private UUID playerUUID;

    public Location pos1;
    public Location pos2;

    public PlayerData(Player player) {
        this.player = player;
        this.playerUUID = player.getUniqueId();
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }
    public Player getPlayer() {
        return player;
    }

}
