package me.kp.moon.moonpvp.kit.listeners;

import me.kp.moon.moonpvp.Main;
import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.kit.KitType;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

public class AnchorListener implements Listener {

    @EventHandler
    public void EntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            player.setVelocity(new Vector());
            return;
        }
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        Player damager = (Player) event.getDamager();
        PlayerData playerDataDamager = PlayerDataManager.getPlayerData(damager);
        if (playerData == null || playerDataDamager == null) return;
        if (playerData.kitType == KitType.ANCHOR || playerDataDamager.kitType == KitType.ANCHOR) {
            player.getWorld().playSound(player.getLocation(), Sound.ANVIL_BREAK, 0.4f, 4.0f);
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> player.setVelocity(new Vector()), 1L);
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> damager.setVelocity(new Vector()), 1L);
        }
    }

}
