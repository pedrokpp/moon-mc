package me.kp.moon.moonbans.listeners;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.chat.WrappedPacketInChat;
import me.kp.moon.moonbans.Main;
import me.kp.moon.moonbans.data.PlayerData;
import me.kp.moon.moonbans.data.PlayerDataManager;
import me.kp.moon.moonbans.mysql.MySQL;
import me.kp.moon.moonbans.utils.SysUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PacketListener extends PacketListenerAbstract {

    public PacketListener() {
        super(PacketListenerPriority.LOW);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (event.getPacketId()  == PacketType.Play.Client.CHAT) {
            WrappedPacketInChat wrappedPacketInChat = new WrappedPacketInChat(event.getNMSPacket());
            String message = wrappedPacketInChat.getMessage();
            if (!message.startsWith("/") && playerData.isMuted) {
                event.setCancelled(true);
                if (playerData.cacheTempMuteTime > -1 && System.currentTimeMillis() - playerData.cacheTempMuteTime > 0) {
                    player.sendMessage("§aVocê foi desmutado devido ao tempo do seu mute ter excedido.");
                    playerData.setMuted(false);
                    playerData.setCacheTempMuteTime(0);
                    return;
                }
                SysUtils.sendMessageToStaff("§cO player §e" + player.getName() + " §ctentou falar mutado.");
                Bukkit.getLogger().info("[MUTADO] " + player.getName() + ": " + message);
                player.sendMessage("§cVocê está mutado.\n" +
                        "§fSeu mute acabará em: §c" + (playerData.cacheTempMuteTime == -1 ? "NUNCA" : SysUtils.timeConverter(playerData.cacheTempMuteTime)) + "§f.");
            }

        }
    }

}
