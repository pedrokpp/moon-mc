package me.kp.moon.moonpvp.api;

import me.kp.moon.moonpvp.Main;
import me.kp.moon.moonpvp.cache.SysCache;
import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.PlayerTag;
import me.kp.moon.moonpvp.utils.DiscordWebhook;
import me.kp.moon.moonpvp.utils.PlayerUtils;
import me.kp.moon.moonpvp.utils.SysUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class FakeAPI {

    public static final List<String> randomNicks = Arrays.asList(
      "nomoreLies",
      "nomoreLies",
      "nomoreLies",
      "nomoreLies",
      "nomoreLies",
      "nomoreLies",
      "nomoreLies",
      "nomoreLies",
      "nomoreLies",
      "nomoreLies",
      "nomoreLies",
      "nomoreLies",
      "nomoreLies",
      "nomoreLies",
      "nomoreLies",
      "nomoreLies"
    );

    private static void changeGamerProfileName(String name, Player player) {
        try {
            Method getHandle = player.getClass().getMethod("getHandle",
                    (Class<?>[]) null);
            try {
                Class.forName("com.mojang.authlib.GameProfile");
            } catch (ClassNotFoundException e) {
                return;
            }
            Object profile = getHandle.invoke(player).getClass()
                    .getMethod("getProfile")
                    .invoke(getHandle.invoke(player));
            Field ff = profile.getClass().getDeclaredField("name");
            ff.setAccessible(true);
            ff.set(profile, name);
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.hidePlayer(player);
                players.showPlayer(player);
            }
        } catch (NoSuchMethodException | SecurityException
                | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private static void addFakeCooldown(PlayerData playerData) {
        playerData.setFakeCooldown(true);
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            PlayerData newData = PlayerDataManager.getPlayerData(playerData.getPlayer());
            if (newData == null) return;
            newData.setFakeCooldown(false);
        }, 15 * 20L);
    }

    private static boolean logWebhook(PlayerData playerData, String fake) {
        Player player = playerData.getPlayer();
        DiscordWebhook webhook = new DiscordWebhook(SysUtils.webhookURLLOGFAKE);
        webhook.setContent("O player **" + playerData.username + "** mudou seu nick para ``" + fake + "``.");
        try {
            webhook.execute();
        } catch (IOException e) {
            player.sendMessage("§cErro ao registrar log, cancelando ação... §7(Contate a equipe de desenvolvimento)");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean isNickValid(String nick) {
        Pattern p = Pattern.compile("[^a-zA-Z0-9_]");
        if (p.matcher(nick).find()) return false;
        return nick.length() >= 3 && nick.length() <= 16;
    }

    public static boolean hasFake(PlayerData playerData) {
        Player player = playerData.getPlayer();
        return !player.getName().equalsIgnoreCase(playerData.username);
    }

    public static void applyFake(String fake, PlayerData playerData) {
        Player player = playerData.getPlayer();
        if (!logWebhook(playerData, fake)) return;
        List<String> reports = SysCache.cacheReports.get(player.getName());
        if (reports != null && reports.size() > 0) {
            SysCache.cacheReports.remove(player.getName());
            SysCache.cacheReports.put(fake, reports);
        }
        TagAPI.deletePlayer(player);
        PlayerUtils.changePlayerTag(player, PlayerTag.MEMBRO, playerData);
        changeGamerProfileName(fake, player);
        PlayerUtils.changePlayerTag(player, PlayerTag.MEMBRO, playerData);
        addFakeCooldown(playerData);
    }

}
