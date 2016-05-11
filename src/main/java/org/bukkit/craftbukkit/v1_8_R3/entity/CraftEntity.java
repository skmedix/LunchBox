package org.bukkit.craftbukkit.v1_8_R3.entity;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityAmbient;
import net.minecraft.server.v1_8_R3.EntityAnimal;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityArrow;
import net.minecraft.server.v1_8_R3.EntityBat;
import net.minecraft.server.v1_8_R3.EntityBlaze;
import net.minecraft.server.v1_8_R3.EntityBoat;
import net.minecraft.server.v1_8_R3.EntityCaveSpider;
import net.minecraft.server.v1_8_R3.EntityChicken;
import net.minecraft.server.v1_8_R3.EntityComplexPart;
import net.minecraft.server.v1_8_R3.EntityCow;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityCreeper;
import net.minecraft.server.v1_8_R3.EntityEgg;
import net.minecraft.server.v1_8_R3.EntityEnderCrystal;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.EntityEnderPearl;
import net.minecraft.server.v1_8_R3.EntityEnderSignal;
import net.minecraft.server.v1_8_R3.EntityEnderman;
import net.minecraft.server.v1_8_R3.EntityEndermite;
import net.minecraft.server.v1_8_R3.EntityExperienceOrb;
import net.minecraft.server.v1_8_R3.EntityFallingBlock;
import net.minecraft.server.v1_8_R3.EntityFireball;
import net.minecraft.server.v1_8_R3.EntityFireworks;
import net.minecraft.server.v1_8_R3.EntityFishingHook;
import net.minecraft.server.v1_8_R3.EntityFlying;
import net.minecraft.server.v1_8_R3.EntityGhast;
import net.minecraft.server.v1_8_R3.EntityGiantZombie;
import net.minecraft.server.v1_8_R3.EntityGolem;
import net.minecraft.server.v1_8_R3.EntityGuardian;
import net.minecraft.server.v1_8_R3.EntityHanging;
import net.minecraft.server.v1_8_R3.EntityHorse;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import net.minecraft.server.v1_8_R3.EntityItem;
import net.minecraft.server.v1_8_R3.EntityItemFrame;
import net.minecraft.server.v1_8_R3.EntityLargeFireball;
import net.minecraft.server.v1_8_R3.EntityLeash;
import net.minecraft.server.v1_8_R3.EntityLightning;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityMagmaCube;
import net.minecraft.server.v1_8_R3.EntityMinecartAbstract;
import net.minecraft.server.v1_8_R3.EntityMinecartChest;
import net.minecraft.server.v1_8_R3.EntityMinecartCommandBlock;
import net.minecraft.server.v1_8_R3.EntityMinecartFurnace;
import net.minecraft.server.v1_8_R3.EntityMinecartHopper;
import net.minecraft.server.v1_8_R3.EntityMinecartMobSpawner;
import net.minecraft.server.v1_8_R3.EntityMinecartRideable;
import net.minecraft.server.v1_8_R3.EntityMinecartTNT;
import net.minecraft.server.v1_8_R3.EntityMonster;
import net.minecraft.server.v1_8_R3.EntityMushroomCow;
import net.minecraft.server.v1_8_R3.EntityOcelot;
import net.minecraft.server.v1_8_R3.EntityPainting;
import net.minecraft.server.v1_8_R3.EntityPig;
import net.minecraft.server.v1_8_R3.EntityPigZombie;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EntityPotion;
import net.minecraft.server.v1_8_R3.EntityProjectile;
import net.minecraft.server.v1_8_R3.EntityRabbit;
import net.minecraft.server.v1_8_R3.EntitySheep;
import net.minecraft.server.v1_8_R3.EntitySilverfish;
import net.minecraft.server.v1_8_R3.EntitySkeleton;
import net.minecraft.server.v1_8_R3.EntitySlime;
import net.minecraft.server.v1_8_R3.EntitySmallFireball;
import net.minecraft.server.v1_8_R3.EntitySnowball;
import net.minecraft.server.v1_8_R3.EntitySnowman;
import net.minecraft.server.v1_8_R3.EntitySpider;
import net.minecraft.server.v1_8_R3.EntitySquid;
import net.minecraft.server.v1_8_R3.EntityTNTPrimed;
import net.minecraft.server.v1_8_R3.EntityTameableAnimal;
import net.minecraft.server.v1_8_R3.EntityThrownExpBottle;
import net.minecraft.server.v1_8_R3.EntityVillager;
import net.minecraft.server.v1_8_R3.EntityWaterAnimal;
import net.minecraft.server.v1_8_R3.EntityWeather;
import net.minecraft.server.v1_8_R3.EntityWitch;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.EntityWitherSkull;
import net.minecraft.server.v1_8_R3.EntityWolf;
import net.minecraft.server.v1_8_R3.EntityZombie;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public abstract class CraftEntity implements Entity {

    private static final PermissibleBase perm = new PermissibleBase(new ServerOperator() {
        public boolean isOp() {
            return false;
        }

        public void setOp(boolean value) {}
    });
    protected final CraftServer server;
    protected net.minecraft.entity.Entity entity;
    private EntityDamageEvent lastDamageEvent;
    private final Entity.Spigot spigot = new Entity.Spigot() {
        public boolean isInvulnerable() {
            return CraftEntity.this.getHandle().isInvulnerable(DamageSource.GENERIC);
        }
    };

    public CraftEntity(CraftServer server, net.minecraft.entity.Entity entity) {
        this.server = server;
        this.entity = entity;
    }

    public static CraftEntity getEntity(CraftServer server, net.minecraft.entity.Entity entity) {
        if (entity instanceof EntityLiving) {
            if (entity instanceof EntityHuman) {
                if (entity instanceof EntityPlayer) {
                    return new CraftPlayer(server, (EntityPlayer) entity);
                }

                return new CraftHumanEntity(server, (EntityHuman) entity);
            }

            if (entity instanceof EntityWaterAnimal) {
                if (entity instanceof EntitySquid) {
                    return new CraftSquid(server, (EntitySquid) entity);
                }

                return new CraftWaterMob(server, (EntityWaterAnimal) entity);
            }

            if (!(entity instanceof EntityCreature)) {
                if (entity instanceof EntitySlime) {
                    if (entity instanceof EntityMagmaCube) {
                        return new CraftMagmaCube(server, (EntityMagmaCube) entity);
                    }

                    return new CraftSlime(server, (EntitySlime) entity);
                }

                if (entity instanceof EntityFlying) {
                    if (entity instanceof EntityGhast) {
                        return new CraftGhast(server, (EntityGhast) entity);
                    }

                    return new CraftFlying(server, (EntityFlying) entity);
                }

                if (entity instanceof EntityEnderDragon) {
                    return new CraftEnderDragon(server, (EntityEnderDragon) entity);
                }

                if (entity instanceof EntityAmbient) {
                    if (entity instanceof EntityBat) {
                        return new CraftBat(server, (EntityBat) entity);
                    }

                    return new CraftAmbient(server, (EntityAmbient) entity);
                }

                if (entity instanceof EntityArmorStand) {
                    return new CraftArmorStand(server, (EntityArmorStand) entity);
                }

                return new CraftLivingEntity(server, (EntityLiving) entity);
            }

            if (entity instanceof EntityAnimal) {
                if (entity instanceof EntityChicken) {
                    return new CraftChicken(server, (EntityChicken) entity);
                }

                if (entity instanceof EntityCow) {
                    if (entity instanceof EntityMushroomCow) {
                        return new CraftMushroomCow(server, (EntityMushroomCow) entity);
                    }

                    return new CraftCow(server, (EntityCow) entity);
                }

                if (entity instanceof EntityPig) {
                    return new CraftPig(server, (EntityPig) entity);
                }

                if (!(entity instanceof EntityTameableAnimal)) {
                    if (entity instanceof EntitySheep) {
                        return new CraftSheep(server, (EntitySheep) entity);
                    }

                    if (entity instanceof EntityHorse) {
                        return new CraftHorse(server, (EntityHorse) entity);
                    }

                    if (entity instanceof EntityRabbit) {
                        return new CraftRabbit(server, (EntityRabbit) entity);
                    }

                    return new CraftAnimals(server, (EntityAnimal) entity);
                }

                if (entity instanceof EntityWolf) {
                    return new CraftWolf(server, (EntityWolf) entity);
                }

                if (entity instanceof EntityOcelot) {
                    return new CraftOcelot(server, (EntityOcelot) entity);
                }
            } else {
                if (entity instanceof EntityMonster) {
                    if (entity instanceof EntityZombie) {
                        if (entity instanceof EntityPigZombie) {
                            return new CraftPigZombie(server, (EntityPigZombie) entity);
                        }

                        return new CraftZombie(server, (EntityZombie) entity);
                    }

                    if (entity instanceof EntityCreeper) {
                        return new CraftCreeper(server, (EntityCreeper) entity);
                    }

                    if (entity instanceof EntityEnderman) {
                        return new CraftEnderman(server, (EntityEnderman) entity);
                    }

                    if (entity instanceof EntitySilverfish) {
                        return new CraftSilverfish(server, (EntitySilverfish) entity);
                    }

                    if (entity instanceof EntityGiantZombie) {
                        return new CraftGiant(server, (EntityGiantZombie) entity);
                    }

                    if (entity instanceof EntitySkeleton) {
                        return new CraftSkeleton(server, (EntitySkeleton) entity);
                    }

                    if (entity instanceof EntityBlaze) {
                        return new CraftBlaze(server, (EntityBlaze) entity);
                    }

                    if (entity instanceof EntityWitch) {
                        return new CraftWitch(server, (EntityWitch) entity);
                    }

                    if (entity instanceof EntityWither) {
                        return new CraftWither(server, (EntityWither) entity);
                    }

                    if (entity instanceof EntitySpider) {
                        if (entity instanceof EntityCaveSpider) {
                            return new CraftCaveSpider(server, (EntityCaveSpider) entity);
                        }

                        return new CraftSpider(server, (EntitySpider) entity);
                    }

                    if (entity instanceof EntityEndermite) {
                        return new CraftEndermite(server, (EntityEndermite) entity);
                    }

                    if (entity instanceof EntityGuardian) {
                        return new CraftGuardian(server, (EntityGuardian) entity);
                    }

                    return new CraftMonster(server, (EntityMonster) entity);
                }

                if (!(entity instanceof EntityGolem)) {
                    if (entity instanceof EntityVillager) {
                        return new CraftVillager(server, (EntityVillager) entity);
                    }

                    return new CraftCreature(server, (EntityCreature) entity);
                }

                if (entity instanceof EntitySnowman) {
                    return new CraftSnowman(server, (EntitySnowman) entity);
                }

                if (entity instanceof EntityIronGolem) {
                    return new CraftIronGolem(server, (EntityIronGolem) entity);
                }
            }
        } else {
            if (entity instanceof EntityComplexPart) {
                EntityComplexPart part = (EntityComplexPart) entity;

                if (part.owner instanceof EntityEnderDragon) {
                    return new CraftEnderDragonPart(server, (EntityComplexPart) entity);
                }

                return new CraftComplexPart(server, (EntityComplexPart) entity);
            }

            if (entity instanceof EntityExperienceOrb) {
                return new CraftExperienceOrb(server, (EntityExperienceOrb) entity);
            }

            if (entity instanceof EntityArrow) {
                return new CraftArrow(server, (EntityArrow) entity);
            }

            if (entity instanceof EntityBoat) {
                return new CraftBoat(server, (EntityBoat) entity);
            }

            if (entity instanceof EntityProjectile) {
                if (entity instanceof EntityEgg) {
                    return new CraftEgg(server, (EntityEgg) entity);
                }

                if (entity instanceof EntitySnowball) {
                    return new CraftSnowball(server, (EntitySnowball) entity);
                }

                if (entity instanceof EntityPotion) {
                    return new CraftThrownPotion(server, (EntityPotion) entity);
                }

                if (entity instanceof EntityEnderPearl) {
                    return new CraftEnderPearl(server, (EntityEnderPearl) entity);
                }

                if (entity instanceof EntityThrownExpBottle) {
                    return new CraftThrownExpBottle(server, (EntityThrownExpBottle) entity);
                }
            } else {
                if (entity instanceof EntityFallingBlock) {
                    return new CraftFallingSand(server, (EntityFallingBlock) entity);
                }

                if (entity instanceof EntityFireball) {
                    if (entity instanceof EntitySmallFireball) {
                        return new CraftSmallFireball(server, (EntitySmallFireball) entity);
                    }

                    if (entity instanceof EntityLargeFireball) {
                        return new CraftLargeFireball(server, (EntityLargeFireball) entity);
                    }

                    if (entity instanceof EntityWitherSkull) {
                        return new CraftWitherSkull(server, (EntityWitherSkull) entity);
                    }

                    return new CraftFireball(server, (EntityFireball) entity);
                }

                if (entity instanceof EntityEnderSignal) {
                    return new CraftEnderSignal(server, (EntityEnderSignal) entity);
                }

                if (entity instanceof EntityEnderCrystal) {
                    return new CraftEnderCrystal(server, (EntityEnderCrystal) entity);
                }

                if (entity instanceof EntityFishingHook) {
                    return new CraftFish(server, (EntityFishingHook) entity);
                }

                if (entity instanceof EntityItem) {
                    return new CraftItem(server, (EntityItem) entity);
                }

                if (entity instanceof EntityWeather) {
                    if (entity instanceof EntityLightning) {
                        return new CraftLightningStrike(server, (EntityLightning) entity);
                    }

                    return new CraftWeather(server, (EntityWeather) entity);
                }

                if (entity instanceof EntityMinecartAbstract) {
                    if (entity instanceof EntityMinecartFurnace) {
                        return new CraftMinecartFurnace(server, (EntityMinecartFurnace) entity);
                    }

                    if (entity instanceof EntityMinecartChest) {
                        return new CraftMinecartChest(server, (EntityMinecartChest) entity);
                    }

                    if (entity instanceof EntityMinecartTNT) {
                        return new CraftMinecartTNT(server, (EntityMinecartTNT) entity);
                    }

                    if (entity instanceof EntityMinecartHopper) {
                        return new CraftMinecartHopper(server, (EntityMinecartHopper) entity);
                    }

                    if (entity instanceof EntityMinecartMobSpawner) {
                        return new CraftMinecartMobSpawner(server, (EntityMinecartMobSpawner) entity);
                    }

                    if (entity instanceof EntityMinecartRideable) {
                        return new CraftMinecartRideable(server, (EntityMinecartRideable) entity);
                    }

                    if (entity instanceof EntityMinecartCommandBlock) {
                        return new CraftMinecartCommand(server, (EntityMinecartCommandBlock) entity);
                    }
                } else {
                    if (entity instanceof EntityHanging) {
                        if (entity instanceof EntityPainting) {
                            return new CraftPainting(server, (EntityPainting) entity);
                        }

                        if (entity instanceof EntityItemFrame) {
                            return new CraftItemFrame(server, (EntityItemFrame) entity);
                        }

                        if (entity instanceof EntityLeash) {
                            return new CraftLeash(server, (EntityLeash) entity);
                        }

                        return new CraftHanging(server, (EntityHanging) entity);
                    }

                    if (entity instanceof EntityTNTPrimed) {
                        return new CraftTNTPrimed(server, (EntityTNTPrimed) entity);
                    }

                    if (entity instanceof EntityFireworks) {
                        return new CraftFirework(server, (EntityFireworks) entity);
                    }
                }
            }
        }

        throw new AssertionError("Unknown entity " + (entity == null ? null : entity.getClass()));
    }

    public Location getLocation() {
        return new Location(this.getWorld(), this.entity.locX, this.entity.locY, this.entity.locZ, this.entity.yaw, this.entity.pitch);
    }

    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(this.getWorld());
            loc.setX(this.entity.locX);
            loc.setY(this.entity.locY);
            loc.setZ(this.entity.locZ);
            loc.setYaw(this.entity.yaw);
            loc.setPitch(this.entity.pitch);
        }

        return loc;
    }

    public Vector getVelocity() {
        return new Vector(this.entity.motX, this.entity.motY, this.entity.motZ);
    }

    public void setVelocity(Vector vel) {
        this.entity.motX = vel.getX();
        this.entity.motY = vel.getY();
        this.entity.motZ = vel.getZ();
        this.entity.velocityChanged = true;
    }

    public boolean isOnGround() {
        return this.entity instanceof EntityArrow ? ((EntityArrow) this.entity).isInGround() : this.entity.onGround;
    }

    public World getWorld() {
        return this.entity.world.getWorld();
    }

    public boolean teleport(Location location) {
        return this.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        if (this.entity.passenger == null && !this.entity.dead) {
            this.entity.mount((net.minecraft.server.v1_8_R3.Entity) null);
            if (!location.getWorld().equals(this.getWorld())) {
                this.entity.teleportTo(location, cause.equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL));
                return true;
            } else {
                this.entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean teleport(Entity destination) {
        return this.teleport(destination.getLocation());
    }

    public boolean teleport(Entity destination, PlayerTeleportEvent.TeleportCause cause) {
        return this.teleport(destination.getLocation(), cause);
    }

    public List getNearbyEntities(double x, double y, double z) {
        List notchEntityList = this.entity.world.a(this.entity, this.entity.getBoundingBox().grow(x, y, z), (Predicate) null);
        ArrayList bukkitEntityList = new ArrayList(notchEntityList.size());
        Iterator iterator = notchEntityList.iterator();

        while (iterator.hasNext()) {
            net.minecraft.server.v1_8_R3.Entity e = (net.minecraft.server.v1_8_R3.Entity) iterator.next();

            bukkitEntityList.add(e.getBukkitEntity());
        }

        return bukkitEntityList;
    }

    public int getEntityId() {
        return this.entity.getId();
    }

    public int getFireTicks() {
        return this.entity.fireTicks;
    }

    public int getMaxFireTicks() {
        return this.entity.maxFireTicks;
    }

    public void setFireTicks(int ticks) {
        this.entity.fireTicks = ticks;
    }

    public void remove() {
        this.entity.dead = true;
    }

    public boolean isDead() {
        return !this.entity.isAlive();
    }

    public boolean isValid() {
        return this.entity.isAlive() && this.entity.valid;
    }

    public Server getServer() {
        return this.server;
    }

    public Vector getMomentum() {
        return this.getVelocity();
    }

    public void setMomentum(Vector value) {
        this.setVelocity(value);
    }

    public Entity getPassenger() {
        return this.isEmpty() ? null : this.getHandle().passenger.getBukkitEntity();
    }

    public boolean setPassenger(Entity passenger) {
        Preconditions.checkArgument(!this.equals(passenger), "Entity cannot ride itself.");
        if (passenger instanceof CraftEntity) {
            ((CraftEntity) passenger).getHandle().mount(this.getHandle());
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmpty() {
        return this.getHandle().passenger == null;
    }

    public boolean eject() {
        if (this.getHandle().passenger == null) {
            return false;
        } else {
            this.getHandle().passenger.mount((net.minecraft.server.v1_8_R3.Entity) null);
            return true;
        }
    }

    public float getFallDistance() {
        return this.getHandle().fallDistance;
    }

    public void setFallDistance(float distance) {
        this.getHandle().fallDistance = distance;
    }

    public void setLastDamageCause(EntityDamageEvent event) {
        this.lastDamageEvent = event;
    }

    public EntityDamageEvent getLastDamageCause() {
        return this.lastDamageEvent;
    }

    public UUID getUniqueId() {
        return this.getHandle().getUniqueID();
    }

    public int getTicksLived() {
        return this.getHandle().ticksLived;
    }

    public void setTicksLived(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Age must be at least 1 tick");
        } else {
            this.getHandle().ticksLived = value;
        }
    }

    public net.minecraft.entity.Entity getHandle() {
        return this.entity;
    }

    public void playEffect(EntityEffect type) {
        this.getHandle().world.broadcastEntityEffect(this.getHandle(), type.getData());
    }

    public void setHandle(net.minecraft.entity.Entity entity) {
        this.entity = entity;
    }

    public String toString() {
        return "CraftEntity{id=" + this.getEntityId() + '}';
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            CraftEntity other = (CraftEntity) obj;

            return this.getEntityId() == other.getEntityId();
        }
    }

    public int hashCode() {
        byte hash = 7;
        int hash1 = 29 * hash + this.getEntityId();

        return hash1;
    }

    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        this.server.getEntityMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    public List getMetadata(String metadataKey) {
        return this.server.getEntityMetadata().getMetadata(this, metadataKey);
    }

    public boolean hasMetadata(String metadataKey) {
        return this.server.getEntityMetadata().hasMetadata(this, metadataKey);
    }

    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        this.server.getEntityMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    public boolean isInsideVehicle() {
        return this.getHandle().vehicle != null;
    }

    public boolean leaveVehicle() {
        if (this.getHandle().vehicle == null) {
            return false;
        } else {
            this.getHandle().mount((net.minecraft.server.v1_8_R3.Entity) null);
            return true;
        }
    }

    public Entity getVehicle() {
        return this.getHandle().vehicle == null ? null : this.getHandle().vehicle.getBukkitEntity();
    }

    public void setCustomName(String name) {
        if (name == null) {
            name = "";
        }

        this.getHandle().setCustomName(name);
    }

    public String getCustomName() {
        String name = this.getHandle().getCustomName();

        return name != null && name.length() != 0 ? name : null;
    }

    public void setCustomNameVisible(boolean flag) {
        this.getHandle().setCustomNameVisible(flag);
    }

    public boolean isCustomNameVisible() {
        return this.getHandle().getCustomNameVisible();
    }

    public void sendMessage(String message) {}

    public void sendMessage(String[] messages) {}

    public String getName() {
        return this.getHandle().getName();
    }

    public boolean isPermissionSet(String name) {
        return CraftEntity.perm.isPermissionSet(name);
    }

    public boolean isPermissionSet(Permission perm) {
        return CraftEntity.perm.isPermissionSet(perm);
    }

    public boolean hasPermission(String name) {
        return CraftEntity.perm.hasPermission(name);
    }

    public boolean hasPermission(Permission perm) {
        return CraftEntity.perm.hasPermission(perm);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return CraftEntity.perm.addAttachment(plugin, name, value);
    }

    public PermissionAttachment addAttachment(Plugin plugin) {
        return CraftEntity.perm.addAttachment(plugin);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return CraftEntity.perm.addAttachment(plugin, name, value, ticks);
    }

    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return CraftEntity.perm.addAttachment(plugin, ticks);
    }

    public void removeAttachment(PermissionAttachment attachment) {
        CraftEntity.perm.removeAttachment(attachment);
    }

    public void recalculatePermissions() {
        CraftEntity.perm.recalculatePermissions();
    }

    public Set getEffectivePermissions() {
        return CraftEntity.perm.getEffectivePermissions();
    }

    public boolean isOp() {
        return CraftEntity.perm.isOp();
    }

    public void setOp(boolean value) {
        CraftEntity.perm.setOp(value);
    }

    public Entity.Spigot spigot() {
        return this.spigot;
    }
}
