package org.bukkit.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;

public abstract class MetadataStoreBase {

    private Map metadataMap = new HashMap();

    public synchronized void setMetadata(Object subject, String metadataKey, MetadataValue newMetadataValue) {
        Validate.notNull(newMetadataValue, "Value cannot be null");
        Plugin owningPlugin = newMetadataValue.getOwningPlugin();

        Validate.notNull(owningPlugin, "Plugin cannot be null");
        String key = this.disambiguate(subject, metadataKey);
        Object entry = (Map) this.metadataMap.get(key);

        if (entry == null) {
            entry = new WeakHashMap(1);
            this.metadataMap.put(key, entry);
        }

        ((Map) entry).put(owningPlugin, newMetadataValue);
    }

    public synchronized List getMetadata(Object subject, String metadataKey) {
        String key = this.disambiguate(subject, metadataKey);

        if (this.metadataMap.containsKey(key)) {
            Collection values = ((Map) this.metadataMap.get(key)).values();

            return Collections.unmodifiableList(new ArrayList(values));
        } else {
            return Collections.emptyList();
        }
    }

    public synchronized boolean hasMetadata(Object subject, String metadataKey) {
        String key = this.disambiguate(subject, metadataKey);

        return this.metadataMap.containsKey(key);
    }

    public synchronized void removeMetadata(Object subject, String metadataKey, Plugin owningPlugin) {
        Validate.notNull(owningPlugin, "Plugin cannot be null");
        String key = this.disambiguate(subject, metadataKey);
        Map entry = (Map) this.metadataMap.get(key);

        if (entry != null) {
            entry.remove(owningPlugin);
            if (entry.isEmpty()) {
                this.metadataMap.remove(key);
            }

        }
    }

    public synchronized void invalidateAll(Plugin owningPlugin) {
        Validate.notNull(owningPlugin, "Plugin cannot be null");
        Iterator iterator = this.metadataMap.values().iterator();

        while (iterator.hasNext()) {
            Map values = (Map) iterator.next();

            if (values.containsKey(owningPlugin)) {
                ((MetadataValue) values.get(owningPlugin)).invalidate();
            }
        }

    }

    protected abstract String disambiguate(Object object, String s);
}
