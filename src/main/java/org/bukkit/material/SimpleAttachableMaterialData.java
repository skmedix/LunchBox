package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public abstract class SimpleAttachableMaterialData extends MaterialData implements Attachable {

    /** @deprecated */
    @Deprecated
    public SimpleAttachableMaterialData(int type) {
        super(type);
    }

    public SimpleAttachableMaterialData(int type, BlockFace direction) {
        this(type);
        this.setFacingDirection(direction);
    }

    public SimpleAttachableMaterialData(Material type, BlockFace direction) {
        this(type);
        this.setFacingDirection(direction);
    }

    public SimpleAttachableMaterialData(Material type) {
        super(type);
    }

    /** @deprecated */
    @Deprecated
    public SimpleAttachableMaterialData(int type, byte data) {
        super(type, data);
    }

    /** @deprecated */
    @Deprecated
    public SimpleAttachableMaterialData(Material type, byte data) {
        super(type, data);
    }

    public BlockFace getFacing() {
        BlockFace attachedFace = this.getAttachedFace();

        return attachedFace == null ? null : attachedFace.getOppositeFace();
    }

    public String toString() {
        return super.toString() + " facing " + this.getFacing();
    }

    public SimpleAttachableMaterialData clone() {
        return (SimpleAttachableMaterialData) super.clone();
    }
}
