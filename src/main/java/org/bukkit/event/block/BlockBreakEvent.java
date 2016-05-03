package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class BlockBreakEvent extends BlockExpEvent implements Cancellable {

    private final Player player;
    private boolean cancel;

    public BlockBreakEvent(Block theBlock, Player player) {
        super(theBlock, 0);
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
