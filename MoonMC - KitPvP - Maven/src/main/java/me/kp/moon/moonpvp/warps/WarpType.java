package me.kp.moon.moonpvp.warps;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

@Getter
public enum WarpType {

    ARENA("Arena", "Vá para a arena principal do servidor.", Material.STONE_SWORD, null, true),
    FISHERMAN("Fisherman", "Fisgue seus inimitos até a morte.", Material.FISHING_ROD, new Location(Bukkit.getWorlds().get(0), 1153.5, 119, 24.5), true),
    FPS("FPS", "Arena destinada a boa performance.", Material.THIN_GLASS, new Location(Bukkit.getWorlds().get(0), 1300.5, 83, 219.5), true),
    _1v1("1v1", "Lute no mano a mano com seus oponentes.", Material.BLAZE_ROD, new Location(Bukkit.getWorlds().get(0), 902.5, 76, -210.5), true),
    SUMO("Sumô", "Derrube oponentes de uma plataforma no um contra um.", Material.APPLE, new Location(Bukkit.getWorlds().get(0), 1134.5, 76, 424.5, -90, 0), true),
    LAVA("Lava Challenge", "Teste suas habilidades com sopa.", Material.LAVA_BUCKET, new Location(Bukkit.getWorlds().get(0), 903.5, 62, 608.5), true),
    KB("Knockback", "Derrube seus oponentes da plataforma com repulsão.", Material.STICK, new Location(Bukkit.getWorlds().get(0), 622.5, 91, 47.5), true),
    ;

    private final String warpName;
    private final String description;
    private final Material icon;
    private final Location location;
    private final boolean enabled;
    WarpType(String warpName, String description, Material icon, Location location, boolean enabled) {
        this.warpName = warpName;
        this.description = description;
        this.icon = icon;
        this.location = location;
        this.enabled = enabled;
    }

    public static WarpType getWarpTypeByName(String name) {
        for (WarpType warp : WarpType.values()) {
            if (warp.getWarpName().toLowerCase().contains(name.toLowerCase())) {
                return warp;
            }
        }
        return null;
    }

}
