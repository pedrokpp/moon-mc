package me.kp.moon.moonpvp.clan.data;

import lombok.Setter;

import java.util.List;

@Setter
public class Clan {

    public String clanName;
    public String clanTag;
    public String clanOwner;
    public List<String> clanMembers;
    public String clanColor;

    public Clan(String clanName, String clanTag, String clanOwner, List<String> clanMembers, String clanColor) {
        this.clanName = clanName;
        this.clanTag = clanTag;
        this.clanOwner = clanOwner;
        this.clanMembers = clanMembers;
        this.clanColor = clanColor;
    }

    public Clan getClanData() {
        return this;
    }

}
