package org.bukkit.craftbukkit.v1_8_R3.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandBlockLogic;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;

public class CraftBlockCommandSender extends ServerCommandSender implements BlockCommandSender {

    private final CommandBlockLogic commandBlock;

    public CraftBlockCommandSender(CommandBlockLogic commandBlockListenerAbstract) {
        this.commandBlock = commandBlockListenerAbstract;
    }

    public Block getBlock() {
        return (Block) this.commandBlock.getEntityWorld().getBlockState(this.commandBlock.getPosition());
    }

    public void sendMessage(String message) {}

    public void sendMessage(String[] messages) {}

    public String getName() {
        return this.commandBlock.getName();
    }

    public boolean isOp() {
        return true;
    }

    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of a block");
    }

    public ICommandSender getTileEntity() {
        return this.commandBlock;
    }
}
