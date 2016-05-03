package org.bukkit.metadata;

import java.util.List;
import org.bukkit.plugin.Plugin;

public interface Metadatable {

    void setMetadata(String s, MetadataValue metadatavalue);

    List getMetadata(String s);

    boolean hasMetadata(String s);

    void removeMetadata(String s, Plugin plugin);
}
