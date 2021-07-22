package me.kp.moon.moonlobby.npc;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Getter
public enum NPC {
    KITPVP("§9§lKIT PVP", new Location(Bukkit.getWorlds().get(0), -46.5, 94, 0.5, -90, 0)),
    DUELS("§9§lDUELS", new Location(Bukkit.getWorlds().get(0), -49.5, 94, 10.5, -90, 0)),
    HG("§9§lHG", new Location(Bukkit.getWorlds().get(0), -49.5, 94, -9.5, -90, 0)),
    ;

    private final String name;
    private final Location location;
    NPC(String name, Location location) {
        this.name = name;
        this.location = location;
    }

}
