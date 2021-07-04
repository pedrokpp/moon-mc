package me.kp.moon.moonpvp.kit.listeners;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.kit.KitType;
import me.kp.moon.moonpvp.kit.KitUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class NinjaListener implements Listener {

    @EventHandler
    public void EntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player || !(event.getDamager() instanceof Player))) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        Player player = (Player) event.getDamager();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (playerData.kitType != KitType.NINJA) return;
        if (playerData.lastKitTarget != event.getEntity().getUniqueId())
            playerData.setLastKitTarget(event.getEntity().getUniqueId());
    }

    @EventHandler
    public void PlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (playerData.kitType != KitType.NINJA) return;
        if (playerData.lastKitTarget == null) return;
        Player target = Bukkit.getPlayer(playerData.lastKitTarget);
        if (target == null) {
            player.sendMessage("§cO último player que você hitou não foi encontrado.");
            playerData.setLastKitTarget(null);
            return;
        }
        if (playerData.kitCooldown) {
            player.sendMessage(Messages.KIT_COOLDOWN.getMessage());
            return;
        }
        double distance = player.getLocation().distance(target.getLocation());
        if (distance > 35.0) {
            player.sendMessage("§cSeu alvo está longe demais!");
            playerData.setLastKitTarget(null);
            return;
        }
        playerData.setKitCooldown(true);
        player.teleport(target.getLocation());
        player.sendMessage("§aVocê foi teleportado até §e" + target.getName() + "§a. §7(Ninja)");
        playerData.setLastKitTarget(null);
        KitUtils.addCooldown(playerData, 10);
    }

}
