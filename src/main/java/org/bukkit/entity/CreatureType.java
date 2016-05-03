package org.bukkit.entity;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** @deprecated */
@Deprecated
public enum CreatureType {

    CREEPER("Creeper", Creeper.class, 50), SKELETON("Skeleton", Skeleton.class, 51), SPIDER("Spider", Spider.class, 52), GIANT("Giant", Giant.class, 53), ZOMBIE("Zombie", Zombie.class, 54), SLIME("Slime", Slime.class, 55), GHAST("Ghast", Ghast.class, 56), PIG_ZOMBIE("PigZombie", PigZombie.class, 57), ENDERMAN("Enderman", Enderman.class, 58), CAVE_SPIDER("CaveSpider", CaveSpider.class, 59), SILVERFISH("Silverfish", Silverfish.class, 60), BLAZE("Blaze", Blaze.class, 61), MAGMA_CUBE("LavaSlime", MagmaCube.class, 62), ENDER_DRAGON("EnderDragon", EnderDragon.class, 63), ENDERMITE("Endermite", Endermite.class, 67), GUARDIAN("Guardian", Guardian.class, 68), PIG("Pig", Pig.class, 90), SHEEP("Sheep", Sheep.class, 91), COW("Cow", Cow.class, 92), CHICKEN("Chicken", Chicken.class, 93), SQUID("Squid", Squid.class, 94), WOLF("Wolf", Wolf.class, 95), MUSHROOM_COW("MushroomCow", MushroomCow.class, 96), SNOWMAN("SnowMan", Snowman.class, 97), RABBIT("Rabbit", Rabbit.class, 101), VILLAGER("Villager", Villager.class, 120);

    private String name;
    private Class clazz;
    private short typeId;
    private static final Map NAME_MAP = new HashMap();
    private static final Map ID_MAP = new HashMap();

    static {
        Iterator iterator = EnumSet.allOf(CreatureType.class).iterator();

        while (iterator.hasNext()) {
            CreatureType type = (CreatureType) iterator.next();

            CreatureType.NAME_MAP.put(type.name, type);
            if (type.typeId != 0) {
                CreatureType.ID_MAP.put(Short.valueOf(type.typeId), type);
            }
        }

    }

    private CreatureType(String name, Class clazz, int typeId) {
        this.name = name;
        this.clazz = clazz;
        this.typeId = (short) typeId;
    }

    public String getName() {
        return this.name;
    }

    public Class getEntityClass() {
        return this.clazz;
    }

    /** @deprecated */
    @Deprecated
    public short getTypeId() {
        return this.typeId;
    }

    public static CreatureType fromName(String name) {
        return (CreatureType) CreatureType.NAME_MAP.get(name);
    }

    /** @deprecated */
    @Deprecated
    public static CreatureType fromId(int id) {
        return id > 32767 ? null : (CreatureType) CreatureType.ID_MAP.get(Short.valueOf((short) id));
    }

    /** @deprecated */
    @Deprecated
    public EntityType toEntityType() {
        return EntityType.fromName(this.getName());
    }

    public static CreatureType fromEntityType(EntityType creatureType) {
        return fromName(creatureType.getName());
    }
}
