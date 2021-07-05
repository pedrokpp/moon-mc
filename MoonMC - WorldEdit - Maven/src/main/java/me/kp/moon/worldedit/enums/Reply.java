package me.kp.moon.worldedit.enums;

import lombok.Getter;

@Getter
public enum Reply {

    APENAS_PLAYERS("§cApenas players podem executar esse comando."),
    SEM_PERMISSAO("§cVocê não tem permissão para utilizar esse comando."),
    ARGUMENTOS_INSUFICIENTES("§cArgumentos insuficientes. §7/%usage%"),
    ;

    private final String reply;
    Reply(String reply) {
        this.reply = reply;
    }

    public String getUsage(String usage) {
        return this.reply.replace("%usage%", usage);
    }

}
