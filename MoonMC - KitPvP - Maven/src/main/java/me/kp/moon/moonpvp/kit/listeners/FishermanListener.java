package me.kp.moon.moonpvp.kit.listeners;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.kit.KitType;
import me.kp.moon.moonpvp.warps.WarpType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishermanListener implements Listener {

    @EventHandler
    public void PlayerFish(PlayerFishEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getCaught() instanceof Player)) return;
        Player player = event.getPlayer();
        Player target = (Player) event.getCaught();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        PlayerData targetData = PlayerDataManager.getPlayerData(target);
        if (playerData == null || targetData == null) return;
        if (playerData.kitType != KitType.FISHERMAN || targetData.kitType == null) return;
        if (player == target) {
            player.sendMessage("§cVocê não pode fisgar a si mesmo.");
            event.setCancelled(true);
            return;
        }
        if (playerData.warpType == WarpType.FISHERMAN && player.getLocation().getY() >= 117) {
            event.setCancelled(true);
            return;
        }
        target.teleport(player.getLocation());
        player.sendMessage("§aVocê fisgou §e" + target.getName() + "§a.");
        target.sendMessage("§cVocê foi fisgado por §e" + player.getName() + "§c.");
    }

}
