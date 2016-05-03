package org.bukkit.plugin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.event.server.ServiceUnregisterEvent;

public class SimpleServicesManager implements ServicesManager {

    private final Map providers = new HashMap();

    public void register(Class service, Object provider, Plugin plugin, ServicePriority priority) {
        RegisteredServiceProvider registeredProvider = null;
        Map map = this.providers;

        synchronized (this.providers) {
            Object registered = (List) this.providers.get(service);

            if (registered == null) {
                registered = new ArrayList();
                this.providers.put(service, registered);
            }

            registeredProvider = new RegisteredServiceProvider(service, provider, priority, plugin);
            int position = Collections.binarySearch((List) registered, registeredProvider);

            if (position < 0) {
                ((List) registered).add(-(position + 1), registeredProvider);
            } else {
                ((List) registered).add(position, registeredProvider);
            }
        }

        Bukkit.getServer().getPluginManager().callEvent(new ServiceRegisterEvent(registeredProvider));
    }

    public void unregisterAll(Plugin plugin) {
        ArrayList unregisteredEvents = new ArrayList();
        Map event = this.providers;
        Iterator it;

        synchronized (this.providers) {
            it = this.providers.entrySet().iterator();

            try {
                while (it.hasNext()) {
                    Entry entry = (Entry) it.next();
                    Iterator it2 = ((List) entry.getValue()).iterator();

                    while (true) {
                        try {
                            if (it2.hasNext()) {
                                RegisteredServiceProvider registered = (RegisteredServiceProvider) it2.next();

                                if (registered.getPlugin().equals(plugin)) {
                                    it2.remove();
                                    unregisteredEvents.add(new ServiceUnregisterEvent(registered));
                                }
                                continue;
                            }
                        } catch (NoSuchElementException nosuchelementexception) {
                            ;
                        }

                        if (((List) entry.getValue()).size() == 0) {
                            it.remove();
                        }
                        break;
                    }
                }
            } catch (NoSuchElementException nosuchelementexception1) {
                ;
            }
        }

        it = unregisteredEvents.iterator();

        while (it.hasNext()) {
            ServiceUnregisterEvent event1 = (ServiceUnregisterEvent) it.next();

            Bukkit.getServer().getPluginManager().callEvent(event1);
        }

    }

    public void unregister(Class service, Object provider) {
        ArrayList unregisteredEvents = new ArrayList();
        Map event = this.providers;
        Iterator it;

        synchronized (this.providers) {
            it = this.providers.entrySet().iterator();

            try {
                while (it.hasNext()) {
                    Entry entry = (Entry) it.next();

                    if (entry.getKey() == service) {
                        Iterator it2 = ((List) entry.getValue()).iterator();

                        while (true) {
                            try {
                                if (it2.hasNext()) {
                                    RegisteredServiceProvider registered = (RegisteredServiceProvider) it2.next();

                                    if (registered.getProvider() == provider) {
                                        it2.remove();
                                        unregisteredEvents.add(new ServiceUnregisterEvent(registered));
                                    }
                                    continue;
                                }
                            } catch (NoSuchElementException nosuchelementexception) {
                                ;
                            }

                            if (((List) entry.getValue()).size() == 0) {
                                it.remove();
                            }
                            break;
                        }
                    }
                }
            } catch (NoSuchElementException nosuchelementexception1) {
                ;
            }
        }

        it = unregisteredEvents.iterator();

        while (it.hasNext()) {
            ServiceUnregisterEvent event1 = (ServiceUnregisterEvent) it.next();

            Bukkit.getServer().getPluginManager().callEvent(event1);
        }

    }

    public void unregister(Object provider) {
        ArrayList unregisteredEvents = new ArrayList();
        Map event = this.providers;
        Iterator it;

        synchronized (this.providers) {
            it = this.providers.entrySet().iterator();

            try {
                while (it.hasNext()) {
                    Entry entry = (Entry) it.next();
                    Iterator it2 = ((List) entry.getValue()).iterator();

                    while (true) {
                        try {
                            if (it2.hasNext()) {
                                RegisteredServiceProvider registered = (RegisteredServiceProvider) it2.next();

                                if (registered.getProvider().equals(provider)) {
                                    it2.remove();
                                    unregisteredEvents.add(new ServiceUnregisterEvent(registered));
                                }
                                continue;
                            }
                        } catch (NoSuchElementException nosuchelementexception) {
                            ;
                        }

                        if (((List) entry.getValue()).size() == 0) {
                            it.remove();
                        }
                        break;
                    }
                }
            } catch (NoSuchElementException nosuchelementexception1) {
                ;
            }
        }

        it = unregisteredEvents.iterator();

        while (it.hasNext()) {
            ServiceUnregisterEvent event1 = (ServiceUnregisterEvent) it.next();

            Bukkit.getServer().getPluginManager().callEvent(event1);
        }

    }

    public Object load(Class service) {
        Map map = this.providers;

        synchronized (this.providers) {
            List registered = (List) this.providers.get(service);

            return registered == null ? null : service.cast(((RegisteredServiceProvider) registered.get(0)).getProvider());
        }
    }

    public RegisteredServiceProvider getRegistration(Class service) {
        Map map = this.providers;

        synchronized (this.providers) {
            List registered = (List) this.providers.get(service);

            return registered == null ? null : (RegisteredServiceProvider) registered.get(0);
        }
    }

    public List getRegistrations(Plugin plugin) {
        ImmutableList.Builder ret = ImmutableList.builder();
        Map map = this.providers;

        synchronized (this.providers) {
            Iterator iterator = this.providers.values().iterator();

            while (iterator.hasNext()) {
                List registered = (List) iterator.next();
                Iterator iterator1 = registered.iterator();

                while (iterator1.hasNext()) {
                    RegisteredServiceProvider provider = (RegisteredServiceProvider) iterator1.next();

                    if (provider.getPlugin().equals(plugin)) {
                        ret.add((Object) provider);
                    }
                }
            }

            return ret.build();
        }
    }

    public List getRegistrations(Class service) {
        Map map = this.providers;

        synchronized (this.providers) {
            List registered = (List) this.providers.get(service);

            if (registered == null) {
                return ImmutableList.of();
            } else {
                ImmutableList.Builder ret = ImmutableList.builder();
                Iterator iterator = registered.iterator();

                while (iterator.hasNext()) {
                    RegisteredServiceProvider provider = (RegisteredServiceProvider) iterator.next();

                    ret.add((Object) provider);
                }

                return ret.build();
            }
        }
    }

    public Set getKnownServices() {
        Map map = this.providers;

        synchronized (this.providers) {
            return ImmutableSet.copyOf((Collection) this.providers.keySet());
        }
    }

    public boolean isProvidedFor(Class service) {
        Map map = this.providers;

        synchronized (this.providers) {
            return this.providers.containsKey(service);
        }
    }
}
