package org.bukkit.configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public class MemorySection implements ConfigurationSection {

    protected final Map map = new LinkedHashMap();
    private final Configuration root;
    private final ConfigurationSection parent;
    private final String path;
    private final String fullPath;

    protected MemorySection() {
        if (!(this instanceof Configuration)) {
            throw new IllegalStateException("Cannot construct a root MemorySection when not a Configuration");
        } else {
            this.path = "";
            this.fullPath = "";
            this.parent = null;
            this.root = (Configuration) this;
        }
    }

    protected MemorySection(ConfigurationSection parent, String path) {
        Validate.notNull(parent, "Parent cannot be null");
        Validate.notNull(path, "Path cannot be null");
        this.path = path;
        this.parent = parent;
        this.root = parent.getRoot();
        Validate.notNull(this.root, "Path cannot be orphaned");
        this.fullPath = createPath(parent, path);
    }

    public Set getKeys(boolean deep) {
        LinkedHashSet result = new LinkedHashSet();
        Configuration root = this.getRoot();

        if (root != null && root.options().copyDefaults()) {
            ConfigurationSection defaults = this.getDefaultSection();

            if (defaults != null) {
                result.addAll(defaults.getKeys(deep));
            }
        }

        this.mapChildrenKeys(result, this, deep);
        return result;
    }

    public Map getValues(boolean deep) {
        LinkedHashMap result = new LinkedHashMap();
        Configuration root = this.getRoot();

        if (root != null && root.options().copyDefaults()) {
            ConfigurationSection defaults = this.getDefaultSection();

            if (defaults != null) {
                result.putAll(defaults.getValues(deep));
            }
        }

        this.mapChildrenValues(result, this, deep);
        return result;
    }

    public boolean contains(String path) {
        return this.get(path) != null;
    }

    public boolean isSet(String path) {
        Configuration root = this.getRoot();

        return root == null ? false : (root.options().copyDefaults() ? this.contains(path) : this.get(path, (Object) null) != null);
    }

    public String getCurrentPath() {
        return this.fullPath;
    }

    public String getName() {
        return this.path;
    }

    public Configuration getRoot() {
        return this.root;
    }

    public ConfigurationSection getParent() {
        return this.parent;
    }

    public void addDefault(String path, Object value) {
        Validate.notNull(path, "Path cannot be null");
        Configuration root = this.getRoot();

        if (root == null) {
            throw new IllegalStateException("Cannot add default without root");
        } else if (root == this) {
            throw new UnsupportedOperationException("Unsupported addDefault(String, Object) implementation");
        } else {
            root.addDefault(createPath(this, path), value);
        }
    }

    public ConfigurationSection getDefaultSection() {
        Configuration root = this.getRoot();
        Configuration defaults = root == null ? null : root.getDefaults();

        return defaults != null && defaults.isConfigurationSection(this.getCurrentPath()) ? defaults.getConfigurationSection(this.getCurrentPath()) : null;
    }

    public void set(String path, Object value) {
        Validate.notEmpty(path, "Cannot set to an empty path");
        Configuration root = this.getRoot();

        if (root == null) {
            throw new IllegalStateException("Cannot use section without a root");
        } else {
            char separator = root.options().pathSeparator();
            int i1 = -1;
            Object section = this;

            int i2;
            String key;

            while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1) {
                key = path.substring(i2, i1);
                ConfigurationSection subSection = ((ConfigurationSection) section).getConfigurationSection(key);

                if (subSection == null) {
                    section = ((ConfigurationSection) section).createSection(key);
                } else {
                    section = subSection;
                }
            }

            key = path.substring(i2);
            if (section == this) {
                if (value == null) {
                    this.map.remove(key);
                } else {
                    this.map.put(key, value);
                }
            } else {
                ((ConfigurationSection) section).set(key, value);
            }

        }
    }

    public Object get(String path) {
        return this.get(path, this.getDefault(path));
    }

    public Object get(String path, Object def) {
        Validate.notNull(path, "Path cannot be null");
        if (path.length() == 0) {
            return this;
        } else {
            Configuration root = this.getRoot();

            if (root == null) {
                throw new IllegalStateException("Cannot access section without a root");
            } else {
                char separator = root.options().pathSeparator();
                int i1 = -1;
                Object section = this;

                int i2;

                while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1) {
                    section = ((ConfigurationSection) section).getConfigurationSection(path.substring(i2, i1));
                    if (section == null) {
                        return def;
                    }
                }

                String key = path.substring(i2);

                if (section == this) {
                    Object result = this.map.get(key);

                    return result == null ? def : result;
                } else {
                    return ((ConfigurationSection) section).get(key, def);
                }
            }
        }
    }

    public ConfigurationSection createSection(String path) {
        Validate.notEmpty(path, "Cannot create section at empty path");
        Configuration root = this.getRoot();

        if (root == null) {
            throw new IllegalStateException("Cannot create section without a root");
        } else {
            char separator = root.options().pathSeparator();
            int i1 = -1;
            Object section = this;

            int i2;
            String key;

            while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1) {
                key = path.substring(i2, i1);
                ConfigurationSection result = ((ConfigurationSection) section).getConfigurationSection(key);

                if (result == null) {
                    section = ((ConfigurationSection) section).createSection(key);
                } else {
                    section = result;
                }
            }

            key = path.substring(i2);
            if (section == this) {
                MemorySection result1 = new MemorySection(this, key);

                this.map.put(key, result1);
                return result1;
            } else {
                return ((ConfigurationSection) section).createSection(key);
            }
        }
    }

    public ConfigurationSection createSection(String path, Map map) {
        ConfigurationSection section = this.createSection(path);
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (entry.getValue() instanceof Map) {
                section.createSection(entry.getKey().toString(), (Map) entry.getValue());
            } else {
                section.set(entry.getKey().toString(), entry.getValue());
            }
        }

        return section;
    }

    public String getString(String path) {
        Object def = this.getDefault(path);

        return this.getString(path, def != null ? def.toString() : null);
    }

    public String getString(String path, String def) {
        Object val = this.get(path, def);

        return val != null ? val.toString() : def;
    }

    public boolean isString(String path) {
        Object val = this.get(path);

        return val instanceof String;
    }

    public int getInt(String path) {
        Object def = this.getDefault(path);

        return this.getInt(path, def instanceof Number ? NumberConversions.toInt(def) : 0);
    }

    public int getInt(String path, int def) {
        Object val = this.get(path, Integer.valueOf(def));

        return val instanceof Number ? NumberConversions.toInt(val) : def;
    }

    public boolean isInt(String path) {
        Object val = this.get(path);

        return val instanceof Integer;
    }

    public boolean getBoolean(String path) {
        Object def = this.getDefault(path);

        return this.getBoolean(path, def instanceof Boolean ? ((Boolean) def).booleanValue() : false);
    }

    public boolean getBoolean(String path, boolean def) {
        Object val = this.get(path, Boolean.valueOf(def));

        return val instanceof Boolean ? ((Boolean) val).booleanValue() : def;
    }

    public boolean isBoolean(String path) {
        Object val = this.get(path);

        return val instanceof Boolean;
    }

    public double getDouble(String path) {
        Object def = this.getDefault(path);

        return this.getDouble(path, def instanceof Number ? NumberConversions.toDouble(def) : 0.0D);
    }

    public double getDouble(String path, double def) {
        Object val = this.get(path, Double.valueOf(def));

        return val instanceof Number ? NumberConversions.toDouble(val) : def;
    }

    public boolean isDouble(String path) {
        Object val = this.get(path);

        return val instanceof Double;
    }

    public long getLong(String path) {
        Object def = this.getDefault(path);

        return this.getLong(path, def instanceof Number ? NumberConversions.toLong(def) : 0L);
    }

    public long getLong(String path, long def) {
        Object val = this.get(path, Long.valueOf(def));

        return val instanceof Number ? NumberConversions.toLong(val) : def;
    }

    public boolean isLong(String path) {
        Object val = this.get(path);

        return val instanceof Long;
    }

    public List getList(String path) {
        Object def = this.getDefault(path);

        return this.getList(path, def instanceof List ? (List) def : null);
    }

    public List getList(String path, List def) {
        Object val = this.get(path, def);

        return (List) (val instanceof List ? val : def);
    }

    public boolean isList(String path) {
        Object val = this.get(path);

        return val instanceof List;
    }

    public List getStringList(String path) {
        List list = this.getList(path);

        if (list == null) {
            return new ArrayList(0);
        } else {
            ArrayList result = new ArrayList();
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Object object = iterator.next();

                if (object instanceof String || this.isPrimitiveWrapper(object)) {
                    result.add(String.valueOf(object));
                }
            }

            return result;
        }
    }

    public List getIntegerList(String path) {
        List list = this.getList(path);

        if (list == null) {
            return new ArrayList(0);
        } else {
            ArrayList result = new ArrayList();
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Object object = iterator.next();

                if (object instanceof Integer) {
                    result.add((Integer) object);
                } else if (object instanceof String) {
                    try {
                        result.add(Integer.valueOf((String) object));
                    } catch (Exception exception) {
                        ;
                    }
                } else if (object instanceof Character) {
                    result.add(Integer.valueOf(((Character) object).charValue()));
                } else if (object instanceof Number) {
                    result.add(Integer.valueOf(((Number) object).intValue()));
                }
            }

            return result;
        }
    }

    public List getBooleanList(String path) {
        List list = this.getList(path);

        if (list == null) {
            return new ArrayList(0);
        } else {
            ArrayList result = new ArrayList();
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Object object = iterator.next();

                if (object instanceof Boolean) {
                    result.add((Boolean) object);
                } else if (object instanceof String) {
                    if (Boolean.TRUE.toString().equals(object)) {
                        result.add(Boolean.valueOf(true));
                    } else if (Boolean.FALSE.toString().equals(object)) {
                        result.add(Boolean.valueOf(false));
                    }
                }
            }

            return result;
        }
    }

    public List getDoubleList(String path) {
        List list = this.getList(path);

        if (list == null) {
            return new ArrayList(0);
        } else {
            ArrayList result = new ArrayList();
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Object object = iterator.next();

                if (object instanceof Double) {
                    result.add((Double) object);
                } else if (object instanceof String) {
                    try {
                        result.add(Double.valueOf((String) object));
                    } catch (Exception exception) {
                        ;
                    }
                } else if (object instanceof Character) {
                    result.add(Double.valueOf((double) ((Character) object).charValue()));
                } else if (object instanceof Number) {
                    result.add(Double.valueOf(((Number) object).doubleValue()));
                }
            }

            return result;
        }
    }

    public List getFloatList(String path) {
        List list = this.getList(path);

        if (list == null) {
            return new ArrayList(0);
        } else {
            ArrayList result = new ArrayList();
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Object object = iterator.next();

                if (object instanceof Float) {
                    result.add((Float) object);
                } else if (object instanceof String) {
                    try {
                        result.add(Float.valueOf((String) object));
                    } catch (Exception exception) {
                        ;
                    }
                } else if (object instanceof Character) {
                    result.add(Float.valueOf((float) ((Character) object).charValue()));
                } else if (object instanceof Number) {
                    result.add(Float.valueOf(((Number) object).floatValue()));
                }
            }

            return result;
        }
    }

    public List getLongList(String path) {
        List list = this.getList(path);

        if (list == null) {
            return new ArrayList(0);
        } else {
            ArrayList result = new ArrayList();
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Object object = iterator.next();

                if (object instanceof Long) {
                    result.add((Long) object);
                } else if (object instanceof String) {
                    try {
                        result.add(Long.valueOf((String) object));
                    } catch (Exception exception) {
                        ;
                    }
                } else if (object instanceof Character) {
                    result.add(Long.valueOf((long) ((Character) object).charValue()));
                } else if (object instanceof Number) {
                    result.add(Long.valueOf(((Number) object).longValue()));
                }
            }

            return result;
        }
    }

    public List getByteList(String path) {
        List list = this.getList(path);

        if (list == null) {
            return new ArrayList(0);
        } else {
            ArrayList result = new ArrayList();
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Object object = iterator.next();

                if (object instanceof Byte) {
                    result.add((Byte) object);
                } else if (object instanceof String) {
                    try {
                        result.add(Byte.valueOf((String) object));
                    } catch (Exception exception) {
                        ;
                    }
                } else if (object instanceof Character) {
                    result.add(Byte.valueOf((byte) ((Character) object).charValue()));
                } else if (object instanceof Number) {
                    result.add(Byte.valueOf(((Number) object).byteValue()));
                }
            }

            return result;
        }
    }

    public List getCharacterList(String path) {
        List list = this.getList(path);

        if (list == null) {
            return new ArrayList(0);
        } else {
            ArrayList result = new ArrayList();
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Object object = iterator.next();

                if (object instanceof Character) {
                    result.add((Character) object);
                } else if (object instanceof String) {
                    String str = (String) object;

                    if (str.length() == 1) {
                        result.add(Character.valueOf(str.charAt(0)));
                    }
                } else if (object instanceof Number) {
                    result.add(Character.valueOf((char) ((Number) object).intValue()));
                }
            }

            return result;
        }
    }

    public List getShortList(String path) {
        List list = this.getList(path);

        if (list == null) {
            return new ArrayList(0);
        } else {
            ArrayList result = new ArrayList();
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Object object = iterator.next();

                if (object instanceof Short) {
                    result.add((Short) object);
                } else if (object instanceof String) {
                    try {
                        result.add(Short.valueOf((String) object));
                    } catch (Exception exception) {
                        ;
                    }
                } else if (object instanceof Character) {
                    result.add(Short.valueOf((short) ((Character) object).charValue()));
                } else if (object instanceof Number) {
                    result.add(Short.valueOf(((Number) object).shortValue()));
                }
            }

            return result;
        }
    }

    public List getMapList(String path) {
        List list = this.getList(path);
        ArrayList result = new ArrayList();

        if (list == null) {
            return result;
        } else {
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Object object = iterator.next();

                if (object instanceof Map) {
                    result.add((Map) object);
                }
            }

            return result;
        }
    }

    public Vector getVector(String path) {
        Object def = this.getDefault(path);

        return this.getVector(path, def instanceof Vector ? (Vector) def : null);
    }

    public Vector getVector(String path, Vector def) {
        Object val = this.get(path, def);

        return val instanceof Vector ? (Vector) val : def;
    }

    public boolean isVector(String path) {
        Object val = this.get(path);

        return val instanceof Vector;
    }

    public OfflinePlayer getOfflinePlayer(String path) {
        Object def = this.getDefault(path);

        return this.getOfflinePlayer(path, def instanceof OfflinePlayer ? (OfflinePlayer) def : null);
    }

    public OfflinePlayer getOfflinePlayer(String path, OfflinePlayer def) {
        Object val = this.get(path, def);

        return val instanceof OfflinePlayer ? (OfflinePlayer) val : def;
    }

    public boolean isOfflinePlayer(String path) {
        Object val = this.get(path);

        return val instanceof OfflinePlayer;
    }

    public ItemStack getItemStack(String path) {
        Object def = this.getDefault(path);

        return this.getItemStack(path, def instanceof ItemStack ? (ItemStack) def : null);
    }

    public ItemStack getItemStack(String path, ItemStack def) {
        Object val = this.get(path, def);

        return val instanceof ItemStack ? (ItemStack) val : def;
    }

    public boolean isItemStack(String path) {
        Object val = this.get(path);

        return val instanceof ItemStack;
    }

    public Color getColor(String path) {
        Object def = this.getDefault(path);

        return this.getColor(path, def instanceof Color ? (Color) def : null);
    }

    public Color getColor(String path, Color def) {
        Object val = this.get(path, def);

        return val instanceof Color ? (Color) val : def;
    }

    public boolean isColor(String path) {
        Object val = this.get(path);

        return val instanceof Color;
    }

    public ConfigurationSection getConfigurationSection(String path) {
        Object val = this.get(path, (Object) null);

        if (val != null) {
            return val instanceof ConfigurationSection ? (ConfigurationSection) val : null;
        } else {
            val = this.get(path, this.getDefault(path));
            return val instanceof ConfigurationSection ? this.createSection(path) : null;
        }
    }

    public boolean isConfigurationSection(String path) {
        Object val = this.get(path);

        return val instanceof ConfigurationSection;
    }

    protected boolean isPrimitiveWrapper(Object input) {
        return input instanceof Integer || input instanceof Boolean || input instanceof Character || input instanceof Byte || input instanceof Short || input instanceof Double || input instanceof Long || input instanceof Float;
    }

    protected Object getDefault(String path) {
        Validate.notNull(path, "Path cannot be null");
        Configuration root = this.getRoot();
        Configuration defaults = root == null ? null : root.getDefaults();

        return defaults == null ? null : defaults.get(createPath(this, path));
    }

    protected void mapChildrenKeys(Set output, ConfigurationSection section, boolean deep) {
        Iterator iterator;

        if (section instanceof MemorySection) {
            MemorySection keys = (MemorySection) section;

            iterator = keys.map.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry key = (Entry) iterator.next();

                output.add(createPath(section, (String) key.getKey(), this));
                if (deep && key.getValue() instanceof ConfigurationSection) {
                    ConfigurationSection subsection = (ConfigurationSection) key.getValue();

                    this.mapChildrenKeys(output, subsection, deep);
                }
            }
        } else {
            Set keys1 = section.getKeys(deep);

            iterator = keys1.iterator();

            while (iterator.hasNext()) {
                String key1 = (String) iterator.next();

                output.add(createPath(section, key1, this));
            }
        }

    }

    protected void mapChildrenValues(Map output, ConfigurationSection section, boolean deep) {
        Iterator iterator;
        Entry entry;

        if (section instanceof MemorySection) {
            MemorySection values = (MemorySection) section;

            iterator = values.map.entrySet().iterator();

            while (iterator.hasNext()) {
                entry = (Entry) iterator.next();
                output.put(createPath(section, (String) entry.getKey(), this), entry.getValue());
                if (entry.getValue() instanceof ConfigurationSection && deep) {
                    this.mapChildrenValues(output, (ConfigurationSection) entry.getValue(), deep);
                }
            }
        } else {
            Map values1 = section.getValues(deep);

            iterator = values1.entrySet().iterator();

            while (iterator.hasNext()) {
                entry = (Entry) iterator.next();
                output.put(createPath(section, (String) entry.getKey(), this), entry.getValue());
            }
        }

    }

    public static String createPath(ConfigurationSection section, String key) {
        return createPath(section, key, section == null ? null : section.getRoot());
    }

    public static String createPath(ConfigurationSection section, String key, ConfigurationSection relativeTo) {
        Validate.notNull(section, "Cannot create path without a section");
        Configuration root = section.getRoot();

        if (root == null) {
            throw new IllegalStateException("Cannot create path without a root");
        } else {
            char separator = root.options().pathSeparator();
            StringBuilder builder = new StringBuilder();

            if (section != null) {
                for (ConfigurationSection parent = section; parent != null && parent != relativeTo; parent = parent.getParent()) {
                    if (builder.length() > 0) {
                        builder.insert(0, separator);
                    }

                    builder.insert(0, parent.getName());
                }
            }

            if (key != null && key.length() > 0) {
                if (builder.length() > 0) {
                    builder.append(separator);
                }

                builder.append(key);
            }

            return builder.toString();
        }
    }

    public String toString() {
        Configuration root = this.getRoot();

        return this.getClass().getSimpleName() + "[path=\'" + this.getCurrentPath() + "\', root=\'" + (root == null ? null : root.getClass().getSimpleName()) + "\']";
    }
}
