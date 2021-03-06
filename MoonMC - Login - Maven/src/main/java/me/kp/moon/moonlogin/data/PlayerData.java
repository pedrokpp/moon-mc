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

    private boolean warnedUnregister = false;

    private boolean kickable = false;

    private int timer = 30;

    public PlayerData(Player player) {
        this.player = player;
    }

}
