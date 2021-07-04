package me.kp.moon.moonpvp.utils;

import me.kp.moon.moonpvp.data.PlayerData;
import org.bukkit.entity.Player;

import java.util.Random;

public class DeathUtils {

    public static void giveRandomXP(PlayerData playerData) {
        Player player = playerData.getPlayer();
        Random random = new Random();
        int xp = !player.hasPermission("coinsbooster.x2") ? random.nextInt((25 - 10) + 1) + 10 :
                (random.nextInt((25 - 10) + 1) + 10) * 2;
        int actualXP = playerData.cacheXP;
        playerData.setCacheXP(actualXP + xp);
        player.sendMessage("§a+" + xp + " XP");
    }

    public static void removeRandomXP(PlayerData playerData) {
        Player player = playerData.getPlayer();
        Random random = new Random();
        int xp = (random.nextInt((25 - 10) + 1) + 10) / 2;
        int actualXP = playerData.cacheXP;
        if (actualXP - xp <= 0) {
            xp = Math.max(actualXP - xp, 0);
            player.sendMessage("§cVocê atingiu o limite mínimo de XP.");
            player.sendMessage("§c-" + xp + " XP");
            playerData.setCacheXP(0);
            return;
        }
        playerData.setCacheXP(actualXP - xp);
        player.sendMessage("§c-" + xp + " XP");
    }

    public static void giveRandomCoins(PlayerData playerData) {
        Player player = playerData.getPlayer();
        Random random = new Random();
        int coins = !player.hasPermission("coinsbooster.x2") ? random.nextInt((25 - 10) + 1) + 10 :
                (random.nextInt((25 - 10) + 1) + 10) * 2;
        int actualCoins = playerData.cacheCoins;
        playerData.setCacheCoins(actualCoins + coins);
        player.sendMessage("§a+" + coins + " moedas");
    }

    public static void removeRandomCoins(PlayerData playerData) {
        Player player = playerData.getPlayer();
        Random random = new Random();
        int coins = (random.nextInt((25 - 10) + 1) + 10) / 2;
        int actualCoins = playerData.cacheCoins;
        if (actualCoins - coins <= 0) {
            coins = Math.max(actualCoins - coins, 0);
            player.sendMessage("§cVocê atingiu o limite mínimo de moedas.");
            player.sendMessage("§c-" + coins + " moedas");
            playerData.setCacheCoins(0);
            return;
        }
        playerData.setCacheCoins(actualCoins - coins);
        player.sendMessage("§c-" + coins + " moedas");
    }
    
}
