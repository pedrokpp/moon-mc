package me.kp.moon.bot.comandos;

import me.kp.moon.bot.Main;
import me.kp.moon.bot.utils.Config;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class PingarTicket extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getMember() == null) return;
        Role staffRole = Main.jda.getRoleById(Config.roleStaffID);
        Role pingRole = Main.jda.getRoleById(Config.pingRoleID);
        if (!event.getMember().getRoles().contains(staffRole)) return;
        if (pingRole == null) {
            System.out.println("!! pingRole == null !!");
            return;
        }
        if (event.getMessage().getContentRaw().toLowerCase().contains("me pinga no ticket")) {
            if (event.getMember().getRoles().contains(pingRole))
                event.getMessage().reply("Você já será pingado quando algum ticket for criado.").queue();
            else {
                event.getMessage().reply("Pronto. Agora você será notificado quando algum ticket for criado.").queue();
                event.getGuild().addRoleToMember(event.getMember(), pingRole).queue();
            }
        }
    }
}
