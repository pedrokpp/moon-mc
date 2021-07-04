package me.kp.moon.moonpvp.gui;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.PlayerGroup;
import me.kp.moon.moonpvp.enums.PlayerRank;
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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;

public class StatusGUI implements Listener {

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getType().equals(Material.SKULL_ITEM)) {
            openGUI(player, player);
        }
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && event.getInventory().getName().equals("§aPerfil") && event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()) {
            event.setCancelled(true);
        }
    }

    public static void openGUI(Player player, Player target) {
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        PlayerRank playerRank = PlayerRank.getRank(playerData);
        Inventory inv = Bukkit.createInventory(null, 54, "§aPerfil");
        ItemStack glass = ItemUtils.getCustomItemStack(Material.THIN_GLASS, " ", " ");
        for (int i = 0; i < 54; ++i) {
            if ((i <= 9 || i >= 17) && (i <= 27 || i >= 35)) {
                if (i <= 36 || i >= 44) {
                    inv.setItem(i, glass);
                }
            }
        }
        double kdr = playerData.cacheDeaths == 0 ? (double) playerData.cacheKills : (double) playerData.cacheKills / (double) playerData.cacheDeaths;
        inv.setItem(11, glass);
        inv.setItem(12, glass);
        inv.setItem(14, glass);
        inv.setItem(15, glass);
        inv.setItem(4, ItemUtils.editItemStack(ItemUtils.getPlayerSkull(player.getName()), "§6Informações", Arrays.asList("§fNick: §a" + player.getName(), "§fUUID: §a" + player.getUniqueId(), "§fMoedas: §a" + new DecimalFormat("###,###.##").format(playerData.cacheCoins), "§fPrimeiro acesso: §a" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(player.getFirstPlayed()))));
        inv.setItem(10, ItemUtils.getCustomItemStack(Material.DIAMOND_SWORD, "§6Estatísticas", Arrays.asList("§fKills: §a" + playerData.cacheKills, "§fDeaths: §a" + playerData.cacheDeaths, "§fKDR: §a" + String.format("%.2f",kdr),"§fKillstreak: §a" + playerData.cacheKillStreak)));
        inv.setItem(13, ItemUtils.getCustomItemStack(Material.EYE_OF_ENDER, "§6Cargos", Arrays.asList("§fVIP: §a" + (player.hasPermission("tag.vip") ? "Sim" : "Não"), "§fCargo: §a" + PlayerGroup.getGroup(player).getColoredName())));
        inv.setItem(16, ItemUtils.getCustomItemStack(Material.EXP_BOTTLE, "§6Experiência", Arrays.asList("§fXP: §a" + playerData.cacheXP, "§fRank: §7(" + playerRank.getColoredSymbol() + "§7) " + playerRank.getColoredName(), "§fBoost: §a" + (player.hasPermission("coinsbooster.x2") ? "x2.0" : "x1.0"))));
        PlayerRank[] values = PlayerRank.values();
        for (int i = values.length - 1; i >= 0; i--) {
            PlayerRank rank = values[i];
            if (playerData.cacheXP >= rank.getXp())
                inv.addItem(ItemUtils.editItemStack(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5), "§7(" + rank.getColoredSymbol() + "§7) " + rank.getColoredName(), Collections.singletonList("§a" + player.getName() + " já atingiu esse rank.")));
            else
                inv.addItem(ItemUtils.editItemStack(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14), "§7(" + rank.getColoredSymbol() + "§7) " + rank.getColoredName(), Collections.singletonList("§c" + player.getName() + " ainda não atingiu esse rank.")));
        }
        inv.remove(glass);
        target.openInventory(inv);
    }

}
