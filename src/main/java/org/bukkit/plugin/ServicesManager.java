package org.bukkit.plugin;

import java.util.Collection;
import java.util.List;

public interface ServicesManager {

    void register(Class oclass, Object object, Plugin plugin, ServicePriority servicepriority);

    void unregisterAll(Plugin plugin);

    void unregister(Class oclass, Object object);

    void unregister(Object object);

    Object load(Class oclass);

    RegisteredServiceProvider getRegistration(Class oclass);

    List getRegistrations(Plugin plugin);

    Collection getRegistrations(Class oclass);

    Collection getKnownServices();

    boolean isProvidedFor(Class oclass);
}
