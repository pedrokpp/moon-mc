package me.kp.moon.moonpvp.warps.listeners;

import me.kp.moon.moonpvp.Main;
import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.listeners.PlayerKill;
import me.kp.moon.moonpvp.utils.ItemUtils;
import me.kp.moon.moonpvp.utils.PlayerUtils;
import me.kp.moon.moonpvp.warps.WarpType;
import me.kp.moon.moonpvp.warps.WarpUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class SumoListener implements Listener {

    public static final ItemStack INVITE_ITEM = ItemUtils.getCustomItemStack(Material.APPLE, "§aDesafiar", "§5Esse é easter egg mas só ");
    public static final ItemStack OFF_QUEUE_ITEM = ItemUtils.editItemStack(new ItemStack(Material.INK_SACK, 1, (short) 8), "§aEntrar na Queue Sumô", null);
    public static final ItemStack ON_QUEUE_ITEM = ItemUtils.editItemStack(new ItemStack(Material.INK_SACK, 1, (short) 10), "§9Buscando players no Sumô...", null);

    private static final World WORLD = Bukkit.getWorlds().get(0);
    public static final List<UUID> queue = new ArrayList<>();

    private static final Location firstDuelLoc = new Location(WORLD, 1156.5, 74, 428.5, -180, 0);
    private static final Location secondDuelLoc = new Location(WORLD, 1156.5, 74, 420.5, 0, 0);

    private void teleportPlayers(Player player, Player target) {
        player.teleport(firstDuelLoc);
        target.teleport(secondDuelLoc);
    }
    private void giveDuelItems(Player player) {
        player.closeInventory();
        Inventory inv = player.getInventory();
        inv.clear();
    }
    private void start1v1(Player player, Player target) {
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        PlayerData targetData = PlayerDataManager.getPlayerData(target);
        if (playerData == null || targetData == null) return;
        queue.remove(player.getUniqueId());
        queue.remove(target.getUniqueId());

        Bukkit.getOnlinePlayers().forEach(online -> {
            player.hidePlayer(online);
            target.hidePlayer(online);
        });
        player.showPlayer(target);
        target.showPlayer(player);

        teleportPlayers(player, target);
        giveDuelItems(player);
        giveDuelItems(target);

        playerData.setCombat(true);
        playerData.setLastCombatPlayer(target);
        playerData.setInDuel(true);

        targetData.setCombat(true);
        targetData.setLastCombatPlayer(player);
        targetData.setInDuel(true);
    }
    private void autoRespawn(Player player) {
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            PlayerData playerData = PlayerDataManager.getPlayerData(player);
            if (playerData == null) return;
            playerData.setCombat(false);
            playerData.setLastCombatPlayer(null);
            playerData.setInDuel(false);
            playerData.setLastDuelPlayer(null);
            playerData.setWarpType(WarpType.SUMO);
            WarpUtils.teleportPlayerToWarp(player, playerData.warpType);
            WarpUtils.giveWarpItems(playerData);
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
        }, 2L);
    }

    @EventHandler(ignoreCancelled = true)
    public void PlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof Player)) return;
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        Player target = (Player) event.getRightClicked();
        PlayerData targetData = PlayerDataManager.getPlayerData(target);
        if (playerData == null || targetData == null) return;
        if (player.getItemInHand().isSimilar(INVITE_ITEM)) {
            if (targetData.lastDuelPlayer != null && (targetData.lastDuelPlayer.getUniqueId() == player.getUniqueId())) {
                // se o target tiver o player como lastDuelPlayer, significa que o player aceitou o duelo
                player.sendMessage("§aVocê aceitou o duelo de §e" + target.getName() + "§a.");
                target.sendMessage("§aO player §e" + player.getName() + "§a aceitou o seu duelo.");
                start1v1(player, target);
            } else {
                if (playerData.lastDuelPlayer == null) {
                    playerData.setLastDuelPlayer(target);
                    player.sendMessage("§aVocê duelou o player §e" + target.getName() + "§a.");
                    target.sendMessage("§aVocê foi duelado pelo player §e" + player.getName() + "§a.");
                } else if (playerData.lastDuelPlayer.getUniqueId() != target.getUniqueId()) {
                    playerData.setLastDuelPlayer(target);
                    player.sendMessage("§aVocê duelou o player §e" + target.getName() + "§a.");
                    target.sendMessage("§aVocê foi duelado pelo player §e" + player.getName() + "§a.");
                } else {
                    player.sendMessage("§cVocê já desafiou esse player.");
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void PlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (playerData.warpType == WarpType.SUMO) {
            if (event.getAction().name().contains("RIGHT") || event.getAction().name().contains("LEFT")) {
                if (player.getItemInHand().isSimilar(OFF_QUEUE_ITEM)) {
                    // entrar na queue e puxar aleatório ou ficar esperando por um player
                    player.setItemInHand(ON_QUEUE_ITEM);
                    queue.add(player.getUniqueId());
                    if (queue.size() == 1) {
                        player.sendMessage("§7Queue vazia, aguardando jogadores...");
                    } else {
                        queue.remove(player.getUniqueId());
                        Player random = Bukkit.getPlayer(queue.get(new Random().nextInt(queue.size())));
                        queue.remove(random.getUniqueId());
                        random.sendMessage("§aSeu duelo aleatório é contra o player §e" + player.getName() + "§a.");
                        player.sendMessage("§aSeu duelo aleatório é contra o player §e" + random.getName() + "§a.");
                        start1v1(player, random);
                    }
                } else if (player.getItemInHand().isSimilar(ON_QUEUE_ITEM)) {
                    queue.remove(player.getUniqueId());
                    player.setItemInHand(OFF_QUEUE_ITEM);
                    player.sendMessage("§cVocê saiu da queue de Sumô.");
                }
            }
        }
    }

    @EventHandler
    public void PlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (playerData.warpType == WarpType.SUMO) {
            if (event.getTo().getBlock().getType() == Material.WATER ||
                    event.getTo().getBlock().getType() == Material.STATIONARY_WATER) {
                try {
                    Player killer = playerData.lastCombatPlayer;
                    PlayerData killerData = PlayerDataManager.getPlayerData(killer);
                    if (killerData == null) return;
                    PlayerUtils.killerKillPlayer(killer, killerData, playerData);
                    PlayerUtils.deadKillPlayer(player, playerData, killer);
                    autoRespawn(player);
                    autoRespawn(killer);
                } catch (Exception ignore) {}
            }
        }
    }

}
