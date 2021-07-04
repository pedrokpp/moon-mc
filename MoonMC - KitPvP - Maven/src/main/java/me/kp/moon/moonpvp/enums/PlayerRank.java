package me.kp.moon.moonpvp.enums;

import lombok.Getter;
import me.kp.moon.moonpvp.data.PlayerData;
import org.bukkit.ChatColor;

@Getter
public enum PlayerRank {
    IMMORTAL("Immortal", '✪', ChatColor.DARK_RED, 10000),
    MASTER("Master", '❂', ChatColor.AQUA, 8700),
    ELITE("Elite", '✵', ChatColor.DARK_BLUE, 7500),
    EMERALD("Emerald", '✹', ChatColor.DARK_GREEN, 6200),
    GOLD1("Gold I", '✸', ChatColor.GOLD, 5000),
    GOLD2("Gold II", '✸', ChatColor.YELLOW, 4200),
    SILVER1("Silver I", '✶', ChatColor.WHITE, 3200),
    SILVER2("Silver II", '✶', ChatColor.DARK_GRAY, 2450),
    SILVER3("Silver III", '✶', ChatColor.GRAY, 1750),
    RECRUIT("Recruit", '=', ChatColor.WHITE, 1000),
    UNRANKED("Unranked", '-', ChatColor.WHITE, 0);

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
        return IMMORTAL;
    }
}
