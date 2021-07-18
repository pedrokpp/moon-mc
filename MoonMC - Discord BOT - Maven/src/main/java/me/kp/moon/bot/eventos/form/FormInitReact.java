package me.kp.moon.bot.eventos.form;

import me.kp.moon.bot.enums.GlobalVariables;
import me.kp.moon.bot.utils.Config;
import me.kp.moon.bot.utils.FormUtils;
import me.kp.moon.bot.utils.TicketUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class FormInitReact extends ListenerAdapter {

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (!event.getChannel().getId().equals(Config.formChannelID)) return;
        if (!event.getReaction().getReactionEmote().isEmoji()) return;

        if (event.getReaction().getReactionEmote().getEmoji().equals("📝")) { // formulário de trial
            event.getReaction().removeReaction(event.getUser()).queue();
            if (FormUtils.hasThread(event.getUserId())) {
                event.getUser().openPrivateChannel().queue(dm ->
                        dm.sendMessage("Você já possui um formulário em aberto. Por favor aguarde o encerramento do atual.").queue(message ->
                                        message.delete().queueAfter(3, TimeUnit.SECONDS,
                                                error -> event.getChannel().sendMessage("<@" + event.getUserId() + "> você já possui um formulário em aberto.").queue(
                                                        msg -> msg.delete().queueAfter(3, TimeUnit.SECONDS))),
                                error -> event.getChannel().sendMessage("<@" + event.getUserId() + "> você já possui um formulário em aberto.").queue(
                                        message -> message.delete().queueAfter(3, TimeUnit.SECONDS)
                                )));
                return;
            }
            event.getUser().openPrivateChannel().queue(dm -> dm.sendMessage(new EmbedBuilder()
                            .setTitle("🚀 MoonMC - Sistema de Formulário")
                            .setDescription("Seja bem vindo ao sistema de formulários do MoonMC.")
                            .addField("Para iniciar:", "Digite ``/iniciar`` para começar o formulário para **TRIAL**", true)
                            .addField("Para cancelar:", "Digite ``/cancelar`` para parar a qualquer momento o formulário", true)
                            .setColor(GlobalVariables.mainColor)
                            .setFooter(GlobalVariables.footer)
                            .build()).queue(),
                            error -> event.getChannel().sendMessage(event.getUser().getAsMention() + " você precisa habilitar suas mensagens privadas para isso.").queue(
                                    message -> message.delete().queueAfter(3, TimeUnit.SECONDS)
                            ));
        }

    }
}
