package me.kp.moon.moonpvp.listeners;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class BuildListener implements Listener {

    @EventHandler
    public void BlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) {
            event.setCancelled(true);
            return;
        }
        if (playerData.evento) return;
        if (!playerData.build) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void BlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) {
            event.setCancelled(true);
            return;
        }
        if (playerData.evento) return;
        if (!playerData.build) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void BucketFill(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) {
            event.setCancelled(true);
            return;
        }
        if (playerData.evento) return;
        if (!playerData.build) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void BucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) {
            event.setCancelled(true);
            return;
        }
        if (playerData.evento) return;
        if (!playerData.build) {
            event.setCancelled(true);
        }
    }

}
