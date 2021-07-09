package me.kp.moon.moonpvp.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SysUtils {

    public static boolean chat = true;
    public static boolean clans = true;

    public static final String webhookURLLOGGRANT = "https://discord.com/api/webhooks/860993843351781416/JWi2DQ6Fy2YOZM5MrDTS_p6sxmeeSGhiosfq1NZi6fRjQfm0tfLPKp7c7-BXKdNemIfS";
    public static final String webhookURLLOGFAKE = "https://discord.com/api/webhooks/861766145299578881/cQqFK2NJLvhtCcsexsDjKrjTWa6mEBHQuvZvUxXWhPSmgdNJHXvksTGfTqY263PHdzSk";

    private static final List<Location> spawnLocs = Arrays.asList(new Location(Bukkit.getWorlds().get(0), 478.0, 74.0, 274.0), new Location(Bukkit.getWorlds().get(0), 431.0, 74.0, 338.0),
            new Location(Bukkit.getWorlds().get(0), 338.0, 74.0, 314.0), new Location(Bukkit.getWorlds().get(0), 349.0, 74.0, 204.0));

    public static List<String> blacklistedWords = Arrays.asList("preto", "macaco", "filho da puta", "seu merda", "lixo", "l");

    public static Location spawn = new Location(Bukkit.getWorlds().get(0), 910.5, 50, 230.5, -90, 0);

    public static void setChat(boolean chat) {
        SysUtils.chat = chat;
    }

    public static Location getRandomSpawnLocation() {
        return spawnLocs.get(new Random().nextInt(spawnLocs.size()));
    }

    public static void sendActionBar(final Player player, final String message) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{'text': '" + message + "'}"), (byte)2));
    }

    public static void sendTitle(final Player player, final String title, final String subTitle) {
        final IChatBaseComponent chatSubtitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + subTitle + "\"}");
        final IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + title + "\"}");
        final PacketPlayOutTitle titulo = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        final PacketPlayOutTitle subtitulo = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubtitle);
        final PacketPlayOutTitle length = new PacketPlayOutTitle(5, 50, 5);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(subtitulo);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(titulo);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(length);
    }

    public static void sendTab(Player player, String header, String footer) {
        try {
            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
            Field head = packet.getClass().getDeclaredField("a");
            head.setAccessible(true);
            head.set(packet, IChatBaseComponent.ChatSerializer.a("'" + header + "'"));
            Field foot = packet.getClass().getDeclaredField("b");
            foot.setAccessible(true);
            foot.set(packet, IChatBaseComponent.ChatSerializer.a("'" + footer + "'"));
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        }
        catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    public static void endTask(int taskID) {
        Bukkit.getScheduler().getPendingTasks().forEach(task -> {
            if (task.getTaskId() == taskID) task.cancel();
        });
    }

    public static float convertMsToSeconds(long ms) {
        return ms/1000f;
    }



}
