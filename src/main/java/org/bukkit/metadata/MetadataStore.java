package org.bukkit.metadata;

import java.util.List;
import org.bukkit.plugin.Plugin;

public interface MetadataStore<T> {

    void setMetadata(T object, String s, MetadataValue metadatavalue);

    List getMetadata(T object, String s);

    boolean hasMetadata(T object, String s);

    void removeMetadata(T object, String s, Plugin plugin);

    void invalidateAll(Plugin plugin);
}
