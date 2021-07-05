package me.kp.moon.moonpvp.listeners;

import me.kp.moon.moonpvp.Main;
import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Strings;
import me.kp.moon.moonpvp.utils.ItemUtils;
import me.kp.moon.moonpvp.warps.WarpType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.help.HelpTopic;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class World implements Listener {

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (player.isOp()) return;
        List<String> commands = Arrays.asList("/pl", "/lp", "/plugin", "/version", "/ver", "/help", "/?", "/me", "/msg", "/minecraft:tell", "bukkit:msg", "bukkit:op", "bukkit:help", "plugins", "bukkit:pl", "bukkit:plugins", "version", "/bukkit:w", "/w", "/about", "/bukkit:about", "/minecraft:me", "/bukkit:version", "/plugins", "/bukkit:plugin", "/icanhasbukkit", "/bukkit:?", "/bukkit:help", "/bukkit:?", "/bukkit:ver", "/pex", "/promote", "/demote", "/permissionsex:pex", "/permissionsex:demote", "/permissionsex:promote", "/rl", "/reload", "/bukkit:reload", "/bukkit:rl");
        String command = event.getMessage().split(" ")[0].toLowerCase();
        HelpTopic topic = Bukkit.getServer().getHelpMap().getHelpTopic(command);
        if (commands.contains(command) || topic == null) {
            player.sendMessage("§cComando inexistente.");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void PlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String[] command = event.getMessage().split(" ");
        List<String> blacklist = Arrays.asList("/skins", "/ab", "/exploitfixer", "/skinsrestorer:skin", "/skinsrestorer:skins", "/sr", "/skinsrestorer", "/skinsrestorer:skinsrestorer", "/skinsrestorer:sr");
        if (blacklist.contains(command[0].toLowerCase())) {
            player.sendMessage("§cComando inexistente.");
            event.setCancelled(true);
        }
        if (command[0].equalsIgnoreCase("/skin")) {
            if (command.length == 1) {
                player.sendMessage("" +
                        "§9§lMoon§1§lMC §7- §6Sistema de skins\n" +
                        "§e/skin set <name> §7- §fAltere sua skin.\n" +
                        "§e/skin update §7- §fAtualize sua skin.\n" +
                        "§e/skin clear §7- §fLimpe sua skin.");
                event.setCancelled(true);
                return;
            }
            if (!(command[1].equalsIgnoreCase("clear") || command[1].equalsIgnoreCase("set") ||
                    command[1].equalsIgnoreCase("update") || command[1].equalsIgnoreCase("url"))) {
                event.setCancelled(true);
                player.sendMessage("§cComando inexistente.");
            }
        }
// clear set update url
    }

    @EventHandler
    public void onEntityDeath(final EntityDeathEvent event) {
        event.setDroppedExp(0);
        if (!(event.getEntity() instanceof Player)) {
            event.getDrops().clear();
        }
    }

    @EventHandler
    public void onEntityExplode(final EntityExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBurn(final BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void Inventory(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        ItemStack itemStack = event.getItem().getItemStack();
        if (playerData.admin || playerData.superadmin || playerData.vanish) {
            event.setCancelled(true);
            return;
        }
        if (playerData.inDuel) {
            event.setCancelled(true);
            return;
        }
        if (playerData.kitType != null || playerData.warpType != null) {
            event.setCancelled(itemStack.getType() != Material.MUSHROOM_SOUP && itemStack.getType() != Material.BOWL &&
                    itemStack.getType() != Material.BROWN_MUSHROOM && itemStack.getType() != Material.RED_MUSHROOM &&
                    (itemStack.getType() != Material.INK_SACK && itemStack.getDurability() != 3));
        }
    }

    @EventHandler
    public void onItemSpawn(final ItemSpawnEvent event) {
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> event.getEntity().remove(), 10 * 20L);
    }

    @EventHandler
    public void ItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        ItemStack itemStack = event.getItemDrop().getItemStack();
        if (playerData.evento) event.setCancelled(false);
        else {
            if (playerData.warpType == WarpType.LAVA) {
                if (itemStack.getType() == Material.BOWL) {
                    event.getItemDrop().remove();
                    return;
                }
            }
            if (playerData.warpType == null) {
                event.setCancelled(true);
            } else {
                event.setCancelled(itemStack.getType() != Material.MUSHROOM_SOUP && itemStack.getType() != Material.BOWL &&
                        itemStack.getType() != Material.BROWN_MUSHROOM && itemStack.getType() != Material.RED_MUSHROOM &&
                        itemStack.getType() != Material.IRON_HELMET && itemStack.getType() != Material.IRON_CHESTPLATE &&
                        itemStack.getType() != Material.IRON_LEGGINGS && itemStack.getType() != Material.IRON_BOOTS &&
                        (itemStack.getType() != Material.INK_SACK && itemStack.getDurability() != 3));
            }
        }
    }

    @EventHandler
    private void onServerListPing(ServerListPingEvent event) {
        event.setMotd(Strings.getMotd());
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (event.getDamage() - 2.25 > 0) event.setDamage(event.getDamage() - 2.25);
        if (event.getDamager() instanceof Player) {
            final Player player = (Player) event.getDamager();
            if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                if (event.getDamage() - 6.3 > 0) event.setDamage(event.getDamage() - 6.3);
            }
            if (player.getItemInHand().getType().name().contains("_SWORD") || player.getItemInHand().getType().name().contains("_AXE")) {
                if (player.getItemInHand().containsEnchantment(Enchantment.DAMAGE_ALL)) {
                    if (player.getHealth() < player.getMaxHealth() - 3) if (event.getDamage() - 1.5 > 0) event.setDamage(event.getDamage() - 1.5);
                }
                player.getItemInHand().setDurability((short) 0);
                player.updateInventory();
            }
        }
    }

    @EventHandler
    public void onEntityShootBow(final EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            event.getBow().setDurability((short) (-1));
        }
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        event.getPlayer().getItemInHand().setDurability((short) 0);
    }

    @EventHandler
    public void onSignChange(final SignChangeEvent event) {
        for (int index = 0; index < 4; ++index) {
            event.setLine(index, ChatColor.translateAlternateColorCodes('&', event.getLine(index)));
        }
    }

    @EventHandler
    public void onFoodLevelChange(final FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(final WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.hasItem()) {
            if (event.getMaterial() == Material.MUSHROOM_SOUP && event.getAction().name().contains("RIGHT") && player.getHealth() != player.getMaxHealth()) {
                player.setHealth((player.getHealth() < player.getMaxHealth() - 7.0) ? (player.getHealth() + 7.0) : player.getMaxHealth());
                event.getItem().setType(Material.BOWL);
                player.updateInventory();
            }
        }
    }

}
