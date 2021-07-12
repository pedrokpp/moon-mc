package me.kp.moon.moonpvp.api;

import me.kp.moon.moonpvp.Main;
import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.PlayerGroup;
import me.kp.moon.moonpvp.enums.PlayerRank;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Field;
import java.text.DecimalFormat;

public class ScoreAPI {

    public static final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

    private static void addTeam(String teamName, String entry, String prefix) {
        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
        }
        team.setPrefix(prefix);
        team.setSuffix("");
        team.addEntry(entry);
    }

    public static void setupScore() {
        Objective objective = scoreboard.getObjective("aaa");
        if (objective == null) objective = scoreboard.registerNewObjective("aaa", "bbb");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§9§lMoon§1§lMC");

        objective.getScore("§1§3§r").setScore(10); // branco
        objective.getScore("§1§2§r").setScore(9); // cargo
        objective.getScore("§1§1§r").setScore(8); // rank
        objective.getScore("§1§0§r").setScore(7); // branco
        objective.getScore("§9§r").setScore(6); // kills
        objective.getScore("§8§r").setScore(5); // deaths
        objective.getScore("§7§r").setScore(4); // Killstreak
        objective.getScore("§4§r").setScore(3); // branco
        objective.getScore("§3§r").setScore(2); // coins
        objective.getScore("§2§r").setScore(1); // xp
        objective.getScore("§1§r").setScore(0); // branco

        addTeam("cargo", "§1§2§r", "§e* §fCargo: ");
        addTeam("rank", "§1§1§r", "§e* §fRank: ");
        addTeam("kills", "§9§r", "§e* §fKills: ");
        addTeam("deaths", "§8§r", "§e* §fDeaths: ");
        addTeam("ks", "§7§r", "§e* §fStreak: ");
        addTeam("coins", "§3§r", "§e* §fCoins: ");
        addTeam("xp", "§2§r", "§e* §fXP: ");

    }

    public static void updateScore(PlayerData playerData) {
        ScoreAPI scoreAPI = new ScoreAPI();
        scoreAPI.updateValue("cargo", playerData, PlayerGroup.getGroup(playerData.getPlayer()).getColoredName());
        scoreAPI.updateValue("rank", playerData, PlayerRank.getRank(playerData).getColoredName());
        scoreAPI.updateValue("kills", playerData, "§a" + playerData.cacheKills + " kills");
        scoreAPI.updateValue("deaths", playerData, "§a" + playerData.cacheDeaths + " mortes");
        scoreAPI.updateValue("ks", playerData, "§a" + playerData.cacheKillStreak + " kills");
        scoreAPI.updateValue("coins", playerData, "§a" + new DecimalFormat("###,###.##").format(playerData.cacheCoins) + " coins");
        scoreAPI.updateValue("xp", playerData,  "§a" + new DecimalFormat("###,###.##").format(playerData.cacheXP) + " XP");
    }

    public static void runTask() {
        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> Bukkit.getOnlinePlayers().forEach(player -> {
            PlayerData playerData = PlayerDataManager.getPlayerData(player);
            if (playerData == null) return;
            if (playerData.scoreEnabled) {
                updateScore(playerData);
            }
        }), 0L, 2 * 20L);
    }

    public void updateValue(String teamName, PlayerData playerData, String suffix) {
        Player player = playerData.getPlayer();
        if (suffix.length() > 16) {
            suffix = suffix.substring(0, 16);
        }
        Team team = player.getScoreboard().getTeam(teamName);
        if (team == null) {
            return;
        }
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
        setField(packet, "a", team.getName());
        setField(packet, "h", 2);
        setField(packet, "c", team.getPrefix());
        setField(packet, "d", suffix);
        setField(packet, "i", 0);
        setField(packet, "e", "always");
        setField(packet, "f", 0);

        sendPacket(player, packet);
    }

    public void sendPacket(Player player, PacketPlayOutScoreboardTeam packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public void setField(PacketPlayOutScoreboardTeam packet, String field, Object value) {
        try {
            Field f = packet.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(packet, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
