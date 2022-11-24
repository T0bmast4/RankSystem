package de.tobi.ranksystem.listeners;

import de.tobi.ranksystem.main.Main;
import de.tobi.ranksystem.mysql.SQLGroup;
import de.tobi.ranksystem.permissions.GroupManager;
import de.tobi.ranksystem.permissions.PermissionsGroup;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.PermissionAttachment;

public class JoinListener implements Listener {

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        SQLGroup.registerPlayer(player.getUniqueId().toString());

        GroupManager groupManager = Main.getInstance().getGroupManager();
        PermissionsGroup permissionsGroup = groupManager.getGroup(SQLGroup.getPlayerGroup(player.getUniqueId().toString()));

        if(permissionsGroup == null) {
            PermissionsGroup defaultGroup = groupManager.getGroup("default");
            PermissionAttachment attachment = player.addAttachment(Main.getPlugin(Main.class));
            for (int i = 0; i < defaultGroup.getPermissions().size(); i++) {
                attachment.setPermission(defaultGroup.getPermissions().get(i), true);
            }
            SQLGroup.setPlayerGroup(player.getUniqueId().toString(), "default");
        }else {
            PermissionAttachment attachment = player.addAttachment(Main.getPlugin(Main.class));
            for (int i = 0; i < permissionsGroup.getPermissions().size(); i++) {
                attachment.setPermission(permissionsGroup.getPermissions().get(i), true);
            }
        }
    }
}
