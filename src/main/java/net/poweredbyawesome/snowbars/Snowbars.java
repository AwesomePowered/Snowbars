package net.poweredbyawesome.snowbars;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Snowbars extends JavaPlugin implements Listener {

    public static Snowbars intance;
    public List<Snowbar> bars = new ArrayList<>();

    @Override
    public void onEnable() {
        intance = this;
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent ev) {
        String message = ev.getMessage().toLowerCase();
        if (message.startsWith("@snowbar") && message.split(" ").length >= 2 && ev.getPlayer().hasPermission("snowbars.create")) {
            if (!StringUtils.isNumeric(message.split(" ")[1])) {
                ev.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: &b@snowbar &f<&bmaxHeight&f>"));
                return;
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
                makeBar(ev.getPlayer().getTargetBlock(null, 5), Integer.valueOf(message.split(" ")[1]));
                ev.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou made a snowbar!"));
            }, 0);
            ev.setCancelled(true);
        }

        if (message.equalsIgnoreCase("@stopbars") && ev.getPlayer().hasPermission("snowbars.create")) {
            ev.setCancelled(true);
            for (Snowbar snowbars : bars) {
                snowbars.destroy();
            }
            bars.clear(); //iterators, lul
        }
    }

    public void makeBar(Block block, int maxHeight) {
        Snowbar snowbar = new Snowbar(block, maxHeight, 1);
        snowbar.visualize();
        bars.add(snowbar);
    }

}
