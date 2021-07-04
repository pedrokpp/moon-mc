package me.kp.moon.moonpvp.kit.listeners;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.kit.KitType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CamelListener implements Listener {

    @EventHandler
    public void EntityDamage(EntityDamageEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (playerData.kitType != KitType.CAMEL) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.CONTACT) event.setCancelled(true);
    }

    @EventHandler
    public void PlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (playerData.kitType != KitType.CAMEL) return;
        Block block = event.getTo().getBlock().getRelative(BlockFace.DOWN);
        if (block.getBiome().name().contains("DESERT") || block.getType() == Material.SAND) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 4 * 20, 0), true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 4 * 20, 0), true);
        }
    }

}
