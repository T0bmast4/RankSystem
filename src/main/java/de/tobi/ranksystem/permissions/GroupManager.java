package de.tobi.ranksystem.permissions;

import de.tobi.ranksystem.main.Main;
import de.tobi.ranksystem.mysql.SQLGroup;
import de.tobi.ranksystem.mysql.SQLPerms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;

public class GroupManager {

    public HashMap<String, PermissionsGroup> groups = new HashMap<>();

    public PermissionsGroup getGroup(String name) {
        return groups.get(name);
    }

    public void addGroup(PermissionsGroup permissionsGroup) {
        groups.put(permissionsGroup.getName(), permissionsGroup);
        SQLPerms.createGroup(permissionsGroup.getName(), permissionsGroup.getSortId(), permissionsGroup.getPrefix(), permissionsGroup.getSuffix());
    }

    public void removeGroup(String name) {
        groups.remove(name);
        SQLPerms.removeGroup(name);
    }

    public boolean groupExists(String name) {
        if(SQLPerms.groupExists(name)) {
            return true;
        }
        return false;
    }

    public void setPlayerGroup(Player player, String name) {
        PermissionAttachment permissionAttachment = player.addAttachment(Main.getPlugin(Main.class));
        permissionAttachment.unsetPermission("rank." + SQLGroup.getPlayerGroup(player.getUniqueId().toString()));
        permissionAttachment.setPermission("rank." + name, true);
        SQLGroup.setPlayerGroup(player.getUniqueId().toString(), name);
    }

    public HashMap<String, PermissionsGroup> getGroups() {
        return groups;
    }
}
