package me.kp.moon.moonpvp.utils;

import me.kp.moon.moonpvp.Main;
import me.kp.moon.moonpvp.api.TagAPI;
import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.PlayerRank;
import me.kp.moon.moonpvp.enums.PlayerTag;
import me.kp.moon.moonpvp.gui.SeusKitsGUI;
import me.kp.moon.moonpvp.gui.ShopGUI;
import me.kp.moon.moonpvp.gui.SoupTypeGUI;
import me.kp.moon.moonpvp.gui.WarpsGUI;
import me.kp.moon.moonpvp.kit.KitUtils;
import me.kp.moon.moonpvp.warps.WarpType;
import me.kp.moon.moonpvp.warps.WarpUtils;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

public class PlayerUtils {

    public static void killerKillPlayer(Player killer, PlayerData killerData, PlayerData playerData) {
        Player player = playerData.getPlayer();
        killer.sendMessage("§aVocê matou §7" + player.getName() + "§a.");
        killerData.setCacheLastRank(PlayerRank.getRank(killerData));
        DeathUtils.giveRandomCoins(killerData);
        DeathUtils.giveRandomXP(killerData);
        PlayerUtils.checkRank(killer, killerData);
        if (playerData.warpType == WarpType.FPS) WarpUtils.refreshFPSArmor(killer);
        killerData.setCacheKills(killerData.cacheKills + 1);
        killerData.setCacheKillStreak(killerData.cacheKillStreak + 1);
        // anunciar killstreak
        if (killerData.cacheKillStreak % 5 == 0 && killerData.cacheKillStreak >= 10) Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage("§6" + killer.getName() + " §eatingiu um killstreak de §6" + killerData.cacheKillStreak + "§e!"));
    }

    public static void deadKillPlayer(Player player, PlayerData playerData, Player killer) {
        player.sendMessage("§cVocê morreu para §7" + killer.getName() + "§c.");
        playerData.setCacheLastRank(PlayerRank.getRank(playerData));
        DeathUtils.removeRandomCoins(playerData);
        DeathUtils.removeRandomXP(playerData);
        playerData.setCacheDeaths(playerData.cacheDeaths + 1);
        PlayerUtils.checkRank(player, playerData);
        // anunciar perda de killstreak
        if (playerData.cacheKillStreak % 5 == 0 && playerData.cacheKillStreak >= 10) Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage("§6" + player.getName() + " §eperdeu seu killstreak de §6" + playerData.cacheKillStreak + " §epara §6" +
                killer.getName() + "§e!"));
        playerData.setCacheKillStreak(0);
        playerData.setLastCombatPlayer(null);
        playerData.setCombat(false);
    }

    public static void sendPlayerToSpawn(Player player) {
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return;

        player.closeInventory();
        if (player.getFireTicks() != 0) player.setFireTicks(0);
        player.getActivePotionEffects().forEach(ef -> player.removePotionEffect(ef.getType()));
        player.setMaxHealth(20.0);
        player.setHealth(20.0);
        giveInitialItems(player);
        player.teleport(SysUtils.spawn);
        if (playerData.evento) playerData.setEvento(false);
        if (playerData.screenshare) playerData.setScreenshare(false);
        if (playerData.combat) playerData.setCombat(false);
        if (playerData.build) playerData.setBuild(false);
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            WarpUtils.clearWarp(player);
            KitUtils.clearKits(player);
        }, 5L);
    }

    public static void giveInitialItems(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setItem(0, SeusKitsGUI.ICON);
        player.getInventory().setItem(2, ShopGUI.ICON);
        player.getInventory().setItem(4, SoupTypeGUI.ICON);
        player.getInventory().setItem(6, WarpsGUI.ICON);
        player.getInventory().setItem(8, ItemUtils.editItemStack(ItemUtils.getPlayerSkull(player.getName()), "§aSeu perfil", null));
    }

    public static void sendMessageToStaff(String message) {
        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("command.staffchat")).forEach(staff -> staff.sendMessage(message.replace("&", "§")));
    }

    public static int getPlayerPing(Player player) {
        return ((CraftPlayer)player).getHandle().ping;
    }

    public static String getPlayerTag(Player player) {
        if (player.hasPermission("displayname.dono")) return "§4§lDONO §4";
        else if (player.hasPermission("displayname.subdono")) return "§4§lS-DONO §4";
        else if (player.hasPermission("displayname.developer")) return "§a§lDEV §a";
        else if (player.hasPermission("displayname.diretor")) return "§b§lDIRETOR §b";
        else if (player.hasPermission("displayname.gerente")) return "§9§lGERENTE §9";
        else if (player.hasPermission("displayname.admin")) return "§c§lADMIN §c";
        else if (player.hasPermission("displayname.coord")) return "§3§lCOORD §3";
        else if (player.hasPermission("displayname.modplus")) return "§2§lMOD+ §2";
        else if (player.hasPermission("displayname.modgc")) return "§5§lMODGC §5";
        else if (player.hasPermission("displayname.mod")) return "§5§lMOD §5";
        else if (player.hasPermission("displayname.builder")) return "§2§lBUILDER §2";
        else if (player.hasPermission("displayname.helper")) return "§e§lHELPER §e";
        else if (player.hasPermission("displayname.ytplus")) return "§b§lYOUTUBER+ §b";
        return null;
    }

    public static void changePlayerTag(Player player, PlayerTag tag, PlayerData playerData) {
        player.setDisplayName(tag.getPrefix().toUpperCase() + player.getName());
        player.setPlayerListName(tag.getPrefix().toUpperCase() + player.getName());
        playerData.setPlayerTag(tag);
        TagAPI.updatePlayerTeam(player, tag);
    }

    public static void giveAdminItems(Player player) {
        Inventory inv = player.getInventory();
        inv.clear();
//        inv.setItem(2, ItemUtils.getCustomItemStack(Material.MAGMA_CREAM, "§aQuick Admin", "§5Sim, outro easter egg '-' r$r$"));
        inv.setItem(4, ItemUtils.getCustomItemStack(Material.BOOK, "§bInformações", "§4§lLARGA DE SER CURIOSO >:("));
    }

    public static void addChatCooldown(PlayerData playerData) {
        playerData.setChatCooldown(true);
        playerData.setLastMessageMS(System.currentTimeMillis() + (4 * 1000L));
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            playerData.setChatCooldown(false);
            playerData.setLastMessageMS(null);
        }, 4 * 20L);
    }

    public static void throwRandomFirework(Player p) {
        Firework fw = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        //Our random generator
        Random r = new Random();

        //Get the type
        int rt = r.nextInt(5) + 1;
        FireworkEffect.Type type = FireworkEffect.Type.BALL;
        if (rt == 2) type = FireworkEffect.Type.BALL_LARGE;
        if (rt == 3) type = FireworkEffect.Type.BURST;
        if (rt == 4) type = FireworkEffect.Type.CREEPER;
        if (rt == 5) type = FireworkEffect.Type.STAR;

        //Get our random colours
        int r1i = r.nextInt(17) + 1;
        int r2i = r.nextInt(17) + 1;
        Color c1 = Color.fromRGB(r1i);
        Color c2 = Color.fromRGB(r2i);

        //Create our effect with this
        FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();

        //Then apply the effect to the meta
        fwm.addEffect(effect);

        //Generate some random power and set it
        int rp = r.nextInt(2) + 1;
        fwm.setPower(rp);

        //Then apply this to our rocket
        fw.setFireworkMeta(fwm);
    }

    public static void checkRank(Player player, PlayerData playerData) {
        if (playerData.cacheLastRank == PlayerRank.INFINITY) return;
        if (playerData.cacheLastRank.next().getXp() <= playerData.cacheXP) {
            SysUtils.sendTitle(player, "§aParabéns", "§eVocê alcançou o rank " + playerData.cacheLastRank.next().getColoredName());
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
            PlayerUtils.throwRandomFirework(player);
            PlayerUtils.throwRandomFirework(player);
        }
    }

    public static void addPlayersCombatLog(PlayerData playerData, PlayerData damagerData, int seconds) {
        long ms = System.currentTimeMillis() + (seconds * 1000L);
        playerData.setCombat(true);
        playerData.setLastCombatPlayer(damagerData.getPlayer());
        playerData.setCombatLogTime(ms);
        damagerData.setCombat(true);
        damagerData.setLastCombatPlayer(playerData.getPlayer());
        damagerData.setCombatLogTime(ms);
    }

    public static void removePlayerCombatLog(PlayerData playerData) {
        playerData.setCombatLogTime(0L);
        playerData.setCombat(false);
        playerData.setLastCombatPlayer(null);
    }

}

