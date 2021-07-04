package me.kp.moon.moonpvp.evento;

import me.kp.moon.moonpvp.data.PlayerData;
import me.kp.moon.moonpvp.data.PlayerDataManager;
import me.kp.moon.moonpvp.enums.Messages;
import me.kp.moon.moonpvp.kit.KitUtils;
import me.kp.moon.moonpvp.utils.PlayerUtils;
import me.kp.moon.moonpvp.warps.WarpUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class EventoComando implements CommandExecutor {
    private static void sendHelp(Player player) {
        if (player.hasPermission("command.evento")) {
            player.sendMessage("§9§lMoon§1§lMC §7- §eSistema de Eventos");
            player.sendMessage(" ");
            player.sendMessage("§e/evento entrar §7- §fEntre em um evento.");
            player.sendMessage("§e/evento sair §7- §fSaia do evento em andamento.");
            player.sendMessage("§e/evento spec §7- §fSeja espectador do evento.");
            player.sendMessage(" ");
            player.sendMessage("§e/evento build §7- §fAltere o build.");
            player.sendMessage("§e/evento cleararena §7- §fLimpe a arena.");
            player.sendMessage("§e/evento damage §7- §fAltere o dano (exceto pvp).");
            player.sendMessage("§e/evento effect <efeito/clear> <amplificador> <segundos> <player/all>§7- §fAdicione efeitos de poção aos integrantes do evento.");
            player.sendMessage("§e/evento explicar <evento> §7- §fExplique um evento automaticamente.");
            player.sendMessage("§e/evento kick <player> §7- §fExpulse um player do evento.");
            player.sendMessage("§e/evento participantes §7- §fLista os participantes do evento.");
            player.sendMessage("§e/evento pvp §7- §fAltere o pvp.");
            player.sendMessage("§e/evento setspecloc §7- §fSete a localização que os espectadores aparecerão.");
            player.sendMessage("§e/evento skit <player/all> §7- §fSete o kit do evento.");
            player.sendMessage("§e/evento specs §7- §fHabilite/desabilite espectadores.");
            player.sendMessage("§e/evento start §7- §fInicie um evento.");
            player.sendMessage("§e/evento stop §7- §fEncerre o evento em andamento.");
            player.sendMessage("§e/evento toggle §7- §fHabilite/desabilite a entrada do evento em andamento.");
            player.sendMessage("§e/evento tpall §7- §fTeleporte todos os players do evento até você.");
            player.sendMessage("§e/evento tpto <evento> §7- §fTeleporte todos os players para localizações predefinidas.");
            player.sendMessage("§e/evento whitelist <add/remove/list> <player> §7- §fLibere a entrada de players específicos uma vez.");
            player.sendMessage(" ");
        } else {
            player.sendMessage("§9§lMoon§1§lMC §7- §eSistema de Eventos");
            player.sendMessage(" ");
            player.sendMessage("§e/evento entrar §7- §fEntre em um evento.");
            player.sendMessage("§e/evento sair §7- §fSaia do evento em andamento.");
            player.sendMessage("§e/evento spec §7- §fSeja espectador do evento.");
            player.sendMessage(" ");
        }
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.APENAS_PLAYERS.getMessage());
            return true;
        }
        Player player = (Player) sender;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        if (playerData == null) return true;
        if (command.getName().equalsIgnoreCase("evento")) {
            if (args.length == 0) {
                sendHelp(player);
                return false;
            }

            if (!player.hasPermission("command.evento")) {
                if (!EventoUtils.evento) {
                    player.sendMessage("§cA sala de eventos não está aberta.");
                    return true;
                }
                switch (args[0].toLowerCase()) {
                    case "entrar":
                        if (playerData.evento) {
                            player.sendMessage("§cVocê já está no evento.");
                            return true;
                        }
                        if (!EventoUtils.tp) {
                            if (EventoUtils.whitelist.contains(player.getUniqueId())) {
                                playerData.setEvento(true);
                                player.teleport(EventoUtils.mainArena);
                                player.sendMessage("§aVocê entrou pela whitelist. §7(já foi retirado)");
                                EventoUtils.whitelist.remove(player.getUniqueId());
                                player.getInventory().clear();
                                KitUtils.clearKits(player);
                                WarpUtils.clearWarp(player);
                            } else {
                                player.sendMessage("§cA sala do evento já foi fechada. Fique ligado para quando for aberta para espectadores.");
                            }
                            return true;
                        }
                        playerData.setEvento(true);
                        player.setAllowFlight(false);
                        player.teleport(EventoUtils.mainArena);
                        player.getInventory().clear();
                        player.sendMessage("§aVocê entrou no evento.");
                        KitUtils.clearKits(player);
                        WarpUtils.clearWarp(player);
                        break;
                    case "sair":
                        if (!playerData.evento) {
                            player.sendMessage("§cVocê não está no evento.");
                            return true;
                        }
                        playerData.setEvento(false);
                        PlayerUtils.sendPlayerToSpawn(player);
                        player.sendMessage("§cVocê saiu do evento.");
                        break;
                    case "spec":
                        if (playerData.evento) {
                            player.sendMessage("§cVocê está no evento.");
                            return true;
                        }
                        if (!(playerData.kitType == null && playerData.warpType == null)) {
                            player.sendMessage("§cVocê não pode ter kits selecionados para isso.");
                            return true;
                        }
                        if (!EventoUtils.specs) {
                            player.sendMessage("§cOs espectadores estão desativados no momento.");
                            return true;
                        }
                        player.teleport(EventoUtils.specLoc);
                        player.sendMessage("§aVocê está espectando o evento.");
                        break;
                    default:
                        player.sendMessage("§cNão foi possível encontrar a opção §e" + args[0] + "§c.");
                        break;
                }
            }
            else {
                if (args[0].equalsIgnoreCase("start")) {
                    if (EventoUtils.evento) {
                        player.sendMessage("§cA sala de eventos já está aberta.");
                        return true;
                    }
                    EventoUtils.evento = true;
                    player.sendMessage("§aVocê abriu a sala de eventos.");
                    EventoUtils.whitelist.add(player.getUniqueId());
                    player.chat("/evento entrar");
                    player.setGameMode(GameMode.CREATIVE);
                }
                else if (args[0].equalsIgnoreCase("stop")) {
                    if (!EventoUtils.evento) {
                        player.sendMessage("§cA sala de eventos já está fechada.");
                        return true;
                    }
                    EventoUtils.evento = false;
                    player.sendMessage("§aVocê fechou a sala de eventos.");
                    EventoUtils.getEventoPlayers().forEach(p -> {
                        Objects.requireNonNull(PlayerDataManager.getPlayerData(p)).setEvento(false);
                        Objects.requireNonNull(PlayerDataManager.getPlayerData(p)).setCombat(false);
                        p.teleport(p.getWorld().getSpawnLocation());
                        PlayerUtils.giveInitialItems(p);
                        p.sendMessage("§cO evento foi finalizado.");
                        p.chat("/spawn");
                        p.getActivePotionEffects().forEach(ef -> p.removePotionEffect(ef.getType()));
                    });
                    EventoUtils.resetEventoClass();
                }
                else {
                    if (!EventoUtils.evento) {
                        player.sendMessage("§cA sala de eventos não está aberta.");
                        return true;
                    }
                    switch (args[0].toLowerCase()) {
                        case "entrar":
                            if (playerData.evento) {
                                player.sendMessage("§cVocê já está no evento.");
                                return true;
                            }
                            if (!EventoUtils.tp) {
                                if (EventoUtils.whitelist.contains(player.getUniqueId())) {
                                    playerData.setEvento(true);
                                    player.teleport(EventoUtils.mainArena);
                                    player.sendMessage("§aVocê entrou pela whitelist. §7(já foi retirado)");
                                    player.getInventory().clear();
                                    EventoUtils.whitelist.remove(player.getUniqueId());
                                    KitUtils.clearKits(player);
                                    WarpUtils.clearWarp(player);
                                } else {
                                    player.sendMessage("§cA sala do evento já foi fechada. Fique ligado para quando for aberta para espectadores.");
                                }
                                return true;
                            }
                            playerData.setEvento(true);
                            player.teleport(EventoUtils.mainArena);
                            player.getInventory().clear();
                            player.sendMessage("§aVocê entrou no evento.");
                            KitUtils.clearKits(player);
                            WarpUtils.clearWarp(player);
                            break;
                        case "sair":
                            if (!playerData.evento) {
                                player.sendMessage("§cVocê não está no evento.");
                                return true;
                            }
                            playerData.setEvento(false);
                            PlayerUtils.sendPlayerToSpawn(player);
                            player.sendMessage("§cVocê saiu do evento.");
                            break;
                        case "spec":
                            if (playerData.evento) {
                                player.sendMessage("§cVocê está no evento.");
                                return true;
                            }
                            if (!(playerData.kitType == null && playerData.warpType == null)) {
                                player.sendMessage("§cVocê não pode ter kits selecionados para isso.");
                                return true;
                            }
                            if (!EventoUtils.specs) {
                                player.sendMessage("§cOs espectadores estão desativados no momento.");
                                return true;
                            }
                            player.teleport(EventoUtils.specLoc);
                            player.sendMessage("§aVocê está espectando o evento.");
                            break;
                        case "build":
                            if (EventoUtils.build) {
                                player.sendMessage("§cVocê desativou o build.");
                                EventoUtils.build = false;
                            } else {
                                player.sendMessage("§aVocê ativou o build.");
                                EventoUtils.build = true;
                            }
                            break;
                        case "cleararena":
                            EventoUtils.clearBlocks();
                            player.sendMessage("§aVocê limpou a arena. §7(tire as obsidians e cobblestones manualmente)");
                            break;
                        case "damage":
                            if (EventoUtils.damage) {
                                player.sendMessage("§cVocê desativou o damage. §7(lembre-se de desativar o §4pvp§7)");
                                EventoUtils.damage = false;
                            } else {
                                player.sendMessage("§aVocê ativou o damage. §7(lembre-se de ativar o §4pvp§7)");
                                EventoUtils.damage = true;
                            }
                            break;
                        case "effect":
                            if (args.length == 2) {
                                if (args[1].equalsIgnoreCase("clear")) {
                                    EventoUtils.getEventoPlayers().forEach(p -> p.getActivePotionEffects().forEach(ef -> p.removePotionEffect(ef.getType())));
                                    player.sendMessage("§aVocê limpou todos os efeitos ativos de todos os players.");
                                    return false;
                                }
                                else player.sendMessage(Messages.ARGUMENTOS_INSUFICIENTES.getUsage("evento effect <efeito/clear> <amplificador> <segundos> <player/all>"));
                                return false;
                            }
                            if (args.length < 5) {
                                sendHelp(player);
                                return true;
                            }
                            PotionEffectType potionEffectType = EventoUtils.getPotionEffectTypeByName(args[1]);
                            if (potionEffectType == null) {
                                player.sendMessage("§cEfeito inválido.");
                                return true;
                            }
                            int amplif;
                            int secs;
                            try {
                                amplif = Integer.parseInt(args[2]);
                                secs = Integer.parseInt(args[3]);
                            } catch (NumberFormatException exception) {
                                sendHelp(player);
                                return true;
                            }
                            if (args[4].equalsIgnoreCase("all")) {
                                EventoUtils.getEventoPlayers().forEach(p -> p.addPotionEffect(new PotionEffect(potionEffectType, secs * 20, amplif - 1)));
                                player.sendMessage("§aEfeito §e"+potionEffectType.getName() + " " + amplif + " §aaplicado para todos os jogadores no evento por §e" + secs + " segundos§a.");
                                return false;
                            } else {
                                Player target = Bukkit.getPlayer(args[4]);
                                if (target == null) {
                                    player.sendMessage("§cNão foi possível encontrar o player §e" + args[4] + "§c.");
                                    return true;
                                }
                                if (target == player) {
                                    player.sendMessage("§cVocê não pode dar efeitos diretamente para você mesmo.");
                                }
                                if (!Objects.requireNonNull(PlayerDataManager.getPlayerData(target)).evento) {
                                    player.sendMessage("§cEste player não está no evento.");
                                    return true;
                                }
                                target.addPotionEffect(new PotionEffect(potionEffectType, secs * 20, amplif));
                                player.sendMessage("§aEfeito §e"+potionEffectType.getName() + " " + amplif + " §aaplicado para §e" + target.getName() + " §apor §e" + secs + " segundos§a.");
                            }
                            break;
                        case "explicar":
                            if (args.length < 2) {
                                player.sendMessage("§cEscolha um evento para explicar");
                                return true;
                            }
                            EventoType evento = EventoType.getEventoByName(args[1]);
                            if (evento == null) {
                                player.sendMessage("§cOpção de evento inválida.");
                                return true;
                            }
                            player.sendMessage("§aIniciando explicação do evento §e" + evento.getName().toUpperCase() + "§a...");
                            EventoType.explicarEvento(evento);
                            break;
                        case "kick":
                            if (args.length < 2) {
                                sendHelp(player);
                                return true;
                            }
                            Player target = Bukkit.getPlayer(args[1]);
                            if (target == null) {
                                player.sendMessage("§cNão foi possível encontrar o player §e" + args[1] + "§c.");
                                return true;
                            }
                            if (target == player) {
                                player.sendMessage("§cTenho certeza que você não é tão retardado assim.");
                                player.sendMessage("§5toma aqui um easter egg então, r$r$r$");
                                return true;
                            }
                            if (!Objects.requireNonNull(PlayerDataManager.getPlayerData(target)).evento) {
                                player.sendMessage("§cEste player não está no evento.");
                                return true;
                            }
                            Objects.requireNonNull(PlayerDataManager.getPlayerData(target)).setEvento(false);
                            PlayerUtils.sendPlayerToSpawn(target);
                            target.sendMessage("§cVocê foi expulso do evento.");
                            player.sendMessage("§aVocê expulsou §e" + target.getName() + " §ado evento.");
                            break;
                        case "participantes":
                            int size = EventoUtils.getEventoPlayersNames().size();
                            player.sendMessage("§aO evento possui §e" + size + " players§a, sendo eles: §7" + StringUtils.join(EventoUtils.getEventoPlayersNames(), "§a, §7"));
                            break;
                        case "pvp":
                            if (EventoUtils.pvp) {
                                player.sendMessage("§cVocê desativou o pvp. §7(lembre-se de desativar o §4damage§7)");
                                EventoUtils.pvp = false;
                            } else {
                                player.sendMessage("§aVocê ativou o pvp. §7(lembre-se de ativar o §4damage§7)");
                                EventoUtils.pvp = true;
                            }
                            break;
                        case "setspecloc":
                            EventoUtils.specLoc = player.getLocation();
                            player.sendMessage("§aLocalização do spawn dos espectadores setada.");
                            break;
                        case "skit":
                            if (args.length < 2) {
                                sendHelp(player);
                                return true;
                            }
                            if (args[1].equalsIgnoreCase("all")) {
                                EventoUtils.getEventoPlayers().forEach(p -> {
                                    if (p == player) return;
                                    p.closeInventory();
                                    p.getInventory().setArmorContents(player.getInventory().getArmorContents());
                                    p.getInventory().setContents(player.getInventory().getContents());
                                    p.sendMessage("§aVocê recebeu o kit do evento.");
                                });
                                player.sendMessage("§aTodos os players receberam seu kit.");
                                return false;
                            }
                            Player t = Bukkit.getPlayer(args[1]);
                            if (t == null) {
                                player.sendMessage("§cNão foi possível encontrar o player §e" + args[1] + "§c.");
                                return true;
                            }
                            t.closeInventory();
                            t.getInventory().setArmorContents(player.getInventory().getArmorContents());
                            t.getInventory().setContents(player.getInventory().getContents());
                            t.sendMessage("§aVocê recebeu o kit do evento.");
                            player.sendMessage("§aO player §e" + t.getName() + " §arecebeu seu kit.");
                            break;
                        case "specs":
                            if (!EventoUtils.specs) {
                                if (EventoUtils.specLoc == null) {
                                    player.sendMessage("§cAntes de habilitar espectadores escolha a localização de spawn deles.");
                                    return true;
                                }
                                EventoUtils.specs = true;
                                player.sendMessage("§aVocê habilitou os espectadores.");
                            } else {
                                EventoUtils.specs = false;
                                player.sendMessage("§cVocê desabilitou os espectadores.");
                            }
                            break;
                        case "toggle":
                            if (!EventoUtils.tp) {
                                player.sendMessage("§aVocê ativou a entrada de novos players no evento.");
                                EventoUtils.tp = true;
                            } else {
                                player.sendMessage("§cVocê desativou a entrada de novos players no evento.");
                                EventoUtils.tp = false;
                            }
                            break;
                        case "tpall":
                            EventoUtils.getEventoPlayers().forEach(p -> p.teleport(player.getLocation()));
                            player.sendMessage("§aVocê teleportou todos os players do evento até você.");
                            break;
                        case "tpto":
                            if (args.length < 2) {
                                player.sendMessage("§cEscolha algum evento para teleportar.");
                                return true;
                            }
                            EventoType ev = EventoType.getEventoByName(args[1]);
                            if (ev == null) {
                                player.sendMessage("§cOpção de evento inválida.");
                                return true;
                            }
                            EventoUtils.getEventoPlayers().forEach(p -> p.teleport(ev.getLocation()));
                            break;
                        case "whitelist":
                            if (args.length < 3) {
                                if (args[1].equalsIgnoreCase("list")) {
                                    player.sendMessage("§aLista de players na whitelist: §7" + StringUtils.join(EventoUtils.getWhitelistPlayersNames(), "§a, §7"));
                                    return false;
                                }
                                sendHelp(player);
                                return true;
                            }
                            Player tt = Bukkit.getPlayer(args[2]);
                            if (tt == null) {
                                player.sendMessage("§cNão foi possível encontrar o player §e" + args[2] + "§c.");
                                return true;
                            }
                            if (args[1].equalsIgnoreCase("add")) {
                                if (EventoUtils.whitelist.contains(tt.getUniqueId())) {
                                    player.sendMessage("§cO player §e" + tt.getName() + " §cjá está na whitelist.");
                                    return true;
                                }
                                EventoUtils.whitelist.add(tt.getUniqueId());
                                player.sendMessage("§aO player §e" + tt.getName() + " §afoi adicionado na whitelist.");
                                return true;
                            } else if (args[1].equalsIgnoreCase("remove")) {
                                if (!EventoUtils.whitelist.contains(tt.getUniqueId())) {
                                    player.sendMessage("§cO player §e" + tt.getName() + " §cnão está na whitelist.");
                                    return true;
                                }
                                EventoUtils.whitelist.remove(tt.getUniqueId());
                                player.sendMessage("§aO player §e" + tt.getName() + " §afoi §cremovido §ada whitelist.");
                                return false;
                            } else {
                                player.sendMessage("§cNão foi possível encontrar essa opção.");
                                return false;
                            }
                        default:
                            sendHelp(player);
                            player.sendMessage("§cNão foi possível encontrar a opção §e" + args[0] + "§c.");
                            break;
                    }
                }
            }
        }
        return false;
    }
}
