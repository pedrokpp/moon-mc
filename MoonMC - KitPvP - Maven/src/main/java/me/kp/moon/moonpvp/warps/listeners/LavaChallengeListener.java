package me.kp.moon.moonpvp.warps.listeners;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.PlayerRank;
import me.kp.moon.moonpvp.utils.PlayerUtils;
import me.kp.moon.moonpvp.warps.WarpType;
import me.kp.moon.moonpvp.warps.WarpUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class LavaChallengeListener implements Listener {

    @EventHandler
    private void onEntityDamage(final EntityDamageEvent event) {
        if (event.isCancelled()) return;
        if (event.getEntity() instanceof Player && Objects.requireNonNull(PlayerDataManager.getPlayerData((Player) event.getEntity())).warpType == WarpType.LAVA && !event.getCause().name().contains("LAVA") && !event.getCause().name().contains("FIRE"))
            event.setCancelled(true);
    }

    @EventHandler
    private void onSignChange(final SignChangeEvent event) {
        final Player player = event.getPlayer();
        if (player.hasPermission("staff.customsigns") && event.getLine(0).equalsIgnoreCase("Lava Challenge")) {
            if (event.getLine(1).equalsIgnoreCase("Facil")) {
                event.setLine(0, "§6§lDESAFIO");
                event.setLine(1, "§7Nível:");
                event.setLine(2, "§a§lFácil");
                event.setLine(3, "§7(Clique)");
                player.sendMessage("§aPlaca de §eLava Challenge Nível Fácil §areconhecida.");
            }
            else if (event.getLine(1).equalsIgnoreCase("Medio")) {
                event.setLine(0, "§6§lDESAFIO");
                event.setLine(1, "§7Nível:");
                event.setLine(2, "§e§lMédio");
                event.setLine(3, "§7(Clique)");
                player.sendMessage("§aPlaca de §eLava Challenge Nível Médio §areconhecida.");
            }
            else if (event.getLine(1).equalsIgnoreCase("Dificil")) {
                event.setLine(0, "§6§lDESAFIO");
                event.setLine(1, "§7Nível:");
                event.setLine(2, "§c§lDifícil");
                event.setLine(3, "§7(Clique)");
                player.sendMessage("§aPlaca de §eLava Challenge Nível Difícil §areconhecida.");
            }
            else if (event.getLine(1).equalsIgnoreCase("Insano")) {
                event.setLine(0, "§6§lDESAFIO");
                event.setLine(1, "§7Nível:");
                event.setLine(2, "§4§lInsano");
                event.setLine(3, "§7(Clique)");
                player.sendMessage("§aPlaca de §eLava Challenge Nível Insano §areconhecida.");
            }
        }
    }

    @EventHandler
    private void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.isCancelled()) return;
        final Player player = event.getPlayer();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;
        if (event.hasBlock() && event.getClickedBlock().getType().name().contains("SIGN") && event.getAction().name().contains("RIGHT")) {
            final Sign sign = (Sign)event.getClickedBlock().getState();
            if (sign.getLine(0).equals("§6§lDESAFIO") && sign.getLine(1).equals("§7Nível:") && sign.getLine(3).equals("§7(Clique)")) {
                if (player.getAllowFlight() || player.isFlying() || playerData.admin || playerData.superadmin || playerData.vanish
                        || player.getGameMode() != GameMode.SURVIVAL || playerData.warpType != WarpType.LAVA) {
                    player.kickPlayer("§cAdminastro está vendo as pegadinhas que você apronta.");
                    return;
                }
                playerData.setCacheLastRank(PlayerRank.getRank(playerData));
                switch (sign.getLine(2)) {
                    case "§a§lFácil": {
                        Bukkit.broadcastMessage("§6" + player.getName() + " §ecompletou o §aNível Fácil §edo §6Lava Challenge§e!");
                        final int coins = player.hasPermission("coinsbooster.x2") ? 20 : 10;
                        final int points = 1;
                        playerData.setCacheLavaFacil(playerData.cacheLavaFacil + 1);
                        playerData.setCacheCoins(playerData.cacheCoins + coins);
                        playerData.setCacheXP(playerData.cacheXP + points);
                        player.sendMessage("§a+" + coins + " moedas");
                        player.sendMessage("§a+" + points + " XP");
                        break;
                    }
                    case "§e§lMédio": {
                        Bukkit.broadcastMessage("§6" + player.getName() + " §ecompletou o §6Nível Médio §edo §6Lava Challenge§e!");
                        final int coins = player.hasPermission("coinsbooster.x2") ? 100 : 50;
                        final int points = 1;
                        playerData.setCacheLavaFacil(playerData.cacheLavaFacil + 1);
                        playerData.setCacheCoins(playerData.cacheCoins + coins);
                        playerData.setCacheXP(playerData.cacheXP + points);
                        player.sendMessage("§a+" + coins + " moedas");
                        player.sendMessage("§a+" + points + " XP");
                        break;
                    }
                    case "§c§lDifícil": {
                        Bukkit.broadcastMessage("§6" + player.getName() + " §ecompletou o §cNível Difícil §edo §6Lava Challenge§e!");
                        final int coins = player.hasPermission("coinsbooster.x2") ? 200 : 100;
                        final int points = 1;
                        playerData.setCacheLavaFacil(playerData.cacheLavaFacil + 1);
                        playerData.setCacheCoins(playerData.cacheCoins + coins);
                        playerData.setCacheXP(playerData.cacheXP + points);
                        player.sendMessage("§a+" + coins + " moedas");
                        player.sendMessage("§a+" + points + " XP");
                        break;
                    }
                    case "§4§lInsano": {
                        Bukkit.broadcastMessage("§6" + player.getName() + " §ecompletou o §4Nível Insano §edo §6Lava Challenge§e!");
                        final int coins = player.hasPermission("coinsbooster.x2") ? 300 : 150;
                        final int points = 1;
                        playerData.setCacheLavaFacil(playerData.cacheLavaFacil + 1);
                        playerData.setCacheCoins(playerData.cacheCoins + coins);
                        playerData.setCacheXP(playerData.cacheXP + points);
                        player.sendMessage("§a+" + coins + " moedas");
                        player.sendMessage("§a+" + points + " XP");
                        break;
                    }
                }
                WarpUtils.teleportPlayerToWarp(player, WarpType.LAVA);
                PlayerUtils.checkRank(player, playerData);
            }
        }
    }

}
