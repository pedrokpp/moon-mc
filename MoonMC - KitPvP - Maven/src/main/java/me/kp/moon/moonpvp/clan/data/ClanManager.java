package me.kp.moon.moonpvp.clan.data;

import me.kp.moon.moonpvp.Main;
import me.kp.moon.moonpvp.clan.ClanSQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClanManager {

    public static ConcurrentHashMap<String, Clan> clanDataHashMap = new ConcurrentHashMap<>();

    public static void loadAllClans() {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            List<String> clanList = ClanSQL.getAllClanTags();
            if (clanList == null) return;
            clanList.forEach(ClanManager::addClan);
        });
    }

    public static void addClan(String clanTag) {
        if (!clanDataHashMap.containsKey(clanTag))
            clanDataHashMap.put(clanTag, new Clan(
                    ClanSQL.getClanName(clanTag),
                    clanTag,
                    ClanSQL.getClanOwner(clanTag),
                    ClanSQL.getClanMembers(clanTag),
                    ClanSQL.getClanColor(clanTag)
            ));
    }

    public static void removeClan(String clanTag) {
        clanDataHashMap.remove(clanTag);
    }

    public static Clan getClanByTag(String tag) {
        for (Map.Entry<String, Clan> entry : clanDataHashMap.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(tag)){
                return entry.getValue();
            }
        }
        return null;
    }

    public static Clan getPlayerClan(Player player) {
        for (Map.Entry<String, Clan> entry : clanDataHashMap.entrySet()) {
            if (entry.getValue().clanMembers.contains(player.getName())){
                return entry.getValue();
            }
        }
        return null;
    }

    public static void updateClan(Clan clan, Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            if (clan.clanMembers.contains(player.getName())) {
                ClanSQL.removeClanMember(player, clan.clanTag);
            } else {
                ClanSQL.addClanMember(player, clan.clanTag);
            }
        });
    }

}
