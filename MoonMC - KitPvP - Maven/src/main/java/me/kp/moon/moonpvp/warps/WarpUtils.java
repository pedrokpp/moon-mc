package me.kp.moon.moonpvp.warps;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.warps.listeners.SumoListener;
import me.kp.moon.moonpvp.warps.listeners._1v1Listener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class WarpUtils {

    public static int countArena = 0;
    public static int countFisherman = 0;
    public static int countFPS = 0;
    public static int count1v1 = 0;
    public static int countSumo = 0;
    public static int countLava = 0;
    public static int countKb = 0;

    public static void clearWarp(Player player) {
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        playerData.setWarpType(null);
        playerData.setInDuel(false);
        playerData.setLastDuelPlayer(null);
        _1v1Listener.queue.remove(player.getUniqueId());
        SumoListener.queue.remove(player.getUniqueId());
//        playerData.setLastKitTarget(null);
    }

    public static void giveWarpItems(PlayerData playerData) {
        PlayerInventory inv = playerData.getPlayer().getInventory();
        inv.clear();
        playerData.getPlayer().closeInventory();
        if (playerData.warpType == WarpType._1v1) {
            inv.setItem(0, _1v1Listener.INVITE_ITEM);
            inv.setItem(8, _1v1Listener.OFF_QUEUE_ITEM);
        }
        if (playerData.warpType == WarpType.FISHERMAN) inv.setItem(0, new ItemStack(Material.FISHING_ROD));
        if (playerData.warpType == WarpType.SUMO) {
            inv.setItem(0, SumoListener.INVITE_ITEM);
            inv.setItem(8, SumoListener.OFF_QUEUE_ITEM);
        }
        if (playerData.warpType == WarpType.LAVA) {
            inv.setItem(13, new ItemStack(Material.BOWL, 64));
            inv.setItem(14, new ItemStack(Material.RED_MUSHROOM, 64));
            inv.setItem(15, new ItemStack(Material.BROWN_MUSHROOM, 64));
            for (int i = 0; i < 36; ++i) {
                inv.addItem(new ItemStack(Material.MUSHROOM_SOUP));
            }
        }
        if (playerData.warpType == WarpType.KB) {
            ItemStack kb = new ItemStack(Material.STICK);
            ItemMeta kbm = kb.getItemMeta();
            kbm.addEnchant(Enchantment.KNOCKBACK, 2, true);
            kb.setItemMeta(kbm);
            inv.setItem(0, kb);
        }
        if (playerData.warpType == WarpType.FPS) {
            inv.setHelmet(new ItemStack(Material.IRON_HELMET));
            inv.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
            inv.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
            inv.setBoots(new ItemStack(Material.IRON_BOOTS));
            ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
            sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
            inv.setItem(0, sword);
            inv.setItem(13, new ItemStack(Material.BOWL, 64));
            inv.setItem(14, new ItemStack(Material.RED_MUSHROOM, 64));
            inv.setItem(15, new ItemStack(Material.BROWN_MUSHROOM, 64));
            for (int i = 0; i < 36; ++i) {
                inv.addItem(new ItemStack(Material.MUSHROOM_SOUP));
            }
        }
    }

    public static void refreshFPSArmor(Player player) {
        PlayerInventory inv = player.getInventory();
        inv.setHelmet(new ItemStack(Material.IRON_HELMET));
        inv.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        inv.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        inv.setBoots(new ItemStack(Material.IRON_BOOTS));
        player.sendMessage("§aSua armadura foi restaurada.");
    }

    public static void teleportPlayerToWarp(Player player, WarpType warp) {
        player.teleport(warp.getLocation());
        Bukkit.getOnlinePlayers().forEach(online -> {
            PlayerData onlineData = PlayerDataManager.getPlayerData(online);
            if (onlineData != null) {
                if (!player.hasPermission("command.staffchat")) {
                    if (!onlineData.admin || !onlineData.superadmin || !onlineData.vanish)
                        player.showPlayer(online);
                } else {
                    player.showPlayer(online);
                }
            }
        });
    }

    public static void updateCounts() {
        countArena = 0;
        countFisherman = 0;
        countFPS = 0;
        count1v1 = 0;
        countSumo = 0;
        countLava = 0;
        countKb = 0;
        Bukkit.getOnlinePlayers().forEach(player -> {
            PlayerData playerData = PlayerDataManager.getPlayerData(player);
            if (playerData == null) return;
            if (playerData.warpType == WarpType.ARENA) countArena += 1;
            if (playerData.warpType == WarpType.FISHERMAN) countFisherman += 1;
            if (playerData.warpType == WarpType.FPS) countFPS += 1;
            if (playerData.warpType == WarpType._1v1) count1v1 += 1;
            if (playerData.warpType == WarpType.SUMO) countSumo += 1;
            if (playerData.warpType == WarpType.LAVA) countLava += 1;
            if (playerData.warpType == WarpType.KB) countKb += 1;
        });
    }

    public static int getCount(WarpType warp) {
        if (warp == WarpType.ARENA) return countArena;
        if (warp == WarpType.FISHERMAN) return countFisherman;
        if (warp == WarpType.FPS) return countFPS;
        if (warp == WarpType._1v1) return count1v1;
        if (warp == WarpType.SUMO) return countSumo;
        if (warp == WarpType.LAVA) return countLava;
        if (warp == WarpType.KB) return countKb;
        return -1;
    }

}
