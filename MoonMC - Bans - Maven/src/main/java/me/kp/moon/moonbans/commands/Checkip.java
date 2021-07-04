package me.kp.moon.moonbans.commands;

import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;
import me.kp.moon.moonbans.enums.Messages;
import me.kp.moon.moonbans.mysql.MySQL;
import me.kp.moon.moonbans.utils.SysUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Checkip implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("checkip")) {
            if (!sender.hasPermission("command.checkip")) {
                sender.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(label + " <player/ip>"));
                return true;
            }
            for (String whitelist : SysUtils.whitelist) {
                if (whitelist.equalsIgnoreCase(args[0].trim())) {
                    sender.sendMessage("§cVocê não pode consultar dados desse IP.");
                    return true;
                }
            }
            String ip;
            if (!args[0].contains(".")) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) ip = MySQL.getIP(args[0], true);
                else ip = target.getAddress().getHostName();
            } else {
                ip = args[0];
            }
            if (ip == null) {
                sender.sendMessage("§cFalha ao pegar dados desse IP.");
                return true;
            }
            try {
                IPResponse response = SysUtils.ipInfo.lookupIP(ip);
                sender.sendMessage("§aInformações sobre o IP §e" + ip + "§a:\n" +
                        "§fDNS: §7" + response.getHostname() + "\n" +
                        "§fProvedor: §7" + response.getOrg() + "\n" +
                        "§fCidade: §7" + response.getCity() + "\n" +
                        "§fEstado: §7" + response.getRegion() + "\n" +
                        "");
                return false;
            } catch (RateLimitedException e) {
                sender.sendMessage("§cAPI em rate limit. Aguarde alguns instantes para tentar novamente.");
                return true;
            }
        }
        return false;
    }

}
