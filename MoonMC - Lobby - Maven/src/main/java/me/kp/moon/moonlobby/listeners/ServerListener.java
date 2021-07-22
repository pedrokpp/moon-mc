package me.kp.moon.moonlobby.listeners;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class ServerListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.EMERALD_BLOCK) {
            Player player = event.getPlayer();
            Location loc = player.getLocation();
            if (player.getGameMode() == GameMode.SURVIVAL)
                player.setVelocity(new Vector(-4.3f, 1, 0));
            player.playSound(loc, Sound.ORB_PICKUP, 6.0f, 1.0f);
        }
    }

}
