package de.tobi.ranksystem.commands;

import de.tobi.ranksystem.main.Main;
import de.tobi.ranksystem.mysql.SQLGroup;
import de.tobi.ranksystem.permissions.GroupManager;
import de.tobi.ranksystem.permissions.PermissionsGroup;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.ArrayList;

public class rankCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player)sender;
            if(player.hasPermission("rank.inhaber")) {
                GroupManager groupManager = Main.getInstance().getGroupManager();
                if(args.length == 3) {
                    if (args[1].equalsIgnoreCase("set")) {
                        if(groupManager.groupExists(args[2])) {
                            groupManager.setPlayerGroup(player, args[2]);
                            player.sendMessage("§aDu hast den Spieler §7" + player.getName() + " §aden Rang §7" + args[2] + " §agegeben.");
                        }else{
                            player.sendMessage("§cDiese Gruppe existiert nicht.");
                        }
                    } else if (args[1].equalsIgnoreCase("unset")) {
                        groupManager.setPlayerGroup(player, "default");
                        player.sendMessage("§aDu hast den Spieler §7" + player.getName() + " §aden Rang §7" + args[2] + " §centzogen.");
                    } else {
                        sendFeedback(player);
                    }
                }else if(args.length == 2) {
                    if (args[0].equalsIgnoreCase("create")) {
                        if(!groupManager.groupExists(args[1])) {
                            PermissionsGroup permissionsGroup = new PermissionsGroup(args[1], 99, "", "", new ArrayList<>());
                            groupManager.addGroup(permissionsGroup);
                            player.sendMessage("§aDu hast die Gruppe §7" + permissionsGroup.getName() + " §aerstellt!");
                        }else{
                            player.sendMessage("§cDiese Gruppe existiert bereits!");
                        }
                    } else if (args[0].equalsIgnoreCase("delete")) {
                        if(groupManager.groupExists(args[1])) {
                            groupManager.removeGroup(args[1]);
                            player.sendMessage("§aDu hast die Gruppe §7" + args[1] + " §cgelöscht!");
                        }else{
                            player.sendMessage("§cDiese Gruppe existiert nicht!");
                        }
                    }else{
                        sendFeedback(player);
                    }

                }else if(args.length >= 4) {
                    if(groupManager.groupExists(args[0])) {
                        if (args[2].equalsIgnoreCase("permission")) {
                            if (args[1].equalsIgnoreCase("add")) {
                                groupManager.getGroup(args[0]).addPermission(args[3]);
                                player.sendMessage("§aDu hast der Gruppe §7" + args[0] + " §adie Permissions §6" + args[3] + " §ahinzugefügt!");
                            } else if (args[1].equalsIgnoreCase("remove")) {
                                groupManager.getGroup(args[0]).removePermission(args[3]);
                                player.sendMessage("§aDu hast der Gruppe §7" + args[0] + " §adie Permissions §6" + args[3] + " §centfernt!");
                            }
                        } else if (args[2].equalsIgnoreCase("prefix")) {
                            StringBuilder builder = new StringBuilder();
                            for(int i = 3; i < args.length; i++) {
                                if(i == args.length) {
                                    builder.append(args[i]);
                                }else{
                                    builder.append(args[i] + " ");
                                }
                            }
                            groupManager.getGroup(args[0]).setPrefix(builder.toString());
                            player.sendMessage("§aDu hast den Prefix der Gruppe §7" + args[0] + " §aauf " + builder.toString().replace("&", "§") + " §agesetzt");
                        } else if (args[2].equalsIgnoreCase("suffix")) {
                            StringBuilder builder = new StringBuilder();
                            for(int i = 3; i < args.length; i++) {
                                if(i == args.length) {
                                    builder.append(args[i]);
                                }else{
                                    builder.append(args[i] + " ");
                                }
                            }
                            groupManager.getGroup(args[0]).setSuffix(builder.toString());
                            player.sendMessage("§aDu hast den Suffix der Gruppe §7" + args[0] + " §aauf " + builder.toString().replace("&", "§") + " §agesetzt");
                        } else if (args[2].equalsIgnoreCase("sortId")) {
                            try {
                                groupManager.getGroup(args[0]).setSortId(args[3]);
                                player.sendMessage("§aDu hast die SortID der Gruppe §7" + args[0] + " §aauf §6" + args[3] + " §agesetzt");
                            }catch (NumberFormatException e) {
                                e.printStackTrace();
                                player.sendMessage("§cBitte benutze eine Zahl!");
                            }
                        } else {
                            sendFeedback(player);
                        }
                    }else{
                        player.sendMessage("§cDiese Gruppe existiert nicht!");
                    }
                }else if(args.length == 1) {
                    Player player2 = Bukkit.getPlayer(args[0]);
                    if (player2 != null) {
                        player.sendMessage("§aDer Spieler hat den Rang: §7" + SQLGroup.getPlayerGroup(player2.getUniqueId().toString()));
                    } else {
                        player.sendMessage("§cDieser Spieler ist nicht online.");
                    }
                 }else{
                    sendFeedback(player);
                }
            }else{
                player.sendMessage("§cDafür hast du keine Rechte!");
            }
        }
        return false;
    }

    private void sendFeedback(Player player) {
        player.sendMessage("§cBitte benutze /rank <Spieler> | Man sieht den Rang des Spielers");
        player.sendMessage("§cBitte benutze /rank <Spieler> <set | unset> <Group>");
        player.sendMessage(" ");
        player.sendMessage("§cBitte benutze /rank <create | delete> <GroupName>");
        player.sendMessage(" ");
        player.sendMessage("§cBitte benutze /rank <Group> <add | remove> permission <Permission>");
        player.sendMessage("§cBitte benutze /rank <Group> set prefix <Prefix>");
        player.sendMessage("§cBitte benutze /rank <Group> set suffix <Suffix>");
        player.sendMessage("§cBitte benutze /rank <Group> set sortId <sortID>");
    }
}
