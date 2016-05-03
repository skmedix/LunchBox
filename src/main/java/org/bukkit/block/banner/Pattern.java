package org.bukkit.block.banner;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.bukkit.DyeColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("Pattern")
public class Pattern implements ConfigurationSerializable {

    private static final String COLOR = "color";
    private static final String PATTERN = "pattern";
    private final DyeColor color;
    private final PatternType pattern;

    public Pattern(DyeColor color, PatternType pattern) {
        this.color = color;
        this.pattern = pattern;
    }

    public Pattern(Map map) {
        this.color = DyeColor.valueOf(getString(map, "color"));
        this.pattern = PatternType.getByIdentifier(getString(map, "pattern"));
    }

    private static String getString(Map map, Object key) {
        Object str = map.get(key);

        if (str instanceof String) {
            return (String) str;
        } else {
            throw new NoSuchElementException(map + " does not contain " + key);
        }
    }

    public Map serialize() {
        return ImmutableMap.of("color", this.color.toString(), "pattern", this.pattern.getIdentifier());
    }

    public DyeColor getColor() {
        return this.color;
    }

    public PatternType getPattern() {
        return this.pattern;
    }

    public int hashCode() {
        byte hash = 3;
        int hash1 = 97 * hash + (this.color != null ? this.color.hashCode() : 0);

        hash1 = 97 * hash1 + (this.pattern != null ? this.pattern.hashCode() : 0);
        return hash1;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            Pattern other = (Pattern) obj;

            return this.color == other.color && this.pattern == other.pattern;
        }
    }
}
