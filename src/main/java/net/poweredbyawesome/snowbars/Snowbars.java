package net.poweredbyawesome.snowbars;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public final class Snowbars extends JavaPlugin implements Listener {

    public boolean debug = false;
    public static Snowbars intance;
    public List<Snowbar> bars = new ArrayList<>();
    public Map<UUID, Snowbar> dejays = new HashMap<>();

    @Override
    public void onEnable() {
        intance = this;
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
        getCommand("snowbar").setExecutor(new DjCommand(this));
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent ev) {
        String message = ev.getMessage().toLowerCase();
        if (message.equalsIgnoreCase("@stopbars") && ev.getPlayer().hasPermission("snowbars.create")) {
            ev.setCancelled(true);
            for (Snowbar snowbars : bars) {
                snowbars.destroy();
            }
            bars.clear(); //iterators, lul
        }
    }

    public void makeBar(Block block, int maxHeight) {
        Snowbar snowbar = new Snowbar(block, maxHeight, true);
        snowbar.visualize();
        bars.add(snowbar);
    }

    public boolean isSnowbar(Location loc) { //todo, put on a different class.
        for (Snowbar snowbar : bars) {
            if (snowbar.isSnowbar(loc)) {
                return true;
            }
        }
        return false;
    }

    public Snowbar getSnowbar(Location loc) {
        for (Snowbar snowbar : bars) {
            if (snowbar.isSnowbar(loc)) {
                return snowbar;
            }
        }
        return null;
    }

    public void debug(Object... o) {
        if (debug) getLogger().log(Level.INFO, Arrays.toString(o));
    }

}
