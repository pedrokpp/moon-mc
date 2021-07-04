package me.kp.moon.moonpvp.evento;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventoUtils {

    public static boolean evento = false;
    public static boolean specs = false;
    public static boolean build = false;
    public static boolean damage = false;
    public static boolean pvp = false;
    public static boolean tp = false;

    public static Location mainArena = new Location(Bukkit.getWorlds().get(0), 980, 73, 460);
    public static Location specLoc = null;

    public static List<UUID> whitelist = new ArrayList<>();
    public static List<Location> blocks = new ArrayList<>();

    public static List<String> getEventoPlayersNames() {
        List<String> players = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(p -> {
            PlayerData playerData = PlayerDataManager.getPlayerData(p);
            if (playerData == null) return;
            if (playerData.evento) players.add(p.getName());
        });
        return players;
    }

    public static List<Player> getEventoPlayers() {
        List<Player> players = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(p -> {
            PlayerData playerData = PlayerDataManager.getPlayerData(p);
            if (playerData == null) return;
            if (playerData.evento) players.add(p);
        });
        return players;
    }

    public static PotionEffectType getPotionEffectTypeByName(String name) {
        name = name.replace("slowness", "slow").replace("haste", "fast")
        .replace("fatigue", "slow_dig").replace("strength", "increase");
        for (PotionEffectType effect : PotionEffectType.values()) {
            if (effect == null) continue;
            if (effect.getName().toLowerCase().contains(name.toLowerCase())) {
                return effect;
            }
        }
        return null;
    }

    public static List<String> getWhitelistPlayersNames() {
        List<String> players = new ArrayList<>();
        for (UUID uuid : whitelist) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) players.add(p.getName());
        }
        return players;
    }

    public static void clearBlocks() {
        blocks.forEach(blockLoc -> {
            if (blockLoc.getBlock().getType() != Material.AIR) {
                blockLoc.getBlock().setType(Material.AIR);
            }
        });
        blocks.clear();
    }

    public static void resetEventoClass() {
        if (blocks.size() > 0) clearBlocks();
        evento = false;
        specs = false;
        build = false;
        damage = false;
        pvp = false;
        tp = false;
        specLoc = null;
        whitelist.clear();
        blocks.clear();
    }

}
