package de.tobi.ranksystem.main;

import de.tobi.ranksystem.commands.rankCommand;
import de.tobi.ranksystem.listeners.JoinListener;
import de.tobi.ranksystem.mysql.MySQL;
import de.tobi.ranksystem.mysql.SQLPerms;
import de.tobi.ranksystem.permissions.GroupManager;
import de.tobi.ranksystem.permissions.PermissionsGroup;
import de.tobi.ranksystem.util.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public final class Main extends JavaPlugin {

    public static MySQL mysql;
    private ConfigUtil configUtil;
    private static Main instance;
    private GroupManager groupManager;

    @Override
    public void onEnable() {
        instance = this;
        groupManager = new GroupManager();
        configUtil = new ConfigUtil(this);
        ConnectMySQL();
        createGroups();
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        getCommand("rank").setExecutor(new rankCommand());
    }

    private void ConnectMySQL() {
        mysql = new MySQL(configUtil.getConfig().getString("MySQL.host"), configUtil.getConfig().getString("MySQL.database"), configUtil.getConfig().getString("MySQL.user"), configUtil.getConfig().getString("MySQL.password"));

        mysql.update("CREATE TABLE IF NOT EXISTS Players(UUID varchar(64), RANK varchar(64))");
        mysql.update("CREATE TABLE IF NOT EXISTS Permissions(NAME varchar(64), SORTID int(64), PREFIX varchar(64), SUFFIX varchar(64), PERMISSIONS varchar(300))");
    }

    private void createGroups() {
        if(!SQLPerms.groupExists("default")) {
            ArrayList<String> list = new ArrayList<>();
            list.add("rank.default");
            PermissionsGroup permissionsGroup = new PermissionsGroup("default", 99, "§7Spieler §8: §7", "", list);
            groupManager.addGroup(permissionsGroup);
        }
        ResultSet rs = Main.mysql.query("SELECT * FROM Permissions");
        try {
            while(rs.next()) {
                ArrayList<String> list = new ArrayList<>();
                String permissionsString = rs.getString("PERMISSIONS");
                String[] permissions = permissionsString.split(";");
                for(int i = 0; i < permissions.length; i++) {
                    list.add(permissions[i]);
                }
                PermissionsGroup permissionsGroup = new PermissionsGroup(rs.getString("NAME"), rs.getInt("SORTID"), rs.getString("PREFIX"), rs.getString("SUFFIX"), list);
                groupManager.addGroup(permissionsGroup);
                System.out.println("Group created | " + permissionsGroup.getName());
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public ConfigUtil getConfigUtil() {
        return configUtil;
    }

    public static Main getInstance() {
        return instance;
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }
}
