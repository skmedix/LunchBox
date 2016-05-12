package org.bukkit.craftbukkit.v1_8_R3.block;

import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.v1_8_R3.BlockJukeBox;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.util.BlockPos;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;

public class CraftJukebox extends CraftBlockState implements Jukebox {

    private final CraftWorld world;
    private final BlockJukebox.TileEntityJukebox jukebox;

    public CraftJukebox(Block block) {
        super(block);
        this.world = (CraftWorld) block.getWorld();
        this.jukebox = (BlockJukebox.TileEntityJukebox) this.world.getTileEntityAt(this.getX(), this.getY(), this.getZ());
    }

    public CraftJukebox(Material material, BlockJukebox.TileEntityJukebox te) {
        super(material);
        this.world = null;
        this.jukebox = te;
    }

    public Material getPlaying() {
        ItemStack record = this.jukebox.getRecord();

        return record == null ? Material.AIR : CraftMagicNumbers.getMaterial(record.getItem());
    }

    public void setPlaying(Material record) {
        if (record != null && CraftMagicNumbers.getItem(record) != null) {
            this.jukebox.setRecord(new ItemStack(CraftMagicNumbers.getItem(record), 1));
        } else {
            record = Material.AIR;
            this.jukebox.setRecord((ItemStack) null);
        }

        if (this.isPlaced()) {
            this.jukebox.updateContainingBlockInfo();
            if (record == Material.AIR) {
                this.world.getHandle().setTypeAndData(new BlockPos(this.getX(), this.getY(), this.getZ()), Blocks.jukebox.getBlockData().set(BlockJukeBox.HAS_RECORD, Boolean.valueOf(false)), 3);
            } else {
                this.world.getHandle().setTypeAndData(new BlockPos(this.getX(), this.getY(), this.getZ()), Blocks.jukebox.getBlockData().set(BlockJukeBox.HAS_RECORD, Boolean.valueOf(true)), 3);
            }

            this.world.playEffect(this.getLocation(), Effect.RECORD_PLAY, record.getId());
        }
    }

    public boolean isPlaying() {
        return this.getRawData() == 1;
    }

    public boolean eject() {
        this.requirePlaced();
        boolean result = this.isPlaying();

        ((BlockJukebox) Blocks.jukebox).(this.world.getHandle(), new BlockPos(this.getX(), this.getY(), this.getZ()), (IBlockState) null);
        return result;
    }

    public BlockJukebox.TileEntityJukebox getTileEntity() {
        return this.jukebox;
    }
}
