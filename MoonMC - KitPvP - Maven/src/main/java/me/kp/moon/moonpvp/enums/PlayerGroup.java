package me.kp.moon.moonpvp.enums;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum PlayerGroup {

    DONO("DONO", 0, "Dono", "tag.dono", ChatColor.DARK_RED, 1),
    SUBDONO("SUBDONO", 1, "S-Dono", "tag.subdono", ChatColor.DARK_RED, 2),
    DEVELOPER("DEVELOPER", 2, "Dev", "tag.developer", ChatColor.GREEN, 3),
    DIRETOR("DIRETOR", 3, "Diretor", "tag.diretor", ChatColor.AQUA, 4),
    GERENTE("GERENTE", 4, "Gerente", "tag.gerente", ChatColor.BLUE, 5),
    ADMIN("ADMIN", 5, "Admin", "tag.admin", ChatColor.RED, 6),
    COORD("COORD", 6, "Coord", "tag.coord", ChatColor.DARK_AQUA, 7),
    MODPLUS("MODPLUS", 7, "Mod+", "tag.modplus", ChatColor.DARK_GREEN, 8),
    MODGC("MODGC", 8, "ModGC", "tag.modgc", ChatColor.DARK_PURPLE, 9),
    MOD("MOD", 9, "Mod", "tag.mod", ChatColor.DARK_PURPLE, 10),
    BUILDER("BUILDER", 10, "Builder", "tag.builder", ChatColor.DARK_GREEN, 11),
    Helper("Helper", 11, "Helper", "tag.helper", ChatColor.YELLOW, 12),
    YTPLUS("YTPLUS", 12, "YT+", "tag.youtuber+", ChatColor.AQUA, 13),
    YT("YT", 13, "YT", "tag.youtuber", ChatColor.DARK_AQUA, 14),
    PARTNER("PARTNER", 14, "Partner", "tag.partner", ChatColor.GOLD, 15),
    BETA("BETA", 15, "Beta", "tag.beta", ChatColor.DARK_BLUE, 16),
    LUAR("LUAR", 16, "Luar", "tag.luar", ChatColor.BLUE, 17),
    BOOSTER("BOOSTER", 17, "Booster", "tag.booster", ChatColor.LIGHT_PURPLE, 18),
    PRO("PRO", 18, "Pro", "tag.pro", ChatColor.GOLD, 19),
    VIP("VIP", 19, "VIP", "tag.vip", ChatColor.GREEN, 20),
    MEMBRO("MEMBRO", 20, "Membro", "tag.membro", ChatColor.GRAY, 21);

    private final String name;
    private final String permission;
    private final ChatColor color;
    private final int priority;

    PlayerGroup(final String s, final int n, final String name, final String permission, final ChatColor color, final int priority) {
        this.name = name;
        this.permission = permission;
        this.color = color;
        this.priority = priority;
    }

    public String getName() {
        return this.name;
    }

    public String getPermission() {
        return this.permission;
    }

    public ChatColor getColor() {
        return this.color;
    }

    public String getColoredName() {
        return this.getColor() + this.getName();
    }

    public int getPriority() {
        return this.priority;
    }

    public String getBoldColoredName() {
        return this.getColor() + "§l" + this.getName();
    }

    public static PlayerGroup getByName(final String name) {
        for (PlayerGroup group : PlayerGroup.values()) {
            if (group.name().equalsIgnoreCase(name)) {
                return group;
            }
        }
        return null;
    }

    public static PlayerGroup getGroup(final Player player) {
        for (PlayerGroup group : PlayerGroup.values()) {
            if (player.hasPermission(group.getPermission())) {
                return group;
            }
        }
        return PlayerGroup.MEMBRO;
    }

    public static String getPlayerNameWithGroup(Player player) {
        PlayerGroup group = getGroup(player);
        String prefix = group.getBoldColoredName().toUpperCase();
        return prefix + group.getColor() + " " + player.getName();
    }

}
