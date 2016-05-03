package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityHanging;
import net.minecraft.server.v1_8_R3.EntityPainting;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Art;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftArt;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Painting;

public class CraftPainting extends CraftHanging implements Painting {

    public CraftPainting(CraftServer server, EntityPainting entity) {
        super(server, (EntityHanging) entity);
    }

    public Art getArt() {
        EntityPainting.EnumArt art = this.getHandle().art;

        return CraftArt.NotchToBukkit(art);
    }

    public boolean setArt(Art art) {
        return this.setArt(art, false);
    }

    public boolean setArt(Art art, boolean force) {
        EntityPainting painting = this.getHandle();
        EntityPainting.EnumArt oldArt = painting.art;

        painting.art = CraftArt.BukkitToNotch(art);
        painting.setDirection(painting.direction);
        if (!force && !painting.survives()) {
            painting.art = oldArt;
            painting.setDirection(painting.direction);
            return false;
        } else {
            this.update();
            return true;
        }
    }

    public boolean setFacingDirection(BlockFace face, boolean force) {
        if (super.setFacingDirection(face, force)) {
            this.update();
            return true;
        } else {
            return false;
        }
    }

    private void update() {
        WorldServer world = ((CraftWorld) this.getWorld()).getHandle();
        EntityPainting painting = new EntityPainting(world);

        painting.blockPosition = this.getHandle().blockPosition;
        painting.art = this.getHandle().art;
        painting.setDirection(this.getHandle().direction);
        this.getHandle().die();
        this.getHandle().velocityChanged = true;
        world.addEntity(painting);
        this.entity = painting;
    }

    public EntityPainting getHandle() {
        return (EntityPainting) this.entity;
    }

    public String toString() {
        return "CraftPainting{art=" + this.getArt() + "}";
    }

    public EntityType getType() {
        return EntityType.PAINTING;
    }
}
