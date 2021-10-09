package me.kp.moon.moonlogin.mysql;

import me.kp.moon.moonlogin.auth.AuthAPI;
import me.kp.moon.moonlogin.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

public class MySQL {

    public static Connection connection;
    public static String host, database, username, password;
    public static int port;

    public static Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        MySQL.connection = connection;
    }

    public static boolean playerExiste(Player player) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM " +
                    " login WHERE UUID = ?");
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public static boolean playerExiste(UUID uuid) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM " +
                    " login WHERE UUID = ?");
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public static boolean playerExiste(String playerName) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM " +
                    " login WHERE Username = ?");
            preparedStatement.setString(1, playerName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public static void registerPlayer(PlayerData playerData) {
        Player player = playerData.getPlayer();
        if (playerExiste(player)) return;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO "
                    + "login " +
                    "(UUID, IP, Username, Password)" +
                    "VALUES (?,?,?,?)");
            preparedStatement.setString(1, player.getUniqueId().toString()); // UUID
            preparedStatement.setString(2, player.getAddress().getHostName().trim()); // IP
            preparedStatement.setString(3, player.getName()); // Username
            preparedStatement.setString(4, AuthAPI.encodeString(playerData.getPassword())); // Password encoded

            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void cacheDecodedPassword(PlayerData playerData, boolean check) {
        Player player = playerData.getPlayer();
        if (check) {
            if (!playerExiste(player)) return;
        }
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM " +
                    " login WHERE UUID = ?");
            preparedStatement.setString(1, player.getUniqueId().toString()); // UUID

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String pass = resultSet.getString("Password");
                playerData.setPassword(AuthAPI.decodeString(pass));
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void unregisterPlayer(Player player, boolean check) {
        if (check) {
            if (!playerExiste(player)) return;
        }
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("DELETE FROM " +
                    " login WHERE UUID = ?");
            preparedStatement.setString(1, player.getUniqueId().toString()); // UUID
            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        System.out.println("Unregister do player " + player.getName() + " aplicado (2)");
    }

    public static boolean unregisterPlayer(String playerName, boolean check) {
        if (check) {
            if (!playerExiste(playerName)) return false;
        }
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("DELETE FROM " +
                    " login WHERE UUID = ?");
            preparedStatement.setString(1, playerName); // UUID
            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        System.out.println("Unregister do player " + playerName + " aplicado (2)");
        return true;
    }

    public static void updatePassword(Player player, String newPassword, boolean check) {
        if (check) {
            if (!playerExiste(player)) return;
        }
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE login " +
                    "SET Password = ? WHERE UUID = ?");
            preparedStatement.setString(1, AuthAPI.encodeString(newPassword)); // UUID
            preparedStatement.setString(2, player.getUniqueId().toString()); // UUID

            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void connectToDBS() {
        host = "190.115.197.21";
        port = 3306;
        database = "s4213_clans";
        username = "u4213_fYXySgriqK";
        password = "YlWUstG4=dE6G@QqxkmrFElj";

        try {
            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    Bukkit.getConsoleSender().sendMessage("§9[MoonLogin] §cNão foi possível conectar-se ao Banco de dados!");
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port +
                        "/" + database, username, password));
                Bukkit.getConsoleSender().sendMessage("§9[MoonLogin] §aConectado ao Banco de dados!");
            }

        } catch (SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

}