package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntitySkeleton;
import net.minecraft.server.EnumSkeletonType;

import org.apache.commons.lang3.Validate;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;

public class CraftSkeleton extends CraftMonster implements Skeleton {

    public CraftSkeleton(CraftServer server, EntitySkeleton entity) {
        super(server, entity);
    }

    @Override
    public EntitySkeleton getHandle() {
        return (EntitySkeleton) entity;
    }

    @Override
    public String toString() {
        return "CraftSkeleton";
    }

    public EntityType getType() {
        return EntityType.SKELETON;
    }

    public SkeletonType getSkeletonType() {
        return SkeletonType.values()[getHandle().getSkeletonType().ordinal()];
    }

    public void setSkeletonType(SkeletonType type) {
        Validate.notNull(type);
        getHandle().setSkeletonType(EnumSkeletonType.a(type.ordinal())); // PAIL: rename
    }
}
