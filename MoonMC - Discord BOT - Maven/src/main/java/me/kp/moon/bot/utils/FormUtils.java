package me.kp.moon.bot.utils;

import me.kp.moon.bot.Main;
import me.kp.moon.bot.enums.GlobalVariables;
import me.kp.moon.bot.eventos.form.FormQuestions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FormUtils {

    public static HashMap<String, List<String>> formHashMap = new HashMap<>();

    public static boolean hasThread(String id) {
        return formHashMap.containsKey(id);
    }

    public static void addID(String id) {
        formHashMap.put(id, new ArrayList<>());
    }

    public static void removeID(String id) {
        formHashMap.remove(id);
    }

    public static int getQuestionNumber(String id) {
        if (formHashMap.containsKey(id))
            return formHashMap.get(id).size();
        return -1;
    }

    public static void sendQuestionEmbed(MessageChannel channel, String id) {
        int num = getQuestionNumber(id);
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("🚀 QUESTÃO " + (num + 1))
                .addField(FormQuestions.getQuestionText(num), "``` ```", true)
                .setTimestamp(OffsetDateTime.now())
                .setFooter("MoonMC - Sistema de Formulários", "https://cdn.discordapp.com/icons/753798848903905350/a_bdb67f83e73a1634305aee789bf71c80.gif")
                .setColor(GlobalVariables.mainColor);
        channel.sendMessage(embedBuilder.build()).queue(null, err -> removeID(id));
    }

    public static void addAnswer(String id, String answer) {
        if (formHashMap.containsKey(id)) {
            List<String> clone = formHashMap.get(id);
            clone.add(answer);
            formHashMap.replace(id, clone);
        }
    }

    public static void validateForm(User user) {
        MessageChannel channel = Main.jda.getTextChannelById(Config.formLandChannelID);
        String id = user.getId();
        if (!formHashMap.containsKey(id)) return;
        if (channel == null) {
            System.out.println("channel == null (validateForm)");
            return;
        }
        List<String> respostas = formHashMap.get(id);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("🚀 MoonMC - Sistema de Formulários");
        embedBuilder.setDescription("Formulário recebido de ``" + user.getAsTag() + "``");
        embedBuilder.setColor(GlobalVariables.mainColor);
        embedBuilder.setTimestamp(OffsetDateTime.now());
        embedBuilder.setFooter(user.getAsTag());
        int counter = 0;
        for (String resposta : respostas) {
            embedBuilder.addField(FormQuestions.getQuestionText(counter), "``" + resposta + "``", false);
            counter++;
        }
        channel.sendMessage(embedBuilder.build()).queue(msg -> {
            msg.addReaction("✅").queue();
            msg.addReaction("❌").queue();
        });
    }

}
