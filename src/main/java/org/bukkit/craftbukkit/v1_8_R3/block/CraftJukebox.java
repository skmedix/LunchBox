package org.bukkit.craftbukkit.v1_8_R3.block;

import net.minecraft.server.v1_8_R3.BlockJukeBox;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.ItemStack;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;

public class CraftJukebox extends CraftBlockState implements Jukebox {

    private final CraftWorld world;
    private final BlockJukeBox.TileEntityRecordPlayer jukebox;

    public CraftJukebox(Block block) {
        super(block);
        this.world = (CraftWorld) block.getWorld();
        this.jukebox = (BlockJukeBox.TileEntityRecordPlayer) this.world.getTileEntityAt(this.getX(), this.getY(), this.getZ());
    }

    public CraftJukebox(Material material, BlockJukeBox.TileEntityRecordPlayer te) {
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
            this.jukebox.update();
            if (record == Material.AIR) {
                this.world.getHandle().setTypeAndData(new BlockPosition(this.getX(), this.getY(), this.getZ()), Blocks.JUKEBOX.getBlockData().set(BlockJukeBox.HAS_RECORD, Boolean.valueOf(false)), 3);
            } else {
                this.world.getHandle().setTypeAndData(new BlockPosition(this.getX(), this.getY(), this.getZ()), Blocks.JUKEBOX.getBlockData().set(BlockJukeBox.HAS_RECORD, Boolean.valueOf(true)), 3);
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

        ((BlockJukeBox) Blocks.JUKEBOX).dropRecord(this.world.getHandle(), new BlockPosition(this.getX(), this.getY(), this.getZ()), (IBlockData) null);
        return result;
    }

    public BlockJukeBox.TileEntityRecordPlayer getTileEntity() {
        return this.jukebox;
    }
}
