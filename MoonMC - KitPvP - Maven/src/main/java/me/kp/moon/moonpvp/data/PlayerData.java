package me.kp.moon.moonpvp.data;

import lombok.Setter;
import me.kp.moon.moonpvp.enums.PlayerRank;
import me.kp.moon.moonpvp.enums.PlayerTag;
import me.kp.moon.moonpvp.kit.KitType;
import me.kp.moon.moonpvp.warps.WarpType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
public class PlayerData {

    public final UUID uuid;

    public String username = null;

    public String soupType = "mush";

    public boolean skinCooldown = true;
    public String lastSkin = null;

    public boolean staffChat = false;
    public boolean seeStaffChat = true;

    public boolean score = true;

    public boolean screenshare = false;

    public boolean admin = false;
    public boolean superadmin = false;
    public boolean vanish = false;
    public boolean build = false;
    public boolean hasHiddenStaff = false;

    public Long lastMessageMS = System.currentTimeMillis();
    public boolean chatCooldown = false;
    public boolean reportCooldown = false;

    public boolean fallDamageSponge = false;

    public String cacheLastClan = null;
    public PlayerRank cacheLastRank = null;
    public int cacheCoins = 0;
    public int cacheXP = 0;
    public int cacheKillStreak = 0;
    public int cacheKills = 0;
    public int cacheDeaths = 0;

    public boolean evento = false;

    public int cacheLavaFacil = 0;
    public int cacheLavaMedio = 0;
    public int cacheLavaDificil = 0;
    public int cacheLavaInsano = 0;

    public UUID lastTell = null;
    public boolean tell = true;
    public boolean tellSpy = false;

    public List<UUID> ignoredPlayers = new ArrayList<>();

    public KitType kitType = null;
    public UUID lastKitTarget = null;
    public boolean kitCooldown = false;
    public Long kitCooldownMS = null;
    public Location gladiatorLocation = null;
    public int witherTaskID = -1;

    public boolean combat = false;
    public Player lastCombatPlayer = null;
    public Long combatLogTime = 0L;

    public WarpType warpType = null;
    public Player lastDuelPlayer = null;
    public boolean inDuel = false;

    public PlayerTag playerTag = null;
    public String lastTag = null;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void addIgnoredPlayer(Player player) {
        if (!ignoredPlayers.contains(player.getUniqueId())) ignoredPlayers.add(player.getUniqueId());
    }

    public void removeIgnoredPlayer(Player player) {
        ignoredPlayers.remove(player.getUniqueId());
    }

}
