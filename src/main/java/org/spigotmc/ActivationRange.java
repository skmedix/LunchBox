package org.spigotmc;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.Chunk;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityAmbient;
import net.minecraft.server.v1_8_R3.EntityAnimal;
import net.minecraft.server.v1_8_R3.EntityArrow;
import net.minecraft.server.v1_8_R3.EntityComplexPart;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityCreeper;
import net.minecraft.server.v1_8_R3.EntityEnderCrystal;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.EntityFireball;
import net.minecraft.server.v1_8_R3.EntityFireworks;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityMonster;
import net.minecraft.server.v1_8_R3.EntityProjectile;
import net.minecraft.server.v1_8_R3.EntitySheep;
import net.minecraft.server.v1_8_R3.EntitySlime;
import net.minecraft.server.v1_8_R3.EntityTNTPrimed;
import net.minecraft.server.v1_8_R3.EntityVillager;
import net.minecraft.server.v1_8_R3.EntityWeather;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.SpigotTimings;

public class ActivationRange {

    static AxisAlignedBB maxBB = AxisAlignedBB.a(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    static AxisAlignedBB miscBB = AxisAlignedBB.a(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    static AxisAlignedBB animalBB = AxisAlignedBB.a(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    static AxisAlignedBB monsterBB = AxisAlignedBB.a(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);

    public static byte initializeEntityActivationType(Entity entity) {
        return (byte) (!(entity instanceof EntityMonster) && !(entity instanceof EntitySlime) ? (!(entity instanceof EntityCreature) && !(entity instanceof EntityAmbient) ? 3 : 2) : 1);
    }

    public static boolean initializeEntityActivationState(Entity entity, SpigotWorldConfig config) {
        return entity.activationType == 3 && config.miscActivationRange == 0 || entity.activationType == 2 && config.animalActivationRange == 0 || entity.activationType == 1 && config.monsterActivationRange == 0 || entity instanceof EntityHuman || entity instanceof EntityProjectile || entity instanceof EntityEnderDragon || entity instanceof EntityComplexPart || entity instanceof EntityWither || entity instanceof EntityFireball || entity instanceof EntityWeather || entity instanceof EntityTNTPrimed || entity instanceof EntityEnderCrystal || entity instanceof EntityFireworks;
    }

    public static void activateEntities(World world) {
        SpigotTimings.entityActivationCheckTimer.startTiming();
        int miscActivationRange = world.spigotConfig.miscActivationRange;
        int animalActivationRange = world.spigotConfig.animalActivationRange;
        int monsterActivationRange = world.spigotConfig.monsterActivationRange;
        int maxRange = Math.max(monsterActivationRange, animalActivationRange);

        maxRange = Math.max(maxRange, miscActivationRange);
        maxRange = Math.min((world.spigotConfig.viewDistance << 4) - 8, maxRange);
        Iterator iterator = world.players.iterator();

        while (iterator.hasNext()) {
            Entity player = (Entity) iterator.next();

            player.activatedTick = (long) MinecraftServer.currentTick;
            ActivationRange.maxBB = player.getBoundingBox().grow((double) maxRange, 256.0D, (double) maxRange);
            ActivationRange.miscBB = player.getBoundingBox().grow((double) miscActivationRange, 256.0D, (double) miscActivationRange);
            ActivationRange.animalBB = player.getBoundingBox().grow((double) animalActivationRange, 256.0D, (double) animalActivationRange);
            ActivationRange.monsterBB = player.getBoundingBox().grow((double) monsterActivationRange, 256.0D, (double) monsterActivationRange);
            int i = MathHelper.floor(ActivationRange.maxBB.a / 16.0D);
            int j = MathHelper.floor(ActivationRange.maxBB.d / 16.0D);
            int k = MathHelper.floor(ActivationRange.maxBB.c / 16.0D);
            int l = MathHelper.floor(ActivationRange.maxBB.f / 16.0D);

            for (int i1 = i; i1 <= j; ++i1) {
                for (int j1 = k; j1 <= l; ++j1) {
                    if (world.getWorld().isChunkLoaded(i1, j1)) {
                        activateChunkEntities(world.getChunkAt(i1, j1));
                    }
                }
            }
        }

        SpigotTimings.entityActivationCheckTimer.stopTiming();
    }

    private static void activateChunkEntities(Chunk chunk) {
        List[] alist = chunk.entitySlices;
        int i = chunk.entitySlices.length;

        for (int j = 0; j < i; ++j) {
            List slice = alist[j];
            Iterator iterator = slice.iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                if ((long) MinecraftServer.currentTick > entity.activatedTick) {
                    if (entity.defaultActivationState) {
                        entity.activatedTick = (long) MinecraftServer.currentTick;
                    } else {
                        switch (entity.activationType) {
                        case 1:
                            if (ActivationRange.monsterBB.b(entity.getBoundingBox())) {
                                entity.activatedTick = (long) MinecraftServer.currentTick;
                            }
                            break;

                        case 2:
                            if (ActivationRange.animalBB.b(entity.getBoundingBox())) {
                                entity.activatedTick = (long) MinecraftServer.currentTick;
                            }
                            break;

                        case 3:
                        default:
                            if (ActivationRange.miscBB.b(entity.getBoundingBox())) {
                                entity.activatedTick = (long) MinecraftServer.currentTick;
                            }
                        }
                    }
                }
            }
        }

    }

    public static boolean checkEntityImmunities(Entity entity) {
        if (!entity.inWater && entity.fireTicks <= 0) {
            if (!(entity instanceof EntityArrow)) {
                if (!entity.onGround || entity.passenger != null || entity.vehicle != null) {
                    return true;
                }
            } else if (!((EntityArrow) entity).inGround) {
                return true;
            }

            if (entity instanceof EntityLiving) {
                EntityLiving living = (EntityLiving) entity;

                if (living.hurtTicks > 0 || living.effects.size() > 0) {
                    return true;
                }

                if (entity instanceof EntityCreature && ((EntityCreature) entity).getGoalTarget() != null) {
                    return true;
                }

                if (entity instanceof EntityVillager && ((EntityVillager) entity).cm()) {
                    return true;
                }

                if (entity instanceof EntityAnimal) {
                    EntityAnimal animal = (EntityAnimal) entity;

                    if (animal.isBaby() || animal.isInLove()) {
                        return true;
                    }

                    if (entity instanceof EntitySheep && ((EntitySheep) entity).isSheared()) {
                        return true;
                    }
                }

                if (entity instanceof EntityCreeper && ((EntityCreeper) entity).cn()) {
                    return true;
                }
            }

            return false;
        } else {
            return true;
        }
    }

    public static boolean checkIfActive(Entity entity) {
        SpigotTimings.checkIfActiveTimer.startTiming();
        if (entity.isAddedToChunk() && !(entity instanceof EntityFireworks)) {
            boolean isActive = entity.activatedTick >= (long) MinecraftServer.currentTick || entity.defaultActivationState;

            if (!isActive) {
                if (((long) MinecraftServer.currentTick - entity.activatedTick - 1L) % 20L == 0L) {
                    if (checkEntityImmunities(entity)) {
                        entity.activatedTick = (long) (MinecraftServer.currentTick + 20);
                    }

                    isActive = true;
                }
            } else if (!entity.defaultActivationState && entity.ticksLived % 4 == 0 && !checkEntityImmunities(entity)) {
                isActive = false;
            }

            int x = MathHelper.floor(entity.locX);
            int z = MathHelper.floor(entity.locZ);
            Chunk chunk = entity.world.getChunkIfLoaded(x >> 4, z >> 4);

            if (isActive && (chunk == null || !chunk.areNeighborsLoaded(1))) {
                isActive = false;
            }

            SpigotTimings.checkIfActiveTimer.stopTiming();
            return isActive;
        } else {
            SpigotTimings.checkIfActiveTimer.stopTiming();
            return true;
        }
    }
}
