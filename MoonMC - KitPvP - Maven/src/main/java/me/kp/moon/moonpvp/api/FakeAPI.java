package me.kp.moon.moonpvp.api;

import me.kp.moon.moonpvp.Main;
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

public class FakeAPI {

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
        }, 90 * 20L);
    }

    private static boolean logWebhook(PlayerData playerData, String fake) {
        Player player = playerData.getPlayer();
        DiscordWebhook webhook = new DiscordWebhook(SysUtils.webhookURLLOGFAKE);
        webhook.setContent("O player **" + player.getName() + "** mudou seu nick para ``" + fake + "``.");
        try {
            webhook.execute();
        } catch (IOException e) {
            player.sendMessage("§cErro ao registrar log, cancelando ação... §7(Contate a equipe de desenvolvimento)");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean hasFake(PlayerData playerData) {
        Player player = playerData.getPlayer();
        return !player.getName().equalsIgnoreCase(playerData.username);
    }

    public static void applyFake(String fake, PlayerData playerData) {
        Player player = playerData.getPlayer();
        if (!logWebhook(playerData, fake)) return;
        changeGamerProfileName(fake, player);
        addFakeCooldown(playerData);
        PlayerUtils.changePlayerTag(player, PlayerTag.MEMBRO, playerData);
    }

}
