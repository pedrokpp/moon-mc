package me.kp.moon.moonpvp.gui;

import me.kp.moon.moonpvp.cache.PlayerCache;
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

import java.util.Arrays;

public class SoupTypeGUI implements Listener {

    public static final ItemStack ICON = ItemUtils.getCustomItemStack(Material.MUSHROOM_SOUP, "§6Mude seu estilo de sopa", (String) null);

    @EventHandler
    private void onInteractSoupType(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.hasItem() && event.getItem().isSimilar(SoupTypeGUI.ICON)) {
            SoupTypeGUI.openGUI(player);
        }
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null && event.getInventory().getName().contains("§eMude seu estilo de sopa")) {
            if (event.getCurrentItem().getType() != Material.AIR) {
                event.setCancelled(true);
                player.closeInventory();
                if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Cogumelos")) {
                    if (playerData.soupType.equalsIgnoreCase("mush")) {
                        player.sendMessage("§cO seu estilo de sopa já é o selecionado.");
                    } else {
                        playerData.setSoupType("mush");
                        if (!PlayerCache.cacheSoupType.containsKey(player.getName()))
                            PlayerCache.cacheSoupType.put(player.getName(), "mush");
                        else
                            PlayerCache.cacheSoupType.replace(player.getName(), "mush");
                        player.sendMessage("§aVocê alterou seu estilo de sopa para: §eCOGUMELOS§a.");
                    }
                } else {
                    if (playerData.soupType.equalsIgnoreCase("cocoa")) {
                        player.sendMessage("§cO seu estilo de sopa já é o selecionado.");
                    } else {
                        playerData.setSoupType("cocoa");
                        if (!PlayerCache.cacheSoupType.containsKey(player.getName()))
                            PlayerCache.cacheSoupType.put(player.getName(), "cocoa");
                        else
                            PlayerCache.cacheSoupType.replace(player.getName(), "cocoa");
                        player.sendMessage("§aVocê alterou seu estilo de sopa para: §eCOCOA BEANS§a.");
                    }
                }
            }
        }
    }

    public static void openGUI(Player player) {
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        final Inventory inv = Bukkit.createInventory(null, 9, "§eMude seu estilo de sopa");
        if (playerData.soupType.equalsIgnoreCase("mush")) {
            inv.setItem(3, ItemUtils.getCustomItemStack(Material.RED_MUSHROOM, "§aCogumelos", Arrays.asList("§7Você receberá cogumelos.", " ", "§a§oSelecionado")));
            inv.setItem(5, ItemUtils.editItemStack(ItemUtils.cocoa, "§cCocoa Beans", Arrays.asList("§7Você receberá cocoa beans.", " ", "§c§oNão selecionado")));
        } else {
            inv.setItem(3, ItemUtils.getCustomItemStack(Material.RED_MUSHROOM, "§cCogumelos", Arrays.asList("§7Você receberá cogumelos.", " ", "§c§oNão selecionado")));
            inv.setItem(5, ItemUtils.editItemStack(ItemUtils.cocoa, "§aCocoa Beans", Arrays.asList("§7Você receberá cocoa beans.", " ", "§a§oSelecionado")));
        }
        player.openInventory(inv);
    }

}
