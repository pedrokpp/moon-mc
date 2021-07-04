package me.kp.moon.moonpvp.listeners;

import me.kp.moon.moonpvp.Main;
import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.kit.KitType;
import me.kp.moon.moonpvp.utils.PlayerUtils;
import me.kp.moon.moonpvp.utils.SysUtils;
import me.kp.moon.moonpvp.warps.WarpType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.List;

public class CombatLog implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void PlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (playerData.combat) {
            final List<String> commands = Arrays.asList("/spawn", "/resetkit", "/admin", "/kit", "/warp", "/lobby");
            if (commands.contains(event.getMessage().split(" ")[0].toLowerCase())) {
                event.setCancelled(true);
                if (playerData.warpType == WarpType._1v1 || playerData.warpType == WarpType.SUMO) {
                    player.sendMessage("§cVocê não pode usar comandos em combate.");
                } else {
                    player.sendMessage("§cVocê não pode usar comandos em combate. §7Aguarde " +
                            String.format("%.1f", SysUtils.convertMsToSeconds(playerData.combatLogTime - System.currentTimeMillis())) + " segundos.");
                }
            }
        }
        if (playerData.screenshare && !player.hasPermission("command.staffchat")) {
            event.setCancelled(true);
            player.sendMessage("§cVocê não pode executar comandos na sala de SS.");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void EntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        Player player = (Player) event.getEntity();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        Player damager = (Player) event.getDamager();
        PlayerData damagerData = PlayerDataManager.getPlayerData(damager);
        if (playerData == null || damagerData == null) return;
        if ((playerData.kitType == null || playerData.warpType == null) ||
                (damagerData.kitType == null || damagerData.warpType == null)) return;
        if ((playerData.warpType == WarpType._1v1 && damagerData.warpType == WarpType._1v1) ||
                (playerData.warpType == WarpType.SUMO && damagerData.warpType == WarpType.SUMO)) return;

        if ((playerData.kitType == KitType.GLADIATOR || damagerData.kitType == KitType.GLADIATOR) &&
                (playerData.gladiatorLocation != null || damagerData.gladiatorLocation != null)) {
            PlayerUtils.addPlayersCombatLog(playerData, damagerData, 1000);
            PlayerUtils.addPlayersCombatLog(damagerData, playerData, 1000);
        } else {
            PlayerUtils.addPlayersCombatLog(playerData, damagerData, 20);
            PlayerUtils.addPlayersCombatLog(damagerData, playerData, 20);
        }
    }

}
