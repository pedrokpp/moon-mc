package me.kp.moon.bot.eventos.form;

import me.kp.moon.bot.Main;
import me.kp.moon.bot.utils.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.OffsetDateTime;

public class FormReview extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if (event.getUser() == null) return;
        if (event.getUser().isBot()) return;
        if (!event.getReaction().getReactionEmote().isEmoji()) return;
        if (!event.getReaction().getChannel().getId().equalsIgnoreCase(Config.formLandChannelID)) return;

        String emoji = event.getReaction().getReactionEmote().getEmoji();

        if (emoji.equals("✅") || emoji.equals("❌")) {
            event.retrieveMessage().queue(msg -> {
                if (msg.getEmbeds().isEmpty()) return;
                MessageEmbed.Footer footer = msg.getEmbeds().get(0).getFooter();
                if (footer == null) return;
                String targetID = footer.getText();
                if (targetID == null) return;
                boolean aprovado = emoji.equals("✅");
                Color color = aprovado ? Color.GREEN : Color.RED;
                String state = aprovado ? "aprovado" : "reprovado";
                msg.editMessage(new EmbedBuilder()
                        .setTitle("🚀 MoonMC - Sistema de Formulários")
                        .setColor(color)
                        .setDescription("``" + targetID + "`` foi " + state + " por " + event.getUser().getAsMention())
                        .setTimestamp(OffsetDateTime.now())
                .build()).queue();
                msg.getReactions().forEach(reaction -> reaction.removeReaction().queue());
            });
        }
    }
}
