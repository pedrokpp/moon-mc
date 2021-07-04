package me.kp.moon.moonpvp.evento;

import lombok.Getter;
import me.kp.moon.moonpvp.Main;
import me.kp.moon.moonpvp.enums.Strings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.List;

@Getter
public enum EventoType {


    PVP("ArenaPvP", Arrays.asList("Olá, jogadores! Sejam todos bem-vindos ao evento &c&lArena PvP&l&f!", "Ao início do evento, vocês jogadores receberão um kit pré-definido com certa quantia de \"mantimentos\".", "Ao decorrer do evento e a diminuição de players, todos serão puxados à novas arenas, e terão um novo kit.", "Todos terão 20 segundos de invencibilidade para se organizarem.", "Agora, iremos repassar as &c&lRegras do Evento&l&f:", "&c٠ &fNão ultrapassem nem andem por cima das obsidians das arenas, caso contrário, será desclassificado.", "&c٠ &fNão faça times a mais do que o evento proporciona, algo a mais resultará em kick.", "Iniciando o Evento! Boa sorte a todos e divirtam-se!"), new Location(Bukkit.getWorlds().get(0), 732.5, 80, 521.5)),
    LAVA("LavaChallenge", Arrays.asList("Olá, jogadores! Sejam todos bem-vindos ao evento LAVA!", "Ao início do evento, vocês jogadores receberão um kit pré-definido com certa quantia de \"mantimentos\".", "Todos serão colocados em uma arena com um nível de lava e todos terão de \"Tankar\" o dano até o último sobreviver!", "Durante o evento o kit não será reiniciado, portanto usem todas as sopas com sabedoria!", "Todos terão 10 segundos de invencibilidade até o dano ser liberado, portanto estejam preparados!", "Iniciando o Evento! Boa sorte a todos e divirtam-se!"), new Location(Bukkit.getWorlds().get(0), 641.5, 118, 518.5)),
    MDR("Mdr", Arrays.asList("Olá, jogadores! Sejam todos bem-vindos ao evento Mãe Da Rua!", "Ao início do evento, vocês jogadores receberão um kit pré-definido com maçãs douradas &9&lILIMITADAS&f!", "O grande objetivo desse evento é atravessar a \"Rua\" sem ser pego/a, portanto tomem cuidado com o pegador!", "Caso morra nesse evento você será desclassificado.", "Ou caso descumpra a travessia ou fique brincando também será desclassificado, lembre-se de seguir as regras do promotor do evento.", "Todos terão 10 segundos de invencibilidade ate o dano ser liberado, portanto estejam preparados!", "Iniciando o Evento! Boa sorte a todos e divirtam-se!"), new Location(Bukkit.getWorlds().get(0), 801.5, 100, 519.5)),
    _1v1("1v1", Arrays.asList("Olá, jogadores! Sejam todos bem-vindos ao evento 1x1!", "Ao início do evento, vocês jogadores receberão um kit pré-definido com certa quantia de \"mantimentos\".", "Ao decorrer do Evento e a diminuição de players, todos serão puxados para uma arena de 1 contra 1 e terão que duelar até um ser vitorioso!", "Todos terão 10 segundos de invencibilidade até o PvP ser liberado, portanto estejam preparados!", "Iniciando o Evento! Boa sorte a todos e divirtam-se!"), new Location(Bukkit.getWorlds().get(0), 868.5, 95, 457.5)),
    ;
    private final String name;
    private final List<String> explicacao;
    private final Location location;
    EventoType(String name, List<String> explicacao, Location location) {
        this.name = name;
        this.explicacao = explicacao;
        this.location = location;
    }

    public static EventoType getEventoByName(String name) {
        for (EventoType evento : EventoType.values()) {
            if (evento.getName().equalsIgnoreCase(name))
                return evento;
        }
        return null;
    }

    public static void explicarEvento(EventoType evento) {
        List<String> explic = evento.getExplicacao();
        int actualsec = 1;
        for (String message : explic) {
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                Bukkit.broadcastMessage(Strings.getPrefix() + " §f" + ChatColor.translateAlternateColorCodes('&', message));
            }, actualsec * 20L);
            actualsec += 5;
        }
    }

}


// mdr 801.5 100 519.5
// lava 641.5 118 518.5
// pvp 732.5 80 521.5
// 1v1 868.5 95 457.5