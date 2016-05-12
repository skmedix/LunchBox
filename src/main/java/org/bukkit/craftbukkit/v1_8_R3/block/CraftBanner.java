package org.bukkit.craftbukkit.v1_8_R3.block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityBanner;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class CraftBanner extends CraftBlockState implements Banner {

    private final TileEntityBanner banner;
    private DyeColor base;
    private List patterns = new ArrayList();

    public CraftBanner(Block block) {
        super(block);
        CraftWorld world = (CraftWorld) block.getWorld();

        this.banner = (TileEntityBanner) world.getTileEntityAt(this.getX(), this.getY(), this.getZ());
        this.base = DyeColor.getByDyeData((byte) this.banner.getBaseColor());
        if (this.banner.getPatternList() != null) {
            for (int i = 0; i < this.banner.getPatternList().size(); ++i) {
                NBTTagCompound p = this.banner.func_181021_d().getCompoundTagAt(i);

                this.patterns.add(new Pattern(DyeColor.getByDyeData((byte) p.getInteger("Color")), PatternType.getByIdentifier(p.getString("Pattern"))));
            }
        }

    }

    public CraftBanner(Material material, TileEntityBanner te) {
        super(material);
        this.banner = te;
        this.base = DyeColor.getByDyeData((byte) this.banner.getBaseColor());
        if (this.banner.getPatternList() != null) {
            for (int i = 0; i < this.banner.getPatternList().size(); ++i) {
                NBTTagCompound p = this.banner.func_181021_d().getCompoundTagAt(i);

                this.patterns.add(new Pattern(DyeColor.getByDyeData((byte) p.getInteger("Color")), PatternType.getByIdentifier(p.getString("Pattern"))));
            }
        }

    }

    public DyeColor getBaseColor() {
        return this.base;
    }

    public void setBaseColor(DyeColor color) {
        this.base = color;
    }

    public List getPatterns() {
        return new ArrayList(this.patterns);
    }

    public void setPatterns(List patterns) {
        this.patterns = new ArrayList(patterns);
    }

    public void addPattern(Pattern pattern) {
        this.patterns.add(pattern);
    }

    public Pattern getPattern(int i) {
        return (Pattern) this.patterns.get(i);
    }

    public Pattern removePattern(int i) {
        return (Pattern) this.patterns.remove(i);
    }

    public void setPattern(int i, Pattern pattern) {
        this.patterns.set(i, pattern);
    }

    public int numberOfPatterns() {
        return this.patterns.size();
    }

    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            //this.banner.color = this.base.getDyeData();
            NBTTagList newPatterns = new NBTTagList();
            Iterator iterator = this.patterns.iterator();

            while (iterator.hasNext()) {
                Pattern p = (Pattern) iterator.next();
                NBTTagCompound compound = new NBTTagCompound();

                compound.setInteger("Color", p.getColor().getDyeData());
                compound.setString("Pattern", p.getPattern().getIdentifier());
                newPatterns.appendTag(compound);
            }

            //this.banner.patterns = newPatterns;
            this.banner.getTileData().setTag("Pattern", newPatterns);
            this.banner.updateContainingBlockInfo();
        }

        return result;
    }
}
