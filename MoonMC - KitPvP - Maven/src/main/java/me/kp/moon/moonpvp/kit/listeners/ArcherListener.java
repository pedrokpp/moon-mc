package me.kp.moon.moonpvp.kit.listeners;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.kit.KitType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ArcherListener implements Listener {

    private final List<PotionEffectType> effects = Arrays.asList(PotionEffectType.BLINDNESS, PotionEffectType.SLOW, PotionEffectType.POISON, PotionEffectType.WITHER, PotionEffectType.JUMP);

    @EventHandler
    public void Damage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow) || !(event.getEntity() instanceof Player)) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            Player player = (Player) event.getEntity();
            Arrow arrow = (Arrow) event.getDamager();
            String damagerName = arrow.getCustomName();
            Player damager = Bukkit.getPlayer(damagerName);
            if (damager == null) return;
            PlayerData damagerData = PlayerDataManager.getPlayerData(damager);
            if (damagerData == null) return;
            if (damagerData.kitType != KitType.ARCHER) return;

            int chance = new Random().nextInt(10);
            if (chance < 4) {
                PotionEffectType effectType = effects.get(new Random().nextInt(effects.size()));
                int amp = effectType == PotionEffectType.JUMP ? 2 : 0;
                player.addPotionEffect(new PotionEffect(effectType, 4 * 20 /* 2,5 seg */, amp));
                damager.sendMessage("§aVocê acertou uma flecha e causou §e" + effectType.getName().toUpperCase() + "§a.");
                player.sendMessage("§cVocê recebeu §e" + effectType.getName().toUpperCase() + "§c por uma flecha especial de um archer.");
            }

            if (event.getDamage() > player.getHealth()) {
                if (damager.getInventory().contains(Material.ARROW))
                    damager.getInventory().addItem(new ItemStack(Material.ARROW, 1));
                else {
                    for (ItemStack item : damager.getInventory()) {
                        if (item == null) continue;
                        if (item.getType() == Material.MUSHROOM_SOUP) {
                            item.setType(Material.ARROW);
                            damager.sendMessage("§aVocê recebeu uma flecha por matar alguém.");
                            return;
                        }
                    }
                }
                damager.sendMessage("§aVocê recebeu uma flecha por matar alguém.");
            }
        }
    }

    @EventHandler
    public void Arrow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (playerData.kitType != KitType.ARCHER) return;
        event.getProjectile().setCustomNameVisible(false);
        event.getProjectile().setCustomName(player.getName());
    }

//    @EventHandler
//    public void PlayerDeath(PlayerDeathEvent event) {
//        Player player = event.getEntity();
//        Player killer = player.getKiller();
//        if (killer == null) return;
//        PlayerData killerData = PlayerDataManager.getPlayerData(killer);
//        if (killerData == null) return;
//        if (killerData.kitType != KitType.ARCHER) return;
//        if (killer.getInventory().contains(Material.ARROW))
//            killer.getInventory().addItem(new ItemStack(Material.ARROW, 1));
//        else {
//            for (ItemStack item : killer.getInventory()) {
//                if (item == null) continue;
//                if (item.getType() == Material.MUSHROOM_SOUP) {
//                    item.setType(Material.ARROW);
//                    killer.sendMessage("§aVocê recebeu uma flecha por matar alguém.");
//                    return;
//                }
//            }
//        }
//        killer.sendMessage("§aVocê recebeu uma flecha por matar alguém.");
//    }

}
