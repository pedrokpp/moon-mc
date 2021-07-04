package me.kp.moon.moondiscord.bot.comandos;

import me.kp.moon.moondiscord.bot.utils.BotUtils;
import me.kp.moon.moondiscord.plugin.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Console extends ListenerAdapter {

    //TODO arrumar o comando (nao está funcionando)

    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        String args = e.getMessage().getContentRaw();
        MessageChannel channel = e.getChannel();

        if (e.getMember() == null) { return; }

        if (!e.getGuild().getId().equals(BotUtils.ID_SERVER_STAFF)) { return; }

        if (args.startsWith(BotUtils.PREFIX + "console")) {
            if (!e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                e.getMessage().delete().queue();
            }
            else {
                String command = args.replace(BotUtils.PREFIX + "console", "");
                if (command.equals("")) {
                    channel.sendMessage("Você precisa especificar que comando executar!").queue();
                } else {
                    command = args.replace(BotUtils.PREFIX + "console ", "");
                    if (Bukkit.isPrimaryThread()) {
                        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
                        channel.sendMessage("Comando executado! O output deste comando foi: `` ``").queue();
                    }
                    else {
                        Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), new Runnable() {
                            @Override
                            public void run() {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                PrintStream ps = new PrintStream(baos);
                                PrintStream old = System.out;
                                System.setOut(ps);
                                System.out.flush();
                                System.setOut(old);
                                String output = baos.toString();

                                EmbedBuilder embed = new EmbedBuilder()
                                        .setAuthor("Comando executado", null, e.getMember().getUser().getEffectiveAvatarUrl())
                                        .setDescription("Output: \n\n> ``" + output + "``")
                                        .setColor(BotUtils.MAIN_COLOR);
                                channel.sendMessage(embed.build()).queue();
                            }
                        });
                    }
                }
            }
        }

    }

}
