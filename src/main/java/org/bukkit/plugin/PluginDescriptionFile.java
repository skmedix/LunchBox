package org.bukkit.plugin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

public final class PluginDescriptionFile {

    private static final ThreadLocal YAML = new ThreadLocal() {
        protected Yaml initialValue() {
            return new Yaml(new SafeConstructor() {
                {
                    this.yamlConstructors.put((Object) null, new AbstractConstruct() {
                        public Object construct(final Node node) {
                            return !node.getTag().startsWith("!@") ? SafeConstructor.undefinedConstructor.construct(node) : new PluginAwareness() {
                                public String toString() {
                                    return node.toString();
                                }
                            };
                        }
                    });
                    PluginAwareness.Flags[] apluginawareness_flags;
                    int i = (apluginawareness_flags = PluginAwareness.Flags.values()).length;

                    for (int j = 0; j < i; ++j) {
                        final PluginAwareness.Flags flag = apluginawareness_flags[j];

                        this.yamlConstructors.put(new Tag("!@" + flag.name()), new AbstractConstruct() {
                            public PluginAwareness.Flags construct(Node node) {
                                return flag;
                            }
                        });
                    }

                }
            });
        }
    };
    String rawName = null;
    private String name = null;
    private String main = null;
    private String classLoaderOf = null;
    private List depend = ImmutableList.of();
    private List softDepend = ImmutableList.of();
    private List loadBefore = ImmutableList.of();
    private String version = null;
    private Map commands = null;
    private String description = null;
    private List authors = null;
    private String website = null;
    private String prefix = null;
    private boolean database = false;
    private PluginLoadOrder order;
    private List permissions;
    private Map lazyPermissions;
    private PermissionDefault defaultPerm;
    private Set awareness;

    public PluginDescriptionFile(InputStream stream) throws InvalidDescriptionException {
        this.order = PluginLoadOrder.POSTWORLD;
        this.permissions = null;
        this.lazyPermissions = null;
        this.defaultPerm = PermissionDefault.OP;
        this.awareness = ImmutableSet.of();
        this.loadMap(this.asMap(((Yaml) PluginDescriptionFile.YAML.get()).load(stream)));
    }

    public PluginDescriptionFile(Reader reader) throws InvalidDescriptionException {
        this.order = PluginLoadOrder.POSTWORLD;
        this.permissions = null;
        this.lazyPermissions = null;
        this.defaultPerm = PermissionDefault.OP;
        this.awareness = ImmutableSet.of();
        this.loadMap(this.asMap(((Yaml) PluginDescriptionFile.YAML.get()).load(reader)));
    }

    public PluginDescriptionFile(String pluginName, String pluginVersion, String mainClass) {
        this.order = PluginLoadOrder.POSTWORLD;
        this.permissions = null;
        this.lazyPermissions = null;
        this.defaultPerm = PermissionDefault.OP;
        this.awareness = ImmutableSet.of();
        this.name = pluginName.replace(' ', '_');
        this.version = pluginVersion;
        this.main = mainClass;
    }

    public String getName() {
        return this.name;
    }

    public String getVersion() {
        return this.version;
    }

    public String getMain() {
        return this.main;
    }

    public String getDescription() {
        return this.description;
    }

    public PluginLoadOrder getLoad() {
        return this.order;
    }

    public List getAuthors() {
        return this.authors;
    }

    public String getWebsite() {
        return this.website;
    }

    public boolean isDatabaseEnabled() {
        return this.database;
    }

    public List getDepend() {
        return this.depend;
    }

    public List getSoftDepend() {
        return this.softDepend;
    }

