package me.kp.moon.moonpvp.enums;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Strings {

    private static final List<String> motds = Arrays.asList("§fConfira nossa §6SEGUNDA SEASON§f!", "§fVeja as atualizações no §9Discord§f!", "§fCheque o novo kit §6Gladiator§f!", "§fNovo sistema de §6ranks §fe §6medalhas§f!");

    private static String getMotdMessage(String motd) {
        float y = (float) motd.length();
        for (char c : motd.toCharArray()) {
            if (c == '§') y -= 2.5;
        }
        int ac = (int) ((60 - y) / 2);
        StringBuilder space = new StringBuilder();
        for (int i = 0; i < ac; i++) {
            space.append(" ");
        }
        return space + motd;
    }

    public static String getDiscord() { return "https://discord.gg/A8wwkTuesh"; }

    public static String getName() {
        return "§9§lMoon§1§lMC";
    }

    public static String getPrefix() {
        return (getName()) + " §7»";
    }

    public static ArrayList<String> getMessages() {
        final ArrayList<String> motds = new ArrayList<>();
        motds.add((getPrefix()) + " §fQuer combater §chackers§f conosco? §eSe aplique para a nossa equipe! Utilize §9/discord§e.");
        motds.add((getPrefix()) + " §fHá dúvidas ou sugestões? §eUtilize nossos chats do §9/discord§e.");
        motds.add((getPrefix()) + " §aVIP's §fpelos menores preços e com vantagens distribuídas! §eVá ao nosso §9/discord§e.");
        motds.add((getPrefix()) + " §fFique ligado em nossas atualizações.");
        motds.add((getPrefix()) + " §fEntre em nosso §9/discord§f!");
        motds.add((getPrefix()) + " §fHá um player te perturbando? §eConsidere bloqueá-lo usando §c/block§f!");
        motds.add((getPrefix()) + " §fLembre-se de seguir as §cregras§f!");
        return motds;
    }

    public static String getMotd() {
        if (Bukkit.getServer().hasWhitelist()) {
            return getMotdMessage(getName() + "§f » §7[1.7 - 1.8]") + "\n" + getMotdMessage("§cO servidor encontra-se em manutenção.");
        }
        return getMotdMessage(getName() + "§f » §7[1.7 - 1.8]") + "\n" + getMotdMessage(motds.get(new Random().nextInt(motds.size())));
    }

}
