package org.spigotmc;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityExperienceOrb;
import net.minecraft.server.v1_8_R3.EntityGhast;
import net.minecraft.server.v1_8_R3.EntityItem;
import net.minecraft.server.v1_8_R3.EntityItemFrame;
import net.minecraft.server.v1_8_R3.EntityPainting;
import net.minecraft.server.v1_8_R3.EntityPlayer;

public class TrackingRange {

    public static int getEntityTrackingRange(Entity entity, int defaultRange) {
        SpigotWorldConfig config = entity.world.spigotConfig;

        return entity instanceof EntityPlayer ? config.playerTrackingRange : (entity.activationType == 1 ? config.monsterTrackingRange : (entity instanceof EntityGhast ? (config.monsterTrackingRange > config.monsterActivationRange ? config.monsterTrackingRange : config.monsterActivationRange) : (entity.activationType == 2 ? config.animalTrackingRange : (!(entity instanceof EntityItemFrame) && !(entity instanceof EntityPainting) && !(entity instanceof EntityItem) && !(entity instanceof EntityExperienceOrb) ? config.otherTrackingRange : config.miscTrackingRange))));
    }
}
