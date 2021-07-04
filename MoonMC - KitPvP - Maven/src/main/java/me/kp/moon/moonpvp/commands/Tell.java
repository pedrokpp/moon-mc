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

import java.util.Arrays;

public class Tell implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return true;
        if (command.getName().equalsIgnoreCase("tell")) {
            if (args.length < 2) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("on")) {
                        if (playerData.tell) {
                            player.sendMessage("§aVocê já está com o tell ativado.");
                            return true;
                        }
                        player.sendMessage("§aVocê ativou o seu tell.");
                        playerData.setTell(true);
                        return false;
                    }
                    else if (args[0].equalsIgnoreCase("off")) {
                        if (!playerData.tell) {
                            player.sendMessage("§cVocê já está com o tell desativado.");
                            return true;
                        }
                        player.sendMessage("§cVocê desativou o seu tell.");
                        playerData.setTell(false);
                        return false;
                    }
                    else if (args[0].equalsIgnoreCase("spy")) {
                        if (!player.hasPermission("command.tellspy")) {
                            player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(command.getName() + " <player> <mensagem> || " +
                                    command.getName() + " <on/off>"));
                            return true;
                        }
                        if (!playerData.tellSpy) {
                            playerData.setTellSpy(true);
                            player.sendMessage("§aVocê ativou o modo tell spy.");
                        } else {
                            playerData.setTellSpy(false);
                            player.sendMessage("§cVocê desativou o modo tell spy.");
                        }
                        return false;
                    }
                }
                player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(command.getName() + " <player> <mensagem> || " +
                        command.getName() + " <on/off>"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("§cNão foi possível encontrar o player §e" + args[0] + "§c.");
                return true;
            }
            if (target == player) {
                player.sendMessage("§cVocê não pode enviar mensagens para si mesmo.");
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
            String message = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
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
