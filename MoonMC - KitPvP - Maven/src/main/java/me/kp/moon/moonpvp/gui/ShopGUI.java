package me.kp.moon.moonpvp.gui;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class ShopGUI implements Listener {

    public static final ItemStack ICON = ItemUtils.getCustomItemStack(Material.EMERALD, "§9Loja de moedas", "§aAcesse a loja com moedas do servidor!");

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.hasItem() && event.getItem().isSimilar(ShopGUI.ICON)) {
            openGUI(player);
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && event.getInventory().getName().equals("§eMenu de Compras") && event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()) {
            final String display = event.getCurrentItem().getItemMeta().getDisplayName();
            final Player player = (Player)event.getWhoClicked();
            event.setCancelled(true);
            switch (display) {
                case "§bKits":
                    ShopKitsGUI.openGUI(player);
                    break;
                case "§bReset KDR":
                    ShopKDRGUI.openGUI(player);
                    break;
                case "§bCores do clan":
                    player.chat("/clan cores");
                    break;
            }
        }
    }

    public static void openGUI(final Player player) {
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        final int coins = playerData.cacheCoins;
        final String money = new DecimalFormat("###,###.##").format(coins);
        final Inventory inv = Bukkit.createInventory(null, 36, "§eMenu de Compras");
        inv.setItem(4, ItemUtils.getCustomItemStack(Material.GOLD_INGOT, "§6Carteira", "§fVocê possui §a" + money + " §fmoedas."));
        inv.setItem(20, ItemUtils.getCustomItemStack(Material.STONE_SWORD, "§bKits", "§fCompre novos §bkits §fcom moedas."));
        inv.setItem(22, ItemUtils.getCustomItemStack(Material.REDSTONE, "§bReset KDR", "§fResete suas estatísticas."));
        inv.setItem(24, ItemUtils.getCustomItemStack(Material.WOOL, "§bCores do clan", "§fAltere a cor da tag do seu clan."));
        player.openInventory(inv);
    }

}
