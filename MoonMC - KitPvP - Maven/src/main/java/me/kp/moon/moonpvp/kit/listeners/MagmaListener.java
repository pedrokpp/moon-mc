package me.kp.moon.moonpvp.kit.listeners;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.kit.KitType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Random;

public class MagmaListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void EntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        Player damager = (Player) event.getDamager();
        PlayerData damagerData = PlayerDataManager.getPlayerData(damager);
        Player target = (Player) event.getEntity();
        if (damagerData == null) return;
        if (damagerData.kitType != KitType.MAGMA) return;
        int chance = new Random().nextInt(10);
        if (chance < 3) {
            target.setFireTicks(150);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void PlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (playerData.kitType != KitType.MAGMA) return;
        if (event.getFrom().getBlock().getType().name().contains("WATER")) {
                player.damage(2.0);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void PlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof  Player)) return;
        Player player = (Player) event.getEntity();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (playerData.kitType != KitType.MAGMA) return;
        if (event.getCause().name().contains("FIRE") || event.getCause().name().contains("LAVA")) {
            event.setCancelled(true);
        }
    }

}
