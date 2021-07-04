package me.kp.moon.moondiscord.plugin.utils;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import me.kp.moon.moondiscord.mysql.MySQL;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerUtils {

    public static HashMap<UUID, String> staffJoin = new HashMap<>();

    public static List<UUID> inStaffChat = new ArrayList<>();

    public static MySQL mySQL = new MySQL();

    public static String getPlayerTagFromMember(Member member) {
        if (!mySQL.discordIDExiste(member.getId())) return "§9§lDC §9" + member.getEffectiveName();
        UUID uuid = UUID.fromString(mySQL.getUUIDFromDiscordID(member.getId()));
        String displayTag = mySQL.getDisplayTAGFromUUID(uuid.toString());
        if (displayTag == null)
            return "§9§lDC §9" + member.getEffectiveName();
        if (displayTag.contains("§"))
            return displayTag + mySQL.getPlayerNameFromDiscordID(member.getId());
        return "§9§lDC §9" + member.getEffectiveName();
    }

    public static void sendStaffChatMineToDiscord(Player player, String message, WebhookClient client) {
        WebhookMessageBuilder builder = new WebhookMessageBuilder()
                .setUsername(player.getName())
                .setAvatarUrl("https://minotar.net/avatar/" + player.getName())
                .setContent(message);
        client.send(builder.build());
    }
    
    public static String getPlayerTag(Player player) {
        if (player.hasPermission("displayname.dono")) return "§4§lDONO §4";// "§4§lDONO §4" + player.getName();
        else if (player.hasPermission("displayname.subdono")) return "§4§lS-DONO §4";// "§4§lS-DONO §4" + player.getName();
        else if (player.hasPermission("displayname.developer")) return "§a§lDEV §a";// "§a§lDEV §a" + player.getName();
        else if (player.hasPermission("displayname.diretor")) return "§b§lDIRETOR §b";// "§b§lDIRETOR §b" + player.getName();
        else if (player.hasPermission("displayname.gerente")) return "§9§lGERENTE §9";// "§9§lGERENTE §9" + player.getName();
        else if (player.hasPermission("displayname.admin")) return "§c§lADMIN §c";// "§3§lCOORD §3" + player.getName();
        else if (player.hasPermission("displayname.coord")) return "§3§lCOORD §3";// "§2§lMOD+ §2" + player.getName();
        else if (player.hasPermission("displayname.modplus")) return "§2§lMOD+ §2";// "§2§lBUILDER §2" + player.getName();
        else if (player.hasPermission("displayname.modgc")) return "§5§lMODGC §5";// "§5§lMODGC §5" + player.getName();
        else if (player.hasPermission("displayname.mod")) return "§5§lMOD §5";// "§5§lMOD §5" + player.getName();
        else if (player.hasPermission("displayname.builder")) return "§2§lBUILDER §2";// "§e§lHELPER §e" + player.getName();
        else if (player.hasPermission("displayname.helper")) return "§e§lHELPER §e";// "§e§lHELPER §e" + player.getName();
        return null;
    }
}
