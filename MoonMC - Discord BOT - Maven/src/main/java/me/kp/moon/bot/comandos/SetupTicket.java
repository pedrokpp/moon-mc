package me.kp.moon.bot.comandos;

import me.kp.moon.bot.Main;
import me.kp.moon.bot.enums.GlobalVariables;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SetupTicket extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.PRIVATE)) return;
        if (event.getMember() == null) return;
        if (event.getAuthor().isBot()) return;
        String[] message = event.getMessage().getContentRaw().split(" ");
        String command = message[0].substring(1);
        if (command.equalsIgnoreCase("setup-ticket")) {
            if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                event.getMessage().reply("Você não tem permissão para isso.").queue();
                return;
            }
            if (event.getMessage().getMentionedChannels().isEmpty()) {
                event.getMessage().reply("Por favor, mencione um canal.").queue();
                return;
            }
            MessageChannel channel = event.getMessage().getMentionedChannels().get(0);
            try {
                channel.sendMessage(new EmbedBuilder()
                        .setAuthor("🚀 MoonMC - Sistema de Tickets", null, Main.jda.getGuilds().get(0).getIconUrl())
                        .setDescription("Reaja essa mensagem para abrir um ticket.")
                        .setFooter(GlobalVariables.footer)
                        .setColor(GlobalVariables.mainColor)
                        .build()).queue();
                new Thread(() -> {
                    try {
                        Thread.sleep(2 * 1000);
                        channel.addReactionById(channel.getLatestMessageId(), "📩").queue();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } catch (Exception ignore) {
                event.getMessage().reply("Não foi possível enviar uma mensagem ao canal mencionado.").queue();
            }
        }
    }
}
