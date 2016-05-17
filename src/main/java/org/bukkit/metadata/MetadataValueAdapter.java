package org.bukkit.metadata;

import java.lang.ref.WeakReference;
import org.apache.commons.lang3.Validate;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.NumberConversions;

public abstract class MetadataValueAdapter implements MetadataValue {

    protected final WeakReference owningPlugin;

    protected MetadataValueAdapter(Plugin owningPlugin) {
        Validate.notNull(owningPlugin, "owningPlugin cannot be null");
        this.owningPlugin = new WeakReference(owningPlugin);
    }

    public Plugin getOwningPlugin() {
        return (Plugin) this.owningPlugin.get();
    }

    public int asInt() {
        return NumberConversions.toInt(this.value());
    }

    public float asFloat() {
        return NumberConversions.toFloat(this.value());
    }

    public double asDouble() {
        return NumberConversions.toDouble(this.value());
    }

    public long asLong() {
        return NumberConversions.toLong(this.value());
    }

    public short asShort() {
        return NumberConversions.toShort(this.value());
    }

    public byte asByte() {
        return NumberConversions.toByte(this.value());
    }

    public boolean asBoolean() {
        Object value = this.value();

        return value instanceof Boolean ? ((Boolean) value).booleanValue() : (value instanceof Number ? ((Number) value).intValue() != 0 : (value instanceof String ? Boolean.parseBoolean((String) value) : value != null));
    }

    public String asString() {
        Object value = this.value();

        return value == null ? "" : value.toString();
    }
}
