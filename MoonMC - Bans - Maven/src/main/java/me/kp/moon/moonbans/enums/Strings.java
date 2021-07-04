package me.kp.moon.moonbans.enums;

import me.kp.moon.moonbans.utils.SysUtils;

public class Strings {

    public static String getDiscord() {
        return "https://discord.gg/A8wwkTuesh";
    }

    public static String getName() {
        return "§9§lMoon§1§lMC";
    }

    public static String getPermaBanMessage() {
        return "§cVocê foi banido por violar as diretrizes da nossa comunidade.\n\n§fSe achou injusto, " +
                "sinta-se livre para solicitar uma appeal em §9" + Strings.getDiscord() + "\n\n" +
                "§eAtenciosamente, equipe do " + Strings.getName() + "§e.";
    }

    public static String getTempBanMessage(long ms) {
        return "§cVocê foi banido temporariamente por violar as diretrizes da nossa comunidade.\n\n§fSe achou injusto, " +
                "sinta-se livre para solicitar uma appeal em §9" + Strings.getDiscord() + "\n\n" +
                "§fData do fim do banimento: §c" + SysUtils.timeConverter(ms) +
                "\n\n§eAtenciosamente, equipe do " + Strings.getName() + "§e.";
    }

    public static String getKickMessage(String reason) {
        return "§cVocê foi kickado do servidor.\n\n" +
                "§eMotivo: §f" + reason +
                "\n\n§eAtenciosamente, equipe do " + Strings.getName() + "§e.";
    }

}
