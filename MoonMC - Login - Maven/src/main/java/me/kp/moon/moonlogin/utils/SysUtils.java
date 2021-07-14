package me.kp.moon.moonlogin.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SysUtils {

    public static void sendActionBar(final Player player, final String message) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{'text': '" + message + "'}"), (byte) 2));
    }

    public static void sendTitle(final Player player, final String title, final String subTitle) {
        final IChatBaseComponent chatSubtitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + subTitle + "\"}");
        final IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + title + "\"}");
        final PacketPlayOutTitle titulo = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        final PacketPlayOutTitle subtitulo = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubtitle);
        final PacketPlayOutTitle length = new PacketPlayOutTitle(5, 50, 5);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subtitulo);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(titulo);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
    }

    public static boolean isNickValid(String nick) {
        Pattern p = Pattern.compile("[^a-zA-Z0-9_]");
        if (p.matcher(nick).find()) return false;
        return nick.length() >= 3 && nick.length() <= 16;
    }

}
