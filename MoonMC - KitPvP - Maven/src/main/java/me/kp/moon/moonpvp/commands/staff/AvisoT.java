package me.kp.moon.moonpvp.commands.staff;

import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.enums.Strings;
import me.kp.moon.moonpvp.utils.SysUtils;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class AvisoT implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player)sender;
            if (player.hasPermission("command.avisot")) {
                if (args.length > 0) {
                    final String message = ChatColor.translateAlternateColorCodes('&', StringUtils.join(Arrays.copyOfRange(args, 0, args.length), " ")).replace("{discord}", Strings.getDiscord());
                    Bukkit.getOnlinePlayers().forEach(players -> {
                        SysUtils.sendTitle(players, " ", message);
                    });
                    TextComponent textComponent = new TextComponent("§7§o(STAFF) Aviso enviado por §f" + player.getName() + "§7§o.");
                    textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("" +
                            "§5Parabéns, você achou um easter egg, r$").create()));
                    Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("command.staffchat")).forEach(staffer -> {
                        staffer.sendMessage(textComponent);
                    });
                }
                else {
                    player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(label + " <mensagem>"));
                }
            }
            else {
                player.sendMessage(Messages.SEM_PERMISSAO.getMessage());
            }
        }
        else {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
        }
        return true;
    }

}
