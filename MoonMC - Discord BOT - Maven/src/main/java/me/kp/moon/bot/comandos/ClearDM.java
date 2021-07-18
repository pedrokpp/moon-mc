package me.kp.moon.bot.comandos;

import me.kp.moon.bot.utils.BotUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClearDM extends ListenerAdapter {

    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().startsWith(".clear-dm")) {
            if (BotUtils.clearingDM) {
                event.getMessage().reply("Essa ação já está sendo executada por outro usuário. Favor aguardar.").queue();
                return;
            }
            BotUtils.clearingDM = true;
            MessageChannel channel = event.getChannel();
            List<Message> messages = channel.getHistory().retrievePast(50).complete();
            OffsetDateTime twoWeeksAgo = OffsetDateTime.now().minus(2, ChronoUnit.WEEKS);

            messages.forEach(msg -> {
                if (msg.getAuthor().isBot() && !msg.getTimeCreated().isBefore(twoWeeksAgo)) {
                    msg.delete().queue();
                }
            });
            channel.sendMessage("Mensagens limpas. Caso haja mais mensagens, execute o comando novamente.").queue(
                    msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS)
            );
            BotUtils.clearingDM = false;
        }
    }

}
