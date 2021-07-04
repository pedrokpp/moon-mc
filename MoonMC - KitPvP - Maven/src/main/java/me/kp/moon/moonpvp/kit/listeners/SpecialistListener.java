package me.kp.moon.moonpvp.kit.listeners;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.kit.KitType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SpecialistListener implements Listener {

    private final List<Enchantment> enchants = Arrays.asList(Enchantment.DAMAGE_ALL, Enchantment.KNOCKBACK, Enchantment.FIRE_ASPECT);

    @EventHandler(ignoreCancelled = true)
    public void PlayerEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();
        PlayerData damagerData = PlayerDataManager.getPlayerData(damager);
        if (damagerData == null) return;
        if (damagerData.kitType != KitType.SPECIALIST) return;
        if (event.getDamage() >= player.getHealth()) {
            damager.getInventory().forEach(item -> {
                if (item != null) {
                    if (item.getType() == Material.STONE_SWORD) {
                        Enchantment enchant = enchants.get(new Random().nextInt(enchants.size()));
                        item.getEnchantments().forEach((ec, amp) -> item.removeEnchantment(ec));
                        item.addEnchantment(enchant, 1);
                        String name = enchant.getName().toUpperCase().replace("DAMAGE_ALL", "SHARPNESS").replace("_", " ");
                        damager.sendMessage("§aSua espada foi encantada com §e" + name + "§a.");
                    }
                }
            });
        }
    }

}
