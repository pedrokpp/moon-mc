package me.kp.moon.moonlogin.commands;

import me.kp.moon.moonlogin.auth.AuthAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Base64;
import java.util.Random;

public class LoginTeste implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("loginteste")) {
            if (!sender.getName().equalsIgnoreCase("pedrokp")) {
                sender.sendMessage("§cComando inexistente.");
                return true;
            }
            if (args.length == 0) {
                return true;
            }
            sender.sendMessage("original input: " + args[0]);
            int rand = AuthAPI.genDiscriminator();
            String rando = AuthAPI.encodeString(args[0], rand);
            sender.sendMessage("encoded rand " + rand + ": " + rando);
            sender.sendMessage("size: " + rando.length());
        }
        return false;
    }
}
//        String originalInput = "test input";
//        String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
