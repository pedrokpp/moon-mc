package me.kp.moon.worldedit.utils;

import me.kp.moon.worldedit.Main;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class WorldEditUtils {

    private static final ItemStack wand = new ItemStack(Material.WOOD_AXE, 1);
    public static final List<String> wandNames = Arrays.asList("sucesso das 9inha", "fica tranquilo, estou aqui", "chora não bb", "larga as pantufa", "te fraga, sem banho");
    public static ItemStack getWand() {
        ItemStack privateWand = wand;
        ItemMeta wandMeta = wand.getItemMeta();
        wandMeta.setDisplayName("§9" + wandNames.get(new Random().nextInt(wandNames.size())));
        privateWand.setItemMeta(wandMeta);
        return privateWand;
    }

    public static void setBlocks(Material material, int id, Location location1, Location location2) {
        final World world = location1.getWorld();
        int highestX = Math.max(location2.getBlockX(), location1.getBlockX());
        int lowestX = Math.min(location2.getBlockX(), location1.getBlockX());

        int highestY = Math.max(location2.getBlockY(), location1.getBlockY());
        int lowestY = Math.min(location2.getBlockY(), location1.getBlockY());

        int highestZ = Math.max(location2.getBlockZ(), location1.getBlockZ());
        int lowestZ = Math.min(location2.getBlockZ(), location1.getBlockZ());

        new BukkitRunnable() {
            int lastRun = 0;

            @Override
            public void run() {
                HashMap<Chunk, HashSet<Block>> chunkSplitter = new HashMap<>();
                for (int x = lowestX; x <= highestX; x++) {
                    for (int z = lowestZ; z <= highestZ; z++) {
                        for (int y = lowestY; y <= highestY; y++) {
                            Location location = new Location(world, x, y, z);
                            Chunk chunk = location.getChunk();
                            Block block = location.getBlock();
                            HashSet<Block> blockSet;
                            if (chunkSplitter.containsKey(chunk)) {
                                blockSet = chunkSplitter.get(chunk);
                            } else {
                                blockSet = new HashSet<>();
                            }
                            if (material != block.getType()) {
                                blockSet.add(block);
                                chunkSplitter.put(chunk, blockSet);
                            }
                        }
                    }
                }
                for (HashSet<Block> entry : chunkSplitter.values()) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for (Block block : entry) {
                                block.setType(material);
                                block.setData((byte) id);
                            }
                        }
                    }.runTaskLater(Main.getInstance(), lastRun + 3);
                    lastRun++;
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

}
