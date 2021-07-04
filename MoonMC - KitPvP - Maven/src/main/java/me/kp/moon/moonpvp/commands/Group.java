package me.kp.moon.moonpvp.commands;

import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.enums.PlayerGroup;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Group implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("group") || command.getName().equalsIgnoreCase("setgroup")) {
            if (!player.hasPermission("command.setgroup")) {
                player.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }
            if (args.length < 2) {
                player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(command.getName() + " <player> <grupo>"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("§cNão foi possível encontrar o player §e" + args[0] + "§c.");
                return true;
            }
            if (target == player) {
                player.sendMessage("§cVocê não pode alterar o seu próprio grupo.");
                return true;
            }
            PlayerGroup group = PlayerGroup.getByName(args[1]);
            if (group == null) {
                player.sendMessage("§cNão foi possível encontrar o cargo §e" + args[1] + "§c.");
                return true;
            }
            if (!player.hasPermission(group.getPermission())) {
                player.sendMessage("§cVocê não tem permissão para setar esse cargo.");
                return true;
            }
            player.sendMessage("§aVocê definiu o cargo " + group.getColoredName() + " §apara " + group.getColor() + target.getName() + "§a.");
            target.sendMessage("§aVocê recebeu o cargo " + group.getColoredName() + " §ade " + group.getColor() + player.getName() + "§a.");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + target.getName() + " parent set " +
                    args[1].replace("+", "plus").replace("membro", "default"));
            TextComponent textComponent = new TextComponent("§7§o(STAFF) O player §f" + player.getName() +
                    " §7§oatualizou o cargo de §f" + target.getName() + "§7§o.");
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("" +
                    "§f§oDe: " + group.getColoredName() +
                    "\n§f§oPara: " + group.getColoredName()).create()));
            Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("command.staffchat")).forEach(staffer -> {
                staffer.sendMessage(textComponent);
            });
        }
        return false;
    }
}
