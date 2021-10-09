package me.kp.moon.moondiscord.mysql;

import org.bukkit.Bukkit;

import java.sql.*;

public class MySQL {

    public static Connection connection;
    public static String host, database, username, password, table;
    public static int port;

    public void connectToDBS() {
        host = "190.115.197.21";
        port = 3306;
        database = "s4213_clans";
        username = "u4213_fYXySgriqK";
        password = "YlWUstG4=dE6G@QqxkmrFElj";
        table = "discord";

        try {
            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    Bukkit.getConsoleSender().sendMessage("§9[MoonDiscord] §cNão foi possível conectar-se ao Banco de dados!");
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port +
                        "/" + database, username, password));
                Bukkit.getConsoleSender().sendMessage("§9[MoonDiscord] §aConectado ao Banco de dados!");
            }

        } catch (SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        MySQL.connection = connection;
    }

    /*  MySQL Setter Getter  */

    public boolean discordIDExiste(String discordID) {
        try {
            PreparedStatement preparedStatement = this.getConnection().prepareStatement("SELECT * FROM "
                    + table + " WHERE Discord_ID = ?");
            preparedStatement.setString(1, discordID);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public boolean uuidExiste(String uuid) {
        try {
            PreparedStatement preparedStatement = this.getConnection().prepareStatement("SELECT * FROM "
                    + table + " WHERE UUID = ?");
            preparedStatement.setString(1, uuid.toString());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public boolean hasDisplayTAG(String uuid) {
        try {
            PreparedStatement preparedStatement = this.getConnection().prepareStatement("SELECT DisplayTAG FROM "
                    + table + " WHERE UUID = ?");
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public void createUser(String uuid, String uname, String discordID, String playerTAG) {
        if (discordIDExiste(discordID)) return;
        try {
            PreparedStatement preparedStatement = this.getConnection().prepareStatement("INSERT INTO "
                    + table + " (Username, UUID, Discord_ID, DisplayTAG) VALUE (?,?,?,?)");
            preparedStatement.setString(1, uname);
            preparedStatement.setString(2, uuid);
            preparedStatement.setString(3, discordID);
            preparedStatement.setString(4, playerTAG);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public String getDiscordIDFromUUID(String uuid) {
        if (!uuidExiste(uuid)) return null;
        try {
            PreparedStatement preparedStatement = this.getConnection().prepareStatement("SELECT * FROM "
                    + table + " WHERE UUID = ?");
            preparedStatement.setString(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString("Discord_ID");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public String getUUIDFromDiscordID(String discordID) {
        if (!discordIDExiste(discordID)) return null;
        try {
            PreparedStatement preparedStatement = this.getConnection().prepareStatement("SELECT * FROM "
                    + table + " WHERE Discord_ID = ?");
            preparedStatement.setString(1, discordID);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString("UUID");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public String getPlayerNameFromDiscordID(String discordID) {
        if (!discordIDExiste(discordID)) return null;
        try {
            PreparedStatement preparedStatement = this.getConnection().prepareStatement("SELECT * FROM "
                    + table + " WHERE Discord_ID = ?");
            preparedStatement.setString(1, discordID);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString("Username");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public String getDisplayTAGFromUUID(String uuid) {
        if (!uuidExiste(uuid)) return null;
        try {
            PreparedStatement preparedStatement = this.getConnection().prepareStatement("SELECT * FROM "
                    + table + " WHERE UUID = ?");
            preparedStatement.setString(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString("DisplayTAG");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public void updateDisplayTAG(String uuid, String displayTag) {
        if (!uuidExiste(uuid)) return;
        try {
            PreparedStatement preparedStatement = this.getConnection().prepareStatement("UPDATE "
                    + table + " SET DisplayTAG=? WHERE UUID=?");
            preparedStatement.setString(1, displayTag);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

}


/*
190.115.196.51
3306
s4213_clans
u4213_fYXySgriqK
MpfXYXUq4Ju7=!@ZRh5D9zpP
 */