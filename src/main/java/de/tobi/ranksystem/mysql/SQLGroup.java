package de.tobi.ranksystem.mysql;

import de.tobi.ranksystem.main.Main;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLGroup {

    private static Main plugin = Main.getInstance();


    public static boolean playerExists(String uuid) {

        try {
            ResultSet rs = Main.mysql.query("SELECT * FROM Players WHERE UUID= '" + uuid + "'");

            if(rs.next()) {
                return rs.getString("UUID") != null;
            }
            return false;
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void registerPlayer(String uuid) {
        if(!(playerExists(uuid))) {
            Main.mysql.update("INSERT INTO Players(UUID, RANK) VALUES ('" + uuid + "', 'default');");
        }
    }

    public static void setPlayerGroup(String uuid, String rank) {
        if (playerExists(uuid)) {
            Main.mysql.update("UPDATE Players SET RANK= '" + rank + "' WHERE UUID= '" + uuid + "';");
        } else {
            registerPlayer(uuid);
            setPlayerGroup(uuid, rank);
        }
    }

    public static String getPlayerGroup(String uuid) {
        try {
            ResultSet rs = Main.mysql.query("SELECT * FROM Players WHERE UUID = '" + uuid + "'");
            if (rs.next())
                return rs.getString("RANK");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
}
