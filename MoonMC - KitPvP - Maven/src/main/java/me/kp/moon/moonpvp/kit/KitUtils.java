package me.kp.moon.moonpvp.kit;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class KitUtils {

    public static void clearKits(Player player) {
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        playerData.setKitType(null);
        playerData.setGladiatorLocation(null);
        playerData.setWarpType(null);
        playerData.setLastKitTarget(null);
        playerData.setKitCooldown(false);
    }

    public static void giveKitItems(PlayerData playerData) {
        PlayerInventory inv = playerData.getPlayer().getInventory();
        inv.clear();
        playerData.getPlayer().getInventory().setItem(0, new ItemStack(Material.STONE_SWORD));
        if (playerData.kitType == KitType.PVP) {
            playerData.getPlayer().getInventory().getItem(0).addEnchantment(Enchantment.DAMAGE_ALL, 1);
        }
        if (playerData.kitType == KitType.ARCHER) {
            playerData.getPlayer().getInventory().setItem(2, new ItemStack(Material.ARROW, 16));
            playerData.getPlayer().getInventory().setItem(1, new ItemStack(Material.BOW, 1));
        }
        if (playerData.kitType.getItem() != null) {
            playerData.getPlayer().getInventory().setItem(1, new ItemStack(playerData.kitType.getItem()));
        }
        if (playerData.kitType == KitType.GRANDPA) {
            ItemStack kb = new ItemStack(Material.STICK);
            ItemMeta kbm = kb.getItemMeta();
            kbm.addEnchant(Enchantment.KNOCKBACK, 2, true);
            kb.setItemMeta(kbm);
            inv.setItem(1, kb);
        }
        inv.setItem(8, new ItemStack(Material.COMPASS));
        inv.setItem(13, new ItemStack(Material.BOWL, 32));
        if (playerData.soupType.equalsIgnoreCase("mush")) {
            inv.setItem(14, new ItemStack(Material.RED_MUSHROOM, 32));
            inv.setItem(15, new ItemStack(Material.BROWN_MUSHROOM, 32));
        } else {
            inv.setItem(14, new ItemStack(Material.INK_SACK, 32, (short) 3));
        }
        for (int i = 0; i < 36; ++i) {
            inv.addItem(new ItemStack(Material.MUSHROOM_SOUP));
        }
        playerData.getPlayer().closeInventory();
    }

    public static void addCooldown(PlayerData playerData, int seconds) {
        playerData.setKitCooldown(true);
        playerData.setKitCooldownMS(System.currentTimeMillis() + ((seconds + 1) * 1000L));
    }

    public static void removeCooldown(PlayerData playerData) {
        playerData.setKitCooldown(false);
        playerData.setKitCooldownMS(null);
    }

}
