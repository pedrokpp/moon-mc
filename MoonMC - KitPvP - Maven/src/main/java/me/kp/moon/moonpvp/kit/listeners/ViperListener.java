package me.kp.moon.moonpvp.kit.listeners;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.kit.KitType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class ViperListener implements Listener {

    @EventHandler
    public void EntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        Player damager = (Player) event.getDamager();
        PlayerData damagerData = PlayerDataManager.getPlayerData(damager);
        Player target = (Player) event.getEntity();
        if (damagerData == null) return;
        if (damagerData.kitType != KitType.VIPER) return;
        int chance = new Random().nextInt(10);
        if (chance < 3) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 5 * 20, 1));
        }
    }
    
}
