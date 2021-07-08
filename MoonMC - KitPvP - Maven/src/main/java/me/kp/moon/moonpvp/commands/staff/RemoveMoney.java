package me.kp.moon.moonpvp.commands.staff;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveMoney implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("removemoney")) {
            if (!player.hasPermission("command.removemoney")) {
                player.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }
            if (args.length < 2) {
                player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(command.getName() + " <player> <quantia>"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("§cNão foi posssível encontrar o player §e" + args[0] + "§c.");
                return true;
            }
            PlayerData targetData = PlayerDataManager.getPlayerData(target);
            if (targetData == null) return true;
            int quantia;
            try {
                quantia = Integer.parseInt(args[1]);
            } catch (NumberFormatException exception) {
                player.sendMessage(Messages.APENAS_NUMEROS_ARGS.getMessage());
                return true;
            }
            int actualCoins = targetData.cacheCoins;
            int quantiaFinal = Math.max(actualCoins - quantia, 0);
            targetData.setCacheCoins(quantiaFinal);
            player.sendMessage("§cVocê removeu §7$" + quantiaFinal + " §cdo player §e" + target.getName() + "§c.");
            TextComponent textComponent = new TextComponent("§7§o(STAFF) O player §f" + player.getName() +
                    " §7§oretirou coins de §f" + target.getName() + "§7§o.");
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("" +
                    "§f§oDe: §c§m§o$" + actualCoins +
                    "\n§f§oPara: §c§o$" + quantiaFinal).create()));
            Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("command.staffchat")).forEach(staffer -> {
                staffer.sendMessage(textComponent);
            });
        }
        return false;
    }
}
