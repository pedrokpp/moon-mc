package me.kp.moon.worldedit.listeners;

import me.kp.moon.worldedit.data.PlayerData;
import me.kp.moon.worldedit.data.PlayerDataManager;
import me.kp.moon.worldedit.utils.WorldEditUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class WandListener implements Listener {

    @EventHandler
    public void PlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (WorldEditUtils.wandNames.contains(player.getItemInHand().getItemMeta().getDisplayName().replace("§9", "")) &&
                player.hasPermission("worldedit")) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Location location = event.getClickedBlock().getLocation();
                playerData.setPos2(location);
                player.sendMessage("§9§lMoonWE: §fPosição §e2 §fsetada em §e" +
                        location.getBlockX() + "§f, §e" + location.getBlockY() + "§f, §e" + location.getBlockZ() + "§f.");
                event.setCancelled(true);
            } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                Location location = event.getClickedBlock().getLocation();
                playerData.setPos1(location);
                player.sendMessage("§9§lMoonWE: §fPosição §e1 §fsetada em §e" +
                        location.getBlockX() + "§f, §e" + location.getBlockY() + "§f, §e" + location.getBlockZ() + "§f.");
                event.setCancelled(true);
            }
        }
    }

}
