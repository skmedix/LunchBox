package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;

public enum EntityEffect {

    HURT(2), DEATH(3), WOLF_SMOKE(6), WOLF_HEARTS(7), WOLF_SHAKE(8), SHEEP_EAT(10), IRON_GOLEM_ROSE(11), VILLAGER_HEART(12), VILLAGER_ANGRY(13), VILLAGER_HAPPY(14), WITCH_MAGIC(15), ZOMBIE_TRANSFORM(16), FIREWORK_EXPLODE(17);

    private final byte data;
    private static final Map BY_DATA = Maps.newHashMap();

    static {
        EntityEffect[] aentityeffect;
        int i = (aentityeffect = values()).length;

        for (int j = 0; j < i; ++j) {
            EntityEffect entityEffect = aentityeffect[j];

            EntityEffect.BY_DATA.put(Byte.valueOf(entityEffect.data), entityEffect);
        }

    }

    private EntityEffect(int data) {
        this.data = (byte) data;
    }

    /** @deprecated */
    @Deprecated
    public byte getData() {
        return this.data;
    }

    /** @deprecated */
    @Deprecated
    public static EntityEffect getByData(byte data) {
        return (EntityEffect) EntityEffect.BY_DATA.get(Byte.valueOf(data));
    }
}
