package org.bukkit.potion;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.apache.commons.lang3.Validate;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.LivingEntity;

@SerializableAs("PotionEffect")
public class PotionEffect implements ConfigurationSerializable {

    private static final String AMPLIFIER = "amplifier";
    private static final String DURATION = "duration";
    private static final String TYPE = "effect";
    private static final String AMBIENT = "ambient";
    private static final String PARTICLES = "has-particles";
    private final int amplifier;
    private final int duration;
    private final PotionEffectType type;
    private final boolean ambient;
    private final boolean particles;

    public PotionEffect(PotionEffectType type, int duration, int amplifier, boolean ambient, boolean particles) {
        Validate.notNull(type, "effect type cannot be null");
        this.type = type;
        this.duration = duration;
        this.amplifier = amplifier;
        this.ambient = ambient;
        this.particles = particles;
    }

    public PotionEffect(PotionEffectType type, int duration, int amplifier, boolean ambient) {
        this(type, duration, amplifier, ambient, true);
    }

    public PotionEffect(PotionEffectType type, int duration, int amplifier) {
        this(type, duration, amplifier, true);
    }

    public PotionEffect(Map map) {
        this(getEffectType(map), getInt(map, "duration"), getInt(map, "amplifier"), getBool(map, "ambient", false), getBool(map, "has-particles", true));
    }

    private static PotionEffectType getEffectType(Map map) {
        int type = getInt(map, "effect");
        PotionEffectType effect = PotionEffectType.getById(type);

        if (effect != null) {
            return effect;
        } else {
            throw new NoSuchElementException(map + " does not contain " + "effect");
        }
    }

    private static int getInt(Map map, Object key) {
        Object num = map.get(key);

        if (num instanceof Integer) {
            return ((Integer) num).intValue();
        } else {
            throw new NoSuchElementException(map + " does not contain " + key);
        }
    }

    private static boolean getBool(Map map, Object key, boolean def) {
        Object bool = map.get(key);

        return bool instanceof Boolean ? ((Boolean) bool).booleanValue() : def;
    }

    public Map serialize() {
        return ImmutableMap.of("effect", Integer.valueOf(this.type.getId()), "duration", Integer.valueOf(this.duration), "amplifier", Integer.valueOf(this.amplifier), "ambient", Boolean.valueOf(this.ambient), "has-particles", Boolean.valueOf(this.particles));
    }

    public boolean apply(LivingEntity entity) {
        return entity.addPotionEffect(this);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof PotionEffect)) {
            return false;
        } else {
            PotionEffect that = (PotionEffect) obj;

            return this.type.equals(that.type) && this.ambient == that.ambient && this.amplifier == that.amplifier && this.duration == that.duration && this.particles == that.particles;
        }
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public int getDuration() {
        return this.duration;
    }

    public PotionEffectType getType() {
        return this.type;
    }

    public boolean isAmbient() {
        return this.ambient;
    }

    public boolean hasParticles() {
        return this.particles;
    }

    public int hashCode() {
        byte hash = 1;
        int hash1 = hash * 31 + this.type.hashCode();

        hash1 = hash1 * 31 + this.amplifier;
        hash1 = hash1 * 31 + this.duration;
        hash1 ^= 572662306 >> (this.ambient ? 1 : -1);
        hash1 ^= 572662306 >> (this.particles ? 1 : -1);
        return hash1;
    }

    public String toString() {
        return this.type.getName() + (this.ambient ? ":(" : ":") + this.duration + "t-x" + this.amplifier + (this.ambient ? ")" : "");
    }
}
