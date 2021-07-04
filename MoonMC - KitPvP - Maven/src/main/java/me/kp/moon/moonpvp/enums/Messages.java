package me.kp.moon.moonpvp.enums;

public enum Messages {

    ARGUMENTOS_INSUFICIENTES("§cArgumentos insuficientes. §7/%usage%"),
    NAO_PODE_COM_KIT("§cVocê não pode fazer isso com um kit."),
    APENAS_NUMEROS_ARGS("§cApenas números podem ser usados como argumentos para este comando."),
    SEM_PERMISSAO("§cVocê não tem permissão para utilizar esse comando."),
    KIT_COOLDOWN("§cO seu kit está em cooldown."),
    APENAS_PLAYERS("§cApenas players podem executar esse comando.");

    private final String message;
    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getUsage(String usage) {
        return message.replace("%usage%", usage);
    }
}
