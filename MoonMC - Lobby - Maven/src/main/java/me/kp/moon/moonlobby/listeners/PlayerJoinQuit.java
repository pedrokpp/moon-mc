package me.kp.moon.moonlobby.listeners;

import me.kp.moon.moonlobby.data.PlayerData;
import me.kp.moon.moonlobby.data.PlayerDataManager;
import me.kp.moon.moonlobby.npc.NPCManager;
import me.kp.moon.moonlobby.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuit implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        PlayerDataManager.addPlayer(player);
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) {
            player.kickPlayer("§cOcorreu um erro ao sincronizar seus dados. Por favor relogue.");
            return;
        }
        for (NPC npc : NPC.values()) {
            NPCManager.createNPC(playerData, npc.getLocation(), npc);
        }
        Location spawnLoc = player.getWorld().getSpawnLocation().add(0.5, 0.5, 0.5);
        spawnLoc.setYaw(90);
        player.teleport(spawnLoc);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        PlayerDataManager.removePlayer(event.getPlayer());
    }

}
