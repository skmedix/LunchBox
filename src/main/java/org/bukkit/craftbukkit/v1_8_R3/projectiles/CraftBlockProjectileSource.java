package org.bukkit.craftbukkit.v1_8_R3.projectiles;

import java.util.Random;

import com.kookykraftmc.lunchbox.LunchBox;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.projectile.*;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.WitherSkull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.util.Vector;

public class CraftBlockProjectileSource implements BlockProjectileSource {

    private final TileEntityDispenser dispenserBlock;

    public CraftBlockProjectileSource(TileEntityDispenser dispenserBlock) {
        this.dispenserBlock = dispenserBlock;
    }

    public Block getBlock() {
        return (Block) this.dispenserBlock.getWorld().getBlockState(new BlockPos((this.dispenserBlock.getPos())));
    }

    public Projectile launchProjectile(Class projectile) {
        return this.launchProjectile(projectile, (Vector) null);
    }

    public Projectile launchProjectile(Class projectile, Vector velocity) {
        Validate.isTrue(this.getBlock().getType() == Material.DISPENSER, "Block is no longer dispenser");
        BlockSourceImpl isourceblock = new BlockSourceImpl(this.dispenserBlock.getWorld(), this.dispenserBlock.getPos());
        IPosition iposition = BlockDispenser.getDispensePosition(isourceblock);
        EnumFacing enumdirection = BlockDispenser.getFacing(isourceblock.getBlockMetadata());
        World world = this.dispenserBlock.getWorld();
        Object launch = null;

        if (Snowball.class.isAssignableFrom(projectile)) {
            launch = new EntitySnowball(world, iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (Egg.class.isAssignableFrom(projectile)) {
            launch = new EntityEgg(world, iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (EnderPearl.class.isAssignableFrom(projectile)) {
            launch = new EntityEnderPearl(world, (EntityLiving) null);
            ((Entity) launch).setPosition(iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (ThrownExpBottle.class.isAssignableFrom(projectile)) {
            launch = new EntityExpBottle(world, iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (ThrownPotion.class.isAssignableFrom(projectile)) {
            launch = new EntityPotion(world, iposition.getX(), iposition.getY(), iposition.getZ(), CraftItemStack.asNMSCopy(new ItemStack(Material.POTION, 1)));
        } else if (Arrow.class.isAssignableFrom(projectile)) {
            launch = new EntityArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());
            ((EntityArrow) launch).canBePickedUp = 1;
            //((EntityArrow) launch).projectileSource = this; Remove for now todo
        } else if (Fireball.class.isAssignableFrom(projectile)) {
            double d0 = iposition.getX() + (double) ((float) enumdirection.getFrontOffsetX() * 0.3F);
            double d1 = iposition.getY() + (double) ((float) enumdirection.getFrontOffsetY() * 0.3F);
            double d2 = iposition.getZ() + (double) ((float) enumdirection.getFrontOffsetZ() * 0.3F);
            Random random = world.rand;
            double d3 = random.nextGaussian() * 0.05D + (double) enumdirection.getFrontOffsetX();
            double d4 = random.nextGaussian() * 0.05D + (double) enumdirection.getFrontOffsetY();
            double d5 = random.nextGaussian() * 0.05D + (double) enumdirection.getFrontOffsetZ();

            if (SmallFireball.class.isAssignableFrom(projectile)) {
                launch = new EntitySmallFireball(world, d0, d1, d2, d3, d4, d5);
            } else {
                double d6;

                if (WitherSkull.class.isAssignableFrom(projectile)) {
                    launch = new EntityWitherSkull(world);
                    ((Entity) launch).setPosition(d0, d1, d2);
                    d6 = (double) net.minecraft.util.MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
                    ((EntityFireball) launch).accelerationX = d3 / d6 * 0.1D;
                    ((EntityFireball) launch).accelerationY = d4 / d6 * 0.1D;
                    ((EntityFireball) launch).accelerationZ = d5 / d6 * 0.1D;
                } else {
                    launch = new EntityLargeFireball(world);
                    ((Entity) launch).setPosition(d0, d1, d2);
                    d6 = (double) MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
                    ((EntityFireball) launch).accelerationX = d3 / d6 * 0.1D;
                    ((EntityFireball) launch).accelerationY = d4 / d6 * 0.1D;
                    ((EntityFireball) launch).accelerationZ = d5 / d6 * 0.1D;
                }
            }

            //((EntityFireball) launch).projectileSource = this;//todo
        }

        Validate.notNull(launch, "Projectile not supported");
        if (launch instanceof IProjectile) {
            if (launch instanceof EntityThrowable) {
                //((EntityThrowable) launch).projectileSource = this; //todo
            }

            float a = 6.0F;
            float b = 1.1F;

            if (launch instanceof EntityPotion || launch instanceof ThrownExpBottle) {
                a *= 0.5F;
                b *= 1.25F;
            }

            ((IProjectile) launch).setThrowableHeading((double) enumdirection.getFrontOffsetX(), (double) ((float) enumdirection.getFrontOffsetY() + 0.1F), (double) enumdirection.getFrontOffsetZ(), b, a);
        }

        if (velocity != null) {
            CraftEntity.getEntity(LunchBox.getServer(), (Entity) launch).setVelocity(velocity);
        }

        world.spawnEntityInWorld((Entity) launch);
        return (Projectile) CraftEntity.getEntity(LunchBox.getServer(), (Entity) launch);
    }
}
