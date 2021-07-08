package me.kp.moon.moonpvp.commands.staff;

import me.kp.moon.moonpvp.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Gamemode implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("gamemode") || command.getName().equalsIgnoreCase("gm")) {
            if (!player.hasPermission("command.gamemode")) {
                player.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }
            if (args.length == 0) {
                player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(label + " <modo> [player]"));
                return true;
            }
            if (args.length == 1) {
                switch (args[0]) {
                    case "0":
                        player.sendMessage("§aVocê alterou seu modo de jogo para §eSURVIVAL§a.");
                        player.setGameMode(GameMode.SURVIVAL);
                        break;
                    case "1":
                        player.sendMessage("§aVocê alterou seu modo de jogo para §eCREATIVE§a.");
                        player.setGameMode(GameMode.CREATIVE);
                        break;
                    case "2":
                        player.sendMessage("§aVocê alterou seu modo de jogo para §eADVENTURE§a.");
                        player.setGameMode(GameMode.ADVENTURE);
                        break;
                    case "3":
                        player.sendMessage("§aVocê alterou seu modo de jogo para §eSPECTATOR§a.");
                        player.setGameMode(GameMode.SPECTATOR);
                        break;
                    default:
                        player.sendMessage("§cNão foi possível encontrar o modo \"§e" + args[0] + "§c\".");
                        break;
                }
                return false;
            }
            if (args.length == 2) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    player.sendMessage("§cNão foi possível encontrar o player §e" + args[1] + "§c.");
                    return true;
                }
                switch (args[0]) {
                    case "0":
                        player.sendMessage("§aVocê alterou o modo de jogo de §e" + target.getName() + " §apara §eSURVIVAL§a.");
                        target.sendMessage("§aSeu modo de jogo foi alterado para para §eSURVIVAL§a.");
                        target.setGameMode(GameMode.SURVIVAL);
                        break;
                    case "1":
                        player.sendMessage("§aVocê alterou o modo de jogo de §e" + target.getName() + " §apara §eCREATIVE§a.");
                        target.sendMessage("§aSeu modo de jogo foi alterado para para §eCREATIVE§a.");
                        target.setGameMode(GameMode.CREATIVE);
                        break;
                    case "2":
                        player.sendMessage("§aVocê alterou o modo de jogo de §e" + target.getName() + " §apara §eADVENTURE§a.");
                        target.sendMessage("§aSeu modo de jogo foi alterado para para §eADVENTURE§a.");
                        target.setGameMode(GameMode.ADVENTURE);
                        break;
                    case "3":
                        player.sendMessage("§aVocê alterou o modo de jogo de §e" + target.getName() + " §apara §eSPECTATOR§a.");
                        target.sendMessage("§aSeu modo de jogo foi alterado para para §eSPECTATOR§a.");
                        target.setGameMode(GameMode.SPECTATOR);
                        break;
                    default:
                        player.sendMessage("§cNão foi possível encontrar o modo \"§e" + args[0] + "§c\".");
                        break;
                }
                return false;
            }
        }
        return false;
    }
}
