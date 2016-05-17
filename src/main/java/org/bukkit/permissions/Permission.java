package org.bukkit.permissions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Level;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class Permission {

    public static final PermissionDefault DEFAULT_PERMISSION = PermissionDefault.OP;
    private final String name;
    private final Map children;
    private PermissionDefault defaultValue;
    private String description;

    public Permission(String name) {
        this(name, (String) null, (PermissionDefault) null, (Map) null);
    }

    public Permission(String name, String description) {
        this(name, description, (PermissionDefault) null, (Map) null);
    }

    public Permission(String name, PermissionDefault defaultValue) {
        this(name, (String) null, defaultValue, (Map) null);
    }

    public Permission(String name, String description, PermissionDefault defaultValue) {
        this(name, description, defaultValue, (Map) null);
    }

    public Permission(String name, Map children) {
        this(name, (String) null, (PermissionDefault) null, children);
    }

    public Permission(String name, String description, Map children) {
        this(name, description, (PermissionDefault) null, children);
    }

    public Permission(String name, PermissionDefault defaultValue, Map children) {
        this(name, (String) null, defaultValue, children);
    }

    public Permission(String name, String description, PermissionDefault defaultValue, Map children) {
        this.children = new LinkedHashMap();
        this.defaultValue = Permission.DEFAULT_PERMISSION;
        Validate.notNull(name, "Name cannot be null");
        this.name = name;
        this.description = description == null ? "" : description;
        if (defaultValue != null) {
            this.defaultValue = defaultValue;
        }

        if (children != null) {
            this.children.putAll(children);
        }

        this.recalculatePermissibles();
    }

    public String getName() {
        return this.name;
    }

    public Map getChildren() {
        return this.children;
    }

    public PermissionDefault getDefault() {
        return this.defaultValue;
    }

    public void setDefault(PermissionDefault value) {
        if (this.defaultValue == null) {
            throw new IllegalArgumentException("Default value cannot be null");
        } else {
            this.defaultValue = value;
            this.recalculatePermissibles();
        }
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String value) {
        if (value == null) {
            this.description = "";
        } else {
            this.description = value;
        }

    }

    public Set getPermissibles() {
        return Bukkit.getServer().getPluginManager().getPermissionSubscriptions(this.name);
    }

    public void recalculatePermissibles() {
        Set perms = this.getPermissibles();

        Bukkit.getServer().getPluginManager().recalculatePermissionDefaults(this);
        Iterator iterator = perms.iterator();

        while (iterator.hasNext()) {
            Permissible p = (Permissible) iterator.next();

            p.recalculatePermissions();
        }

    }

    public Permission addParent(String name, boolean value) {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        String lname = name.toLowerCase();
        Permission perm = pm.getPermission(lname);

        if (perm == null) {
            perm = new Permission(lname);
            pm.addPermission(perm);
        }

        this.addParent(perm, value);
        return perm;
    }

    public void addParent(Permission perm, boolean value) {
        perm.getChildren().put(this.getName(), Boolean.valueOf(value));
        perm.recalculatePermissibles();
    }

    public static List loadPermissions(Map data, String error, PermissionDefault def) {
        ArrayList result = new ArrayList();
        Iterator iterator = data.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            try {
                result.add(loadPermission(entry.getKey().toString(), (Map) entry.getValue(), def, result));
            } catch (Throwable throwable) {
                Bukkit.getServer().getLogger().log(Level.SEVERE, String.format(error, new Object[] { entry.getKey()}), throwable);
            }
        }

        return result;
    }

    public static Permission loadPermission(String name, Map data) {
        return loadPermission(name, data, Permission.DEFAULT_PERMISSION, (List) null);
    }

    public static Permission loadPermission(String name, Map data, PermissionDefault def, List output) {
        Validate.notNull(name, "Name cannot be null");
        Validate.notNull(data, "Data cannot be null");
        String desc = null;
        Object children = null;

        if (data.get("default") != null) {
            PermissionDefault childrenNode = PermissionDefault.getByName(data.get("default").toString());

            if (childrenNode == null) {
                throw new IllegalArgumentException("\'default\' key contained unknown value");
            }

            def = childrenNode;
        }

        if (data.get("children") != null) {
            Object childrenNode1 = data.get("children");

            if (childrenNode1 instanceof Iterable) {
                children = new LinkedHashMap();
                Iterator iterator = ((Iterable) childrenNode1).iterator();

                while (iterator.hasNext()) {
                    Object child = iterator.next();

                    if (child != null) {
                        ((Map) children).put(child.toString(), Boolean.TRUE);
                    }
                }
            } else {
                if (!(childrenNode1 instanceof Map)) {
                    throw new IllegalArgumentException("\'children\' key is of wrong type");
                }

                children = extractChildren((Map) childrenNode1, name, def, output);
            }
        }

        if (data.get("description") != null) {
            desc = data.get("description").toString();
        }

        return new Permission(name, desc, def, (Map) children);
    }

    private static Map extractChildren(Map input, String name, PermissionDefault def, List output) {
        LinkedHashMap children = new LinkedHashMap();
        Iterator iterator = input.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (entry.getValue() instanceof Boolean) {
                children.put(entry.getKey().toString(), (Boolean) entry.getValue());
            } else {
                if (!(entry.getValue() instanceof Map)) {
                    throw new IllegalArgumentException("Child \'" + entry.getKey().toString() + "\' contains invalid value");
                }

                try {
                    Permission ex = loadPermission(entry.getKey().toString(), (Map) entry.getValue(), def, output);

                    children.put(ex.getName(), Boolean.TRUE);
                    if (output != null) {
                        output.add(ex);
                    }
                } catch (Throwable throwable) {
                    throw new IllegalArgumentException("Permission node \'" + entry.getKey().toString() + "\' in child of " + name + " is invalid", throwable);
                }
            }
        }

        return children;
    }
}
