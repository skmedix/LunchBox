package org.bukkit.plugin;

public class RegisteredServiceProvider<T> implements Comparable<RegisteredServiceProvider<?>> {

    private Class<T> service;
    private Plugin plugin;
    private Object provider;
    private ServicePriority priority;

    public RegisteredServiceProvider(Class service, Object provider, ServicePriority priority, Plugin plugin) {
        this.service = service;
        this.plugin = plugin;
        this.provider = provider;
        this.priority = priority;
    }

    public Class<T> getService() {
        return this.service;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public Object getProvider() {
        return this.provider;
    }

    public ServicePriority getPriority() {
        return this.priority;
    }

    public int compareTo(RegisteredServiceProvider other) {
        return this.priority.ordinal() == other.getPriority().ordinal() ? 0 : (this.priority.ordinal() < other.getPriority().ordinal() ? 1 : -1);
    }
}
