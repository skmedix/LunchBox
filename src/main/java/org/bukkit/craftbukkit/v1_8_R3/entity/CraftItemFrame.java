package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityHanging;
import net.minecraft.server.v1_8_R3.EntityItemFrame;
import net.minecraft.server.v1_8_R3.EnumDirection;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.apache.commons.lang.Validate;
import org.bukkit.Rotation;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;

public class CraftItemFrame extends CraftHanging implements ItemFrame {

    private static int[] $SWITCH_TABLE$org$bukkit$Rotation;

    public CraftItemFrame(CraftServer server, EntityItemFrame entity) {
        super(server, (EntityHanging) entity);
    }

    public boolean setFacingDirection(BlockFace face, boolean force) {
        if (!super.setFacingDirection(face, force)) {
            return false;
        } else {
            this.update();
            return true;
        }
    }

    private void update() {
        EntityItemFrame old = this.getHandle();
        WorldServer world = ((CraftWorld) this.getWorld()).getHandle();
        BlockPosition position = old.getBlockPosition();
        EnumDirection direction = old.getDirection();
        ItemStack item = old.getItem() != null ? old.getItem().cloneItemStack() : null;

        old.die();
        EntityItemFrame frame = new EntityItemFrame(world, position, direction);

        frame.setItem(item);
        world.addEntity(frame);
        this.entity = frame;
    }

    public void setItem(org.bukkit.inventory.ItemStack item) {
        if (item != null && item.getTypeId() != 0) {
            this.getHandle().setItem(CraftItemStack.asNMSCopy(item));
        } else {
            this.getHandle().setItem((ItemStack) null);
        }

    }

    public org.bukkit.inventory.ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(this.getHandle().getItem());
    }

    public Rotation getRotation() {
        return this.toBukkitRotation(this.getHandle().getRotation());
    }

    Rotation toBukkitRotation(int value) {
        switch (value) {
        case 0:
            return Rotation.NONE;

        case 1:
            return Rotation.CLOCKWISE_45;

        case 2:
            return Rotation.CLOCKWISE;

        case 3:
            return Rotation.CLOCKWISE_135;

        case 4:
            return Rotation.FLIPPED;

        case 5:
            return Rotation.FLIPPED_45;

        case 6:
            return Rotation.COUNTER_CLOCKWISE;

        case 7:
            return Rotation.COUNTER_CLOCKWISE_45;

        default:
            throw new AssertionError("Unknown rotation " + value + " for " + this.getHandle());
        }
    }

    public void setRotation(Rotation rotation) {
        Validate.notNull(rotation, "Rotation cannot be null");
        this.getHandle().setRotation(toInteger(rotation));
    }

    static int toInteger(Rotation rotation) {
        switch ($SWITCH_TABLE$org$bukkit$Rotation()[rotation.ordinal()]) {
        case 1:
            return 0;

        case 2:
            return 1;

        case 3:
            return 2;

        case 4:
            return 3;

        case 5:
            return 4;

        case 6:
            return 5;

        case 7:
            return 6;

        case 8:
            return 7;

        default:
            throw new IllegalArgumentException(rotation + " is not applicable to an ItemFrame");
        }
    }

    public EntityItemFrame getHandle() {
        return (EntityItemFrame) this.entity;
    }

    public String toString() {
        return "CraftItemFrame{item=" + this.getItem() + ", rotation=" + this.getRotation() + "}";
    }

    public EntityType getType() {
        return EntityType.ITEM_FRAME;
    }

    static int[] $SWITCH_TABLE$org$bukkit$Rotation() {
        int[] aint = CraftItemFrame.$SWITCH_TABLE$org$bukkit$Rotation;

        if (CraftItemFrame.$SWITCH_TABLE$org$bukkit$Rotation != null) {
            return aint;
        } else {
            int[] aint1 = new int[Rotation.values().length];

            try {
                aint1[Rotation.CLOCKWISE.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                aint1[Rotation.CLOCKWISE_135.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                aint1[Rotation.CLOCKWISE_45.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                aint1[Rotation.COUNTER_CLOCKWISE.ordinal()] = 7;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            try {
                aint1[Rotation.COUNTER_CLOCKWISE_45.ordinal()] = 8;
            } catch (NoSuchFieldError nosuchfielderror4) {
                ;
            }

            try {
                aint1[Rotation.FLIPPED.ordinal()] = 5;
            } catch (NoSuchFieldError nosuchfielderror5) {
                ;
            }

            try {
                aint1[Rotation.FLIPPED_45.ordinal()] = 6;
            } catch (NoSuchFieldError nosuchfielderror6) {
                ;
            }

            try {
                aint1[Rotation.NONE.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror7) {
                ;
            }

            CraftItemFrame.$SWITCH_TABLE$org$bukkit$Rotation = aint1;
            return aint1;
        }
    }
}
