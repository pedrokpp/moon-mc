package me.kp.moon.moonpvp.clan;

import me.kp.moon.moonpvp.mysql.MySQL;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ClanSQL {

    public static void createClan(final String clanName, final String clanTag, final String clanOwner, final ArrayList<String> clanMembers) {
        try {
            final PreparedStatement statement = MySQL.getConnection().prepareStatement("SELECT * FROM clans WHERE clan_name=?");
            statement.setString(1, clanName);
            final ResultSet results = statement.executeQuery();
            results.next();
            if (!clanExists(clanTag)) {
                final PreparedStatement insert = MySQL.getConnection().prepareStatement("INSERT INTO clans (clan_name, clan_tag, clan_owner, clan_members, clan_color) VALUES (?,?,?,?,?)");
                insert.setString(1, clanName);
                insert.setString(2, clanTag);
                insert.setString(3, clanOwner);
                final String formatter = clanMembers.toString().replace("[", "").replace("]", "");
                insert.setString(4, formatter);
                insert.setString(5, "8");
                insert.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setClanColor(final String clanTag, final String clanColor) {
        if (clanExists(clanTag)) {
            try {
                final PreparedStatement statement2 = MySQL.getConnection().prepareStatement("UPDATE clans SET clan_color=? WHERE clan_tag=?");
                statement2.setString(1, clanColor);
                statement2.setString(2, clanTag);
                statement2.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getClanColor(final String clanTag) {
        try {
            final PreparedStatement statement = MySQL.getConnection().prepareStatement("SELECT * FROM clans WHERE clan_tag=?");
            statement.setString(1, clanTag);
            final ResultSet results = statement.executeQuery();
            if (results.next())
                return results.getString("clan_color");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static String getClanTag(final UUID uuid) {
        try {
            final PreparedStatement statement = MySQL.getConnection().prepareStatement("SELECT * FROM clans WHERE clan_members LIKE ?");
            statement.setString(1, "%" + uuid + "%");
            final ResultSet results = statement.executeQuery();
            if (results.next()) {
                return results.getString("clan_tag");
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean clanExists(final String clanTag) {
        try {
            final PreparedStatement statement = MySQL.getConnection().prepareStatement("SELECT * FROM clans WHERE clan_tag=?");
            statement.setString(1, clanTag);
            final ResultSet results = statement.executeQuery();
            if (results.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getClanName(final String clanTag) {
        try {
            final PreparedStatement statement = MySQL.getConnection().prepareStatement("SELECT * FROM clans WHERE clan_tag=?");
            statement.setString(1, clanTag);
            final ResultSet results = statement.executeQuery();
            if (results.next())
                return results.getString("clan_name");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static String getClanTag(final String clanName) {
        try {
            final PreparedStatement statement = MySQL.getConnection().prepareStatement("SELECT * FROM clans WHERE clan_name=?");
            statement.setString(1, clanName);
            final ResultSet results = statement.executeQuery();
            results.next();
            return results.getString("clan_tag");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getClanTag(final Player player) {
        try {
            final PreparedStatement statement = MySQL.getConnection().prepareStatement("SELECT * FROM clans WHERE clan_members LIKE ?");
            statement.setString(1, "%" + player.getName() + "%");
            final ResultSet results = statement.executeQuery();
            if (results.next()) {
                return results.getString("clan_tag");
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getClanOwner(final String clanTag) {
        try {
            final PreparedStatement statement = MySQL.getConnection().prepareStatement("SELECT * FROM clans WHERE clan_tag=?");
            statement.setString(1, clanTag);
            final ResultSet results = statement.executeQuery();
            if (results.next()) {
                return results.getString("clan_owner");
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getAllClanTags() {
        // SELECT * FROM `s4213_clans`.`clans` ORDER BY `clan_tag` ASC LIMIT 1000;
        List<String> clanTags = new ArrayList<>();
        try {
            final PreparedStatement statement = MySQL.getConnection().prepareStatement("SELECT * FROM clans ORDER BY `clan_tag` ASC");
            final ResultSet results = statement.executeQuery();
            while (results.next()) {
                clanTags.add(results.getString("clan_tag"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clanTags.isEmpty() ? null : clanTags;
    }

    public static List<String> getClanMembers(final String clanTag) {
        try {
            final PreparedStatement statement = MySQL.getConnection().prepareStatement("SELECT * FROM clans WHERE clan_tag=?");
            statement.setString(1, clanTag);
            final ResultSet results = statement.executeQuery();
            if (results.next()) {
                final String line = results.getString("clan_members");
                final String[] str = line.split(", ");
                List<String> al;
                al = Arrays.asList(str);
                return al;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void deleteClan(final String clanTag) {
        if (clanExists(clanTag)) {
            try {
                final PreparedStatement statement2 = MySQL.getConnection().prepareStatement("DELETE FROM clans WHERE clan_tag=?");
                statement2.setString(1, clanTag);
                statement2.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void removeClanMember(final Player player, final String clanTag) {
        List<String> cl = getClanMembers(clanTag);
        if (cl == null) return;
        if (clanExists(clanTag) && cl.contains(player.getName())) {
            String membros = cl.toString();
            membros = membros.replace("[", "");
            membros = membros.replace("]", "");
            final String resultado_final;
            membros = (resultado_final = membros.replace(", " + player.getName(), ""));
            try {
                final PreparedStatement statement2 = MySQL.getConnection().prepareStatement("UPDATE clans SET clan_members=? WHERE clan_tag=?");
                statement2.setString(1, membros);
                statement2.setString(2, " ");
                statement2.executeUpdate();
                final PreparedStatement statement3 = MySQL.getConnection().prepareStatement("UPDATE clans SET clan_members=? WHERE clan_tag=?");
                statement3.setString(1, resultado_final);
                statement3.setString(2, clanTag);
                statement3.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addClanMember(final Player player, final String clanTag) {
        List<String> cl = getClanMembers(clanTag);
        if (cl == null) return;
        if (clanExists(clanTag) && !cl.contains(player.getName())) {
            String membros = cl.toString();
            membros = membros.replace("[", "");
            membros = membros.replace("]", "");
            final String resultado_final;
            membros = (resultado_final = membros + ", " + player.getName());
            try {
                final PreparedStatement statement2 = MySQL.getConnection().prepareStatement("UPDATE clans SET clan_members=? WHERE clan_tag=?");
                statement2.setString(1, membros);
                statement2.setString(2, " ");
                statement2.executeUpdate();
                final PreparedStatement statement3 = MySQL.getConnection().prepareStatement("UPDATE clans SET clan_members=? WHERE clan_tag=?");
                statement3.setString(1, resultado_final);
                statement3.setString(2, clanTag);
                statement3.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
