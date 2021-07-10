package me.kp.moon.moonlogin.commands;

import me.kp.moon.moonlogin.data.PlayerData;
import me.kp.moon.moonlogin.data.PlayerDataManager;
import me.kp.moon.moonlogin.enums.Strings;
import me.kp.moon.moonlogin.mysql.MySQL;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Unregister implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("unregister")) {
            if (args.length == 0) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Strings.apenasPlayers());
                    return true;
                }
                Player player = (Player) sender;
                PlayerData playerData = PlayerDataManager.getPlayerData(player);
                if (playerData == null) return true;
                if (!playerData.isWarnedUnregister()) {
                    TextComponent textComponent = new TextComponent("§cPara confirmar sua ação, clique nessa mensagem." +
                            "\n§7Considere apenas mudar sua senha com §8/changepassword§7.");
                    textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eApenas clique aqui se você quer realmente " +
                            "§4desregistrar §esua conta.").create()));
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/unregister"));
                    player.spigot().sendMessage(textComponent);
                    playerData.setWarnedUnregister(true);
                    return false;
                } else {
                    MySQL.unregisterPlayer(player, false);
                    player.kickPlayer("§cVocê se desregistrou com sucesso.");
                }
            } else {
                if (!sender.hasPermission("command.unregister")) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(Strings.apenasPlayers());
                        return true;
                    }
                    Player player = (Player) sender;
                    PlayerData playerData = PlayerDataManager.getPlayerData(player);
                    if (playerData == null) return true;
                    if (!playerData.isWarnedUnregister()) {
                        TextComponent textComponent = new TextComponent("§cPara confirmar sua ação, clique nessa mensagem." +
                                "\n§7Considere apenas mudar sua senha com §8/changepassword§7.");
                        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eApenas clique aqui se você quer realmente " +
                                "§4desregistrar §esua conta.").create()));
                        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/unregister"));
                        player.spigot().sendMessage(textComponent);
                        playerData.setWarnedUnregister(true);
                        return false;
                    } else {
                        MySQL.unregisterPlayer(player, false);
                        player.kickPlayer("§cVocê se desregistrou com sucesso.");
                    }
                    return true;
                } else {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        if (!MySQL.unregisterPlayer(args[0], true)) {
                            sender.sendMessage("§cNão foi possível encontrar esse player no banco de dados.");
                        } else {
                            sender.sendMessage("§aO player §e" + args[0] + " §afoi removido do banco de dados com sucesso.");
                        }
                        return false;
                    } else {
                        MySQL.unregisterPlayer(target, false);
                        target.kickPlayer("§cSua conta foi desregistrada.");
                    }
                }
            }
            return false;
        }
        return false;
    }
}
