package me.kp.moon.moonpvp.listeners;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class ModoAdmin implements Listener {

    @EventHandler
    public void PlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (!(event.getRightClicked() instanceof Player)) return;
        Player target = (Player) event.getRightClicked();
        if (target == null) return;
        if (playerData.admin || playerData.superadmin || playerData.vanish) {
            if (player.getItemInHand().getItemMeta() != null && player.getItemInHand().getItemMeta().getDisplayName().contains("Info"))
                player.chat("/info " + target.getName());
            if (player.getItemInHand().getType() == Material.AIR)
                player.chat("/invsee " + target.getName());
        }
    }
}

//    @EventHandler
//    public void PlayerInteract(PlayerInteractEvent event) {
//        Player player = event.getPlayer();
//        PlayerData playerData = PlayerDataManager.getPlayerData(player);
//        if (playerData == null) return;
//        if (!playerData.admin || !playerData.superadmin) return;
//        if (event.getAction().name().contains("RIGHT")) {
//            if (player.getItemInHand() != null && player.getItemInHand().getItemMeta().getDisplayName().contains("Quick")) {
//                if (playerData.admin) {
//                    player.chat("/admin");
//                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
//                        player.chat("/admin");
//                    }, 20L);
//                }
//                if (playerData.superadmin) {
//                    player.chat("/superadmin");
//                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
//                        player.chat("/superadmin");
//                    }, 20L);
//                }
//            }
//        }
//    }
