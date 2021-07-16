package me.kp.moon.lobby.enums;

import lombok.Getter;
import net.md_5.bungee.api.chat.TextComponent;

@Getter
public enum Reply {
    APENAS_PLAYERS("§cApenas players podem executar esse comando.")
    ;

    private final TextComponent textComponent;
    Reply(String text) {
        this.textComponent = new TextComponent(text);
    }
}
