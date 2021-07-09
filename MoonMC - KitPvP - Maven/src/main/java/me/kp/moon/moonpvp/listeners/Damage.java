package me.kp.moon.moonpvp.listeners;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.warps.WarpType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class Damage implements Listener {

    @EventHandler
    public void PlayerDamage(EntityDamageEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (playerData.evento) return;
        if (playerData.kitType == null || playerData.warpType == null) event.setCancelled(true);
        if (!playerData.inDuel && (playerData.warpType == WarpType._1v1 || playerData.warpType == WarpType.SUMO)) event.setCancelled(true);
        if (playerData.inDuel && playerData.warpType == WarpType.SUMO) {
            if (event.getCause() == EntityDamageEvent.DamageCause.VOID) event.setDamage(20.0);
            else event.setDamage(0);
        }
        if (playerData.admin || playerData.superadmin || playerData.vanish) event.setCancelled(true);
    }

    @EventHandler
    public void PlayerDamagePlayer(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        Player damager = (Player) event.getDamager();
        PlayerData damagerData = PlayerDataManager.getPlayerData(damager);
        if (playerData == null || damagerData == null) return;
        if (playerData.evento) return;
        if (damagerData.evento) return;
        if (playerData.admin || playerData.superadmin || playerData.vanish) event.setCancelled(true);
        if (damagerData.admin || damagerData.superadmin || damagerData.vanish) event.setCancelled(true);
        if (playerData.warpType == WarpType._1v1 || playerData.warpType == WarpType.SUMO) {
            if (playerData.inDuel && !damagerData.inDuel) event.setCancelled(true);
            if (!playerData.inDuel && !damagerData.inDuel) event.setCancelled(true);
        }
    }

}
