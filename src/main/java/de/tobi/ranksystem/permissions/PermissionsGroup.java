package de.tobi.ranksystem.permissions;

import de.tobi.ranksystem.mysql.SQLGroup;
import de.tobi.ranksystem.mysql.SQLPerms;

import java.util.ArrayList;

public class PermissionsGroup {

    private String name;
    private int sortId;
    private String prefix;
    private String suffix;
    private ArrayList<String> permissions;

    public PermissionsGroup(String name, int sortId, String prefix, String suffix, ArrayList<String> permissions) {
        this.name = name;
        this.sortId = sortId;
        this.prefix = prefix;
        this.suffix = suffix;
        this.permissions = permissions;
    }

    public void addPermission(String permission) {
        permissions.add(permission);
        SQLPerms.addPermission(name, permission);
    }

    public void removePermission(String permission) {
        permissions.remove(permission);
        SQLPerms.removePermission(name, permission);
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public int getSortId() {
        return sortId;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        SQLPerms.setPrefix(name, prefix.replace("&", "§"));
    }

    public void setSortId(String sortId) {
        this.sortId = Integer.valueOf(sortId);
        SQLPerms.setSortId(name, String.valueOf(sortId));
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
        SQLPerms.setSuffix(name, suffix.replace("&", "§"));
    }
}
