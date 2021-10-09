package me.kp.moon.moonbans.mysql;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQL {

    public static Connection connection;
    public static String host, database, username, password;
    public static int port;

    public void connectToDBS() {
        host = "190.115.197.21";
        port = 3306;
        database = "s4213_clans";
        username = "u4213_fYXySgriqK";
        password = "YlWUstG4=dE6G@QqxkmrFElj";

        try {
            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    Bukkit.getConsoleSender().sendMessage("§9[MoonBans] §cNão foi possível conectar-se ao Banco de dados!");
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port +
                        "/" + database, username, password));
                Bukkit.getConsoleSender().sendMessage("§9[MoonBans] §aConectado ao Banco de dados!");
            }

        } catch (SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        MySQL.connection = connection;
    }

    public static boolean playerExiste(Player player) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM " +
                    " bans WHERE UUID = ?");
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public static boolean IPExiste(String ip) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM " +
                    " bans WHERE IP = ?");
            preparedStatement.setString(1, ip);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public static boolean UsernameExiste(String username) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM " +
                    " bans WHERE Username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public static void createUser(Player player) {
        if (playerExiste(player)) return;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO "
                    + "bans " +
                    "(UUID, Username, IP, " +
                    "Banned, TempBanTime, BanReason, BanDate, BanAuthor, " +
                    "Muted, TempMuteTime, MuteReason, MuteDate, MuteAuthor) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, player.getUniqueId().toString()); // UUID
            preparedStatement.setString(2, player.getName()); // Username
            preparedStatement.setString(3, player.getAddress().getHostName()); // IP
            preparedStatement.setInt(4, 0); // Banned
            preparedStatement.setLong(5, 0); // TempBanTime
            preparedStatement.setString(6, "None"); // BanReason
            preparedStatement.setDate(7, null); // BanDate
            preparedStatement.setString(8, "None"); // BanAuthor
            preparedStatement.setInt(9, 0); // Muted
            preparedStatement.setLong(10, 0); // TempMuteTime
            preparedStatement.setString(11, "None"); // MuteReason
            preparedStatement.setDate(12, null); // MuteDate
            preparedStatement.setString(13, "None"); // MuteAuthor
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static String getUsername(String ip, boolean check) {
        if (check) if (!IPExiste(ip)) return null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM " +
                    " bans WHERE IP = ?");
            preparedStatement.setString(1, ip);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return resultSet.getString("Username");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static String getIP(String username, boolean check) {
        if (check) if (!UsernameExiste(username)) return null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM " +
                    " bans WHERE Username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return resultSet.getString("IP");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static List<String> getAlts(String username, boolean check) {
        if (check) if (!UsernameExiste(username)) return null;
        List<String> nicks = new ArrayList<>();
        String ip = getIP(username, false);
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM " +
                    " bans WHERE IP = ?");
            preparedStatement.setString(1, ip);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
               nicks.add(resultSet.getString("Username"));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return nicks;
    }
    
    // MUTES
    public static int isMuted(String args, boolean check) {
        boolean ip = args.contains(".");
        if (check) {
            if (ip) if (!IPExiste(args)) return -1;
            else if (!UsernameExiste(args)) return -1;
        }
        String ident = ip ? "IP" : "Username";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM " +
                    " bans WHERE " + ident + " = ?");
            preparedStatement.setString(1, args);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return resultSet.getInt("Muted");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public static Long getTempMuteTime(String args, boolean check) {
        boolean ip = args.contains(".");
        if (check) {
            if (ip) if (!IPExiste(args)) return -3L;
            else if (!UsernameExiste(args)) return -3L;
        }
        String ident = ip ? "IP" : "Username";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM " +
                    " bans WHERE " + ident + " = ?");
            preparedStatement.setString(1, args);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return resultSet.getLong("TempMuteTime");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return -3L;
    }

    public static String getMuteAuthor(String args, boolean check) {
        boolean ip = args.contains(".");
        if (check) {
            if (ip) if (!IPExiste(args)) return null;
            else if (!UsernameExiste(args)) return null;
        }
        String ident = ip ? "IP" : "Username";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM " +
                    " bans WHERE " + ident + " = ?");
            preparedStatement.setString(1, args);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return resultSet.getString("MuteAuthor");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static String getMuteReason(String args, boolean check) {
        boolean ip = args.contains(".");
        if (check) {
            if (ip) if (!IPExiste(args)) return null;
            else if (!UsernameExiste(args)) return null;
        }
        String ident = ip ? "IP" : "Username";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM " +
                    " bans WHERE " + ident + " = ?");
            preparedStatement.setString(1, args);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return resultSet.getString("MuteReason");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static Date getMuteDate(String args, boolean check) {
        boolean ip = args.contains(".");
        if (check) {
            if (ip) if (!IPExiste(args)) return null;
            else if (!UsernameExiste(args)) return null;
        }
        String ident = ip ? "IP" : "Username";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM " +
                    " bans WHERE " + ident + " = ?");
            preparedStatement.setString(1, args);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return resultSet.getDate("MuteDate");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static void applyMute(String args, Long time, String reason, String author, boolean check) {
        boolean ip = args.contains(".");
        if (check) {
            if (ip) if (!IPExiste(args)) return;
            else if (!UsernameExiste(args)) return;
        }
        String ident = ip ? "IP" : "Username";
        try {
            java.util.Date date = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE "
                    + "bans SET Muted=1,TempMuteTime=?,MuteReason=?,MuteAuthor=?,MuteDate=? WHERE " + ident + "=?");
            preparedStatement.setLong(1, time);
            preparedStatement.setString(2, reason);
            preparedStatement.setString(3, author);
            preparedStatement.setDate(4, sqlDate);
            preparedStatement.setString(5, args);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void applyUnmute(String args, boolean check) {
        boolean ip = args.contains(".");
        if (check) {
            if (ip) if (!IPExiste(args)) return;
            else if (!UsernameExiste(args)) return;
        }
        String ident = ip ? "IP" : "Username";
        try {
            java.util.Date date = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE "
                    + "bans SET Muted=0,TempMuteTime=0,MuteReason=?,MuteAuthor=? WHERE " + ident + "=?");
            preparedStatement.setString(1, "None");
            preparedStatement.setString(2, "None");
            preparedStatement.setString(3, args);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    // BANS
    public static int isBanned(String args, boolean check) {
        boolean ip = args.contains(".");
        if (check) {
            if (ip) if (!IPExiste(args)) return -1;
            else if (!UsernameExiste(args)) return -1;
        }
        String ident = ip ? "IP" : "Username";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM " +
                    " bans WHERE " + ident + " = ?");
            preparedStatement.setString(1, args);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return resultSet.getInt("Banned");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return -1;
    }
    
    public static Long getTempBanTime(String args, boolean check) {
        boolean ip = args.contains(".");
        if (check) {
            if (ip) if (!IPExiste(args)) return -3L;
            else if (!UsernameExiste(args)) return -3L;
        }
        String ident = ip ? "IP" : "Username";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM " +
                    " bans WHERE " + ident + " = ?");
            preparedStatement.setString(1, args);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return resultSet.getLong("TempBanTime");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return -3L;
    }

    public static String getBanAuthor(String args, boolean check) {
        boolean ip = args.contains(".");
        if (check) {
            if (ip) if (!IPExiste(args)) return null;
            else if (!UsernameExiste(args)) return null;
        }
        String ident = ip ? "IP" : "Username";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM " +
                    " bans WHERE " + ident + " = ?");
            preparedStatement.setString(1, args);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return resultSet.getString("BanAuthor");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static String getBanReason(String args, boolean check) {
        boolean ip = args.contains(".");
        if (check) {
            if (ip) if (!IPExiste(args)) return null;
            else if (!UsernameExiste(args)) return null;
        }
        String ident = ip ? "IP" : "Username";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM " +
                    " bans WHERE " + ident + " = ?");
            preparedStatement.setString(1, args);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return resultSet.getString("BanReason");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static Date getBanDate(String args, boolean check) {
        boolean ip = args.contains(".");
        if (check) {
            if (ip) if (!IPExiste(args)) return null;
            else if (!UsernameExiste(args)) return null;
        }
        String ident = ip ? "IP" : "Username";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM " +
                    " bans WHERE " + ident + " = ?");
            preparedStatement.setString(1, args);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return resultSet.getDate("BanDate");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static void applyBan(String args, Long time, String reason, String author, boolean check) {
        boolean ip = args.contains(".");
        if (check) {
            if (ip) if (!IPExiste(args)) return;
            else if (!UsernameExiste(args)) return;
        }
        String ident = ip ? "IP" : "Username";
        try {
            java.util.Date date = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE "
                    + "bans SET Banned=1,TempBanTime=?,BanReason=?,BanAuthor=?,BanDate=? WHERE " + ident + "=?");
            preparedStatement.setLong(1, time);
            preparedStatement.setString(2, reason);
            preparedStatement.setString(3, author);
            preparedStatement.setDate(4, sqlDate);
            preparedStatement.setString(5, args);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void applyUnban(String args, boolean check) {
        boolean ip = args.contains(".");
        if (check) {
            if (ip) if (!IPExiste(args)) return;
            else if (!UsernameExiste(args)) return;
        }
        String ident = ip ? "IP" : "Username";
        try {
            java.util.Date date = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE "
                    + "bans SET Banned=0,TempBanTime=0,BanReason=?,BanAuthor=? WHERE " + ident + "=?");
            preparedStatement.setString(1, "None");
            preparedStatement.setString(2, "None");
            preparedStatement.setString(3, args);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

}