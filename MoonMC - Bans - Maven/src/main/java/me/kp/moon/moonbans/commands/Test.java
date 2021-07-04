package me.kp.moon.moonbans.commands;

import me.kp.moon.moonbans.utils.SysUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Test implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cnao pode nao otario");
            return true;
        }
        Player player = (Player) sender;
        long ms = System.currentTimeMillis();
        if (command.getName().equalsIgnoreCase("testt")) {
            if (!player.getName().equalsIgnoreCase("pedrokp")) {
                player.sendMessage("§cChora bb.");
                return true;
            }
            String tempo = args[1].toLowerCase();
            if (!(tempo.endsWith("d") || tempo.endsWith("h") || tempo.endsWith("m") || tempo.endsWith("s"))) {
                sender.sendMessage("§cFormato de tempo inválido. §7(Disponíveis: d, h, m, s)");
                return true;
            }
            try {
                if (tempo.endsWith("d")) ms = SysUtils.addDaysToMs(Integer.parseInt(tempo.replace("d", "")), ms);
                if (tempo.endsWith("h")) ms = SysUtils.addHoursToMs(Integer.parseInt(tempo.replace("h", "")), ms);
                if (tempo.endsWith("m")) ms = SysUtils.addMinutesToMs(Integer.parseInt(tempo.replace("m", "")), ms);
                if (tempo.endsWith("s")) ms = SysUtils.addSecondsToMs(Integer.parseInt(tempo.replace("s", "")), ms);
            } catch (NumberFormatException exception) {
                sender.sendMessage("§cFalha ao converter formato de tempo. §7(tenha certeza de ter digitado certo)");
                return true;
            }
            player.sendMessage(ms + "");
            player.sendMessage(SysUtils.timeConverter(ms));
            player.sendMessage(SysUtils.timeConverter(System.currentTimeMillis()));
        }
        return false;
    }
}
