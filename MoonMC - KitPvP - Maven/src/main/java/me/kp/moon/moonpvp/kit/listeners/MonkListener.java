package me.kp.moon.moonpvp.kit.listeners;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.kit.KitType;
import me.kp.moon.moonpvp.kit.KitUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Random;

public class MonkListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void PlayerInteract(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof Player)) return;
        Player player = event.getPlayer();
        Player target = (Player) event.getRightClicked();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (playerData.kitType != KitType.MONK) return;
        if (playerData.kitCooldown) {
            player.sendMessage(Messages.KIT_COOLDOWN.getMessage());
            return;
        }
        if (player.getItemInHand().getType() == Material.BLAZE_ROD) {
            player.sendMessage("§aVocê embaralhou o inventário de §e" + target.getName() + "§a.");
            PlayerInventory inv = target.getInventory();
            int slot = new Random().nextInt(inv.getSize());
            ItemStack item = inv.getItem(slot);
            inv.setItem(slot, inv.getItemInHand());
            inv.setItemInHand(item);
            target.sendMessage("§cSeu inventário foi embaralhado por §e" + player.getName() + "§c.");
            KitUtils.addCooldown(playerData, 5);
        }
    }

}
