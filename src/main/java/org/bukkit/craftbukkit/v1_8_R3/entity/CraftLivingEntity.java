package org.bukkit.craftbukkit.v1_8_R3.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityArrow;
import net.minecraft.server.v1_8_R3.EntityEgg;
import net.minecraft.server.v1_8_R3.EntityEnderPearl;
import net.minecraft.server.v1_8_R3.EntityFireball;
import net.minecraft.server.v1_8_R3.EntityFishingHook;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityLargeFireball;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityPotion;
import net.minecraft.server.v1_8_R3.EntitySmallFireball;
import net.minecraft.server.v1_8_R3.EntitySnowball;
import net.minecraft.server.v1_8_R3.EntityThrownExpBottle;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.EntityWitherSkull;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.MobEffect;
import net.minecraft.server.v1_8_R3.MobEffectList;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.apache.commons.lang.Validate;
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

    public CraftLivingEntity(CraftServer server, EntityLiving entity) {
        super(server, entity);
        if (entity instanceof EntityInsentient || entity instanceof EntityArmorStand) {
            this.equipment = new CraftEntityEquipment(this);
        }

    }

    public double getHealth() {
        return Math.min((double) Math.max(0.0F, this.getHandle().getHealth()), this.getMaxHealth());
    }

    public void setHealth(double health) {
        if (health >= 0.0D && health <= this.getMaxHealth()) {
            if (this.entity instanceof EntityPlayer && health == 0.0D) {
                ((EntityPlayer) this.entity).die(DamageSource.GENERIC);
            }

            this.getHandle().setHealth((float) health);
        } else {
            throw new IllegalArgumentException("Health must be between 0 and " + this.getMaxHealth());
        }
    }

    public double getMaxHealth() {
        return (double) this.getHandle().getMaxHealth();
    }

    public void setMaxHealth(double amount) {
        Validate.isTrue(amount > 0.0D, "Max health must be greater than 0");
        this.getHandle().getAttributeInstance(GenericAttributes.maxHealth).setValue(amount);
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
        return (double) this.getHandle().getHeadHeight();
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
        return this.getHandle().getAirTicks();
    }

    public void setRemainingAir(int ticks) {
        this.getHandle().setAirTicks(ticks);
    }

    public int getMaximumAir() {
        return this.getHandle().maxAirTicks;
    }

    public void setMaximumAir(int ticks) {
        this.getHandle().maxAirTicks = ticks;
    }

    public void damage(double amount) {
        this.damage(amount, (Entity) null);
    }

    public void damage(double amount, Entity source) {
        DamageSource reason = DamageSource.GENERIC;

        if (source instanceof HumanEntity) {
            reason = DamageSource.playerAttack(((CraftHumanEntity) source).getHandle());
        } else if (source instanceof LivingEntity) {
            reason = DamageSource.mobAttack(((CraftLivingEntity) source).getHandle());
        }

        this.entity.damageEntity(reason, (float) amount);
    }

    public Location getEyeLocation() {
        Location loc = this.getLocation();

        loc.setY(loc.getY() + this.getEyeHeight());
        return loc;
    }

    public int getMaximumNoDamageTicks() {
        return this.getHandle().maxNoDamageTicks;
    }

    public void setMaximumNoDamageTicks(int ticks) {
        this.getHandle().maxNoDamageTicks = ticks;
    }

    public double getLastDamage() {
        return (double) this.getHandle().lastDamage;
    }

    public void setLastDamage(double damage) {
        this.getHandle().lastDamage = (float) damage;
    }

    public int getNoDamageTicks() {
        return this.getHandle().noDamageTicks;
    }

    public void setNoDamageTicks(int ticks) {
        this.getHandle().noDamageTicks = ticks;
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
        return this.getHandle().killer == null ? null : (Player) this.getHandle().killer.getBukkitEntity();
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

        this.getHandle().addEffect(new MobEffect(effect.getType().getId(), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles()));
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
        return this.getHandle().hasEffect(MobEffectList.byId[type.getId()]);
    }

    public void removePotionEffect(PotionEffectType type) {
        this.getHandle().removeEffect(type.getId());
    }

    public Collection getActivePotionEffects() {
        ArrayList effects = new ArrayList();
        Iterator iterator = this.getHandle().effects.values().iterator();

        while (iterator.hasNext()) {
            Object raw = iterator.next();

            if (raw instanceof MobEffect) {
                MobEffect handle = (MobEffect) raw;

                effects.add(new PotionEffect(PotionEffectType.getById(handle.getEffectId()), handle.getDuration(), handle.getAmplifier(), handle.isAmbient(), handle.isShowParticles()));
            }
        }

        return effects;
    }

    public Projectile launchProjectile(Class projectile) {
        return this.launchProjectile(projectile, (Vector) null);
    }

    public Projectile launchProjectile(Class projectile, Vector velocity) {
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
            launch = new EntityThrownExpBottle(world, this.getHandle());
        } else if (Fish.class.isAssignableFrom(projectile) && this.getHandle() instanceof EntityHuman) {
            launch = new EntityFishingHook(world, (EntityHuman) this.getHandle());
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

            ((EntityFireball) launch).projectileSource = this;
            ((net.minecraft.server.v1_8_R3.Entity) launch).setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        }

        Validate.notNull(launch, "Projectile not supported");
        if (velocity != null) {
            ((Projectile) ((net.minecraft.server.v1_8_R3.Entity) launch).getBukkitEntity()).setVelocity(velocity);
        }

        world.addEntity((net.minecraft.server.v1_8_R3.Entity) launch);
        return (Projectile) ((net.minecraft.server.v1_8_R3.Entity) launch).getBukkitEntity();
    }

    public EntityType getType() {
        return EntityType.UNKNOWN;
    }

    public boolean hasLineOfSight(Entity other) {
        return this.getHandle().hasLineOfSight(((CraftEntity) other).getHandle());
    }

    public boolean getRemoveWhenFarAway() {
        return this.getHandle() instanceof EntityInsentient && !((EntityInsentient) this.getHandle()).persistent;
    }

    public void setRemoveWhenFarAway(boolean remove) {
        if (this.getHandle() instanceof EntityInsentient) {
            ((EntityInsentient) this.getHandle()).persistent = !remove;
        }

    }

    public EntityEquipment getEquipment() {
        return this.equipment;
    }

    public void setCanPickupItems(boolean pickup) {
        if (this.getHandle() instanceof EntityInsentient) {
            ((EntityInsentient) this.getHandle()).canPickUpLoot = pickup;
        }

    }

    public boolean getCanPickupItems() {
        return this.getHandle() instanceof EntityInsentient && ((EntityInsentient) this.getHandle()).canPickUpLoot;
    }

    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        return this.getHealth() == 0.0D ? false : super.teleport(location, cause);
    }

    public boolean isLeashed() {
        return !(this.getHandle() instanceof EntityInsentient) ? false : ((EntityInsentient) this.getHandle()).getLeashHolder() != null;
    }

    public Entity getLeashHolder() throws IllegalStateException {
        if (!this.isLeashed()) {
            throw new IllegalStateException("Entity not leashed");
        } else {
            return ((EntityInsentient) this.getHandle()).getLeashHolder().getBukkitEntity();
        }
    }

    private boolean unleash() {
        if (!this.isLeashed()) {
            return false;
        } else {
            ((EntityInsentient) this.getHandle()).unleash(true, false);
            return true;
        }
    }

    public boolean setLeashHolder(Entity holder) {
        if (!(this.getHandle() instanceof EntityWither) && this.getHandle() instanceof EntityInsentient) {
            if (holder == null) {
                return this.unleash();
            } else if (holder.isDead()) {
                return false;
            } else {
                this.unleash();
                ((EntityInsentient) this.getHandle()).setLeashHolder(((CraftEntity) holder).getHandle(), true);
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

    /** @deprecated */
    @Deprecated
    public int getHealth() {
        return NumberConversions.ceil(this.getHealth());
    }

    /** @deprecated */
    @Deprecated
    public void setHealth(int health) {
        this.setHealth((double) health);
    }

    /** @deprecated */
    @Deprecated
    public int getMaxHealth() {
        return NumberConversions.ceil(this.getMaxHealth());
    }

    /** @deprecated */
    @Deprecated
    public void setMaxHealth(int health) {
        this.setMaxHealth((double) health);
    }
}
