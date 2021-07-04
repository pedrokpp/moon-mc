package me.kp.moon.moonpvp.gui;

import me.kp.moon.moonpvp.kit.KitType;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SeusKitsGUI implements Listener {

    public static final ItemStack ICON = ItemUtils.getCustomItemStack(Material.STORAGE_MINECART, "§eSeus Kits", "§5Achou um easter egg, r$");

    @EventHandler
    private void onInteractShop(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.hasItem() && event.getItem().isSimilar(SeusKitsGUI.ICON)) {
            openGUI(player);
        }
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null && event.getInventory().getName().contains("§eKits disponíveis")) {
            if (event.getCurrentItem().getType() != Material.AIR) {
                event.setCancelled(true);
                if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§6Kit ")) {
                    KitType kit = KitType.getKitTypeByName(event.getCurrentItem().getItemMeta().getDisplayName().replace("§6Kit ", ""));
                    if (kit != null && event.isLeftClick()) {
                        if (!player.hasPermission("kit." + kit.getKitname().toLowerCase())) {
                            player.sendMessage("§cVocê não possui permissão para utilizar este kit.");
                            return;
                        }
                        player.chat("/kit " + kit.getKitname());
                    }
                }
            }
        }
    }

    public static void openGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, "§eKits disponíveis");
        ItemStack glass = ItemUtils.getCustomItemStack(Material.THIN_GLASS, " ", " ");
        for (int i = 0; i < 54; ++i) {
            if ((i <= 9 || i >= 17) && (i <= 18 || i >= 26) && (i <= 27 || i >= 35)) {
                if (i <= 36 || i >= 44) {
                    inv.setItem(i, glass);
                }
            }
        }
        inv.setItem(4, ItemUtils.editItemStack(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1), "§6Visualizando: §eKits disponíveis para você", Collections.singletonList("§7§oMostrando kits que você tem permissão.")));
        List<ItemStack> habilitados = new ArrayList<>();
        List<ItemStack> habilitadosEmManut = new ArrayList<>();
        List<ItemStack> desabilitados = new ArrayList<>();
        for (KitType kit : KitType.values()) {
            if (player.hasPermission("kit." + kit.getKitname().toLowerCase())) {
                if (kit.isEnabled()) {
                    ItemStack icon = ItemUtils.getCustomItemStack(kit.getIcon(), "§6Kit " + kit.getKitname(), Arrays.asList("§a" + kit.getDescription(), "§7§oClique para selecionar esse kit."));
                    habilitados.add(icon);
                } else {
                    ItemStack icon = ItemUtils.editItemStack(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8), "§7Kit " + kit.getKitname(), Arrays.asList("§8" + kit.getDescription(), "§c§oVocê possui este kit, mas está em manutenção."));
                    habilitadosEmManut.add(icon);
                }
            } else {
                ItemStack icon2 = kit.isEnabled() ? ItemUtils.editItemStack(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14), "§4Kit " + kit.getKitname(), Arrays.asList("§8" + kit.getDescription(), "§7§o§mClique para selecionar esse kit.")) :
                        ItemUtils.editItemStack(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14), "§4Kit " + kit.getKitname(), Arrays.asList("§8" + kit.getDescription(), "§c§o§mEste kit está em manutenção."));
                desabilitados.add(icon2);
            }
        }
        habilitados.forEach(inv::addItem);
        habilitadosEmManut.forEach(inv::addItem);
        desabilitados.forEach(inv::addItem);
        inv.remove(glass);
        player.openInventory(inv);
    }

}
