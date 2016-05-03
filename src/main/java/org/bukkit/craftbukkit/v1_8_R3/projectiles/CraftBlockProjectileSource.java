package org.bukkit.craftbukkit.v1_8_R3.projectiles;

import java.util.Random;
import net.minecraft.server.v1_8_R3.BlockDispenser;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityArrow;
import net.minecraft.server.v1_8_R3.EntityEgg;
import net.minecraft.server.v1_8_R3.EntityEnderPearl;
import net.minecraft.server.v1_8_R3.EntityFireball;
import net.minecraft.server.v1_8_R3.EntityLargeFireball;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityPotion;
import net.minecraft.server.v1_8_R3.EntityProjectile;
import net.minecraft.server.v1_8_R3.EntitySmallFireball;
import net.minecraft.server.v1_8_R3.EntitySnowball;
import net.minecraft.server.v1_8_R3.EntityThrownExpBottle;
import net.minecraft.server.v1_8_R3.EntityWitherSkull;
import net.minecraft.server.v1_8_R3.EnumDirection;
import net.minecraft.server.v1_8_R3.IPosition;
import net.minecraft.server.v1_8_R3.IProjectile;
import net.minecraft.server.v1_8_R3.ISourceBlock;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.SourceBlock;
import net.minecraft.server.v1_8_R3.TileEntityDispenser;
import net.minecraft.server.v1_8_R3.World;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
        return this.dispenserBlock.getWorld().getWorld().getBlockAt(this.dispenserBlock.getPosition().getX(), this.dispenserBlock.getPosition().getY(), this.dispenserBlock.getPosition().getZ());
    }

    public Projectile launchProjectile(Class projectile) {
        return this.launchProjectile(projectile, (Vector) null);
    }

    public Projectile launchProjectile(Class projectile, Vector velocity) {
        Validate.isTrue(this.getBlock().getType() == Material.DISPENSER, "Block is no longer dispenser");
        SourceBlock isourceblock = new SourceBlock(this.dispenserBlock.getWorld(), this.dispenserBlock.getPosition());
        IPosition iposition = BlockDispenser.a((ISourceBlock) isourceblock);
        EnumDirection enumdirection = BlockDispenser.b(isourceblock.f());
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
            launch = new EntityThrownExpBottle(world, iposition.getX(), iposition.getY(), iposition.getZ());
        } else if (ThrownPotion.class.isAssignableFrom(projectile)) {
            launch = new EntityPotion(world, iposition.getX(), iposition.getY(), iposition.getZ(), CraftItemStack.asNMSCopy(new ItemStack(Material.POTION, 1)));
        } else if (Arrow.class.isAssignableFrom(projectile)) {
            launch = new EntityArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());
            ((EntityArrow) launch).fromPlayer = 1;
            ((EntityArrow) launch).projectileSource = this;
        } else if (Fireball.class.isAssignableFrom(projectile)) {
            double d0 = iposition.getX() + (double) ((float) enumdirection.getAdjacentX() * 0.3F);
            double d1 = iposition.getY() + (double) ((float) enumdirection.getAdjacentY() * 0.3F);
            double d2 = iposition.getZ() + (double) ((float) enumdirection.getAdjacentZ() * 0.3F);
            Random random = world.random;
            double d3 = random.nextGaussian() * 0.05D + (double) enumdirection.getAdjacentX();
            double d4 = random.nextGaussian() * 0.05D + (double) enumdirection.getAdjacentY();
            double d5 = random.nextGaussian() * 0.05D + (double) enumdirection.getAdjacentZ();

            if (SmallFireball.class.isAssignableFrom(projectile)) {
                launch = new EntitySmallFireball(world, d0, d1, d2, d3, d4, d5);
            } else {
                double d6;

                if (WitherSkull.class.isAssignableFrom(projectile)) {
                    launch = new EntityWitherSkull(world);
                    ((Entity) launch).setPosition(d0, d1, d2);
                    d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                    ((EntityFireball) launch).dirX = d3 / d6 * 0.1D;
                    ((EntityFireball) launch).dirY = d4 / d6 * 0.1D;
                    ((EntityFireball) launch).dirZ = d5 / d6 * 0.1D;
                } else {
                    launch = new EntityLargeFireball(world);
                    ((Entity) launch).setPosition(d0, d1, d2);
                    d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                    ((EntityFireball) launch).dirX = d3 / d6 * 0.1D;
                    ((EntityFireball) launch).dirY = d4 / d6 * 0.1D;
                    ((EntityFireball) launch).dirZ = d5 / d6 * 0.1D;
                }
            }

            ((EntityFireball) launch).projectileSource = this;
        }

        Validate.notNull(launch, "Projectile not supported");
        if (launch instanceof IProjectile) {
            if (launch instanceof EntityProjectile) {
                ((EntityProjectile) launch).projectileSource = this;
            }

            float a = 6.0F;
            float b = 1.1F;

            if (launch instanceof EntityPotion || launch instanceof ThrownExpBottle) {
                a *= 0.5F;
                b *= 1.25F;
            }

            ((IProjectile) launch).shoot((double) enumdirection.getAdjacentX(), (double) ((float) enumdirection.getAdjacentY() + 0.1F), (double) enumdirection.getAdjacentZ(), b, a);
        }

        if (velocity != null) {
            ((Projectile) ((Entity) launch).getBukkitEntity()).setVelocity(velocity);
        }

        world.addEntity((Entity) launch);
        return (Projectile) ((Entity) launch).getBukkitEntity();
    }
}
