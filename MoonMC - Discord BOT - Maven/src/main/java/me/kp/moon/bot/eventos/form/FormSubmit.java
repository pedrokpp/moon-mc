package me.kp.moon.bot.eventos.form;

import me.kp.moon.bot.utils.FormUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class FormSubmit extends ListenerAdapter {

    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        String content = event.getMessage().getContentRaw();
        MessageChannel channel = event.getMessage().getChannel();
        String userID = event.getAuthor().getId();
        if (content.startsWith("/iniciar")) {
            if (FormUtils.hasThread(event.getAuthor().getId())) {
                event.getMessage().reply("Você já iniciou seu formulário").queue(msg ->
                        msg.delete().queueAfter(5, TimeUnit.SECONDS), err -> FormUtils.removeID(userID));
            } else {
                FormUtils.addID(userID);
                channel.sendMessage(FormUtils.getQuestionNumber(userID) + " numero questao").queue(null, err -> FormUtils.removeID(userID));
            }
        }
        else if (content.startsWith("/cancelar")) {
            if (FormUtils.hasThread(userID)) {
                FormUtils.removeID(userID);
                event.getMessage().reply("Seu formulário foi cancelado. Caso deseje realizar novamente, sinta-se livre para realizar o mesmo processo.").queue();
            }
        } else {
            if (!FormUtils.hasThread(userID)) return;
            FormUtils.addAnswer(userID, content);
            if (FormUtils.getQuestionNumber(userID) == 5) {
                FormUtils.validateForm(event.getAuthor());
                FormUtils.removeID(userID);
                channel.sendMessage("Você concluiu o formulário e suas respostas foram enviadas à nossa equipe.").queue(null, null);
                return;
            }
            channel.sendMessage(FormUtils.getQuestionNumber(userID) + " numero questao").queue(null, err -> FormUtils.removeID(userID));
        }
    }
}
