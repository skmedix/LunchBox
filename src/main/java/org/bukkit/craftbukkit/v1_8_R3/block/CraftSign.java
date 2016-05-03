package org.bukkit.craftbukkit.v1_8_R3.block;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.TileEntitySign;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;

public class CraftSign extends CraftBlockState implements Sign {

    private final TileEntitySign sign;
    private final String[] lines;

    public CraftSign(Block block) {
        super(block);
        CraftWorld world = (CraftWorld) block.getWorld();

        this.sign = (TileEntitySign) world.getTileEntityAt(this.getX(), this.getY(), this.getZ());
        if (this.sign == null) {
            this.lines = new String[] { "", "", "", ""};
        } else {
            this.lines = new String[this.sign.lines.length];
            System.arraycopy(revertComponents(this.sign.lines), 0, this.lines, 0, this.lines.length);
        }
    }

    public CraftSign(Material material, TileEntitySign te) {
        super(material);
        this.sign = te;
        this.lines = new String[this.sign.lines.length];
        System.arraycopy(revertComponents(this.sign.lines), 0, this.lines, 0, this.lines.length);
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

    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            IChatBaseComponent[] newLines = sanitizeLines(this.lines);

            System.arraycopy(newLines, 0, this.sign.lines, 0, 4);
            this.sign.update();
        }

        return result;
    }

    public static IChatBaseComponent[] sanitizeLines(String[] lines) {
        IChatBaseComponent[] components = new IChatBaseComponent[4];

        for (int i = 0; i < 4; ++i) {
            if (i < lines.length && lines[i] != null) {
                components[i] = CraftChatMessage.fromString(lines[i])[0];
            } else {
                components[i] = new ChatComponentText("");
            }
        }

        return components;
    }

    public static String[] revertComponents(IChatBaseComponent[] components) {
        String[] lines = new String[components.length];

        for (int i = 0; i < lines.length; ++i) {
            lines[i] = revertComponent(components[i]);
        }

        return lines;
    }

    private static String revertComponent(IChatBaseComponent component) {
        return CraftChatMessage.fromComponent(component);
    }

    public TileEntitySign getTileEntity() {
        return this.sign;
    }
}
