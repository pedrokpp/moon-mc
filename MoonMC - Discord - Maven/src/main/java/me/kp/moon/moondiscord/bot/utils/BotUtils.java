package me.kp.moon.moondiscord.bot.utils;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import me.kp.moon.moondiscord.bot.BotMain;
import me.kp.moon.moondiscord.plugin.utils.PlayerUtils;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.Bukkit;

public class BotUtils {

    public static String TOKEN = "ODI5MzUxNTIzMzY0MTEwMzU2.YG23yw.bn_SU3_AaA2mOy5oMcwruiZhMh4";
    public static String PREFIX = ".";
    public static String ID_CHANNEL_JOIN_LOG = "829132156395716618";
    public static String ID_APPEAL_LOG = "830556802726625341";
    public static String ID_CATEGORIA_TICKET = "829408217855164477";
    public static String FOOTER = "2021 © MoonMC - Todos os direitos reservados";
    public static String ID_SERVER_STAFF = "815646478583463966";
    public static String ID_STAFF_CHAT = "843680978949636177";
    public static Integer MAIN_COLOR = 0x572991;
    public static String SC_WEBHOOK_URL = "https://discord.com/api/webhooks/843682366186717194/72UtpkU5IRgmRKukgwRInlI2WAB0LeN4c7dT5oTNp3-AzlDb1-FOzYrCOS5bSfDnR2UV";

    public static WebhookClientBuilder builder = new WebhookClientBuilder(SC_WEBHOOK_URL) // or id, token
            .setThreadFactory((job) -> {
                Thread thread = new Thread(job);
                thread.setName("Hello");
                thread.setDaemon(true);
                return thread;
            })
            .setWait(true);
    public static WebhookClient webhookClient = builder.build();


    public static void sendMsg(String id, String message, MessageEmbed embed) {
        TextChannel channel = BotMain.jda.getTextChannelById(id);

        if (channel == null) {
            System.out.println("[MoonDiscord] ERRO NO getChannel EM sendMsg CLASSE BotUtils.java (channel == null)");
            return;
        }

        if (message == null) {
            channel.sendMessage(embed).queue();
        } else {
            channel.sendMessage(message).queue();
        }

    }

    public static void sendStaffChatDiscordToMine(Member member, String message) {
        String name = PlayerUtils.getPlayerTagFromMember(member);
        String fullMessage = "§c§l[SC] §7[Discord] " + name + " §7» §f" + message;
        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("command.staffchat")).forEach(
                player -> player.sendMessage(fullMessage)
        );
    }

}
