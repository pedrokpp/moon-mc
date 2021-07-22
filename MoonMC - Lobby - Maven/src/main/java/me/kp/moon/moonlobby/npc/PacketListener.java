package me.kp.moon.moonlobby.npc;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import me.kp.moon.moonlobby.data.PlayerData;
import me.kp.moon.moonlobby.data.PlayerDataManager;
import org.bukkit.entity.Entity;
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
        if (event.getPacketId() == PacketType.Play.Client.USE_ENTITY) {
            WrappedPacketInUseEntity wrappedPacketInUseEntity = new WrappedPacketInUseEntity(event.getNMSPacket());
            if (wrappedPacketInUseEntity.getAction() == WrappedPacketInUseEntity.EntityUseAction.INTERACT_AT) return;
            Entity entity = wrappedPacketInUseEntity.getEntity(player.getWorld());
            if (entity == null) {
                int entityID = wrappedPacketInUseEntity.getEntityId();
                if (entityID == playerData.getKitPvPEntityID())
                    player.chat("/server kitpvp");
                else if (entityID == playerData.getDuelsEntityID())
                    player.chat("/server duels");
                else
                    player.sendMessage("§cModo de jogo em desenvolvimento.");
            }

        }
    }

}
