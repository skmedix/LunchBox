package org.bukkit.metadata;

import org.bukkit.plugin.Plugin;

public interface MetadataValue {

    Object value();

    int asInt();

    float asFloat();

    double asDouble();

    long asLong();

    short asShort();

    byte asByte();

    boolean asBoolean();

    String asString();

    Plugin getOwningPlugin();

    void invalidate();
}
