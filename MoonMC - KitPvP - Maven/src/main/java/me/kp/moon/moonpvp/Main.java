package me.kp.moon.moonpvp;

import me.kp.moon.moonpvp.api.TagAPI;
import me.kp.moon.moonpvp.clan.data.ClanManager;
import me.kp.moon.moonpvp.commands.*;
import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Strings;
import me.kp.moon.moonpvp.evento.EventoComando;
import me.kp.moon.moonpvp.evento.EventoListener;
import me.kp.moon.moonpvp.evento.EventoTabComplete;
import me.kp.moon.moonpvp.gui.GuiListener;
import me.kp.moon.moonpvp.kit.Kit;
import me.kp.moon.moonpvp.kit.KitListeners;
import me.kp.moon.moonpvp.kit.KitUtils;
import me.kp.moon.moonpvp.listeners.*;
import me.kp.moon.moonpvp.mysql.MySQL;
import me.kp.moon.moonpvp.utils.PlayerUtils;
import me.kp.moon.moonpvp.utils.SysUtils;
import me.kp.moon.moonpvp.warps.Warp;
import me.kp.moon.moonpvp.warps.WarpListeners;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public final class Main extends JavaPlugin {

    MySQL mySQL = new MySQL();

    public static Main getInstance() {
        return getPlugin(Main.class);
    }

    @Override
    public void onLoad() {
        Bukkit.getConsoleSender().sendMessage("§9[MoonMC] §aIniciando plugin...");
        mySQL.connectToDBS();
    }

    @Override
    public void onEnable() {
        ClanManager.loadAllClans();
        TagAPI.loadTeams();
//        HologramAPI.loadTops();
//        HologramAPI.reloadHolograms();
//        HologramAPI.runTask();
        setupRecipes();
        registerCommands();
        registerEvents();
        schedulerTasks();
        Bukkit.getConsoleSender().sendMessage("§9[MoonMC] §aPlugin iniciado!");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§9[MoonMC] §cPlugin desligado!");
    }

    private void schedulerTasks() {
        // timer da plebe
        Bukkit.getScheduler().runTaskTimer(this, () -> Bukkit.getOnlinePlayers().forEach(players -> {
            SysUtils.sendTab(players, "§9§lMoon§1§lMC \n   §fServidor: §aKitPvP\n", "\n§fDiscord: §a"+ Strings.getDiscord() + "\n§fJogadores: §a" + Bukkit.getOnlinePlayers().size());
            TagAPI.setScoreboard(players);
        }), 0L, 3 * 20L);

        Bukkit.getScheduler().runTaskTimer(this, () -> Bukkit.getOnlinePlayers().forEach(player -> {
            PlayerData playerData = PlayerDataManager.getPlayerData(player);
            if (playerData == null) return;
            if (playerData.kitCooldownMS != null) {
                int diff = (int) ((playerData.kitCooldownMS - System.currentTimeMillis()) / 1000);
                if (diff > 0) player.setLevel(diff);
                else {
                    KitUtils.removeCooldown(playerData);
                    player.setLevel(0);
                }
            }
            if (playerData.combatLogTime > 0L && System.currentTimeMillis() >= playerData.combatLogTime) {
                System.out.println("remvoeu cl!!");
                PlayerUtils.removePlayerCombatLog(playerData);
            }

        }), 0L, 20L);

        Bukkit.getScheduler().runTaskTimer(this, () -> Bukkit.broadcastMessage(Strings.getMessages().get(new Random().nextInt(Strings.getMessages().size()))), 0L, 90 * 20L);

        // timer dos ademir
        Bukkit.getScheduler().runTaskTimer(this, () -> Bukkit.getOnlinePlayers().forEach(players -> {
            PlayerData playerData = PlayerDataManager.getPlayerData(players);
            if (playerData == null) return;
            if (playerData.screenshare) {
                SysUtils.sendActionBar(players, "§cVocê está na SCREENSHARE");
                return;
            }
            if (playerData.admin || playerData.vanish) {
                String mode = playerData.admin ? "ADMIN§f." : "VANISH§f.";
                SysUtils.sendActionBar(players, "§fVocê está no modo §c" + mode);
                Bukkit.getOnlinePlayers().forEach(players2 -> {
                    PlayerData playerData2 = PlayerDataManager.getPlayerData(players2);
                    if (playerData2 == null) {
                        players2.hidePlayer(players);
                        return;
                    }
                    if (!players2.hasPermission("command.staffchat") || playerData2.hasHiddenStaff)
                        players2.hidePlayer(players);
                });
            }
            if (playerData.superadmin) {
                SysUtils.sendActionBar(players, "§fVocê está no modo §cSUPER ADMIN§f.");
                Bukkit.getOnlinePlayers().forEach(players2 -> players2.hidePlayer(players));
            }
        }), 0L, 2 * 20L);
    }

    private void registerEvents() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerLoginJoinQuit(), this);
        pluginManager.registerEvents(new Chat(), this);
        pluginManager.registerEvents(new World(), this);
        pluginManager.registerEvents(new BuildListener(), this);
        pluginManager.registerEvents(new PlayerKill(), this);
        pluginManager.registerEvents(new ModoAdmin(), this);
        pluginManager.registerEvents(new Damage(), this);
        pluginManager.registerEvents(new CombatLog(), this);
        pluginManager.registerEvents(new EventoListener(), this);
        pluginManager.registerEvents(new PvPListener(), this);
        KitListeners.registerKitListeners();
        WarpListeners.registerWarpListeners();
        GuiListener.registerGuiListeners();
    }

    private void registerCommands() {
        getCommand("sc").setExecutor(new StaffChat());
        getCommand("admin").setExecutor(new Admin());
        getCommand("superadmin").setExecutor(new SuperAdmin());
        getCommand("vanish").setExecutor(new Vanish());
        getCommand("sudo").setExecutor(new Sudo());
        getCommand("build").setExecutor(new Build());
        getCommand("invsee").setExecutor(new Invsee());
        getCommand("lastlogin").setExecutor(new LastLogin());
        getCommand("sortearplayer").setExecutor(new SortearPlayer());
        getCommand("sorteio").setExecutor(new Sorteio());
        getCommand("discord").setExecutor(new Discord());
        getCommand("fly").setExecutor(new Fly());
        getCommand("money").setExecutor(new Money());
        getCommand("addmoney").setExecutor(new AddMoney());
        getCommand("removemoney").setExecutor(new RemoveMoney());
        getCommand("setmoney").setExecutor(new SetMoney());
        getCommand("group").setExecutor(new Group());
        getCommand("hidestaff").setExecutor(new HideStaff());
        getCommand("chat").setExecutor(new ChatC());
        getCommand("kit").setExecutor(new Kit());
        getCommand("tell").setExecutor(new Tell());
        getCommand("r").setExecutor(new R());
        getCommand("tag").setExecutor(new Tag());
        getCommand("tags").setExecutor(new Tag());
        getCommand("medal").setExecutor(new Medal());
        getCommand("medals").setExecutor(new Medal());
        getCommand("block").setExecutor(new BlockUnblock());
        getCommand("unblock").setExecutor(new BlockUnblock());
        getCommand("report").setExecutor(new Report());
        getCommand("rptp").setExecutor(new ReportTP());
        getCommand("tp").setExecutor(new Teleport());
        getCommand("tphere").setExecutor(new TeleportHere());
        getCommand("spawn").setExecutor(new Spawn());
        getCommand("gamemode").setExecutor(new Gamemode());
        getCommand("teste").setExecutor(new Teste());
        getCommand("warp").setExecutor(new Warp());
        getCommand("info").setExecutor(new Info());
        getCommand("ab").setExecutor(new ActionBar());
        getCommand("ac").setExecutor(new AvisoC());
        getCommand("at").setExecutor(new AvisoT());
        getCommand("aplicar").setExecutor(new Aplicar());
        getCommand("check").setExecutor(new Check());
        getCommand("crash").setExecutor(new Crash());
        getCommand("head").setExecutor(new Head());
        getCommand("online").setExecutor(new Online());
        getCommand("rank").setExecutor(new Rank());
        getCommand("status").setExecutor(new Status());
        getCommand("evento").setExecutor(new EventoComando());
        getCommand("evento").setTabCompleter(new EventoTabComplete());
        getCommand("ss").setExecutor(new Screenshare());
        getCommand("ping").setExecutor(new Ping());
        getCommand("cc").setExecutor(new CC());
        getCommand("givekit").setExecutor(new GiveKit());
        getCommand("removekit").setExecutor(new RemoveKit());
        getCommand("grant").setExecutor(new Grant());
        getCommand("grant").setTabCompleter(new GrantTabComplete());
        getCommand("ungrant").setExecutor(new Ungrant());
    }

    private void setupRecipes() {
        ItemStack soup = new ItemStack(Material.MUSHROOM_SOUP);
        ItemStack cocoa = new ItemStack(Material.INK_SACK, 1, (short) 3);
        ShapelessRecipe soupRecipe = new ShapelessRecipe(soup);
        soupRecipe.addIngredient(Material.BOWL);
        soupRecipe.addIngredient(cocoa.getData());
        getServer().addRecipe(soupRecipe);
    }

}
