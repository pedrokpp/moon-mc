package me.kp.moon.moonpvp.kit.listeners;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.kit.KitType;
import me.kp.moon.moonpvp.kit.KitUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ThorListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void PlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!event.getAction().name().contains("RIGHT")) return;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (player.getItemInHand().getType() == Material.WOOD_AXE) {
            if (playerData.kitType != KitType.THOR) return;
            if (playerData.kitCooldown) {
                player.sendMessage(Messages.KIT_COOLDOWN.getMessage());
                return;
            }
            Block block = event.getClickedBlock();
            block.getWorld().strikeLightning(block.getLocation());
            KitUtils.addCooldown(playerData, 10);
        }

    }

    @EventHandler(ignoreCancelled = true)
    public void PlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING) {
            event.setDamage(event.getDamage() + 1.75);
        }
    }

}
