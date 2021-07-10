package me.kp.moon.moonpvp.warps.listeners;

import me.kp.moon.moonpvp.Main;
import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.utils.ItemUtils;
import me.kp.moon.moonpvp.utils.PlayerUtils;
import me.kp.moon.moonpvp.warps.WarpType;
import me.kp.moon.moonpvp.warps.WarpUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class _1v1Listener implements Listener {

    public static final ItemStack INVITE_ITEM = ItemUtils.getCustomItemStack(Material.BLAZE_ROD, "§aDesafiar", "§5Outro easter egg ;) r$r$");
    public static final ItemStack OFF_QUEUE_ITEM = ItemUtils.editItemStack(new ItemStack(Material.INK_SACK, 1, (short) 8), "§aEntrar na Queue 1v1", null);
    public static final ItemStack ON_QUEUE_ITEM = ItemUtils.editItemStack(new ItemStack(Material.INK_SACK, 1, (short) 10), "§9Buscando players no 1v1...", null);

    private static final World WORLD = Bukkit.getWorlds().get(0);
    public static final List<Player> queue = new ArrayList<>();

    private static final Location firstDuelLoc = new Location(WORLD, 902.5, 75, -231.5, 0, 0);
    private static final Location secondDuelLoc = new Location(WORLD, 902.5, 75, -189.5, -180, 0);

    private void teleportPlayers(Player player, Player target) {
        player.teleport(firstDuelLoc);
        target.teleport(secondDuelLoc);
    }

    private void giveDuelItems(Player player) {
        player.closeInventory();
        Inventory inv = player.getInventory();
        inv.clear();
        inv.setItem(0, new ItemStack(Material.STONE_SWORD));
        for (int i = 0; i < 8; i++) {
            inv.addItem(new ItemStack(Material.MUSHROOM_SOUP));
        }
    }

    private void start1v1(Player player, Player target) {
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        PlayerData targetData = PlayerDataManager.getPlayerData(target);
        if (playerData == null || targetData == null) return;
        queue.remove(player);
        queue.remove(target);

        player.setHealth(20.0);
        target.setHealth(20.0);

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
            ((CraftPlayer) player).getHandle().playerConnection.a(new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN));
            PlayerData playerData = PlayerDataManager.getPlayerData(player);
            if (playerData == null) return;
            playerData.setCombat(false);
            playerData.setInDuel(false);
            playerData.setLastDuelPlayer(null);
            playerData.setWarpType(WarpType._1v1);
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

    @EventHandler()
    public void PlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.hasItem() && event.getItem().isSimilar(OFF_QUEUE_ITEM)) {
            PlayerData playerData = PlayerDataManager.getPlayerData(player);
            if (playerData == null) return;
            // entrar na queue e puxar aleatório ou ficar esperando por um player
            player.setItemInHand(ON_QUEUE_ITEM);
            queue.add(player);
            if (queue.size() == 1) {
                player.sendMessage("§7Queue vazia, aguardando jogadores...");
            } else {
                queue.remove(player);
                Player random = queue.get(new Random().nextInt(queue.size()));
                queue.remove(random);
                random.sendMessage("§aSeu duelo aleatório é contra o player §e" + player.getName() + "§a.");
                player.sendMessage("§aSeu duelo aleatório é contra o player §e" + random.getName() + "§a.");
                start1v1(player, random);
            }
        } else if (player.getItemInHand().isSimilar(ON_QUEUE_ITEM)) {
            queue.remove(player);
            player.setItemInHand(OFF_QUEUE_ITEM);
            player.sendMessage("§cVocê saiu da queue de 1v1.");
        }
    }

    @EventHandler
    public void PlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        Player player = event.getEntity();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        Player killer = player.getKiller();
        if (killer == null) {
            return;
        }
        PlayerData killerData = PlayerDataManager.getPlayerData(killer);
        if (killerData == null) {
            return;
        }
        if (killer == player || playerData == null) {
            return;
        }
        if ((playerData.warpType == WarpType._1v1 && killerData.warpType == WarpType._1v1)) {
            event.getDrops().forEach(drop -> drop.setType(Material.AIR));
            player.sendMessage("§cVocê perdeu 1v1 contra §7" + killer.getName() + " §cque ficou com §7" + killer.getHealth() + " §ccorações e §7" +
                    Arrays.stream(killer.getInventory().getContents()).filter(item -> item.getType() == Material.MUSHROOM_SOUP).count() + " §csopas.");
            killer.sendMessage("§aVocê ganhou 1v1 contra §7" + killer.getName() + " §acom §7" + killer.getHealth() + " §acorações e §7" +
                    Arrays.stream(killer.getInventory().getContents()).filter(item -> item.getType() == Material.MUSHROOM_SOUP).count() + " §asopas.");
            PlayerUtils.killerKillPlayer(killer, killerData, playerData);
            PlayerUtils.deadKillPlayer(player, playerData, killer);
            autoRespawn(player);
            autoRespawn(killer);
        }
    }

}
