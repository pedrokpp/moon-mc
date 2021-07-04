package me.kp.moon.moonpvp.gui;

import me.kp.moon.moonpvp.utils.ItemUtils;
import me.kp.moon.moonpvp.warps.WarpType;
import me.kp.moon.moonpvp.warps.WarpUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class WarpsGUI implements Listener {

    public static final ItemStack ICON = ItemUtils.getCustomItemStack(Material.COMPASS, "§bWarps", (String) null);

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.hasItem() && event.getItem().isSimilar(WarpsGUI.ICON)) {
            openGUI(player);
        }
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        if (event.isCancelled()) return;
        if (event.getWhoClicked() instanceof Player && event.getInventory().getName().equals("§eWarps") && event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()) {
            if (event.getClick() != ClickType.RIGHT && event.getClick() != ClickType.LEFT) {
                event.setCancelled(true);
                return;
            }
            String display = event.getCurrentItem().getItemMeta().getDisplayName();
            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true);
            if (display.startsWith("§bWarp ")) {
                WarpType warp = WarpType.getWarpTypeByName(display.replace("§bWarp ", ""));
                if (warp == null) {
                    player.sendMessage("§cOcorreu um erro. Reporte esse bug para a staff.");
                    player.closeInventory();
                    return;
                }
                player.closeInventory();
                player.chat("/warp " + warp.getWarpName());
            }
        }
    }

    public static void openGUI(Player player) {
        WarpUtils.updateCounts();
        Inventory inv = Bukkit.createInventory(null, 27, "§eWarps");
        ItemStack glass = new ItemStack(Material.THIN_GLASS);
        for (int i = 0; i < 27; ++i) {
            if (i <= 9 || i >= 17) {
                inv.setItem(i, glass);
            }
        }
        inv.setItem(4, ItemUtils.editItemStack(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3), "§fVisualizando: §bWarps", null));
        for (WarpType warp : WarpType.values()) {
            ItemStack icon = warp.isEnabled() ? ItemUtils.getCustomItemStack(warp.getIcon(), "§bWarp " + warp.getWarpName(), Arrays.asList("§a" + warp.getDescription(), "§7§oClique para selecionar ir para essa warp.", "§8§o" + WarpUtils.getCount(warp) + " players")) :
                    ItemUtils.getCustomItemStack(warp.getIcon(), "§7Warp " + warp.getWarpName(), Arrays.asList("§8" + warp.getDescription(), "§c§oEsta warp está em manutenção."));
            inv.addItem(icon);
        }
        inv.remove(glass);
        player.openInventory(inv);
    }

}
