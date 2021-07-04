package me.kp.moon.moonbans.enums;

import lombok.Getter;

@Getter
public enum Messages {

    SEM_PERMISSAO("§cVocê não tem permissão para utilizar esse comando."),
    ARGUMENTOS_INSUFICIENTES("§cArgumentos insuficientes. §7/%usage%")
    ;

    private final String message;
    Messages(String message) {
        this.message = message;
    }

    public String getUsage(String usage) {
        return this.getMessage().replace("%usage%", usage);
    }
}
