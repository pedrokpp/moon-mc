package me.kp.moon.moonpvp.api;

import me.kp.moon.moonpvp.Main;
import me.kp.moon.moonpvp.mysql.MySQL;
import me.kp.moon.moonpvp.utils.SysUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.HashMap;

public class HologramAPI {
    
    private static final World world = Bukkit.getWorlds().get(0);

    private static double yK = 7.5;
    private static int c = 1;

    public static Location topKillsLoc = new Location(world, 916.435, 49, 236.380);
    public static Location topDeathsLoc = new Location(world, 904.501, 49, 236.456);
    public static Location topXPLoc = new Location(world, 904.522, 49, 224.497);
    public static Location topCoinsLoc = new Location(world, 916.380, 49, 224.623);

    public static HashMap<String, Integer> topKills = new HashMap<>();
    public static HashMap<String, Integer> topDeaths = new HashMap<>();
    public static HashMap<String, Integer> topXP = new HashMap<>();
    public static HashMap<String, Integer> topCoins = new HashMap<>();

    public static void loadTops() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            topKills = MySQL.getTop10("Kills");
            topDeaths = MySQL.getTop10("Deaths");
            topXP = MySQL.getTop10("XP");
            topCoins = MySQL.getTop10("Coins");
        });
    }

    public static void reloadHolograms() {
        world.getNearbyEntities(SysUtils.spawn, 8f, 5f, 8f).stream().filter(entity -> entity instanceof ArmorStand)
                .forEach(Entity::remove);
        topKills.forEach((name, kill) -> {
            // System.out.println(c + name);
            ArmorStand stand = (ArmorStand) world.spawnEntity(topKillsLoc.add(0, yK, 0), EntityType.ARMOR_STAND);
            stand.setArms(false);
            stand.setGravity(false);
            stand.setVisible(false);
            stand.setCustomName("§6" + c + ". §e" + name + " §7: §e" + kill + " kills");
            stand.setCustomNameVisible(true);
            yK-=0.75;
            c++;
        });
        yK = 7.5;
        c = 1;
        topDeaths.forEach((name, death) -> {
            // System.out.println(c + name);
            ArmorStand stand = (ArmorStand) world.spawnEntity(topDeathsLoc.add(0, yK, 0), EntityType.ARMOR_STAND);
            stand.setArms(false);
            stand.setGravity(false);
            stand.setVisible(false);
            stand.setCustomName("§6" + c + ". §e" + name + " §7: §e" + death + " deaths");
            stand.setCustomNameVisible(true);
            yK-=0.75;
            c++;
        });
        yK = 7.5;
        c = 1;
        topXP.forEach((name, xp) -> {
            // System.out.println(c + name);
            ArmorStand stand = (ArmorStand) world.spawnEntity(topXPLoc.add(0, yK, 0), EntityType.ARMOR_STAND);
            stand.setArms(false);
            stand.setGravity(false);
            stand.setVisible(false);
            stand.setCustomName("§6" + c + ". §e" + name + " §7: §e" + xp + " XP");
            stand.setCustomNameVisible(true);
            yK-=0.75;
            c++;
        });
        yK = 7.5;
        c = 1;
        topCoins.forEach((name, coin) -> {
            // System.out.println(c + name);
            ArmorStand stand = (ArmorStand) world.spawnEntity(topCoinsLoc.add(0, yK, 0), EntityType.ARMOR_STAND);
            stand.setArms(false);
            stand.setGravity(false);
            stand.setVisible(false);
            stand.setCustomName("§6" + c + ". §e" + name + " §7: §e$" + coin);
            stand.setCustomNameVisible(true);
            yK-=0.75;
            c++;
        });
        yK = 7.5;
        c = 1;
    }

    public static void runTask() {
        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), HologramAPI::reloadHolograms, 20L, 5 * 60 * 20L);
    }

}
