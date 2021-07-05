package me.kp.moon.moonpvp.listeners;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Medals;
import me.kp.moon.moonpvp.enums.PlayerRank;
import me.kp.moon.moonpvp.enums.PlayerTag;
import me.kp.moon.moonpvp.utils.PlayerUtils;
import me.kp.moon.moonpvp.utils.SysUtils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Chat implements Listener {

    // PlayerUtils.sendMessageToStaff("§c§l[SC] §7[KitPvP] " + PlayerUtils.getPlayerTag(player) +
    //                                player.getName() + " §7» §f" + String.join(" ", args));

    @EventHandler(priority = EventPriority.HIGH)
    public void AsyncPlayerChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();
        String message = player.hasPermission("chat.colorido") ? event.getMessage().replace("&", "§")
                : event.getMessage();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null)
            return;

        if (message.length() < 3 && message.contains("§")) {
            event.setCancelled(true);
            return;
        }

        if (playerData.staffChat) {
            event.setCancelled(true);
            Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("command.staffchat")).forEach(staffer -> {
                PlayerData stafferData = PlayerDataManager.getPlayerData(staffer);
                if (stafferData == null) return;
                String finalMessage = "§c§l[SC] §7[%server%] " + PlayerUtils.getPlayerTag(player) + player.getName() + " §7» §f" + message;
                String server;
                if (playerData.evento) server = "Evento";
                else if (playerData.screenshare) server = "ScreenShare";
                else server = "KitPvP";
                if (stafferData.seeStaffChat) staffer.sendMessage(ChatColor.translateAlternateColorCodes('&', finalMessage.replace("%server%", server)));
            });
            return;
        }

        if (!SysUtils.chat && !player.hasPermission("command.staffchat") && !playerData.screenshare) {
            event.setCancelled(true);
            player.sendMessage("§cO chat está desativado!");
            return;
        }

        if (playerData.chatCooldown) {
            event.setCancelled(true);
            int secs = (int) ((playerData.lastMessageMS - System.currentTimeMillis()) / 1000);
            String q = secs == 1 ? "segundo" : "segundos";
            player.sendMessage("§cAguarde " + secs + " " + q + " para enviar outra mensagem.");
            return;
        }

        event.setCancelled(true);
        String tempMessage = playerData.playerTag != PlayerTag.MEMBRO ?
                "%(RANK)% %[CLAN]% §r" + player.getDisplayName() + " %(MEDAL)% §8»§f " + message : // não é membro
                "%(RANK)% %[CLAN]% §r" + player.getDisplayName() + " %(MEDAL)% §8»§7 " + message; // é membro
        String clanrepl = playerData.cacheLastClan == null ? "" : playerData.cacheLastClan;
        String clanMessage = clanrepl.equals("") ? tempMessage.replace("%[CLAN]% ", clanrepl) : tempMessage.replace("%[CLAN]%", clanrepl);
        String rankMessage = clanMessage.replace("%(RANK)%", "§7(" + PlayerRank.getRank(playerData).getColoredSymbol() + "§7)");
        String medalMessage = playerData.medal == Medals.NENHUM ? rankMessage.replace("%(MEDAL)% ", "") : rankMessage.replace("%(MEDAL)%", playerData.medal.getMedal());
        TextComponent textComponent = new TextComponent(medalMessage);
        Bukkit.getConsoleSender().sendMessage(playerData.screenshare ? "[SS] " + textComponent.toLegacyText() : "[CHAT] " + textComponent.toLegacyText());
        if (!player.hasPermission("command.staffchat")) PlayerUtils.addChatCooldown(playerData);
        event.getRecipients().forEach(players -> {
            PlayerData recipientData = PlayerDataManager.getPlayerData(players);
            if (recipientData == null)
                return;
            if (playerData.screenshare) {
                if (recipientData.screenshare) players.sendMessage(textComponent.toLegacyText());
                return;
            }
            if (recipientData.ignoredPlayers.contains(player.getUniqueId())) return;
            if (recipientData.screenshare) return;
            players.sendMessage(textComponent.toLegacyText());
        });
    }

}
