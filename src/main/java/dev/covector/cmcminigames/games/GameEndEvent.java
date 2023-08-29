package dev.covector.cmcminigames.games;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class GameEndEvent extends Event implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();
    private UUID id;
    private boolean cancelled;

    public GameEndEvent(UUID id) {
        this.id = id;
        this.cancelled = false;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}