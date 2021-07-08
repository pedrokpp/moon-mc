package me.kp.moon.moonlogin.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Setter
@Getter
public class PlayerData {

    private final Player player;

    private String password = null;
    private boolean loggedIn = false;

    public PlayerData(Player player) {
        this.player = player;
    }

}
