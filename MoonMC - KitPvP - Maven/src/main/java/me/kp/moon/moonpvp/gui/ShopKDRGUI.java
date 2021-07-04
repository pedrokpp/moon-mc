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
import org.bukkit.inventory.Inventory;

import java.text.DecimalFormat;
import java.util.Arrays;

public class ShopKDRGUI implements Listener {

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && event.getInventory().getName().equals("§eMenu de Compras §7(KDR)") && event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()) {
            String display = event.getCurrentItem().getItemMeta().getDisplayName();
            Player player = (Player) event.getWhoClicked();
            PlayerData playerData = PlayerDataManager.getPlayerData(player);
            if (playerData == null) return;
            event.setCancelled(true);
            if (display.equals("§cReset KDR")) {
                if (playerData.cacheCoins >= 5000) {
                    playerData.setCacheCoins(playerData.cacheCoins - 5000);
                    playerData.setCacheKills(0);
                    playerData.setCacheDeaths(0);
                    playerData.setCacheKillStreak(0);
//                    PlayerAccount.set1v1Vitorias(player, 0);
//                    PlayerAccount.set1v1Derrotas(player, 0);
//                    PlayerAccount.set1v1WinStreak(player, 0);
//                    PlayerAccount.setSumoVitorias(player, 0);
//                    PlayerAccount.setSumoDerrotas(player, 0);
//                    PlayerAccount.setSumoWinStreak(player, 0);
                    player.sendMessage("§aVocê resetou seu KDR.");
                } else {
                    player.sendMessage("§cVocê não possui moedas suficientes.");
                }
                player.closeInventory();
            } else if (display.equals("§7Voltar para a página anterior")) {
                ShopGUI.openGUI(player);
            }
        }
    }

    public static void openGUI(Player player) {
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        int coins = playerData.cacheCoins;
        String money = new DecimalFormat("###,###.##").format(coins);
        Inventory inv = Bukkit.createInventory(null, 36, "§eMenu de Compras §7(KDR)");
        inv.setItem(4, ItemUtils.getCustomItemStack(Material.GOLD_INGOT, "§6Carteira", "§fVocê possui §a" + money + " §fmoedas!"));
        inv.setItem(22, ItemUtils.getCustomItemStack(Material.REDSTONE, "§cReset KDR", Arrays.asList("§aReinicie suas estatísticas", "§fValor: §a$5.000")));
        inv.setItem(27, ItemUtils.getCustomItemStack(Material.ARROW, "§7Voltar para a página anterior", ""));
        player.openInventory(inv);
    }

}
