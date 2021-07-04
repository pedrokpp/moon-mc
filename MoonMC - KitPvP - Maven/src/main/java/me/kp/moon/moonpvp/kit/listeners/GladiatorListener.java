package me.kp.moon.moonpvp.kit.listeners;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.kit.GladiatorUtils;
import me.kp.moon.moonpvp.kit.KitType;
import me.kp.moon.moonpvp.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class GladiatorListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void PlayerInteract(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof Player)) return;
        Player player = event.getPlayer();
        Player target = (Player) event.getRightClicked();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        PlayerData targetData = PlayerDataManager.getPlayerData(player);
        if (playerData == null || targetData == null) return;
        if (playerData.kitType != KitType.GLADIATOR) return;

        if (player.getItemInHand().getType() == Material.IRON_FENCE) {
            if (playerData.gladiatorLocation != null) {
                player.sendMessage("§cVocê já está em uma arena de gladiator.");
                return;
            }
            if (targetData.admin || targetData.vanish || targetData.superadmin || target.getGameMode() != GameMode.SURVIVAL) {
                player.sendMessage("§cVocê não pode duelar esse player.");
                return;
            }
            playerData.setGladiatorLocation(player.getLocation());
            targetData.setGladiatorLocation(player.getLocation());
            if (GladiatorUtils.checkArenasNearbyPlayer(player)) {
                player.sendMessage("§cNessa área já possui uma arena de gladiator.");
                playerData.setGladiatorLocation(null);
                targetData.setGladiatorLocation(null);
                return;
            }
            GladiatorUtils.spawnArena(player.getLocation());
            GladiatorUtils.teleportPlayersToArena(player, target);
            player.damage(0, target);
            target.damage(0, player);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void EntityDamateEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player || event.getDamager() instanceof Player)) return;
        Player damaged = (Player) event.getEntity();
        PlayerData damagedData = PlayerDataManager.getPlayerData(damaged);
        if (damagedData == null) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            if (damagedData.kitType == KitType.GLADIATOR) {
                if (event.getDamage() >= damaged.getHealth()) {
                    Player last = damagedData.lastCombatPlayer;
                    PlayerData lastData = PlayerDataManager.getPlayerData(last);
                    if (lastData == null) return;
                    if (lastData.gladiatorLocation == null) {
                        last.teleport(damagedData.gladiatorLocation);
                        GladiatorUtils.clearArena(damagedData.gladiatorLocation);
                    } else {
                        last.teleport(lastData.gladiatorLocation);
                        GladiatorUtils.clearArena(lastData.gladiatorLocation);
                    }
                    lastData.setGladiatorLocation(null);
                    damagedData.setGladiatorLocation(null);
                    PlayerUtils.removePlayerCombatLog(lastData);
                }
            }
            return;
        }
        Player damager = (Player) event.getDamager();
        PlayerData damagerData = PlayerDataManager.getPlayerData(damager);
        if (damagerData == null) return;
        KitType damagerKit = damagerData.kitType;
        KitType damagedKit = damagedData.kitType;
        if (damagerKit == null || damagedKit == null) return;

        if (event.getDamage() >= damaged.getHealth()) {
            if (damagerKit == KitType.GLADIATOR) {
                if (damagerData.gladiatorLocation != null) {
                    damager.teleport(damagerData.gladiatorLocation);
                    GladiatorUtils.clearArena(damagerData.gladiatorLocation);
                } else {
                    damager.teleport(damagedData.gladiatorLocation);
                    GladiatorUtils.clearArena(damagedData.gladiatorLocation);
                }
                damagerData.setGladiatorLocation(null);
                damagedData.setGladiatorLocation(null);
                PlayerUtils.removePlayerCombatLog(damagerData);
            } else if (damagedKit == KitType.GLADIATOR) {
                Player last = damagedData.lastCombatPlayer;
                if (damagerData.gladiatorLocation == null) {
                    last.teleport(damagedData.gladiatorLocation);
                    GladiatorUtils.clearArena(damagedData.gladiatorLocation);
                } else {
                    last.teleport(damagerData.gladiatorLocation);
                    GladiatorUtils.clearArena(damagerData.gladiatorLocation);
                }
                damagerData.setGladiatorLocation(null);
                damagedData.setGladiatorLocation(null);
                PlayerUtils.removePlayerCombatLog(damagerData);
            }
        }
    }

}
