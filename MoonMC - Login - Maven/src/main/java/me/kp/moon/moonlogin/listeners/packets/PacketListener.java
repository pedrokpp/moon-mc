package me.kp.moon.moonlogin.listeners.packets;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.chat.WrappedPacketInChat;
import io.github.retrooper.packetevents.packetwrappers.play.in.tabcomplete.WrappedPacketInTabComplete;
import me.kp.moon.moonlogin.data.PlayerData;
import me.kp.moon.moonlogin.data.PlayerDataManager;
import me.kp.moon.moonlogin.enums.Strings;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class PacketListener extends PacketListenerAbstract {

    List<Byte> packetTypeList = Arrays.asList(PacketType.Play.Server.SPAWN_ENTITY, PacketType.Play.Server.PLAYER_INFO,
            PacketType.Play.Client.SETTINGS);

    public PacketListener() {
        super(PacketListenerPriority.HIGH);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (event.getPacketId() == PacketType.Play.Client.TAB_COMPLETE) {
            if (!playerData.isLoggedIn()) {
                event.setCancelled(true);
                return;
            }
            WrappedPacketInTabComplete packet = new WrappedPacketInTabComplete(event.getNMSPacket());
            String[] splitText = packet.getText().split(" ");
            if (splitText[0].startsWith("/") && splitText.length == 1) event.setCancelled(true);
        }
        if (event.getPacketId() == PacketType.Play.Client.POSITION || event.getPacketId() == PacketType.Play.Client.POSITION_LOOK ||
                event.getPacketId() == PacketType.Play.Client.FLYING || event.getPacketId() == PacketType.Play.Client.LOOK) {
            if (!playerData.isLoggedIn()) {
                player.teleport(Strings.spawn);
            }
        } else if (event.getPacketId() == PacketType.Play.Client.CHAT) {
            WrappedPacketInChat packet = new WrappedPacketInChat(event.getNMSPacket());
            if (!playerData.isLoggedIn()) {
                if (!(packet.getMessage().startsWith("/login") || packet.getMessage().startsWith("/logar") ||
                        packet.getMessage().startsWith("/register") || packet.getMessage().startsWith("/registrar") ||
                        packet.getMessage().startsWith("/changepassword") || packet.getMessage().startsWith("/mudarsenha"))) {
                    event.setCancelled(true);
                }
            }
        } else {
            if (!playerData.isLoggedIn()) {
                if (!packetTypeList.contains(event.getPacketId())) event.setCancelled(true);
            }
        }
    }

}
