package me.kp.moon.bot.eventos;

import me.kp.moon.bot.Main;
import me.kp.moon.bot.enums.GlobalVariables;
import me.kp.moon.bot.utils.BotUtils;
import me.kp.moon.bot.utils.Config;
import me.kp.moon.bot.utils.ThreadUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TicketReact extends ListenerAdapter {

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (!(event.getChannel().getId().equalsIgnoreCase(Config.ticketChannelID) || event.getChannel().getName().contains("ticket-")))
            return;
        if (event.getUser().isBot()) return;
        if (!event.getReaction().getReactionEmote().isEmoji()) return;

        if (event.getReaction().getReactionEmote().getEmoji().equals("📩")) {
            event.getReaction().removeReaction(event.getUser()).queue();
            if (ThreadUtils.hasThread(event.getUserId())) {
                event.getUser().openPrivateChannel().queue(dm ->
                        dm.sendMessage("Você já possui um ticket em aberto. Por favor aguarde o encerramento do atual.").queue(message ->
                                        message.delete().queueAfter(3, TimeUnit.SECONDS,
                                                error -> event.getChannel().sendMessage("<@" + event.getUserId() + "> você já possui um ticket em aberto.").queue(
                                                        msg -> msg.delete().queueAfter(3, TimeUnit.SECONDS))),
                                error -> event.getChannel().sendMessage("<@" + event.getUserId() + "> você já possui um ticket em aberto.").queue(
                                        message -> message.delete().queueAfter(3, TimeUnit.SECONDS)
                                )));
                return;
            }
            int rand = BotUtils.getRandomInt(1111, 9999);
            ThreadUtils.addID(event.getUserId());
            event.getGuild().createTextChannel("ticket-" + rand, Main.jda.getCategoryById(Config.ticketCategoryID))
                    .setTopic(event.getUserId())
                    .addRolePermissionOverride(event.getGuild().getIdLong(), null, Collections.singleton(Permission.VIEW_CHANNEL))
                    .addMemberPermissionOverride(event.getUserIdLong(), Collections.singleton(Permission.VIEW_CHANNEL), null)
                    .addRolePermissionOverride(Long.parseLong(Config.roleStaffID), Collections.singleton(Permission.VIEW_CHANNEL), null)
                    .queue(textChannel -> {
                        textChannel.sendMessage("<@" + event.getUserId() + ">").queue(msg -> msg.delete().queue());
                        textChannel.sendMessage("<@&" + Config.pingRoleID + ">").queue(msg -> msg.delete().queue());
                        textChannel.sendMessage(new EmbedBuilder()
                                .setAuthor("Olá, " + event.getUser().getName() + "! Bem vindo ao seu ticket.", null, event.getUser().getAvatarUrl())
                                .setDescription("Sinta-se livre para nos adiantar dizendo o que precisa. Em breve nossa staff te responderá.")
                                .setColor(GlobalVariables.mainColor)
                                .setFooter(GlobalVariables.footer)
                                .build()).queue(message -> message.addReaction("🔒").queue());
                    });
        }

        if (event.getReaction().getReactionEmote().getEmoji().equals("🔒")) {
            if (!event.getChannel().getName().contains("ticket-")) return;
            event.getChannel().sendMessage(new EmbedBuilder()
                    .setAuthor("Ticket fechado por " + event.getUser().getName() + ".", null, event.getUser().getAvatarUrl())
                    .setDescription("**Esse canal será deletado em 5 segundos.**")
                    .addField("Avalie quem te atendeu", "Sinta-se à vontade para avaliar seu atendimento em " +
                            "<#831215785124036668>", false)
                    .setColor(GlobalVariables.mainColor)
                    .setFooter(GlobalVariables.footer)
                    .build()).queue();
            ThreadUtils.removeID(event.getChannel().getTopic());
            event.getChannel().delete().queueAfter(6, TimeUnit.SECONDS);
            String topic = event.getChannel().getTopic() != null ? event.getChannel().getTopic() : "";
            User user = topic == null ? null : Main.jda.getUserById(topic);
            String userName = user != null ? user.getName() : "?";
            BotUtils.sendLog("``" + event.getChannel().getName() + "`` criado por ``" + userName +
                    "`` foi fechado por ``" + event.getUser().getName() + "``");
        }

    }

}
