package org.bukkit.metadata;

import java.util.List;
import org.bukkit.plugin.Plugin;

public interface MetadataStore {

    void setMetadata(Object object, String s, MetadataValue metadatavalue);

    List getMetadata(Object object, String s);

    boolean hasMetadata(Object object, String s);

    void removeMetadata(Object object, String s, Plugin plugin);

    void invalidateAll(Plugin plugin);
}
