package org.bukkit.plugin;

public abstract class PluginBase implements Plugin {

    public final int hashCode() {
        return this.getName().hashCode();
    }

    public final boolean equals(Object obj) {
        return this == obj ? true : (obj == null ? false : (!(obj instanceof Plugin) ? false : this.getName().equals(((Plugin) obj).getName())));
    }

    public final String getName() {
        return this.getDescription().getName();
    }
}
