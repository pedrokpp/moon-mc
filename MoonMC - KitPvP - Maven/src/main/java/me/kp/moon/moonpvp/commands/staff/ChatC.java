package me.kp.moon.moonpvp.commands.staff;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.utils.SysUtils;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatC implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return true;
        if (command.getName().equalsIgnoreCase("chat")) {
            if (!player.hasPermission("command.chat")) {
                player.sendMessage(Messages.SEM_PERMISSAO.getMessage());
                return true;
            }
            if (args.length == 0) {
                player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(label + " <clear/on/off/status>"));
                return true;
            }
            switch (args[0].toLowerCase()) {
                case "clear":
                    for (int i = 0; i < 255; ++i) Bukkit.broadcastMessage(" ");
                    TextComponent textComponent = new TextComponent("§7§o(STAFF) O chat foi limpo.");
                    textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("" +
                            "§f§oPor: " + playerData.playerTag.getColor() + playerData.playerTag.getPrefix().toUpperCase() + player.getName()).create()));
                    Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("command.staffchat")).forEach(staffer -> {
                        staffer.sendMessage(textComponent);
                    });
                    break;
                case "on":
                    if (SysUtils.chat) {
                        player.sendMessage("§cO chat já está ativado.");
                        return true;
                    } else {
                        SysUtils.setChat(true);
                        player.sendMessage("§aVocê ativou o chat.");
                        TextComponent textComponent2 = new TextComponent("§7§o(STAFF) O chat foi ativado.");
                        textComponent2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("" +
                                "§f§oPor: " + playerData.playerTag.getColor() + playerData.playerTag.getPrefix().toUpperCase() + player.getName()).create()));
                        Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("command.staffchat")).forEach(staffer -> {
                            staffer.sendMessage(textComponent2);
                        });
                    }
                    break;
                case "off":
                    if (!SysUtils.chat) {
                        player.sendMessage("§cO chat já está desativado.");
                        return true;
                    } else {
                        SysUtils.setChat(false);
                        player.sendMessage("§aVocê desativou o chat.");
                        TextComponent textComponent2 = new TextComponent("§7§o(STAFF) O chat foi desativado.");
                        textComponent2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("" +
                                "§f§oPor: " + playerData.playerTag.getColor() + playerData.playerTag.getPrefix().toUpperCase() + player.getName()).create()));
                        Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("command.staffchat")).forEach(staffer -> {
                            staffer.sendMessage(textComponent2);
                        });
                    }
                    break;
                case "status":
                    String state = SysUtils.chat ? "§eATIVADO" : "§cDESATIVADO";
                    player.sendMessage("§aO estado atual do chat é: " + state + "§a.");
                    break;
            }
        }
        return false;
    }
}
