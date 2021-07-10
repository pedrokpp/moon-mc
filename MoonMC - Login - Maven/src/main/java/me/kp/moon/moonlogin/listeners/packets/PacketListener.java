package me.kp.moon.moonlogin.listeners.packets;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.chat.WrappedPacketInChat;
import io.github.retrooper.packetevents.packetwrappers.play.in.tabcomplete.WrappedPacketInTabComplete;
import me.kp.moon.moonlogin.auth.AuthAPI;
import me.kp.moon.moonlogin.data.PlayerData;
import me.kp.moon.moonlogin.data.PlayerDataManager;
import me.kp.moon.moonlogin.enums.Strings;
import me.kp.moon.moonlogin.mysql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class PacketListener extends PacketListenerAbstract {

    public PacketListener() {
        super(PacketListenerPriority.HIGH);
    }

    List<Byte> packetTypeList = Arrays.asList(PacketType.Play.Server.SPAWN_ENTITY, PacketType.Play.Server.PLAYER_INFO,
            PacketType.Play.Client.SETTINGS);

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (event.getPacketId() == PacketType.Play.Client.TAB_COMPLETE) {
            if (!playerData.isLoggedIn()) {
                event.setCancelled(true);
                return;
            }
            WrappedPacketInTabComplete packet = new WrappedPacketInTabComplete(event.getNMSPacket());
            String[] splitText = packet.getText().split(" ");
            if (splitText[0].startsWith("/") && splitText.length == 1) event.setCancelled(true);
        }
        if (event.getPacketId() == PacketType.Play.Client.POSITION || event.getPacketId() == PacketType.Play.Client.POSITION_LOOK ||
                event.getPacketId() == PacketType.Play.Client.FLYING || event.getPacketId() == PacketType.Play.Client.LOOK) {
            if (!playerData.isLoggedIn()) {
                player.teleport(Strings.spawn);
            }
        } else if (event.getPacketId() == PacketType.Play.Client.CHAT) {
            WrappedPacketInChat packet = new WrappedPacketInChat(event.getNMSPacket());
            if (!playerData.isLoggedIn()) {
                event.setCancelled(true);
                if (packet.getMessage().startsWith("/login") || packet.getMessage().startsWith("/logar")) {
                    String[] args = packet.getMessage().replace("/login ", "").replace("/logar ", "")
                            .split(" ");
                    if (playerData.getPassword() == null) {
                        player.sendMessage("§cVocê ainda não registrou sua conta.");
                        return;
                    }
                    if (args.length == 0) {
                        player.sendMessage("§eUtilize o comando §7/login <senha>§e.");
                        return;
                    }
                    String pass = args[0];
                    if (!playerData.getPassword().equals(pass)) {
                        player.sendMessage("§cSenha incorreta.");
                    } else {
                        player.sendMessage("§aVocê se autenticou com sucesso.");
                        player.sendMessage("§7Guardamos sua senha com encriptação AES-GCM.");
                        Bukkit.getConsoleSender().sendMessage("§7" + player.getName() + " se autenticou com sucesso.");
                        AuthAPI.authPlayer(playerData);
                    }
                }
                if (packet.getMessage().startsWith("/register") || packet.getMessage().startsWith("/registrar")) {
                    String[] args = packet.getMessage().replace("/register ", "").replace("/registrar ", "")
                            .split(" ");
                    if (playerData.getPassword() != null) {
                        player.sendMessage("§cVocê já registrou sua conta.");
                        return;
                    }
                    if (args.length == 0) {
                        player.sendMessage("§eUtilize o comando §7/register <senha> <senha>§e.");
                        return;
                    }
                    String pass = args[0];
                    String passConfirmation = args[1];
                    if (!pass.equals(passConfirmation)) {
                        player.sendMessage("§cAs senhas não são iguais. Tente novamente.");
                        return;
                    }
                    if (pass.length() < 6) {
                        player.sendMessage("§cSua senha precisa ter mais que 6 caracteres. §7(Essa senha possui " + pass.length() + " caracteres)");
                        return;
                    }
                    playerData.setPassword(pass);
                    MySQL.registerPlayer(playerData);
                    player.sendMessage("§aVocê se autenticou com sucesso.");
                    player.sendMessage("§7Guardamos sua senha com encriptação AES-GCM.");
                    Bukkit.getConsoleSender().sendMessage("§7" + player.getName() + " se autenticou com sucesso.");
                    AuthAPI.authPlayer(playerData);
                }
            } else {
                if (packet.getMessage().startsWith("/changepassword") || packet.getMessage().startsWith("/mudarsenha")) {
                    String[] args = packet.getMessage().replace("/changepassword ", "").replace("/mudarsenha ", "")
                            .split(" ");
                    if (args.length < 2) {
                        player.sendMessage("§cArgumentos insuficientes. §7/changepassword <antiga> <nova>");
                        return;
                    }
                    if (!args[0].equals(playerData.getPassword())) {
                        player.sendMessage("§cSenha atual não corresponde com a apresentada.");
                        return;
                    }
                    if (args[1].length() < 6) {
                        player.sendMessage("§cSua nova senha precisa ter no mínimo 6 caracteres.");
                        return;
                    }
                    MySQL.updatePassword(player, args[1], false);
                    Bukkit.getConsoleSender().sendMessage("§7" + player.getName() + " alterou sua senha.");
                    playerData.setKickable(true);
                }
            }
        } else {
            if (!playerData.isLoggedIn()) {
                if (!packetTypeList.contains(event.getPacketId())) event.setCancelled(true);
            }
        }
    }

}
