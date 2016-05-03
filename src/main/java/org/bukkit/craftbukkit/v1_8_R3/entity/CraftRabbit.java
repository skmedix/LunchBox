package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityAnimal;
import net.minecraft.server.v1_8_R3.EntityRabbit;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Rabbit;

public class CraftRabbit extends CraftAnimals implements Rabbit {

    public CraftRabbit(CraftServer server, EntityRabbit entity) {
        super(server, (EntityAnimal) entity);
    }

    public EntityRabbit getHandle() {
        return (EntityRabbit) this.entity;
    }

    public String toString() {
        return "CraftRabbit{RabbitType=" + this.getRabbitType() + "}";
    }

    public EntityType getType() {
        return EntityType.RABBIT;
    }

    public Rabbit.Type getRabbitType() {
        int type = this.getHandle().getRabbitType();

        return CraftRabbit.CraftMagicMapping.fromMagic(type);
    }

    public void setRabbitType(Rabbit.Type type) {
        EntityRabbit entity = this.getHandle();

        if (this.getRabbitType() == Rabbit.Type.THE_KILLER_BUNNY) {
            WorldServer world = ((CraftWorld) this.getWorld()).getHandle();

            entity.goalSelector = new PathfinderGoalSelector(world != null && world.methodProfiler != null ? world.methodProfiler : null);
            entity.targetSelector = new PathfinderGoalSelector(world != null && world.methodProfiler != null ? world.methodProfiler : null);
            entity.initializePathFinderGoals();
        }

        entity.setRabbitType(CraftRabbit.CraftMagicMapping.toMagic(type));
    }

    private static class CraftMagicMapping {

        private static final int[] types = new int[Rabbit.Type.values().length];
        private static final Rabbit.Type[] reverse = new Rabbit.Type[Rabbit.Type.values().length];

        static {
            set(Rabbit.Type.BROWN, 0);
            set(Rabbit.Type.WHITE, 1);
            set(Rabbit.Type.BLACK, 2);
            set(Rabbit.Type.BLACK_AND_WHITE, 3);
            set(Rabbit.Type.GOLD, 4);
            set(Rabbit.Type.SALT_AND_PEPPER, 5);
            set(Rabbit.Type.THE_KILLER_BUNNY, 99);
        }

        private static void set(Rabbit.Type type, int value) {
            CraftRabbit.CraftMagicMapping.types[type.ordinal()] = value;
            if (value < CraftRabbit.CraftMagicMapping.reverse.length) {
                CraftRabbit.CraftMagicMapping.reverse[value] = type;
            }

        }

        public static Rabbit.Type fromMagic(int magic) {
            return magic >= 0 && magic < CraftRabbit.CraftMagicMapping.reverse.length ? CraftRabbit.CraftMagicMapping.reverse[magic] : (magic == 99 ? Rabbit.Type.THE_KILLER_BUNNY : null);
        }

        public static int toMagic(Rabbit.Type type) {
            return CraftRabbit.CraftMagicMapping.types[type.ordinal()];
        }
    }
}
