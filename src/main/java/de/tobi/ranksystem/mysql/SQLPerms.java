package de.tobi.ranksystem.mysql;

import de.tobi.ranksystem.main.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLPerms {

    private static Main plugin = Main.getInstance();

    public static boolean groupExists(String groupName) {

        try {
            ResultSet rs = Main.mysql.query("SELECT * FROM Permissions WHERE NAME= '" + groupName + "'");

            if(rs.next()) {
                return rs.getString("NAME") != null;
            }
            return false;
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void createGroup(String groupName, int sortId, String prefix, String suffix) {
        if(!(groupExists(groupName))) {
            Main.mysql.update("INSERT INTO Permissions(NAME, SORTID, PREFIX, SUFFIX, PERMISSIONS) VALUES ('" + groupName + "', '" + sortId + "', '" + prefix + "', '" + suffix + "', 'rank." + groupName + "');");
        }
    }

    public static void removeGroup(String groupName) {
        if(groupExists(groupName)) {
            Main.mysql.update("DELETE FROM Permissions WHERE NAME='" + groupName + "';");
        }
    }

    public static void addPermission(String groupName, String permission) {
        String permissions = "";
        try {
            ResultSet rs = Main.mysql.query("SELECT * FROM Permissions WHERE NAME = '" + groupName + "'");
            if (rs.next())
                permissions = rs.getString("PERMISSIONS");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        permissions = permissions + ";" + permission;
        Main.mysql.update("UPDATE Permissions SET PERMISSIONS= '" + permissions + "' WHERE NAME= '" + groupName + "';");
    }

    public static void removePermission(String groupName, String permission) {
        String permissions = "";
        try {
            ResultSet rs = Main.mysql.query("SELECT * FROM Permissions WHERE NAME = '" + groupName + "'");
            if (rs.next())
                permissions = rs.getString("PERMISSIONS");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        permissions = permissions.replace(";" + permission, "");
        Main.mysql.update("UPDATE Permissions SET PERMISSIONS= '" + permissions + "' WHERE NAME= '" + groupName + "';");
    }

    public static void setPrefix(String groupName, String prefix) {
        if (groupExists(groupName)) {
            Main.mysql.update("UPDATE Permissions SET PREFIX= '" + prefix + "' WHERE NAME= '" + groupName + "';");
        }
    }

    public static void setSuffix(String groupName, String suffix) {
        if (groupExists(groupName)) {
            Main.mysql.update("UPDATE Permissions SET SUFFIX= '" + suffix + "' WHERE NAME= '" + groupName + "';");
        }
    }

    public static void setSortId(String groupName, String sortId) {
        if (groupExists(groupName)) {
            Main.mysql.update("UPDATE Permissions SET SORTID= '" + sortId + "' WHERE NAME= '" + groupName + "';");
        }
    }
}
