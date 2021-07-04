package me.kp.moon.moonpvp.commands;

import me.kp.moon.moonpvp.Main;
import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.utils.SysUtils;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class R implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return true;
        if (command.getName().equalsIgnoreCase("r")) {
            Player target = Bukkit.getPlayer(playerData.lastTell);
            if (target == null) {
                player.sendMessage("§cVocê não tem ninguém para responder.");
                return true;
            }
            if (args.length == 0) {
                player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(command.getName() + " <mensagem>"));
                return true;
            }
            PlayerData targetData = PlayerDataManager.getPlayerData(target);
            if (targetData == null) return true;
            if (!targetData.tell || targetData.ignoredPlayers.contains(player.getUniqueId())) {
                player.sendMessage("§cEste player está com o tell desativado.");
                return true;
            }
            playerData.setLastTell(target.getUniqueId());
            targetData.setLastTell(player.getUniqueId());
            String message = StringUtils.join(args, " ");
            player.sendMessage("§8[§7Você §f» §7"+ target.getName() +"§8] §e" + message);
            target.sendMessage("§8[§7"+player.getName()+" §f» §7Você§8] §e" + message);
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                String[] content = message.split(" ");
                for (String word : content) {
                    if (SysUtils.blacklistedWords.contains(word.toLowerCase())) {
                        TextComponent textComponent = new TextComponent("§7§o(STAFF) O player §f" + player.getName() +
                                " §7§opossivelmente §7§oenviou §c§omensagens §c§oofensivas §7§opara §f" + target.getName() + "§7§o.");
                        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§fMensagem: " +
                                "§c§o" + message).create()));
                        Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("command.staffchat")).forEach(staffer -> {
                            staffer.sendMessage(textComponent);
                        });
                    }
                }
            });
            Bukkit.getOnlinePlayers().forEach(all -> {
                PlayerData allData = PlayerDataManager.getPlayerData(all);
                if (allData == null) return;
                if (allData.tellSpy) {
                    all.sendMessage("§7§o(TELL SPY) §8§o[§7§o" + player.getName() + " §f» §7§o" + target.getName() +
                            "§8§o] §e§o" + message);
                }
            });
        }
        return false;
    }
}
