package me.kp.moon.moonpvp.evento;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class EventoListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockBreak(BlockBreakEvent event) {
        if (!EventoUtils.evento) return;
        
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) {
            event.setCancelled(true);
            return;
        }
        if (!playerData.evento) return;
        if (!EventoUtils.build) {
            event.setCancelled(true);
        }
        event.setCancelled(!EventoUtils.blocks.contains(event.getBlock().getLocation()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockPlace(BlockPlaceEvent event) {
        if (!EventoUtils.evento) return;
        
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) {
            event.setCancelled(true);
            return;
        }
        if (!playerData.evento) return;
        if (!EventoUtils.build) {
            event.setCancelled(true);
        }
        EventoUtils.blocks.add(event.getBlock().getLocation());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void BucketFill(PlayerBucketFillEvent event) {
        if (!EventoUtils.evento) return;
        
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) {
            event.setCancelled(true);
            return;
        }
        if (!playerData.evento) return;
        if (!EventoUtils.build) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void BucketEmpty(PlayerBucketEmptyEvent event) {
        if (!EventoUtils.evento) return;
        
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) {
            event.setCancelled(true);
            return;
        }
        if (!playerData.evento) return;
        if (!EventoUtils.build) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void EntityDamage(EntityDamageEvent event) {
        if (!EventoUtils.evento) return;

        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (!playerData.evento) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            if (!EventoUtils.pvp) {
                event.setCancelled(true);
            }
        } else {
            if (!EventoUtils.damage) {
                event.setCancelled(true);
            }
        }
    }

}
