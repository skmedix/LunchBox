package org.apache.commons.lang.enum;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

/** @deprecated */
public abstract class Enum implements Comparable, Serializable {

    private static final long serialVersionUID = -487045951170455942L;
    private static final Map EMPTY_MAP = Collections.unmodifiableMap(new HashMap(0));
    private static Map cEnumClasses = new WeakHashMap();
    private final String iName;
    private final transient int iHashCode;
    protected transient String iToString = null;
    static Class class$org$apache$commons$lang$enum$Enum;
    static Class class$org$apache$commons$lang$enum$ValuedEnum;

    protected Enum(String name) {
        this.init(name);
        this.iName = name;
        this.iHashCode = 7 + this.getEnumClass().hashCode() + 3 * name.hashCode();
    }

    private void init(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("The Enum name must not be empty or null");
        } else {
            Class enumClass = this.getEnumClass();

            if (enumClass == null) {
                throw new IllegalArgumentException("getEnumClass() must not be null");
            } else {
                Class cls = this.getClass();

                boolean ok;

                for (ok = false; cls != null && cls != (Enum.class$org$apache$commons$lang$enum$Enum == null ? (Enum.class$org$apache$commons$lang$enum$Enum = class$("org.apache.commons.lang.enum.Enum")) : Enum.class$org$apache$commons$lang$enum$Enum) && cls != (Enum.class$org$apache$commons$lang$enum$ValuedEnum == null ? (Enum.class$org$apache$commons$lang$enum$ValuedEnum = class$("org.apache.commons.lang.enum.ValuedEnum")) : Enum.class$org$apache$commons$lang$enum$ValuedEnum); cls = cls.getSuperclass()) {
                    if (cls == enumClass) {
                        ok = true;
                        break;
                    }
                }

                if (!ok) {
                    throw new IllegalArgumentException("getEnumClass() must return a superclass of this class");
                } else {
                    Enum.Entry entry;

                    synchronized (Enum.class$org$apache$commons$lang$enum$Enum == null ? (Enum.class$org$apache$commons$lang$enum$Enum = class$("org.apache.commons.lang.enum.Enum")) : Enum.class$org$apache$commons$lang$enum$Enum) {
                        entry = (Enum.Entry) Enum.cEnumClasses.get(enumClass);
                        if (entry == null) {
                            entry = createEntry(enumClass);
                            WeakHashMap myMap = new WeakHashMap();

                            myMap.putAll(Enum.cEnumClasses);
                            myMap.put(enumClass, entry);
                            Enum.cEnumClasses = myMap;
                        }
                    }

                    if (entry.map.containsKey(name)) {
                        throw new IllegalArgumentException("The Enum name must be unique, \'" + name + "\' has already been added");
                    } else {
                        entry.map.put(name, this);
                        entry.list.add(this);
                    }
                }
            }
        }
    }

    protected Object readResolve() {
        Enum.Entry entry = (Enum.Entry) Enum.cEnumClasses.get(this.getEnumClass());

        return entry == null ? null : entry.map.get(this.getName());
    }

    protected static Enum getEnum(Class enumClass, String name) {
        Enum.Entry entry = getEntry(enumClass);

        return entry == null ? null : (Enum) entry.map.get(name);
    }

    protected static Map getEnumMap(Class enumClass) {
        Enum.Entry entry = getEntry(enumClass);

        return entry == null ? Enum.EMPTY_MAP : entry.unmodifiableMap;
    }

    protected static List getEnumList(Class enumClass) {
        Enum.Entry entry = getEntry(enumClass);

        return entry == null ? Collections.EMPTY_LIST : entry.unmodifiableList;
    }

    protected static Iterator iterator(Class enumClass) {
        return getEnumList(enumClass).iterator();
    }

    private static Enum.Entry getEntry(Class enumClass) {
        if (enumClass == null) {
            throw new IllegalArgumentException("The Enum Class must not be null");
        } else if (!(Enum.class$org$apache$commons$lang$enum$Enum == null ? (Enum.class$org$apache$commons$lang$enum$Enum = class$("org.apache.commons.lang.enum.Enum")) : Enum.class$org$apache$commons$lang$enum$Enum).isAssignableFrom(enumClass)) {
            throw new IllegalArgumentException("The Class must be a subclass of Enum");
        } else {
            Enum.Entry entry = (Enum.Entry) Enum.cEnumClasses.get(enumClass);

            if (entry == null) {
                try {
                    Class.forName(enumClass.getName(), true, enumClass.getClassLoader());
                    entry = (Enum.Entry) Enum.cEnumClasses.get(enumClass);
                } catch (Exception exception) {
                    ;
                }
            }

            return entry;
        }
    }

    private static Enum.Entry createEntry(Class enumClass) {
        Enum.Entry entry = new Enum.Entry();

        for (Class cls = enumClass.getSuperclass(); cls != null && cls != (Enum.class$org$apache$commons$lang$enum$Enum == null ? (Enum.class$org$apache$commons$lang$enum$Enum = class$("org.apache.commons.lang.enum.Enum")) : Enum.class$org$apache$commons$lang$enum$Enum) && cls != (Enum.class$org$apache$commons$lang$enum$ValuedEnum == null ? (Enum.class$org$apache$commons$lang$enum$ValuedEnum = class$("org.apache.commons.lang.enum.ValuedEnum")) : Enum.class$org$apache$commons$lang$enum$ValuedEnum); cls = cls.getSuperclass()) {
            Enum.Entry loopEntry = (Enum.Entry) Enum.cEnumClasses.get(cls);

            if (loopEntry != null) {
                entry.list.addAll(loopEntry.list);
                entry.map.putAll(loopEntry.map);
                break;
            }
        }

        return entry;
    }

    public final String getName() {
        return this.iName;
    }

    public Class getEnumClass() {
        return this.getClass();
    }

    public final boolean equals(Object other) {
        return other == this ? true : (other == null ? false : (other.getClass() == this.getClass() ? this.iName.equals(((Enum) other).iName) : (!other.getClass().getName().equals(this.getClass().getName()) ? false : this.iName.equals(this.getNameInOtherClassLoader(other)))));
    }

    public final int hashCode() {
        return this.iHashCode;
    }

    public int compareTo(Object other) {
        if (other == this) {
            return 0;
        } else if (other.getClass() != this.getClass()) {
            if (other.getClass().getName().equals(this.getClass().getName())) {
                return this.iName.compareTo(this.getNameInOtherClassLoader(other));
            } else {
                throw new ClassCastException("Different enum class \'" + ClassUtils.getShortClassName(other.getClass()) + "\'");
            }
        } else {
            return this.iName.compareTo(((Enum) other).iName);
        }
    }

    private String getNameInOtherClassLoader(Object other) {
        try {
            Method e = other.getClass().getMethod("getName", (Class[]) null);
            String name = (String) e.invoke(other, (Object[]) null);

            return name;
        } catch (NoSuchMethodException nosuchmethodexception) {
            ;
        } catch (IllegalAccessException illegalaccessexception) {
            ;
        } catch (InvocationTargetException invocationtargetexception) {
            ;
        }

        throw new IllegalStateException("This should not happen");
    }

    public String toString() {
        if (this.iToString == null) {
            String shortName = ClassUtils.getShortClassName(this.getEnumClass());

            this.iToString = shortName + "[" + this.getName() + "]";
        }

        return this.iToString;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException classnotfoundexception) {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    private static class Entry {

        final Map map = new HashMap();
        final Map unmodifiableMap;
        final List list;
        final List unmodifiableList;

        protected Entry() {
            this.unmodifiableMap = Collections.unmodifiableMap(this.map);
            this.list = new ArrayList(25);
            this.unmodifiableList = Collections.unmodifiableList(this.list);
        }
    }
}
