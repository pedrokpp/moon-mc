package me.kp.moon.moonpvp.kit.listeners;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.kit.KitType;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class StomperListener implements Listener {

    @EventHandler
    private void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (playerData.kitType != KitType.STOMPER) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            player.getNearbyEntities(3.5, 1.0, 3.5).stream().filter(entities -> entities instanceof Player).forEach(entities -> {
                Player target = (Player) entities;
                PlayerData targetData = PlayerDataManager.getPlayerData(target);
                if (targetData == null) return;
                if (target.isSneaking() || targetData.kitType == KitType.ANTISTOMPER) {
                    target.damage(4.0 + 2.25, player);
                } else {
                    target.damage(event.getDamage() + 2.25, player);
                }
                player.getWorld().playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0f, 1.0f);
            });
            if (event.getDamage() > 6.0) {
                event.setDamage(6.0);
            }
        }
    }

    }
