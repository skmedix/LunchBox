package org.bukkit.craftbukkit.v1_8_R3.block;

import net.minecraft.server.v1_8_R3.TileEntityCommand;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class CraftCommandBlock extends CraftBlockState implements CommandBlock {

    private final TileEntityCommand commandBlock;
    private String command;
    private String name;

    public CraftCommandBlock(Block block) {
        super(block);
        CraftWorld world = (CraftWorld) block.getWorld();

        this.commandBlock = (TileEntityCommand) world.getTileEntityAt(this.getX(), this.getY(), this.getZ());
        this.command = this.commandBlock.getCommandBlock().getCommand();
        this.name = this.commandBlock.getCommandBlock().getName();
    }

    public CraftCommandBlock(Material material, TileEntityCommand te) {
        super(material);
        this.commandBlock = te;
        this.command = this.commandBlock.getCommandBlock().getCommand();
        this.name = this.commandBlock.getCommandBlock().getName();
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command != null ? command : "";
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name != null ? name : "@";
    }

    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            this.commandBlock.getCommandBlock().setCommand(this.command);
            this.commandBlock.getCommandBlock().setName(this.name);
        }

        return result;
    }

    public TileEntityCommand getTileEntity() {
        return this.commandBlock;
    }
}
