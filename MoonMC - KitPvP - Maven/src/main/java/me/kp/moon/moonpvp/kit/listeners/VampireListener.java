package me.kp.moon.moonpvp.kit.listeners;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.kit.KitType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class VampireListener implements Listener {

    @EventHandler
    public void PlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        if (killer == null) return;
        PlayerData killerData = PlayerDataManager.getPlayerData(killer);
        if (killerData == null) return;
        if (killerData.kitType != KitType.VAMPIRE) return;
        killer.setMaxHealth(killer.getMaxHealth() + 1.0);
        killer.sendMessage("§aVocê recebeu meio coração a mais por matar §e" + player.getName() + "§a.");
    }

}