    public List getLoadBefore() {
        return this.loadBefore;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public Map getCommands() {
        return this.commands;
    }

    public List getPermissions() {
        if (this.permissions == null) {
            if (this.lazyPermissions == null) {
                this.permissions = ImmutableList.of();
            } else {
                this.permissions = ImmutableList.copyOf((Collection) Permission.loadPermissions(this.lazyPermissions, "Permission node \'%s\' in plugin description file for " + this.getFullName() + " is invalid", this.defaultPerm));
                this.lazyPermissions = null;
            }
        }

        return this.permissions;
    }

    public PermissionDefault getPermissionDefault() {
        return this.defaultPerm;
    }

    public Set getAwareness() {
        return this.awareness;
    }

    public String getFullName() {
        return this.name + " v" + this.version;
    }

    /** @deprecated */
    @Deprecated
    public String getClassLoaderOf() {
        return this.classLoaderOf;
    }

    public void setDatabaseEnabled(boolean database) {
        this.database = database;
    }

    public void save(Writer writer) {
        ((Yaml) PluginDescriptionFile.YAML.get()).dump(this.saveMap(), writer);
    }

    private void loadMap(Map map) throws InvalidDescriptionException {
        try {
            this.name = this.rawName = map.get("name").toString();
            if (!this.name.matches("^[A-Za-z0-9 _.-]+$")) {
                throw new InvalidDescriptionException("name \'" + this.name + "\' contains invalid characters.");
            }

            this.name = this.name.replace(' ', '_');
        } catch (NullPointerException nullpointerexception) {
            throw new InvalidDescriptionException(nullpointerexception, "name is not defined");
        } catch (ClassCastException classcastexception) {
            throw new InvalidDescriptionException(classcastexception, "name is of wrong type");
        }

        try {
            this.version = map.get("version").toString();
        } catch (NullPointerException nullpointerexception1) {
            throw new InvalidDescriptionException(nullpointerexception1, "version is not defined");
        } catch (ClassCastException classcastexception1) {
            throw new InvalidDescriptionException(classcastexception1, "version is of wrong type");
        }

        try {
            this.main = map.get("main").toString();
            if (this.main.startsWith("org.bukkit.")) {
                throw new InvalidDescriptionException("main may not be within the org.bukkit namespace");
            }
        } catch (NullPointerException nullpointerexception2) {
            throw new InvalidDescriptionException(nullpointerexception2, "main is not defined");
        } catch (ClassCastException classcastexception2) {
            throw new InvalidDescriptionException(classcastexception2, "main is of wrong type");
        }

        Iterator iterator;

        if (map.get("commands") != null) {
            ImmutableMap.Builder ex = ImmutableMap.builder();

            Entry ex1;
            ImmutableMap.Builder commandBuilder;

            try {
                for (iterator = ((Map) map.get("commands")).entrySet().iterator(); iterator.hasNext(); ex.put(ex1.getKey().toString(), commandBuilder.build())) {
                    ex1 = (Entry) iterator.next();
                    commandBuilder = ImmutableMap.builder();
                    if (ex1.getValue() != null) {
                        Iterator iterator1 = ((Map) ex1.getValue()).entrySet().iterator();

                        while (iterator1.hasNext()) {
                            Entry commandEntry = (Entry) iterator1.next();

                            if (!(commandEntry.getValue() instanceof Iterable)) {
                                if (commandEntry.getValue() != null) {
                                    commandBuilder.put(commandEntry.getKey().toString(), commandEntry.getValue());
                                }
                            } else {
                                ImmutableList.Builder commandSubList = ImmutableList.builder();
                                Iterator iterator2 = ((Iterable) commandEntry.getValue()).iterator();

                                while (iterator2.hasNext()) {
                                    Object commandSubListItem = iterator2.next();

                                    if (commandSubListItem != null) {
                                        commandSubList.add(commandSubListItem);
                                    }
                                }

                                commandBuilder.put(commandEntry.getKey().toString(), commandSubList.build());
                            }
                        }
                    }
                }
            } catch (ClassCastException classcastexception3) {
                throw new InvalidDescriptionException(classcastexception3, "commands are of wrong type");
            }

            this.commands = ex.build();
        }

        if (map.get("class-loader-of") != null) {
            this.classLoaderOf = map.get("class-loader-of").toString();
        }

        this.depend = makePluginNameList(map, "depend");
        this.softDepend = makePluginNameList(map, "softdepend");
        this.loadBefore = makePluginNameList(map, "loadbefore");
        if (map.get("database") != null) {
            try {
                this.database = ((Boolean) map.get("database")).booleanValue();
            } catch (ClassCastException classcastexception4) {
                throw new InvalidDescriptionException(classcastexception4, "database is of wrong type");
            }
        }

        if (map.get("website") != null) {
            this.website = map.get("website").toString();
        }

        if (map.get("description") != null) {
            this.description = map.get("description").toString();
        }

        if (map.get("load") != null) {
            try {
                this.order = PluginLoadOrder.valueOf(((String) map.get("load")).toUpperCase().replaceAll("\\W", ""));
            } catch (ClassCastException classcastexception5) {
                throw new InvalidDescriptionException(classcastexception5, "load is of wrong type");
            } catch (IllegalArgumentException illegalargumentexception) {
                throw new InvalidDescriptionException(illegalargumentexception, "load is not a valid choice");
            }
        }

        Object ex4;

        if (map.get("authors") != null) {
            ImmutableList.Builder ex2 = ImmutableList.builder();

            if (map.get("author") != null) {
                ex2.add((Object) map.get("author").toString());
            }

            try {
                iterator = ((Iterable) map.get("authors")).iterator();

                while (iterator.hasNext()) {
                    ex4 = iterator.next();
                    ex2.add((Object) ex4.toString());
                }
            } catch (ClassCastException classcastexception6) {
                throw new InvalidDescriptionException(classcastexception6, "authors are of wrong type");
            } catch (NullPointerException nullpointerexception3) {
                throw new InvalidDescriptionException(nullpointerexception3, "authors are improperly defined");
            }

            this.authors = ex2.build();
        } else if (map.get("author") != null) {
            this.authors = ImmutableList.of(map.get("author").toString());
        } else {
            this.authors = ImmutableList.of();
        }

        if (map.get("default-permission") != null) {
            try {
                this.defaultPerm = PermissionDefault.getByName(map.get("default-permission").toString());
            } catch (ClassCastException classcastexception7) {
                throw new InvalidDescriptionException(classcastexception7, "default-permission is of wrong type");
            } catch (IllegalArgumentException illegalargumentexception1) {
                throw new InvalidDescriptionException(illegalargumentexception1, "default-permission is not a valid choice");
            }
        }

        if (map.get("awareness") instanceof Iterable) {
            HashSet ex3 = new HashSet();

            try {
                iterator = ((Iterable) map.get("awareness")).iterator();

                while (iterator.hasNext()) {
                    ex4 = iterator.next();
                    ex3.add((PluginAwareness) ex4);
                }
            } catch (ClassCastException classcastexception8) {
                throw new InvalidDescriptionException(classcastexception8, "awareness has wrong type");
            }

            this.awareness = ImmutableSet.copyOf((Collection) ex3);
        }

        try {
            this.lazyPermissions = (Map) map.get("permissions");
        } catch (ClassCastException classcastexception9) {
            throw new InvalidDescriptionException(classcastexception9, "permissions are of the wrong type");
        }

        if (map.get("prefix") != null) {
            this.prefix = map.get("prefix").toString();
        }

    }

    private static List makePluginNameList(Map map, String key) throws InvalidDescriptionException {
        Object value = map.get(key);

        if (value == null) {
            return ImmutableList.of();
        } else {
            ImmutableList.Builder builder = ImmutableList.builder();

            try {
                Iterator iterator = ((Iterable) value).iterator();

                while (iterator.hasNext()) {
                    Object ex = iterator.next();

                    builder.add((Object) ex.toString().replace(' ', '_'));
                }
            } catch (ClassCastException classcastexception) {
                throw new InvalidDescriptionException(classcastexception, key + " is of wrong type");
            } catch (NullPointerException nullpointerexception) {
                throw new InvalidDescriptionException(nullpointerexception, "invalid " + key + " format");
            }

            return builder.build();
        }
    }

    private Map saveMap() {
        HashMap map = new HashMap();

        map.put("name", this.name);
        map.put("main", this.main);
        map.put("version", this.version);
        map.put("database", Boolean.valueOf(this.database));
        map.put("order", this.order.toString());
        map.put("default-permission", this.defaultPerm.toString());
        if (this.commands != null) {
            map.put("command", this.commands);
        }

        if (this.depend != null) {
            map.put("depend", this.depend);
        }

        if (this.softDepend != null) {
            map.put("softdepend", this.softDepend);
        }

        if (this.website != null) {
            map.put("website", this.website);
        }

        if (this.description != null) {
            map.put("description", this.description);
        }

        if (this.authors.size() == 1) {
            map.put("author", this.authors.get(0));
        } else if (this.authors.size() > 1) {
            map.put("authors", this.authors);
        }

        if (this.classLoaderOf != null) {
            map.put("class-loader-of", this.classLoaderOf);
        }

        if (this.prefix != null) {
            map.put("prefix", this.prefix);
        }

        return map;
    }

    private Map asMap(Object object) throws InvalidDescriptionException {
        if (object instanceof Map) {
            return (Map) object;
        } else {
            throw new InvalidDescriptionException(object + " is not properly structured.");
        }
    }

    /** @deprecated */
    @Deprecated
    public String getRawName() {
        return this.rawName;
    }
}
