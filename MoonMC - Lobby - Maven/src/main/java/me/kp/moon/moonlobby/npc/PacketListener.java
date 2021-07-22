package me.kp.moon.moonlobby.npc;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PacketListener extends PacketListenerAbstract {

    public PacketListener() {
        super(PacketListenerPriority.LOW);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        Player player = event.getPlayer();
        if (event.getPacketId() == PacketType.Play.Client.USE_ENTITY) {
            WrappedPacketInUseEntity wrappedPacketInUseEntity = new WrappedPacketInUseEntity(event.getNMSPacket());
            WrappedPacketInUseEntity.EntityUseAction action = wrappedPacketInUseEntity.getAction();
            Entity entity = wrappedPacketInUseEntity.getEntity(player.getWorld());
            if (action == WrappedPacketInUseEntity.EntityUseAction.ATTACK ||
                    action == WrappedPacketInUseEntity.EntityUseAction.INTERACT) {
                if (entity != null) {
                    //It is NOT an NPC if we can find the Bukkit Entity
                    player.sendMessage("You have attacked an " + entity.getType());
                } else {
                    int entityID = wrappedPacketInUseEntity.getEntityId();
                    player.sendMessage(wrappedPacketInUseEntity.getEntity().getCustomName());
                    player.sendMessage(wrappedPacketInUseEntity.getEntity().getName());
                    player.sendMessage("You have attacked a fake entity with the Entity ID: " + entityID);
                }
            }

        }
    }

}
