package me.kp.moon.moonlobby.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListener implements Listener {

    @EventHandler
    public void onServerPing(ServerListPingEvent event) {
        event.setMotd("motd!!!");
    }

}
