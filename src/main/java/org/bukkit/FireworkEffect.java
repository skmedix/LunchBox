package org.bukkit;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.Validate;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("Firework")
public final class FireworkEffect implements ConfigurationSerializable {

    private static final String FLICKER = "flicker";
    private static final String TRAIL = "trail";
    private static final String COLORS = "colors";
    private static final String FADE_COLORS = "fade-colors";
    private static final String TYPE = "type";
    private final boolean flicker;
    private final boolean trail;
    private final ImmutableList colors;
    private final ImmutableList fadeColors;
    private final FireworkEffect.Type type;
    private String string = null;

    public static FireworkEffect.Builder builder() {
        return new FireworkEffect.Builder();
    }

    FireworkEffect(boolean flicker, boolean trail, ImmutableList colors, ImmutableList fadeColors, FireworkEffect.Type type) {
        if (colors.isEmpty()) {
            throw new IllegalStateException("Cannot make FireworkEffect without any color");
        } else {
            this.flicker = flicker;
            this.trail = trail;
            this.colors = colors;
            this.fadeColors = fadeColors;
            this.type = type;
        }
    }

    public boolean hasFlicker() {
        return this.flicker;
    }

    public boolean hasTrail() {
        return this.trail;
    }

    public List getColors() {
        return this.colors;
    }

    public List getFadeColors() {
        return this.fadeColors;
    }

    public FireworkEffect.Type getType() {
        return this.type;
    }

    public static ConfigurationSerializable deserialize(Map map) {
        FireworkEffect.Type type = FireworkEffect.Type.valueOf((String) map.get("type"));

        if (type == null) {
            throw new IllegalArgumentException(map.get("type") + " is not a valid Type");
        } else {
            return builder().flicker(((Boolean) map.get("flicker")).booleanValue()).trail(((Boolean) map.get("trail")).booleanValue()).withColor((Iterable) map.get("colors")).withFade((Iterable) map.get("fade-colors")).with(type).build();
        }
    }

    public Map serialize() {
        return ImmutableMap.of("flicker", Boolean.valueOf(this.flicker), "trail", Boolean.valueOf(this.trail), "colors", this.colors, "fade-colors", this.fadeColors, "type", this.type.name());
    }

    public String toString() {
        String string = this.string;

        return string == null ? (this.string = "FireworkEffect:" + this.serialize()) : string;
    }

    public int hashCode() {
        byte hash = 1;
        int hash1 = hash * 31 + (this.flicker ? 1231 : 1237);

        hash1 = hash1 * 31 + (this.trail ? 1231 : 1237);
        hash1 = hash1 * 31 + this.type.hashCode();
        hash1 = hash1 * 31 + this.colors.hashCode();
        hash1 = hash1 * 31 + this.fadeColors.hashCode();
        return hash1;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof FireworkEffect)) {
            return false;
        } else {
            FireworkEffect that = (FireworkEffect) obj;

            return this.flicker == that.flicker && this.trail == that.trail && this.type == that.type && this.colors.equals(that.colors) && this.fadeColors.equals(that.fadeColors);
        }
    }

    public static final class Builder {

        boolean flicker = false;
        boolean trail = false;
        final ImmutableList.Builder colors = ImmutableList.builder();
        ImmutableList.Builder fadeColors = null;
        FireworkEffect.Type type;

        Builder() {
            this.type = FireworkEffect.Type.BALL;
        }

        public FireworkEffect.Builder with(FireworkEffect.Type type) throws IllegalArgumentException {
            Validate.notNull(type, "Cannot have null type");
            this.type = type;
            return this;
        }

        public FireworkEffect.Builder withFlicker() {
            this.flicker = true;
            return this;
        }

        public FireworkEffect.Builder flicker(boolean flicker) {
            this.flicker = flicker;
            return this;
        }

        public FireworkEffect.Builder withTrail() {
            this.trail = true;
            return this;
        }

        public FireworkEffect.Builder trail(boolean trail) {
            this.trail = trail;
            return this;
        }

        public FireworkEffect.Builder withColor(Color color) throws IllegalArgumentException {
            Validate.notNull(color, "Cannot have null color");
            this.colors.add((Object) color);
            return this;
        }

        public FireworkEffect.Builder withColor(Color... colors) throws IllegalArgumentException {
            Validate.notNull(colors, "Cannot have null colors");
            if (colors.length == 0) {
                return this;
            } else {
                ImmutableList.Builder list = this.colors;
                Color[] acolor = colors;
                int i = colors.length;

                for (int j = 0; j < i; ++j) {
                    Color color = acolor[j];

                    Validate.notNull(color, "Color cannot be null");
                    list.add((Object) color);
                }

                return this;
            }
        }

        public FireworkEffect.Builder withColor(Iterable colors) throws IllegalArgumentException {
            Validate.notNull(colors, "Cannot have null colors");
            ImmutableList.Builder list = this.colors;
            Iterator iterator = colors.iterator();

            while (iterator.hasNext()) {
                Object color = iterator.next();

                if (!(color instanceof Color)) {
                    throw new IllegalArgumentException(color + " is not a Color in " + colors);
                }

                list.add((Object) ((Color) color));
            }

            return this;
        }

        public FireworkEffect.Builder withFade(Color color) throws IllegalArgumentException {
            Validate.notNull(color, "Cannot have null color");
            if (this.fadeColors == null) {
                this.fadeColors = ImmutableList.builder();
            }

            this.fadeColors.add((Object) color);
            return this;
        }

        public FireworkEffect.Builder withFade(Color... colors) throws IllegalArgumentException {
            Validate.notNull(colors, "Cannot have null colors");
            if (colors.length == 0) {
                return this;
            } else {
                ImmutableList.Builder list = this.fadeColors;

                if (list == null) {
                    list = this.fadeColors = ImmutableList.builder();
                }

                Color[] acolor = colors;
                int i = colors.length;

                for (int j = 0; j < i; ++j) {
                    Color color = acolor[j];

                    Validate.notNull(color, "Color cannot be null");
                    list.add((Object) color);
                }

                return this;
            }
        }

        public FireworkEffect.Builder withFade(Iterable colors) throws IllegalArgumentException {
            Validate.notNull(colors, "Cannot have null colors");
            ImmutableList.Builder list = this.fadeColors;

            if (list == null) {
                list = this.fadeColors = ImmutableList.builder();
            }

            Iterator iterator = colors.iterator();

            while (iterator.hasNext()) {
                Object color = iterator.next();

                if (!(color instanceof Color)) {
                    throw new IllegalArgumentException(color + " is not a Color in " + colors);
                }

                list.add((Object) ((Color) color));
            }

            return this;
        }

        public FireworkEffect build() {
            return new FireworkEffect(this.flicker, this.trail, this.colors.build(), this.fadeColors == null ? ImmutableList.of() : this.fadeColors.build(), this.type);
        }
    }

    public static enum Type {

        BALL, BALL_LARGE, STAR, BURST, CREEPER;
    }
}
