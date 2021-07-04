package me.kp.moon.moonpvp.warps.listeners;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.warps.WarpType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class Damage implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (playerData.warpType != null) {
            if (event.getCause() == EntityDamageEvent.DamageCause.VOID) event.setDamage(21.0);
            if (playerData.warpType == WarpType.FPS && player.getLocation().getY() >= 81) event.setCancelled(true);
            if (playerData.warpType == WarpType.FISHERMAN) {
                if (player.getLocation().getY() >= 117) event.setCancelled(true);
                if (event.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK && event.getCause() != EntityDamageEvent.DamageCause.LAVA &&
                        event.getCause() != EntityDamageEvent.DamageCause.FIRE && event.getCause() != EntityDamageEvent.DamageCause.VOID)
                    event.setDamage(0);
            }
            if (playerData.warpType == WarpType.KB) {
                if (player.getLocation().getY() >= 88) event.setCancelled(true);
                if (event.getCause() == EntityDamageEvent.DamageCause.FALL ||
                        event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) event.setDamage(0);
            }
        }
    }

}
