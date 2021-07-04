package me.kp.moon.moonbans.utils;

import io.ipinfo.api.IPInfo;
import io.ipinfo.api.cache.SimpleCache;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class SysUtils {

    public static List<String> whitelist = Arrays.asList("pedrokp", "177.192.0.92", "Azarada", "189.7.67.221", "Azarado", "187.2.176.72", "RTX2080Ti_FDS", "191-221-135-106.user3p.brasiltelecom.net.br" ,"sudanor");

    public static IPInfo ipInfo = IPInfo.builder().setToken("112b3614fc802c").setCache(new SimpleCache(Duration.ofDays(5))).build();

    public static String timeConverter(Long ms) {
        LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneId.systemDefault());
        return ldt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).replace(" ", " às ");
    }

    public static Long addDaysToMs(int days, long ms) { return ms + (days * 86400000L); }
    public static Long addHoursToMs(int hours, long ms) { return ms + (hours * 3600000L); }
    public static Long addMinutesToMs(int minutes, long ms) { return ms + (minutes * 60000L); }
    public static Long addSecondsToMs(int seconds, long ms) { return ms + (seconds * 1000L); }

    public static void alertBanimentoStaff(String target, String reason, String author, String tempo, boolean silent) {
        TextComponent textComponent = new TextComponent("§7§o(BANIMENTO) §7§oNovo §7§obanimento §7§oregistrado§7§o.");
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("" +
                "§fAlvo: §c" + target + "\n" +
                "§fMotivo: §c" + reason + "\n" +
                "§fAutor: §c" + author + "\n" +
                "§fTempo: §c" + tempo + "\n" +
                "§fSilencioso: " + (silent ? "§aSim" : "§cNão")).create()));
        Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("command.staffchat")).forEach(staffer -> {
            staffer.sendMessage(textComponent);
        });
    }
    public static void alertUnbanStaff(String target, String author) {
        TextComponent textComponent = new TextComponent("§7§o(BANIMENTO) §7§oUm §7§obanimento §7§ofoi §7§orevogado§7§o.");
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("" +
                "§fAlvo: §c" + target + "\n" +
                "§fAutor: §c" + author).create()));
        Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("command.staffchat")).forEach(staffer -> {
            staffer.sendMessage(textComponent);
        });
    }

    public static void alertMutetoStaff(String target, String reason, String author, String tempo) {
        TextComponent textComponent = new TextComponent("§7§o(MUTE) §7§oNovo §7§omute §7§oregistrado§7§o.");
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("" +
                "§fAlvo: §c" + target + "\n" +
                "§fMotivo: §c" + reason + "\n" +
                "§fAutor: §c" + author + "\n" +
                "§fTempo: §c" + tempo).create()));
        Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("command.staffchat")).forEach(staffer -> {
            staffer.sendMessage(textComponent);
        });
    }
    public static void alertUnmuteStaff(String target, String author) {
        TextComponent textComponent = new TextComponent("§7§o(MUTE) §7§oUm §7§omute §7§ofoi revogado§7§o.");
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("" +
                "§fAlvo: §c" + target + "\n" +
                "§fAutor: §c" + author).create()));
        Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("command.staffchat")).forEach(staffer -> {
            staffer.sendMessage(textComponent);
        });
    }

    public static void alertKickStaff(String target, String reason, String author) {
        TextComponent textComponent = new TextComponent("§7§o(KICK) §7§oUm jogador foi §7§okickado §7§odo servidor§7§o.");
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("" +
                "§fAlvo: §c" + target + "\n" +
                "§fMotivo: §c" + reason + "\n" +
                "§fAutor: §c" + author).create()));
        Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("command.staffchat")).forEach(staffer -> {
            staffer.sendMessage(textComponent);
        });
    }

    public static void sendMessageToStaff(String message) {
        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("command.staffchat")).forEach(staff -> {
            staff.sendMessage(message);
        });
    }

    public static void alertPlayers() {
        Bukkit.broadcastMessage("§cUm jogador foi punido por violar as diretrizes da comunidade.");
    }

}
