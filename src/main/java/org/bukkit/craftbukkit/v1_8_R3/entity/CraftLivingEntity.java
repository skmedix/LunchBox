package org.bukkit.craftbukkit.v1_8_R3.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.kookykraftmc.lunchbox.LunchBox;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.*;
import net.minecraft.util.DamageSource;

import net.minecraft.world.WorldServer;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftEntityEquipment;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Fish;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public class CraftLivingEntity extends CraftEntity implements LivingEntity {

    private CraftEntityEquipment equipment;
    private IAttribute attributes;

    public CraftLivingEntity(CraftServer server, EntityLiving entity) {
        super(server, entity);
        if (/*entity instanceof EntityLiving ||*/(net.minecraft.entity.Entity) entity instanceof EntityArmorStand) {//LunchBox - remove for now
            this.equipment = new CraftEntityEquipment(this);
        }
    }

    public double getHealth() {
        return Math.min((double) Math.max(0.0F, this.getHandle().getHealth()), this.getMaxHealth());
    }

    public void setHealth(double health) {
        if (health >= 0.0D && health <= this.getMaxHealth()) {
            if (this.entity instanceof EntityPlayer && health == 0.0D) {
                ((EntityPlayer) this.entity).onDeath(DamageSource.generic);
            }

            this.getHandle().setHealth((float) health);
        } else {
            throw new IllegalArgumentException("Health must be between 0 and " + this.getMaxHealth());
        }
    }

    public double getMaxHealth() {
        return this.getHandle().getMaxHealth();
    }

    public void setMaxHealth(double amount) {
        Validate.isTrue(amount > 0.0D, "Max health must be greater than 0");
        this.getHandle().getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(amount);
        if (this.getHealth() > amount) {
            this.setHealth(amount);
        }

    }

    public void resetMaxHealth() {
        this.setMaxHealth((double) this.getHandle().getMaxHealth());
    }

    /** @deprecated */
    @Deprecated
    public Egg throwEgg() {
        return (Egg) this.launchProjectile(Egg.class);
    }

    /** @deprecated */
    @Deprecated
    public Snowball throwSnowball() {
        return (Snowball) this.launchProjectile(Snowball.class);
    }

    public double getEyeHeight() {
        return (double) this.getHandle().getEyeHeight();
    }

    public double getEyeHeight(boolean ignoreSneaking) {
        return this.getEyeHeight();
    }

    private List getLineOfSight(HashSet transparent, int maxDistance, int maxLength) {
        if (maxDistance > 120) {
            maxDistance = 120;
        }

        ArrayList blocks = new ArrayList();
        BlockIterator itr = new BlockIterator(this, maxDistance);

        while (itr.hasNext()) {
            Block block = (Block) itr.next();

            blocks.add(block);
            if (maxLength != 0 && blocks.size() > maxLength) {
                blocks.remove(0);
            }

            int id = block.getTypeId();

            if (transparent == null) {
                if (id != 0) {
                    break;
                }
            } else if (!transparent.contains(Byte.valueOf((byte) id))) {
                break;
            }
        }

        return blocks;
    }

    private List getLineOfSight(Set transparent, int maxDistance, int maxLength) {
        if (maxDistance > 120) {
            maxDistance = 120;
        }

        ArrayList blocks = new ArrayList();
        BlockIterator itr = new BlockIterator(this, maxDistance);

        while (itr.hasNext()) {
            Block block = (Block) itr.next();

            blocks.add(block);
            if (maxLength != 0 && blocks.size() > maxLength) {
                blocks.remove(0);
            }

            Material material = block.getType();

            if (transparent == null) {
                if (!material.equals(Material.AIR)) {
                    break;
                }
            } else if (!transparent.contains(material)) {
                break;
            }
        }

        return blocks;
    }

    public List getLineOfSight(HashSet transparent, int maxDistance) {
        return this.getLineOfSight(transparent, maxDistance, 0);
    }

    public List getLineOfSight(Set transparent, int maxDistance) {
        return this.getLineOfSight(transparent, maxDistance, 0);
    }

    public Block getTargetBlock(HashSet transparent, int maxDistance) {
        List blocks = this.getLineOfSight(transparent, maxDistance, 1);

        return (Block) blocks.get(0);
    }

    public Block getTargetBlock(Set transparent, int maxDistance) {
        List blocks = this.getLineOfSight(transparent, maxDistance, 1);

        return (Block) blocks.get(0);
    }

    public List getLastTwoTargetBlocks(HashSet transparent, int maxDistance) {
        return this.getLineOfSight(transparent, maxDistance, 2);
    }

    public List getLastTwoTargetBlocks(Set transparent, int maxDistance) {
        return this.getLineOfSight(transparent, maxDistance, 2);
    }

    /** @deprecated */
    @Deprecated
    public Arrow shootArrow() {
        return (Arrow) this.launchProjectile(Arrow.class);
    }

    public int getRemainingAir() {
        return this.getHandle().getAir();
    }

    public void setRemainingAir(int ticks) {
        this.getHandle().setAir(ticks);
    }

    public int getMaximumAir() {
        return this.getHandle().getAir();
    }

    public void setMaximumAir(int ticks) {
        this.getHandle().setAir(ticks);
    }

    public void damage(double amount) {
        this.damage(amount, (Entity) null);
    }

    public void damage(double amount, Entity source) {
        DamageSource reason = DamageSource.generic;

        if (source instanceof HumanEntity) {
            reason = DamageSource.causePlayerDamage(((CraftHumanEntity) source).getMPPlayer());
        } else if (source instanceof LivingEntity) {
            reason = DamageSource.causeMobDamage(((CraftLivingEntity) source).getHandle());
        }

        this.entity.attackEntityFrom(reason, (float) amount);
    }

    public Location getEyeLocation() {
        Location loc = this.getLocation();

        loc.setY(loc.getY() + this.getEyeHeight());
        return loc;
    }

    public int getMaximumNoDamageTicks() {
        return this.getHandle().maxHurtResistantTime;
    }

    public void setMaximumNoDamageTicks(int ticks) {
        this.getHandle().maxHurtResistantTime = ticks;
    }

    /*public double getLastDamage() {
        return (double) this.getHandle().getLastAttackerTime();
    }*/
    //todo
    public void setLastDamage(double damage) {
        //this.getHandle().lastDamage = (float) damage;
        throw new NotImplementedException("TODO");
    }

    public int getNoDamageTicks() {
        //return this.getHandle().noDamageTicks;
        throw new NotImplementedException("TODO");
    }

    public void setNoDamageTicks(int ticks) {
        //this.getHandle().noDamageTicks = ticks;
        throw new NotImplementedException("TODO");
    }

    public EntityLiving getHandle() {
        return (EntityLiving) this.entity;
    }

    public void setHandle(EntityLiving entity) {
        super.setHandle(entity);
    }

    public String toString() {
        return "CraftLivingEntity{id=" + this.getEntityId() + '}';
    }

    public Player getKiller() {
        return this.getHandle().getLastAttacker() == null ? null : (Player) CraftEntity.getEntity(LunchBox.getServer(), this.getHandle().getLastAttacker());
    }

    public boolean addPotionEffect(PotionEffect effect) {
        return this.addPotionEffect(effect, false);
    }

    public boolean addPotionEffect(PotionEffect effect, boolean force) {
        if (this.hasPotionEffect(effect.getType())) {
            if (!force) {
                return false;
            }

            this.removePotionEffect(effect.getType());
        }

        this.getHandle().addPotionEffect(new net.minecraft.potion.PotionEffect(effect.getType().getId(), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles()));
        return true;
    }

    public boolean addPotionEffects(Collection effects) {
        boolean success = true;

        PotionEffect effect;

        for (Iterator iterator = effects.iterator(); iterator.hasNext(); success &= this.addPotionEffect(effect)) {
            effect = (PotionEffect) iterator.next();
        }

        return success;
    }

    public boolean hasPotionEffect(PotionEffectType type) {
        return this.getHandle().isPotionActive(type.getId());
    }

    public void removePotionEffect(PotionEffectType type) {
        this.getHandle().removePotionEffect(type.getId());
    }

    public Collection<PotionEffect> getActivePotionEffects() {
        ArrayList effects = new ArrayList();
        Iterator iterator = this.getHandle().getActivePotionEffects().iterator();

        while (iterator.hasNext()) {
            Object raw = iterator.next();

            if (raw instanceof net.minecraft.potion.PotionEffect) {
                net.minecraft.potion.PotionEffect handle = (net.minecraft.potion.PotionEffect) raw;

                effects.add(new PotionEffect(PotionEffectType.getById(handle.getPotionID()), handle.getDuration(), handle.getAmplifier(), handle.getIsAmbient(), handle.getIsShowParticles()));
            }
        }

        return effects;
    }

    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return this.launchProjectile(projectile, null);
    }

    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        WorldServer world = ((CraftWorld) this.getWorld()).getHandle();
        Object launch = null;

        if (Snowball.class.isAssignableFrom(projectile)) {
            launch = new EntitySnowball(world, this.getHandle());
        } else if (Egg.class.isAssignableFrom(projectile)) {
            launch = new EntityEgg(world, this.getHandle());
        } else if (EnderPearl.class.isAssignableFrom(projectile)) {
            launch = new EntityEnderPearl(world, this.getHandle());
        } else if (Arrow.class.isAssignableFrom(projectile)) {
            launch = new EntityArrow(world, this.getHandle(), 1.0F);
        } else if (ThrownPotion.class.isAssignableFrom(projectile)) {
            launch = new EntityPotion(world, this.getHandle(), CraftItemStack.asNMSCopy(new ItemStack(Material.POTION, 1)));
        } else if (ThrownExpBottle.class.isAssignableFrom(projectile)) {
            launch = new EntityExpBottle(world, this.getHandle());
        } else if (Fish.class.isAssignableFrom(projectile) && this.getHandle() instanceof EntityPlayer/* todo */) {
            launch = new EntityFishHook(world, (EntityPlayer) (Entity) this.getHandle());
        } else if (Fireball.class.isAssignableFrom(projectile)) {
            Location location = this.getEyeLocation();
            Vector direction = location.getDirection().multiply(10);

            if (SmallFireball.class.isAssignableFrom(projectile)) {
                launch = new EntitySmallFireball(world, this.getHandle(), direction.getX(), direction.getY(), direction.getZ());
            } else if (WitherSkull.class.isAssignableFrom(projectile)) {
                launch = new EntityWitherSkull(world, this.getHandle(), direction.getX(), direction.getY(), direction.getZ());
            } else {
                launch = new EntityLargeFireball(world, this.getHandle(), direction.getX(), direction.getY(), direction.getZ());
            }

            ((EntityFireball) launch).shootingEntity = this.getHandle();
            ((net.minecraft.entity.Entity) launch).setPositionAndRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        }

        Validate.notNull(launch, "Projectile not supported");
        if (velocity != null) {
            ((Projectile) CraftEntity.getEntity(LunchBox.getServer(), (net.minecraft.entity.Entity) launch)).setVelocity(velocity);
        }

        world.onEntityAdded((net.minecraft.entity.Entity) launch);
        return (T) CraftEntity.getEntity(LunchBox.getServer(), (net.minecraft.entity.Entity) launch);
    }

    public EntityType getType() {
        return EntityType.UNKNOWN;
    }

    public boolean hasLineOfSight(Entity other) {
        return this.getHandle().canEntityBeSeen(((CraftEntity) other).getHandle());
    }

    public boolean getRemoveWhenFarAway() {
        return this.getHandle() instanceof EntityLiving && !((EntityLiving) this.getHandle()).persistenceRequired;
    }

    public void setRemoveWhenFarAway(boolean remove) {
        if (this.getHandle() instanceof EntityLiving) {
            ((EntityLiving) this.getHandle()).persistenceRequired = !remove;
        }

    }

    public EntityEquipment getEquipment() {
        return this.equipment;
    }

    public void setCanPickupItems(boolean pickup) {
        if (this.getHandle() instanceof EntityLiving) {
            ((EntityLiving) this.getHandle()).canPickUpLoot = pickup;
        }

    }

    public boolean getCanPickupItems() {
        return this.getHandle() instanceof EntityLiving && ((EntityLiving) this.getHandle()).canPickUpLoot;
    }

    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        return this.getHealth() == 0.0D ? false : super.teleport(location, cause);
    }

    public boolean isLeashed() {
        return !(this.getHandle() instanceof EntityLiving) ? false : ((EntityLiving) this.getHandle()).isLeashed;
    }

    public Entity getLeashHolder() throws IllegalStateException {
        if (!this.isLeashed()) {
            throw new IllegalStateException("Entity not leashed");
        } else {
            return CraftEntity.getEntity(LunchBox.getServer(), ((net.minecraft.entity.EntityLiving) getHandle()).getLeashedToEntity());
        }
    }

    private boolean unleash() {
        if (!this.isLeashed()) {
            return false;
        } else {
            ((EntityLiving) this.getHandle()).clearLeashed(true, false);
            return true;
        }
    }

    public boolean setLeashHolder(Entity holder) {
        if (!(this.getHandle() instanceof EntityWither) && this.getHandle() instanceof EntityLiving) {
            if (holder == null) {
                return this.unleash();
            } else if (holder.isDead()) {
                return false;
            } else {
                this.unleash();
                ((EntityLiving) this.getHandle()).setLeashedToEntity(((CraftEntity) holder).getHandle(), true);
                return true;
            }
        } else {
            return false;
        }
    }

    /** @deprecated */
    @Deprecated
    public int getLastDamage() {
        return NumberConversions.ceil(this.getLastDamage());
    }

    /** @deprecated */
    @Deprecated
    public void setLastDamage(int damage) {
        this.setLastDamage((double) damage);
    }

    /** @deprecated */
    @Deprecated
    public void damage(int amount) {
        this.damage((double) amount);
    }

    /** @deprecated */
    @Deprecated
    public void damage(int amount, Entity source) {
        this.damage((double) amount, source);
    }

    /* @deprecated
    @Deprecated
    public int getHealth() {
        return NumberConversions.ceil(this.getHealth());
    }*/

    /** @deprecated */
    @Deprecated
    public void setHealth(int health) {
        this.setHealth((double) health);
    }

    /*
    @Deprecated
    public int getMaxHealth() {
        return NumberConversions.ceil(this.getMaxHealth());
    }*/

    /** @deprecated */
    @Deprecated
    public void setMaxHealth(int health) {
        this.setMaxHealth((double) health);
    }
}
