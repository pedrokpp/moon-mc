package me.kp.moon.moonlobby.listeners;

import me.kp.moon.moonlobby.npc.NPCManager;
import me.kp.moon.moonlobby.npc.NPC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuit implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        for (NPC npc : NPC.values()) {
            NPCManager.createNPC(event.getPlayer(), npc.getLocation(), npc.getName());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

}
