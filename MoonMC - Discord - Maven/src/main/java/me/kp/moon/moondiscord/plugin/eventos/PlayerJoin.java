package me.kp.moon.moondiscord.plugin.eventos;

import me.kp.moon.moondiscord.mysql.MySQL;
import me.kp.moon.moondiscord.plugin.Main;
import me.kp.moon.moondiscord.plugin.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class PlayerJoin implements Listener {

    public MySQL mySQL = new MySQL();

    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        if (player.hasPermission("command.staffchat")) {
            Date date = new Date();
            DateFormat df = new SimpleDateFormat("HH:mm");

            df.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));

            PlayerUtils.staffJoin.put(player.getUniqueId(), df.format(date));

        }
        if (mySQL.uuidExiste(uuid)) {
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                if (player.hasPermission("displayname.dono"))
                    mySQL.updateDisplayTAG(uuid, "§4§lDONO §4");// "§4§lDONO §4" + player.getName();
                else if (player.hasPermission("displayname.subdono"))
                    mySQL.updateDisplayTAG(uuid, "§4§LS-DONO §4");// "§4§lS-DONO §4" + player.getName();
                else if (player.hasPermission("displayname.developer"))
                    mySQL.updateDisplayTAG(uuid, "§a§lDEV §a");// "§a§lDEV §a" + player.getName();
                else if (player.hasPermission("displayname.diretor"))
                    mySQL.updateDisplayTAG(uuid, "§b§lDIRETOR §b");// "§b§lDIRETOR §b" + player.getName();
                else if (player.hasPermission("displayname.gerente"))
                    mySQL.updateDisplayTAG(uuid, "§9§lGERENTE §9");// "§9§lGERENTE §9" + player.getName();
                else if (player.hasPermission("displayname.admin"))
                    mySQL.updateDisplayTAG(uuid, "§c§lADMIN §c");// "§3§lCOORD §3" + player.getName();
                else if (player.hasPermission("displayname.coord"))
                    mySQL.updateDisplayTAG(uuid, "§3§lCOORD §3");// "§2§lMOD+ §2" + player.getName();
                else if (player.hasPermission("displayname.modplus"))
                    mySQL.updateDisplayTAG(uuid, "§2§lMOD+ §2");// "§2§lBUILDER §2" + player.getName();
                else if (player.hasPermission("displayname.modgc"))
                    mySQL.updateDisplayTAG(uuid, "§5§lMODGC §5");// "§5§lMODGC §5" + player.getName();
                else if (player.hasPermission("displayname.mod"))
                    mySQL.updateDisplayTAG(uuid, "§5§lMOD §5");// "§5§lMOD §5" + player.getName();
                else if (player.hasPermission("displayname.builder"))
                    mySQL.updateDisplayTAG(uuid, "§2§lBUILDER §2");// "§e§lHELPER §e" + player.getName();
                else if (player.hasPermission("displayname.helper"))
                    mySQL.updateDisplayTAG(uuid, "§e§lHELPER §e");// "§e§lHELPER §e" + player.getName();
            });
        }
    }

}
