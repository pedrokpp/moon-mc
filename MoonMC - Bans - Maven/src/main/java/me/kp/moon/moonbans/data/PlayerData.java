package me.kp.moon.moonbans.data;

import lombok.Setter;
import org.bukkit.entity.Player;

@Setter
public class PlayerData {

    private final Player player;

    public boolean isMutedWhenJoined = false;
    public boolean isMuted = false;
    public String muteReason = "";
    public String muteAuthor = "";

    public long cacheTempMuteTime = 0L;

    public PlayerData(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
