package me.kp.moon.moonlobby.npc;

import com.mojang.authlib.GameProfile;
import me.kp.moon.moonlobby.data.PlayerData;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class NPCManager {

    public static void createNPC(PlayerData playerData, Location location, NPC npcE) {
        Player player = playerData.getPlayer();
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
        GameProfile npcProfile = new GameProfile(UUID.randomUUID(), npcE.getName());

        EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, npcProfile, new PlayerInteractManager(nmsWorld));

        switch (npcE) {
            case KITPVP:
                playerData.setKitPvPEntityID(npc.getId());
                break;
            case DUELS:
                playerData.setDuelsEntityID(npc.getId());
                break;
            default:
                break;
        }

        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;
        playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
        playerConnection.sendPacket(new PacketPlayOutEntityHeadRotation());
        playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
        playerConnection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (location.getYaw())));
        playerConnection.sendPacket(new PacketPlayOutAnimation(npc, 0));
        playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
    }

}
