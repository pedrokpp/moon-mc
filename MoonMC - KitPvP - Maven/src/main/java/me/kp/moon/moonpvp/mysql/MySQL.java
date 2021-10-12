package me.kp.moon.moonpvp.mysql;

import me.kp.moon.moonpvp.data.PlayerData;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.UUID;

public class MySQL {

    private static Connection connection;

    public void connectToDBS() {
        try {
            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed())
                    return;

                Class.forName("com.mysql.jdbc.Driver");
                String host = "";
                int port = 0;
                String database = "";
                String username = "";
                String password = "";
                setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database,
                        username, password));
                Bukkit.broadcastMessage("§a§l[MYSQL] §fConectado ao banco de dados.");
            }
        } catch (SQLException | ClassNotFoundException exception) {
            Bukkit.broadcastMessage("§c§l[MYSQL] §cNão foi possível conectar no banco de dados.");
            exception.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        MySQL.connection = connection;
    }

    public static boolean playerExiste(String playerName) {
        try {
            final PreparedStatement statement = MySQL.getConnection().prepareStatement("SELECT * FROM player_stats_2 WHERE " +
                    "Username=?");
            statement.setString(1, playerName);
            final ResultSet results = statement.executeQuery();
            if (results.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean playerExiste(UUID uuid) {
        try {
            final PreparedStatement statement = MySQL.getConnection().prepareStatement("SELECT * FROM player_stats_2 WHERE " +
                    "UUID=?");
            statement.setString(1, uuid.toString());
            final ResultSet results = statement.executeQuery();
            if (results.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void createUser(UUID uuid) {
        if (MySQL.playerExiste(uuid))
            return;
        try {
            final PreparedStatement insert = MySQL.getConnection().prepareStatement("INSERT INTO player_stats_2 " +
                    "(UUID, Coins, XP, Kills, Deaths, LavaFacil, LavaMedio, LavaDificil, LavaInsano) VALUES (?,0,0,0,0,0,0,0,0)");
            insert.setString(1, uuid.toString());
            insert.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void updatePlayer(PlayerData playerData, boolean check) {
        if (check) {
            if (!playerExiste(playerData.uuid))
                return;
        }
        try {
            final PreparedStatement statement = MySQL.getConnection().prepareStatement("UPDATE player_stats_2" +
                    "SET (LastTAG, Coins, XP, Kills, Deaths, LavaFacil, LavaMedio, LavaDificil, LavaInsano, LastSkin, Username) " +
                    "WHERE UUID=?");
            statement.setString(1, playerData.lastTag);
            statement.setInt(2, playerData.cacheCoins);
            statement.setInt(3, playerData.cacheXP);
            statement.setInt(4, playerData.cacheKills);
            statement.setInt(5, playerData.cacheDeaths);
            statement.setInt(6, playerData.cacheLavaFacil);
            statement.setInt(7, playerData.cacheLavaMedio);
            statement.setInt(8, playerData.cacheLavaDificil);
            statement.setInt(9, playerData.cacheLavaDificil);
            statement.setString(10, playerData.lastSkin);
            statement.setString(11, playerData.getPlayer().getName());
            statement.setString(12, playerData.uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void cachePlayer(PlayerData playerData, boolean check) {
        if (check) {
            if (MySQL.playerExiste(playerData.uuid))
                return;
        }
        try {
            final PreparedStatement statement = MySQL.getConnection().prepareStatement("SELECT * FROM player_stats_2 WHERE " +
                    "UUID=?");
            statement.setString(1, playerData.uuid.toString());
            final ResultSet results = statement.executeQuery();
            if (results.next()) {
                playerData.cacheKills = results.getInt("Kills");
                playerData.cacheDeaths = results.getInt("Deaths");
                playerData.cacheCoins = results.getInt("Coins");
                playerData.cacheXP = results.getInt("XP");
                playerData.cacheLavaFacil = results.getInt("LavaFacil");
                playerData.cacheLavaMedio = results.getInt("LavaMedio");
                playerData.cacheLavaDificil = results.getInt("LavaDificil");
                playerData.cacheLavaInsano = results.getInt("LavaInsano");
                playerData.lastSkin = results.getString("LastSkin");
                playerData.lastTag = results.getString("LastTAG");
                playerData.username = results.getString("Username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getTop10(String diff) {
        try {
            final PreparedStatement statement = MySQL.getConnection().prepareStatement("SELECT * FROM player_stats2" +
                    " ORDER BY " + diff + " LIMIT 10");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                return resultSet;
            else
                return null;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

}
