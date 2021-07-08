package me.kp.moon.moonpvp.commands.staff;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.enums.PlayerGroup;
import me.kp.moon.moonpvp.utils.PlayerUtils;
import me.kp.moon.moonpvp.utils.SysUtils;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Screenshare implements CommandExecutor {

    private static final Location ss = new Location(Bukkit.getWorlds().get(0), 757.5, 90.0, 360.5, 180.0f, 0.0f);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return true;
        if (command.getName().equalsIgnoreCase("ss")) {
            if (!player.hasPermission("command.screenshare")) {
                player.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }
            if (args.length == 0) {
                player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(label + " <player>"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("§cNão foi possível encontrar o player §e" + args[0] + "§c.");
                return true;
            }
            PlayerData targetData = PlayerDataManager.getPlayerData(target);
            if (targetData == null) return true;
            if (targetData.gladiatorLocation != null || playerData.gladiatorLocation != null) {
                player.sendMessage("§cEsse player está num duelo de gladiator.");
                return true;
            }
            if (target == player) {
                if (playerData.screenshare) {
                    player.sendMessage("§cVocê saiu da ScreenShare.");
                    playerData.setScreenshare(false);
                    PlayerUtils.sendPlayerToSpawn(player);
                } else {
                    player.sendMessage("§aVocê se puxou para a ScreenShare.");
                    playerData.setScreenshare(true);
                    player.teleport(ss);
                }
                return false;
            }
            if (!targetData.screenshare && !playerData.screenshare) {
                target.getInventory().clear();
                player.getInventory().clear();
                target.teleport(ss);
                player.teleport(ss);
                player.sendMessage("§aVocê puxou §e" + target.getName() + " §apara a sala de ScreenShare.");
                target.sendMessage("§cVocê foi puxado para a sala da ScreenShare. Siga as instruções do staffer.");
                SysUtils.sendTitle(target, "", "§cVocê foi puxado para ScreenShare");
                targetData.setScreenshare(true);
                playerData.setScreenshare(true);
                TextComponent textComponent = new TextComponent("§7§o(STAFF) O player §f" + target.getName() + " §7§ofoi puxado para ScreenShare.");
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("" +
                        "§fPor: " + PlayerGroup.getPlayerNameWithGroup(player)).create()));
                Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("command.staffchat")).forEach(staffer -> {
                    staffer.sendMessage(textComponent);
                });
                return false;
            }
            else if (targetData.screenshare && playerData.screenshare) {
                playerData.setScreenshare(false);
                PlayerUtils.sendPlayerToSpawn(player);
                targetData.setScreenshare(false);
                PlayerUtils.sendPlayerToSpawn(target);
                player.sendMessage("§aVocê liberou §e" + target.getName() + " §ada sala de ScreenShare.");
                target.sendMessage("§aVocê foi liberado da sala de ScreenShare.");
                SysUtils.sendTitle(target, "§aVocê foi liberado", "§eObrigado pela compreensão.");
                TextComponent textComponent = new TextComponent("§7§o(STAFF) O player §f" + target.getName() + " §7§ofoi liberado da ScreenShare.");
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("" +
                        "§fPor: " + PlayerGroup.getPlayerNameWithGroup(player)).create()));
                Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("command.staffchat")).forEach(staffer -> {
                    staffer.sendMessage(textComponent);
                });
                return false;
            }
            else if (!targetData.screenshare) { // player já está na ss
                target.getInventory().clear();
                target.teleport(ss);
                player.sendMessage("§aVocê puxou §e" + target.getName() + " §apara a sala de ScreenShare.");
                target.sendMessage("§cVocê foi puxado para a sala da ScreenShare. Siga as instruções do staffer.");
                SysUtils.sendTitle(target, "", "§cVocê foi puxado para ScreenShare");
                targetData.setScreenshare(true);
                TextComponent textComponent = new TextComponent("§7§o(STAFF) O player §f" + target.getName() + " §7§ofoi puxado para ScreenShare.");
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("" +
                        "§fPor: " + PlayerGroup.getPlayerNameWithGroup(player)).create()));
                Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("command.staffchat")).forEach(staffer -> {
                    staffer.sendMessage(textComponent);
                });
                return false;
            }
            else { // target está na ss e player não
                PlayerUtils.sendPlayerToSpawn(player);
                targetData.setScreenshare(false);
                player.sendMessage("§aVocê liberou §e" + target.getName() + " §ada sala de ScreenShare.");
                target.sendMessage("§aVocê foi liberado da sala de ScreenShare.");
                SysUtils.sendTitle(target, "§aVocê foi liberado", "§eObrigado pela compreensão.");
                TextComponent textComponent = new TextComponent("§7§o(STAFF) O player §f" + target.getName() + " §7§ofoi liberado da ScreenShare.");
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("" +
                        "§fPor: " + PlayerGroup.getPlayerNameWithGroup(player)).create()));
                Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("command.staffchat")).forEach(staffer -> {
                    staffer.sendMessage(textComponent);
                });
                return false;
            }
        }
        return false;
    }
}
