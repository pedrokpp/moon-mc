package me.kp.moon.moonpvp.listeners;

import me.kp.moon.moonpvp.Main;
import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.kit.KitType;
import me.kp.moon.moonpvp.utils.PlayerUtils;
import me.kp.moon.moonpvp.warps.WarpType;
import me.kp.moon.moonpvp.warps.WarpUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerKill implements Listener {

    public static void autoRespawn(PlayerData playerData) {
        Player player = playerData.getPlayer();
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            ((CraftPlayer) player).getHandle().playerConnection.a(new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN));
            if (playerData.warpType != WarpType.ARENA) {
                WarpUtils.giveWarpItems(playerData);
                WarpUtils.teleportPlayerToWarp(player, playerData.warpType);
            } else {
                PlayerUtils.sendPlayerToSpawn(player);
            }
        }, 1L);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void PlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        Player player = event.getEntity();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        Player killer = player.getKiller();
        if (killer == null) {
            assert playerData != null;
            playerData.setCacheKillStreak(0);
            playerData.setLastCombatPlayer(null);
            playerData.setCombat(false);
            player.sendMessage("§cVocê morreu por causas desconhecidas, portanto suas estatísticas não foram alteradas.");
            autoRespawn(playerData);
            return;
        }
        PlayerData killerData = PlayerDataManager.getPlayerData(killer);
        if (killerData == null) {
            assert playerData != null;
            playerData.setCacheKillStreak(0);
            playerData.setLastCombatPlayer(null);
            playerData.setCombat(false);
            player.sendMessage("§cVocê morreu por causas desconhecidas, portanto suas estatísticas não foram alteradas.");
            autoRespawn(playerData);
            return;
        }
        if (killer == player || playerData == null) {
            player.sendMessage("§cVocê morreu por causas desconhecidas, portanto suas estatísticas não foram alteradas.");
            assert playerData != null;
            playerData.setCacheKillStreak(0);
            playerData.setLastCombatPlayer(null);
            playerData.setCombat(false);
            autoRespawn(playerData);
            return;
        }
        if ((playerData.warpType == WarpType._1v1 && killerData.warpType == WarpType._1v1) ||
                (playerData.warpType == WarpType.SUMO && killerData.warpType == WarpType.SUMO))
            return;
        if (killerData.kitType == KitType.STOMPER) {
            PlayerUtils.killerKillPlayer(killer, killerData, playerData);
            PlayerUtils.deadKillPlayer(player, playerData, killer);
            autoRespawn(playerData);
            return;
        }
        if (playerData.evento) {
            playerData.setEvento(false);
            playerData.setLastCombatPlayer(null);
            playerData.setCombat(false);
            player.sendMessage("§cVocê foi desqualificado do evento.");
            if (killerData.evento) {
                killer.sendMessage("§aVocê matou §7" + player.getName() + "§a.");
                killer.sendMessage("§7§o(Sem recompensas por estar em um evento)");
            }
            autoRespawn(playerData);
            return;
        }
        if ((playerData.warpType == WarpType.KB && killerData.warpType == WarpType.KB) || (playerData.warpType == WarpType.FISHERMAN && killerData.warpType == WarpType.FISHERMAN)) {
            player.sendMessage("§cVocê morreu em uma warp para diversão, portanto não perdeu nada.");
            playerData.setLastCombatPlayer(null);
            playerData.setCombat(false);
            killer.sendMessage("§aVocê matou §7" + player.getName() + "§a.");
            killer.sendMessage("§7§o(Sem recompensas por estar em uma warp de diversão)");
            autoRespawn(playerData);
            return;
        }
        PlayerUtils.killerKillPlayer(killer, killerData, playerData);
        PlayerUtils.deadKillPlayer(player, playerData, killer);
        autoRespawn(playerData);
    }

}
