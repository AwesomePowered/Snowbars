package net.poweredbyawesome.snowbars;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {

    Snowbars plugin;

    public PlayerListener(Snowbars snowbars) {
        this.plugin = snowbars;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent ev) {
        if (!plugin.dejays.containsKey(ev.getPlayer().getUniqueId())) {
            plugin.debug("Interact","was called but player is not a dj");
            return;
        }
        ev.setCancelled(true);
        Player p = ev.getPlayer();
        if (ev.getAction() == Action.LEFT_CLICK_BLOCK && plugin.isSnowbar(ev.getClickedBlock().getLocation())) {
            plugin.debug("Interact L", "on a snowbar, selecting.");
            plugin.dejays.put(ev.getPlayer().getUniqueId(), plugin.getSnowbar(ev.getClickedBlock().getLocation()));
            sendMessage(p, "&7[Snowbars] &aYou have selected a Snowbar");
            return;
        }
        if (ev.getAction() == Action.LEFT_CLICK_BLOCK) {
            plugin.debug("Interact L", "Making a Snowbar object");
            Snowbar snowbar = new Snowbar(ev.getClickedBlock(), 5,false);
            snowbar.setIsRandom((ev.getPlayer().isSneaking()));
            snowbar.visualize();
            plugin.bars.add(snowbar);
            plugin.saveSnowbars();
            plugin.dejays.put(p.getUniqueId(), snowbar);
            sendMessage(ev.getPlayer(), "&7[Snowbars] &aYou have created a snowbar.");
            plugin.debug("Interact","visualized a snowbar");
            return;
        }
        if (ev.getAction() != Action.RIGHT_CLICK_BLOCK) {
            plugin.debug("Interact R");
            return;
        }

        for (Snowbar snowbar : plugin.bars) {
            if (snowbar.isSnowbar(ev.getClickedBlock().getLocation())) {
                plugin.debug("Interact", "was called on a snowbar, destroying..");
                plugin.dejays.put(p.getUniqueId(), null);
                snowbar.destroy();
                plugin.bars.remove(snowbar);
                plugin.saveSnowbars();
                sendMessage(p, "&7[Snowbars] &cThe snowbar is no longer playing");
                return;
            }
        }
    }

    public void sendMessage(Player p, String message) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

}
