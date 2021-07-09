package me.kp.moon.moonlogin.enums;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Strings {

    public static Location spawn = new Location(Bukkit.getWorlds().get(0), 910.5, 49, 230.5, -90, 0);

    public static String getDiscord() {
        return "https://discord.gg/A8wwkTuesh";
    }

    public static String getName() {
        return "§9§lMoon§1§lMC";
    }

    public static String getKickMessage(String reason) {
        return reason + "\n\n§fCaso isso tenha sido um imprevisto, " +
                "favor contatar a staff em §9" + Strings.getDiscord() + "\n\n" +
                "§eAtenciosamente, equipe do " + Strings.getName() + "§e.";
    }

    public static String apenasPlayers() {
        return "§cApenas players podem executar esse comando.";
    }

}
