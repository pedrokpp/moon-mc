package me.kp.moon.moonpvp.commands;

import me.kp.moon.moonpvp.enums.Messages;
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

public class ActionBar implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player)sender;
            if (player.hasPermission("command.actionbar")) {
                if (args.length > 1) {
                    if (args[0].equalsIgnoreCase("all")) {
                        final String message = ChatColor.translateAlternateColorCodes('&', StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " "));
                        Bukkit.getOnlinePlayers().forEach(players -> SysUtils.sendActionBar(players, message));
                        player.sendMessage("§aMensagem para §eTODOS §aenviada.");
                        TextComponent textComponent = new TextComponent("§7§o(STAFF) Aviso enviado por §f" + player.getName() + "§7§o.");
                        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("" +
                                "§4tu é muito curioso kkkkkk #zoado !!@#!@").create()));
                        Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("command.staffchat")).forEach(staffer -> {
                            staffer.sendMessage(textComponent);
                        });
                    }
                    else {
                        final Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {
                            final String message2 = ChatColor.translateAlternateColorCodes('&', StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " "));
                            SysUtils.sendActionBar(target, message2);
                            player.sendMessage("§aMensagem para §e" + target.getName() + " §aenviada com sucesso!");
                        }
                        else {
                            player.sendMessage("§cNão foi possível encontrar o player " + args[0] + "§c.");
                        }
                    }
                }
                else {
                    player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(label + " <all/player> <mensagem>"));
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
