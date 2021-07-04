package me.kp.moon.moonpvp.api;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.PlayerTag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TagAPI {

    private static final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

    public static Scoreboard getScoreboard() {
        return scoreboard;
    }

    public static void loadTeams() {
        for (PlayerTag tag : PlayerTag.values()) {
            newTeam(tag);
        }
    }

    public static void updatePlayerTeam(Player player, PlayerTag tag) {
        scoreboard.getTeams().forEach(team -> {
            if (team.getName().equalsIgnoreCase(tag.getLetter())) {
                team.addEntry(player.getName());
            }
        });
    }

    public static void deletePlayer(Player player) {
        scoreboard.getTeams().forEach(team -> {
            team.getEntries().forEach(name -> {
                if (name.equalsIgnoreCase(player.getName())) team.removeEntry(player.getName());
            });
        });
    }

    public static void setScoreboard(Player player) {
//        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.getObjective("aaa");
        if (objective == null) objective = scoreboard.registerNewObjective("aaa", "bbb");
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;

        player.setScoreboard(scoreboard);
    }

    public static void newTeam(PlayerTag tag) {
        assert scoreboard != null;
        Team team = scoreboard.registerNewTeam(tag.getLetter());
        team.setPrefix(tag.getPrefix().toUpperCase());
//        team.setDisplayName(tag.getPrefix().toUpperCase());
        team.setSuffix("");
        team.setAllowFriendlyFire(true);
        team.setCanSeeFriendlyInvisibles(false);
//        return team;
    }


}
