package me.kp.moon.moonlobby.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public class PlayerData {

    private int kitPvPEntityID = 0;
    private int duelsEntityID = 0;
    private int xEntityID = 0;

    private final Player player;
    public PlayerData(Player player) {
        this.player = player;
    }

}
