package me.kp.moon.moonpvp.commands;

import me.kp.moon.moonpvp.Main;
import me.kp.moon.moonpvp.cache.SysCache;
import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.utils.PlayerUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Report implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return true;
        if (command.getName().equalsIgnoreCase("report")) {
            if (playerData.reportCooldown) {
                player.sendMessage("§cAguarde para enviar outro report.");
                return true;
            }
            if (args.length < 2) {
                player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(label + " <player> <motivo>"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("§cNão foi possível encontrar o player §e" + args[0] + "§c.");
                return true;
            }
//            if (target == player) {
//                player.sendMessage("§cVocê não pode reportar a si mesmo.");
//                return true;
//            }
            PlayerData targetData = PlayerDataManager.getPlayerData(target);
            if (targetData == null) return true;
            String report = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
            if ((report.length() < 3 && !report.equalsIgnoreCase("ac")) || report.equalsIgnoreCase("hack")
                    || report.equalsIgnoreCase("hacker")) {
                player.sendMessage("§cSeja mais específico com seus reports, por favor.");
                return true;
            }
            player.sendMessage("§aVocê reportou o player §e" + target.getName() + "§a.");
            String kitTarget = targetData.kitType == null ? "Nenhum" : targetData.kitType.getKitname();
            String kitPlayer = playerData.kitType == null ? "Nenhum" : playerData.kitType.getKitname();
            PlayerUtils.sendMessageToStaff(" \n" +
                    "  §c§lALERTA DE REPORT " +
                    "\n§fReportado: §c" + target.getName() + " §7(" + PlayerUtils.getPlayerPing(target) + "ms | " + kitTarget + "§7)" +
                    "\n§fMotivo: §a" + report +
                    "\n§fAutor: §a" + player.getName() + " §7(" + PlayerUtils.getPlayerPing(player) + "ms | " + kitPlayer + "§7)");
            TextComponent textComponent = new TextComponent("§7[Clique aqui para ir até §f" + target.getName() + "§7] \n ");
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClique para teleportar-se!").create()));
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rptp " + target.getName()));
            Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("command.staffchat")).forEach(staffer -> {
                staffer.sendMessage(textComponent);
                staffer.playSound(staffer.getLocation(), Sound.ARROW_HIT, 1.0f, 1.0f);
            });
            SysCache.addReportToPlayer(player, report);
            playerData.setReportCooldown(true);
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                PlayerData playerData2 = PlayerDataManager.getPlayerData(player);
                if (playerData2 != null) playerData.setReportCooldown(false);
            }, 40 * 20L);
        }
        return false;
    }
}
