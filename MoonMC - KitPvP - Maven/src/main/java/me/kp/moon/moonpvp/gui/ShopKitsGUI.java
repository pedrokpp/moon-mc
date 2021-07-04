package me.kp.moon.moonpvp.gui;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.kit.KitType;
import me.kp.moon.moonpvp.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;

public class ShopKitsGUI implements Listener {

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && event.getInventory().getName().equals("§eMenu de Compras §7(Kits)") && event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()) {
            String display = event.getCurrentItem().getItemMeta().getDisplayName().replace("§2", "").replace("§e", "");
            Player player = (Player) event.getWhoClicked();
            PlayerData playerData = PlayerDataManager.getPlayerData(player);
            if (playerData == null) return;
            event.setCancelled(true);

            if (display.equals("§7Voltar para a página anterior")) {
                ShopGUI.openGUI(player);
                return;
            }

            KitType kit = KitType.getKitTypeByName(display);
            if (kit == null) {
                player.sendMessage("§cOcorreu um erro. Reporte esse bug para a staff.");
                player.closeInventory();
                return;
            }
            if (playerData.cacheCoins < kit.getPrice()) {
                player.sendMessage("§cVocê não tem dinheiro suficiente para comprar esse kit.");
                player.closeInventory();
                return;
            }
            if (player.hasPermission("kit." + kit.getKitname().toLowerCase())) return;
            player.closeInventory();
            player.sendMessage("§aVocê comprou o kit §e" + kit.getKitname() + "§a por §7$" + kit.getPrice() + "§a.");
            playerData.setCacheCoins(playerData.cacheCoins - kit.getPrice());
            player.chat("/bal");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set kit." + kit.getKitname().toLowerCase());
        }
    }

    public static void openGUI(Player player) {
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        int coins = playerData.cacheCoins;
        String money = new DecimalFormat("###,###.##").format(coins);
        Inventory inv = Bukkit.createInventory(null, 54, "§eMenu de Compras §7(Kits)");
        ItemStack glass = ItemUtils.getCustomItemStack(Material.THIN_GLASS, " ", " ");
        for (int i = 0; i < 54; ++i) {
            if ((i <= 9 || i >= 17) && (i <= 18 || i >= 26) && (i <= 27 || i >= 35)) {
                if (i <= 36 || i >= 44) {
                    inv.setItem(i, glass);
                }
            }
        }
        inv.setItem(4, ItemUtils.editItemStack(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1), "§6Visualizando: §eKits disponíveis para venda", Collections.singletonList("§7§oMostrando kits que você não tem permissão.")));
        for (KitType kit : KitType.values()) {
            String price = new DecimalFormat("###,###.##").format(kit.getPrice());
            if (!player.hasPermission("kit." + kit.getKitname().toLowerCase())) {
                ItemStack icon = kit.isEnabled() ? ItemUtils.getCustomItemStack(kit.getIcon(), "§e" + kit.getKitname(), Arrays.asList("§e" + kit.getDescription(), "§fValor: §a" + price, "§7§oClique para comprar esse kit.")) :
                        ItemUtils.getCustomItemStack(kit.getIcon(), "§e" + kit.getKitname(), Arrays.asList("§e" + kit.getDescription(), "§c§oEste kit está em manutenção."));
                inv.addItem(icon);
            } else {
                ItemStack icon = kit.isEnabled() ? ItemUtils.editItemStack(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 13), "§2" + kit.getKitname(), Arrays.asList("§a§oVocê já possui esse kit.", "§fValor: §a" + price)) :
                        ItemUtils.editItemStack(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 13), "§2" + kit.getKitname(), Arrays.asList("§a§oVocê já possui esse kit.", "§c§oEste kit está em manutenção." ,"§fValor: §a" + price));
                inv.addItem(icon);
            }
        }
        inv.remove(glass);
        inv.setItem(45, ItemUtils.getCustomItemStack(Material.ARROW, "§7Voltar para a página anterior", ""));
        player.openInventory(inv);
    }

}
