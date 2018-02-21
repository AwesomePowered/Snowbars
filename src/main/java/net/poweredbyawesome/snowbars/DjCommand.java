package net.poweredbyawesome.snowbars;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DjCommand implements CommandExecutor {

    Snowbars plugin;

    public DjCommand(Snowbars snowbars) {
        this.plugin = snowbars;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.hasPermission("snowbars.create") && commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if (plugin.dejays.containsKey(p.getUniqueId())) {
                plugin.dejays.remove(((Player) commandSender).getUniqueId());
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[Snowbars] &cYou are no longer an active Dj"));
            } else {
                plugin.dejays.put(((Player) commandSender).getUniqueId(), null);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[Snowbars] &cYou are now an active Dj"));
            }
        }
        return false;
    }
}
