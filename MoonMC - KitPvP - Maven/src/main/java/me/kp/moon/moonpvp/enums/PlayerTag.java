package me.kp.moon.moonpvp.enums;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Getter
public enum PlayerTag {

    MEMBRO("MEMBRO", "u", "§7", ChatColor.GRAY, 21),
    VIP("VIP", "t", "§a§lVIP §a", ChatColor.GREEN, 20),
    PRO("PRO", "s", "§6§lPro §6", ChatColor.GOLD, 19),
    BOOSTER("BOOSTER", "r", "§d§lBooster §d", ChatColor.LIGHT_PURPLE, 18),
    LUAR("LUAR", "q", "§9§lLuar §9", ChatColor.BLUE, 17),
    BETA("BETA", "p", "§1§lBeta §1", ChatColor.DARK_BLUE, 16),
    PARTNER("PARTNER", "o", "§6§lPartner §6", ChatColor.GOLD, 15),
    YT("YT", "n", "§b§lYT §b", ChatColor.DARK_AQUA, 14),
    YTPLUS("YTPLUS", "m", "§b§lYT+ §b", ChatColor.AQUA, 13),
    HELPER("HELPER", "l", "§e§lHelper §e", ChatColor.YELLOW, 12),
    BUILDER("BUILDER", "k", "§2§lBuilder §2", ChatColor.DARK_GREEN, 11),
    MOD("MOD", "j", "§5§lMod §5", ChatColor.DARK_PURPLE, 10),
    MODGC("MODGC", "i", "§5§lModGC §5", ChatColor.DARK_PURPLE, 9),
    MODPLUS("MODPLUS", "h", "§2§lMod+ §2", ChatColor.DARK_GREEN, 8),
    COORD("COORD", "g", "§3§lCoord §3", ChatColor.DARK_AQUA, 7),
    ADMIN("ADMIN", "f", "§c§lAdmin §c", ChatColor.RED, 6),
    GERENTE("GERENTE", "e", "§9§lGerente §9", ChatColor.BLUE, 5),
    DIRETOR("DIRETOR", "d", "§b§lDiretor §b", ChatColor.AQUA, 4),
    DEV("DEV", "c", "§a§lDev §a", ChatColor.GREEN, 3),
    SUBDONO("SUBDONO", "b", "§4§lS-DONO §4", ChatColor.DARK_RED, 2),
    DONO("DONO", "a", "§4§lDono §4", ChatColor.DARK_RED, 1);

    private final String name;
    private final String letter;
    private final String prefix;
    private final ChatColor color;
    private final int priority;

    PlayerTag(final String name, final String letter, final String prefix, final ChatColor color, final int priority) {
        this.name = name.substring(0, 1).toUpperCase() + this.name().substring(1).toLowerCase();
        this.letter = letter;
        this.prefix = prefix;
        this.color = color;
        this.priority = priority;
    }

    public static PlayerTag getTagByName(final String name) {
        for (PlayerTag tag : PlayerTag.values()) {
            if (tag.getName().equalsIgnoreCase(name)) {
                return tag;
            }
        }
        return null;
    }

    public static PlayerTag getTagByPrefix(String prefix) {
        for (PlayerTag tag : PlayerTag.values()) {
            if (tag.getPrefix().equalsIgnoreCase(prefix)) {
                return tag;
            }
        }
        return null;
    }

    public static PlayerTag getPlayerLastTag(Player player) {
        for (PlayerTag tag : PlayerTag.values()) {
            if (player.hasPermission("displaytag." + tag.getName().toLowerCase())) {
                return tag;
            }
        }
        return null;
    }

}
