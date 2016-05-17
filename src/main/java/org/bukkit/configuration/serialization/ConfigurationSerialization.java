package org.bukkit.configuration.serialization;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.Validate;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

public class ConfigurationSerialization {

    public static final String SERIALIZED_TYPE_KEY = "==";
    private final Class clazz;
    private static Map aliases = new HashMap();

    static {
        registerClass(Vector.class);
        registerClass(BlockVector.class);
        registerClass(ItemStack.class);
        registerClass(Color.class);
        registerClass(PotionEffect.class);
        registerClass(FireworkEffect.class);
        registerClass(Pattern.class);
        registerClass(Location.class);
    }

    protected ConfigurationSerialization(Class clazz) {
        this.clazz = clazz;
    }

    protected Method getMethod(String name, boolean isStatic) {
        try {
            Method method = this.clazz.getDeclaredMethod(name, new Class[] { Map.class});

            return !ConfigurationSerializable.class.isAssignableFrom(method.getReturnType()) ? null : (Modifier.isStatic(method.getModifiers()) != isStatic ? null : method);
        } catch (NoSuchMethodException nosuchmethodexception) {
            return null;
        } catch (SecurityException securityexception) {
            return null;
        }
    }

    protected Constructor getConstructor() {
        try {
            return this.clazz.getConstructor(new Class[] { Map.class});
        } catch (NoSuchMethodException nosuchmethodexception) {
            return null;
        } catch (SecurityException securityexception) {
            return null;
        }
    }

    protected ConfigurationSerializable deserializeViaMethod(Method method, Map args) {
        try {
            ConfigurationSerializable ex = (ConfigurationSerializable) method.invoke((Object) null, new Object[] { args});

            if (ex != null) {
                return ex;
            }

            Logger.getLogger(ConfigurationSerialization.class.getName()).log(Level.SEVERE, "Could not call method \'" + method.toString() + "\' of " + this.clazz + " for deserialization: method returned null");
        } catch (Throwable throwable) {
            Logger.getLogger(ConfigurationSerialization.class.getName()).log(Level.SEVERE, "Could not call method \'" + method.toString() + "\' of " + this.clazz + " for deserialization", throwable instanceof InvocationTargetException ? throwable.getCause() : throwable);
        }

        return null;
    }

    protected ConfigurationSerializable deserializeViaCtor(Constructor ctor, Map args) {
        try {
            return (ConfigurationSerializable) ctor.newInstance(new Object[] { args});
        } catch (Throwable throwable) {
            Logger.getLogger(ConfigurationSerialization.class.getName()).log(Level.SEVERE, "Could not call constructor \'" + ctor.toString() + "\' of " + this.clazz + " for deserialization", throwable instanceof InvocationTargetException ? throwable.getCause() : throwable);
            return null;
        }
    }

    public ConfigurationSerializable deserialize(Map args) {
        Validate.notNull(args, "Args must not be null");
        ConfigurationSerializable result = null;
        Method method = null;

        if (result == null) {
            method = this.getMethod("deserialize", true);
            if (method != null) {
                result = this.deserializeViaMethod(method, args);
            }
        }

        if (result == null) {
            method = this.getMethod("valueOf", true);
            if (method != null) {
                result = this.deserializeViaMethod(method, args);
            }
        }

        if (result == null) {
            Constructor constructor = this.getConstructor();

            if (constructor != null) {
                result = this.deserializeViaCtor(constructor, args);
            }
        }

        return result;
    }

    public static ConfigurationSerializable deserializeObject(Map args, Class clazz) {
        return (new ConfigurationSerialization(clazz)).deserialize(args);
    }

    public static ConfigurationSerializable deserializeObject(Map args) {
        Class clazz = null;

        if (args.containsKey("==")) {
            try {
                String ex = (String) args.get("==");

                if (ex == null) {
                    throw new IllegalArgumentException("Cannot have null alias");
                }

                clazz = getClassByAlias(ex);
                if (clazz == null) {
                    throw new IllegalArgumentException("Specified class does not exist (\'" + ex + "\')");
                }
            } catch (ClassCastException classcastexception) {
                classcastexception.fillInStackTrace();
                throw classcastexception;
            }

            return (new ConfigurationSerialization(clazz)).deserialize(args);
        } else {
            throw new IllegalArgumentException("Args doesn\'t contain type key (\'==\')");
        }
    }

    public static void registerClass(Class clazz) {
        DelegateDeserialization delegate = (DelegateDeserialization) clazz.getAnnotation(DelegateDeserialization.class);

        if (delegate == null) {
            registerClass(clazz, getAlias(clazz));
            registerClass(clazz, clazz.getName());
        }

    }

    public static void registerClass(Class clazz, String alias) {
        ConfigurationSerialization.aliases.put(alias, clazz);
    }

    public static void unregisterClass(String alias) {
        ConfigurationSerialization.aliases.remove(alias);
    }

    public static void unregisterClass(Class clazz) {
        while (ConfigurationSerialization.aliases.values().remove(clazz)) {
            ;
        }

    }

    public static Class getClassByAlias(String alias) {
        return (Class) ConfigurationSerialization.aliases.get(alias);
    }

    public static String getAlias(Class clazz) {
        DelegateDeserialization delegate = (DelegateDeserialization) clazz.getAnnotation(DelegateDeserialization.class);

        if (delegate != null) {
            if (delegate.value() != null && delegate.value() != clazz) {
                return getAlias(delegate.value());
            }

            delegate = null;
        }

        if (delegate == null) {
            SerializableAs alias = (SerializableAs) clazz.getAnnotation(SerializableAs.class);

            if (alias != null && alias.value() != null) {
                return alias.value();
            }
        }

        return clazz.getName();
    }
}
