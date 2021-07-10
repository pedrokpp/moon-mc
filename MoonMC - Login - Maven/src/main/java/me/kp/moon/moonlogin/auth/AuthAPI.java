package me.kp.moon.moonlogin.auth;

import me.kp.moon.moonlogin.data.PlayerData;
import me.kp.moon.moonlogin.utils.SysUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Base64;

public class AuthAPI {

    public static String encodeString(String str) {
        String finalStr = str;
        for (int i = 0; i < 6; i++) {
            finalStr = Base64.getEncoder().encodeToString(finalStr.getBytes());
        }
        return finalStr;
    }

    public static String decodeString(String str) {
        String finalStr = str;
        for (int i = 0; i < 6; i++) {
            byte[] sheesh = Base64.getDecoder().decode(finalStr);
            finalStr = new String(sheesh);
        }
        return finalStr;
    }

    public static void authPlayer(PlayerData playerData) {
        Player player = playerData.getPlayer();
        playerData.setLoggedIn(true);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
        SysUtils.sendTitle(player, "", "§aVocê se autenticou com sucesso!");
        SysUtils.sendActionBar(player, "§aChame seus amigos para o §9Moon§1MC§a!");
        Bukkit.getConsoleSender().sendMessage("§7" + player.getName() + " se autenticou com sucesso.");
        Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(player));
    }

}
