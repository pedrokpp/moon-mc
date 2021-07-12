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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class HologramAPI {

    private static final World world = Bukkit.getWorlds().get(0);

    private static final Location topKillsLoc = new Location(world, 916.435, 50.5, 236.380);
    public static final Location topCoinsLoc = new Location(world, 904.501, 50.5, 236.456);
    public static final Location topXPLoc = new Location(world, 904.522, 50.5, 224.497);
    public static final Location topDeathsLoc = new Location(world, 916.380, 50.5, 224.623);

    private static void spawnArmorStand(Location location, String customName) {
        ArmorStand stand = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);
        stand.setArms(false);
        stand.setGravity(false);
        stand.setVisible(false);
        stand.setCustomName(customName);
        stand.setCustomNameVisible(true);
    }

    private static void getTop10Kills() {
        ResultSet rsTopKills = MySQL.getTop10("Kills");
        int counter = 0;
        Location lastLoc = topKillsLoc.clone();
        if (rsTopKills == null) return;
        spawnArmorStand(lastLoc, "§9§l§nTOP KILLS");
        try {
            while (rsTopKills.next()) {
                Location countLoc = lastLoc.subtract(0, 0.3, 0);
                spawnArmorStand(countLoc, "§9#" + (counter + 1) + " §b" + rsTopKills.getString("Username") +
                        " §f➠ §7" + rsTopKills.getString("Kills") + " kills ");
                counter++;
                lastLoc = countLoc;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private static void getTop10Deaths() {
        ResultSet rsTopKills = MySQL.getTop10("Deaths");
        int counter = 0;
        Location lastLoc = topDeathsLoc.clone();
        if (rsTopKills == null) return;
        spawnArmorStand(lastLoc, "§9§l§nTOP DEATHS");
        try {
            while (rsTopKills.next()) {
                Location countLoc = lastLoc.subtract(0, 0.3, 0);
                spawnArmorStand(countLoc, "§9#" + (counter + 1) + " §b" + rsTopKills.getString("Username") +
                        " §f➠ §7" + rsTopKills.getString("Deaths") + " mortes ");
                counter++;
                lastLoc = countLoc;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private static void getTop10XP() {
        ResultSet rsTopKills = MySQL.getTop10("XP");
        int counter = 0;
        Location lastLoc = topXPLoc.clone();
        if (rsTopKills == null) return;
        spawnArmorStand(lastLoc, "§9§l§nTOP XP");
        try {
            while (rsTopKills.next()) {
                Location countLoc = lastLoc.subtract(0, 0.3, 0);
                spawnArmorStand(countLoc, "§9#" + (counter + 1) + " §b" + rsTopKills.getString("Username") +
                        " §f➠ §7" + rsTopKills.getString("XP") + " XP ");
                counter++;
                lastLoc = countLoc;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private static void getTop10Coins() {
        ResultSet rsTopKills = MySQL.getTop10("Coins");
        int counter = 0;
        Location lastLoc = topCoinsLoc.clone();
        if (rsTopKills == null) return;
        spawnArmorStand(lastLoc, "§9§l§nTOP COINS");
        try {
            while (rsTopKills.next()) {
                Location countLoc = lastLoc.subtract(0, 0.3, 0);
                spawnArmorStand(countLoc, "§9#" + (counter + 1) + " §b" + rsTopKills.getString("Username") +
                        " §f➠ §7" + rsTopKills.getString("Coins") + " moedas ");
                counter++;
                lastLoc = countLoc;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void reloadHolograms() {
        world.getNearbyEntities(SysUtils.spawn, 8f, 5f, 8f).stream().filter(entity -> entity instanceof ArmorStand)
                .forEach(Entity::remove);
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            getTop10Kills();
            getTop10Deaths();
            getTop10XP();
            getTop10Coins();
        }, 10L);
    }

    public static void runTask() {
        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), HologramAPI::reloadHolograms, 0L, 4 * 60 * 20L);
    }

}
