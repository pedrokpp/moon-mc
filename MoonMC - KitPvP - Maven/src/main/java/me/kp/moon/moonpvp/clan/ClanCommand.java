package me.kp.moon.moonpvp.clan;

import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.utils.SysUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// TODO FINALIZAR O COMANDO DE CLAN!@#!#!@##

public class ClanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("clan")) {
            if (!SysUtils.clans && !args[0].equalsIgnoreCase("toggle") && !player.hasPermission("command.clan.manage")) {
                player.sendMessage("§cO sistema de clans está temporáriamente desabilitado.");
                return true;
            }
            if (args.length == 0) {
                String newArgs = Messages.ARGUMENTOS_INSUFICIENTES.getMessage().replace("§7/%usage%", "");
                player.sendMessage(newArgs +
                        "\n§7» /clan criar <nome> <tag>" +
                        "\n§7» /clan convidar <player>" +
                        "\n§7» /clan expulsar <player>" +
                        "\n§7» /clan expulsar <player>" +
                        "\n");
            }
        }
        return false;
    }
}
