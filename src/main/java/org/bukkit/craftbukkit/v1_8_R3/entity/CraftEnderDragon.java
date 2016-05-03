package org.bukkit.craftbukkit.v1_8_R3.entity;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.minecraft.server.v1_8_R3.EntityComplexPart;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;

public class CraftEnderDragon extends CraftComplexLivingEntity implements EnderDragon {

    public CraftEnderDragon(CraftServer server, EntityEnderDragon entity) {
        super(server, (EntityLiving) entity);
    }

    public Set getParts() {
        ImmutableSet.Builder builder = ImmutableSet.builder();
        EntityComplexPart[] aentitycomplexpart;
        int i = (aentitycomplexpart = this.getHandle().children).length;

        for (int j = 0; j < i; ++j) {
            EntityComplexPart part = aentitycomplexpart[j];

            builder.add((Object) ((ComplexEntityPart) part.getBukkitEntity()));
        }

        return builder.build();
    }

    public EntityEnderDragon getHandle() {
        return (EntityEnderDragon) this.entity;
    }

    public String toString() {
        return "CraftEnderDragon";
    }

    public EntityType getType() {
        return EntityType.ENDER_DRAGON;
    }
}
