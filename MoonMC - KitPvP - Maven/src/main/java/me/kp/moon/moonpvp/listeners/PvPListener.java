package me.kp.moon.moonpvp.listeners;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
public class PvPListener implements Listener {

    @EventHandler
    private void onPlayerMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (event.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SPONGE) {
            final Location loc = event.getTo().getBlock().getLocation();
            player.setVelocity(player.getLocation().getDirection().multiply(0).setY(3));
            player.playSound(loc, Sound.ORB_PICKUP, 6.0f, 1.0f);
            player.playEffect(loc, Effect.ENDER_SIGNAL, null);
            player.playEffect(loc, Effect.CLICK1, null);
            player.playEffect(loc, Effect.BLAZE_SHOOT, null);
            if (playerData.kitType != null && !playerData.fallDamageSponge) {
                playerData.setFallDamageSponge(true);
            }
        }
    }

    @EventHandler
    private void PlayerMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (playerData.fallDamageSponge) {
            if (player.getLocation().getY() >= 85) {
                if (event.getTo().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR &&
                        event.getTo().getBlock().getRelative(BlockFace.DOWN).getType() != Material.SPONGE) {
                    playerData.setFallDamageSponge(false);
                }
            }
        }
    }

    @EventHandler
    private void onEntityDamage(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            final Player player = (Player)event.getEntity();
            PlayerData playerData = PlayerDataManager.getPlayerData(player);
            if (playerData == null) return;
            if (playerData.fallDamageSponge) {
                event.setCancelled(true);
                playerData.setFallDamageSponge(false);
            }
        }
    }

    @EventHandler
    private void onSignChange(final SignChangeEvent event) {
        final Player player = event.getPlayer();
        if (player.hasPermission("evento.placa")) {
            if (event.getLine(0).equalsIgnoreCase("Sopa")) {
                event.setLine(0, "§8=-=-=§f=-=-=");
                event.setLine(1, "§9Sopas!");
                event.setLine(2, "§4§l[CLIQUE]");
                event.setLine(3, "§8=-=-=§f=-=-=");
                player.sendMessage("§e§l[PLACA] §ePlaca criada com sucesso!");
            }
            else if (event.getLine(0).equalsIgnoreCase("Recraft")) {
                event.setLine(0, "§8=-=-=§f=-=-=");
                event.setLine(1, "§9Recraft!");
                event.setLine(2, "§4§l[CLIQUE]");
                event.setLine(3, "§8=-=-=§f=-=-=");
                player.sendMessage("§e§l[PLACA] §ePlaca criada com sucesso!");
            }
            else if (event.getLine(0).equalsIgnoreCase("PotPvP")) {
                event.setLine(0, "§8=-=-=§f=-=-=");
                event.setLine(1, "§aPoção!");
                event.setLine(2, "§e§l[CLIQUE]");
                event.setLine(3, "§8=-=-=§f=-=-=");
                player.sendMessage("§e§l[PLACA] §ePlaca criada com sucesso!");
            }
        }
    }

    @EventHandler
    private void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (event.hasItem()) {
            if (event.getMaterial() == Material.MUSHROOM_SOUP && event.getAction().name().contains("RIGHT") && player.getHealth() != player.getMaxHealth()) {
                player.setHealth((player.getHealth() < player.getMaxHealth() - 7.0) ? (player.getHealth() + 7.0) : player.getMaxHealth());
                event.getItem().setType(Material.BOWL);
                player.updateInventory();
            }
            else if (event.getMaterial() == Material.COMPASS && (playerData.kitType != null)) {
                for (int i = 0; i < 100; ++i) {
                    for (final Entity entities : player.getNearbyEntities(i, i, i)) {
                        if (entities instanceof Player) {
                            final Player players = (Player)entities;
                            PlayerData playersData = PlayerDataManager.getPlayerData(players);
                            if (playersData == null) return;
                            if (!(playersData.admin || playersData.superadmin || playersData.vanish) && player.getLocation().distance(players.getLocation()) > 0.0) {
                                player.setCompassTarget(players.getLocation());
                                player.sendMessage("§aBússola apontando para §e" + players.getName() + "§a.");
                                return;
                            }
                        }
                    }
                }
                player.setCompassTarget(new Location(player.getWorld(), 401.5, 74.0, 266.5));
                player.sendMessage("§cNenhum jogador encontrado, bússola apontando para o feast.");
            }
        }
        if (event.hasBlock() && event.getClickedBlock().getType().name().contains("SIGN") && event.getAction().name().contains("RIGHT")) {
            final Sign sign = (Sign)event.getClickedBlock().getState();
            if (sign.getLine(0).equals("§8=-=-=§f=-=-=") && sign.getLine(2).equals("§4§l[CLIQUE]") && sign.getLine(3).equals("§8=-=-=§f=-=-=")) {
                event.setCancelled(true);
                switch (sign.getLine(1)) {
                    case "§9Sopas!": {
                        final Inventory inv = Bukkit.createInventory(null, 54, "");
                        for (int j = 0; j < 54; ++j) {
                            inv.addItem(new ItemStack(Material.MUSHROOM_SOUP));
                        }
                        player.openInventory(inv);
                        break;
                    }
                    case "§9Recraft!": {
                        final Inventory inv = Bukkit.createInventory(null, 9, "");
                        inv.setItem(3, new ItemStack(Material.BOWL, 64));
                        inv.setItem(4, new ItemStack(Material.BROWN_MUSHROOM, 64));
                        inv.setItem(5, new ItemStack(Material.RED_MUSHROOM, 64));
                        player.openInventory(inv);
                        break;
                    }
                    case "§aPoção!": {
                        final Inventory inv = Bukkit.createInventory(null, 54, "");
                        for (int j = 0; j < 54; ++j) {
                            inv.addItem(new ItemStack(Material.POTION, 1, (short) 16421));
                        }
                        player.openInventory(inv);
                        break;
                    }
                }
            }
        }
    }

}
