package me.kp.moon.moonpvp.kit;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Random;

public class GladiatorUtils {

    private static final World WORLD = Bukkit.getWorlds().get(0);
    private static final int arenaSize = 15;

    public static void teleportPlayersToArena(Player player, Player target) {
        Location location = player.getLocation();
        player.teleport(new Location(player.getWorld(), location.getX()-5, location.getY() + (210-location.getY()), location.getZ()-5, -45, 0));
        if (target != null) target.teleport(new Location(target.getWorld(), location.getX()+5, location.getY() + (210-location.getY()), location.getZ()+5, 135, 0));
    }

    public static boolean checkArenasNearby(Location location, int radius) {
        double pX = location.getX();
        double pY = location.getY();
        double pZ = location.getZ();
        for (int x = -(radius); x <= radius; x++) {
            for (int z = -(radius); z <= radius; z++) {
                Block block = WORLD.getBlockAt((int) pX + x, (int) pY, (int) pZ + z);
                if (block.getType() == Material.GLASS) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean checkArenasNearbyPlayer(Player player) {
        Location location = player.getLocation();
        int radius = (arenaSize + 2)/2;
        double pX = location.getX();
        double pY = 209;
        double pZ = location.getZ();
        for (int x = -(radius); x <= radius; x++) {
            for (int z = -(radius); z <= radius; z++) {
                Block block = WORLD.getBlockAt((int) pX + x, (int) pY, (int) pZ + z);
                if (block.getType().name().contains("GLASS")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void spawnArena(Location loc) {
        byte color = (byte) new Random().nextInt(15);
        int radius = arenaSize/2;
        Location location = loc.add(0, 209-loc.getY(), 0);
        double pX = location.getX();
        double pY = location.getY();
        double pZ = location.getZ();
        for (int x = -(radius); x <= radius; x++) {
            for (int y = 0; y <= radius; y ++) {
                for (int z = -(radius); z <= radius; z++) {
                    Block block = WORLD.getBlockAt((int) pX + x, (int) pY + y, (int) pZ + z);
                    block.setType(Material.STAINED_GLASS);
                    block.setData(color);
                }
            }
        }
        for (int x = -(radius-1); x <= radius-1; x++) {
            for (int y = 1; y <= radius-1; y ++) {
                for (int z = -(radius-1); z <= radius-1; z++) {
                    Block block = WORLD.getBlockAt((int) pX + x, (int) pY + y, (int) pZ + z);
                    block.setType(Material.AIR);
                }
            }
        }
    }

    public static void clearArena(Location loc) {
        if (loc == null) return;
        int radius = (arenaSize+2) / 2;
        Location location = loc.add(0, 209 - loc.getY(), 0);
        double pX = location.getX();
        double pY = location.getY();
        double pZ = location.getZ();
        for (int x = -(radius); x <= radius; x++) {
            for (int y = 0; y <= radius; y++) {
                for (int z = -(radius); z <= radius; z++) {
                    Block block = WORLD.getBlockAt((int) pX + x, (int) pY + y, (int) pZ + z);
                    block.setType(Material.AIR);
                }
            }
        }
    }

//    private Player last;
//
//    public void checkGladQuit(PlayerData quitData) {
//        Player quit = quitData.getPlayer();
//        KitType quitKit = quitData.kitType;
//        Bukkit.getOnlinePlayers().forEach(player -> {
//            PlayerData playerData = PlayerDataManager.getPlayerData(player);
//            if (playerData == null) return;
//            if (playerData.lastCombatPlayer.getUniqueId() == quit.getUniqueId())
//                this.last = player;
//            else
//                this.last = null;
//        });
//
//        if (last == null) return;
//        PlayerData lastData = PlayerDataManager.getPlayerData(last);
//        if (lastData == null) return;
//        KitType lastKit = lastData.kitType;
//
//        if (quitKit == KitType.GLADIATOR) {
//            if (quitData.gladiatorLocation != null) {
//                last.teleport(quitData.gladiatorLocation);
//                GladiatorUtils.clearArena(quitData.gladiatorLocation);
//            } else {
//                last.teleport(lastData.gladiatorLocation);
//                GladiatorUtils.clearArena(lastData.gladiatorLocation);
//            }
//            quitData.setGladiatorLocation(null);
//            lastData.setGladiatorLocation(null);
//        } else if (lastKit == KitType.GLADIATOR) {
//            if (lastData.gladiatorLocation != null) {
//                last.teleport(lastData.gladiatorLocation);
//                GladiatorUtils.clearArena(lastData.gladiatorLocation);
//            } else {
//                last.teleport(quitData.gladiatorLocation);
//                GladiatorUtils.clearArena(quitData.gladiatorLocation);
//            }
//            quitData.setGladiatorLocation(null);
//            lastData.setGladiatorLocation(null);
//        }
//    }
}
