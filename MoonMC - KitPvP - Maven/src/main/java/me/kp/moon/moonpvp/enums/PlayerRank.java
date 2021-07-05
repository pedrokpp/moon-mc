package me.kp.moon.moonpvp.enums;

import lombok.Getter;
import me.kp.moon.moonpvp.data.PlayerData;
import org.bukkit.ChatColor;

@Getter
public enum PlayerRank {

    INFINITY("Infinity", '۞', ChatColor.BLACK, 60000),
    ANDROMEDA("Andromeda", '✯', ChatColor.LIGHT_PURPLE, 45000),
    NEBULOUS("Nebulous", '✧', ChatColor.DARK_RED, 20000),
    ECLIPSE("Eclipse", '✪', ChatColor.RED, 10000),
    SUN("Sun", '❂', ChatColor.GOLD, 8700),
    MERCURY("Mercury", '✵', ChatColor.DARK_BLUE, 7500),
    VENUS("Venus", '✹', ChatColor.DARK_GREEN, 6200),
    EARTH("Earth", '✸', ChatColor.BLUE, 5000),
    MARS("Mars", '✸', ChatColor.YELLOW, 4200),
    JUPITER("Jupiter", '✶', ChatColor.WHITE, 3200),
    SATURN("Saturn", '✶', ChatColor.DARK_GRAY, 2450),
    URANUS("Uranus", '✶', ChatColor.GRAY, 1750),
    NEPTUNE("Neptune", '=', ChatColor.WHITE, 1000),
    COMET("Comet", '☄', ChatColor.WHITE, 0);

    private final String name;
    private final char symbol;
    private final ChatColor color;
    private final int xp;

    public PlayerRank next() {
        return PlayerRank.values()[(this.ordinal() - 1) % PlayerRank.values().length];
    }
    public PlayerRank prev() {
        return PlayerRank.values()[(this.ordinal() + 1) % PlayerRank.values().length];
    }

    PlayerRank(String name, char symbol, ChatColor color, int xp) {
        this.name = name;
        this.symbol = symbol;
        this.color = color;
        this.xp = xp;
    }

    public String getColoredName() {
        return this.getColor() + this.getName();
    }

    public String getColoredSymbol() {
        return String.valueOf(this.getColor().toString()) + this.getSymbol();
    }

    public String getColoredSymbolName() {
        return this.getColoredSymbol() + " " + this.getName();
    }

    public static PlayerRank getRank(PlayerData playerData) {
        int xp = playerData.cacheXP;
        for (PlayerRank rank : PlayerRank.values()) {
            if (xp >= rank.getXp())
                return rank;
        }
        return INFINITY;
    }
}
