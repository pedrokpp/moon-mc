package me.kp.moon.moondiscord.plugin.comandos;

import me.kp.moon.moondiscord.mysql.MySQL;
import me.kp.moon.moondiscord.system.SysUtils;
import me.kp.moon.moondiscord.plugin.enums.DefaultMessages;
import me.kp.moon.moondiscord.plugin.utils.PlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Sync implements CommandExecutor {

    public MySQL mySQL = new MySQL();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(DefaultMessages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("command.staffchat")) {
            player.sendMessage(DefaultMessages.SEM_PERMISSAO.getMessage());
            return true;
        }

        if (mySQL.uuidExiste(player.getUniqueId().toString())) {
            player.sendMessage("§9[MoonDiscord] §cVocê já vinculou essa conta a um discord. §7(caso isso seja um erro, " +
                    "contate kp#3343)");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§9[MoonDiscord] §cArgumentos insuficientes. §7(/sync <key>)");
            return true;
        }
        String discordID = SysUtils.keys.get(args[0]);
        if (discordID == null) {
            player.sendMessage("§9[MoonDiscord] §cA key mencionada é inválida. §7(revise a key mencionada)");
            return true;
        }
        
        mySQL.createUser(player.getUniqueId().toString(), player.getName(), discordID, PlayerUtils.getPlayerTag(player));
        player.sendMessage(mySQL.uuidExiste(player.getUniqueId().toString()) ? "§9[MoonDiscord] §aSua conta foi " +
                "vinculada ao Discord de ID §e" + discordID : "§9[MoonDiscord] §cOcorreu um erro ao vincular sua conta.");
        SysUtils.keys.remove(args[0]);


        return false;
    }
}
