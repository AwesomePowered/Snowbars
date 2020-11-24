package net.poweredbyawesome.snowbars.event;

import net.poweredbyawesome.snowbars.Snowbar;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created on 11/23/2020.
 *
 * @author RoboMWM
 */
public class SnowbarSnowEvent extends Event implements Cancellable {

    private static final HandlerList panHandlers = new HandlerList();
    private boolean cancelled;
    private Snowbar snowbar;

    public SnowbarSnowEvent(Snowbar snowbar) {
        this.snowbar = snowbar;
    }

    @Override
    public HandlerList getHandlers() {
        return panHandlers;
    }

    public static HandlerList getHandlerList() {
        return panHandlers;
    }

    public Snowbar getSnowbar() {
        return snowbar;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
