package me.kp.moon.moonpvp.enums;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter
public enum Medals {

    NENHUM("§7Nenhuma", "§7Nenhuma", "§7Sem medalhas."),
    DEV("§eDev", "§e☕", "§7Para quem participou do desenvolvimento do servidor."),
    EX_STAFF("§6Ex-Staff", "§6⚜", "§7Para quem já foi staff do servidor."),
    KAWAII("§dPorquinho", "§d♾", "§7Para quem é fofinho."),
    YIN_YANG("§fYin Yang", "§f☯", "§7Para quem é controlado."),
    ;

    private final String name;
    private final String medal;
    private final String description;
    Medals(String name, String medal, String description) {
        this.name = name;
        this.medal = medal;
        this.description = description;
    }

    public String getName() {
        return this.name.substring(2).replace(" ", "");
    }

    public String getColoredName() {
        return this.name;
    }

    public static Medals getMedalByName(String name) {
        for (Medals medal : Medals.values()) {
            if (medal.getName().toLowerCase().contains(name.toLowerCase())) {
                return medal;
            }
        }
        return null;
    }

    public static void giveMedaltoPlayer(Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set medal.");
    }

}


// ⚔