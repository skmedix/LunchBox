package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class SignChangeEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private final Player player;
    private final String[] lines;

    public SignChangeEvent(Block theBlock, Player thePlayer, String[] theLines) {
        super(theBlock);
        this.player = thePlayer;
        this.lines = theLines;
    }

    public Player getPlayer() {
        return this.player;
    }

    public String[] getLines() {
        return this.lines;
    }

    public String getLine(int index) throws IndexOutOfBoundsException {
        return this.lines[index];
    }

    public void setLine(int index, String line) throws IndexOutOfBoundsException {
        this.lines[index] = line;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public HandlerList getHandlers() {
        return SignChangeEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return SignChangeEvent.handlers;
    }
}
