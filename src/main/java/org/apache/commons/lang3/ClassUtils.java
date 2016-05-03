package org.apache.commons.lang3;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.commons.lang3.mutable.MutableObject;

public class ClassUtils {

    public static final char PACKAGE_SEPARATOR_CHAR = '.';
    public static final String PACKAGE_SEPARATOR = String.valueOf('.');
    public static final char INNER_CLASS_SEPARATOR_CHAR = '$';
    public static final String INNER_CLASS_SEPARATOR = String.valueOf('$');
    private static final Map primitiveWrapperMap = new HashMap();
    private static final Map wrapperPrimitiveMap;
    private static final Map abbreviationMap;
    private static final Map reverseAbbreviationMap;

    public static String getShortClassName(Object object, String s) {
        return object == null ? s : getShortClassName(object.getClass());
    }

    public static String getShortClassName(Class oclass) {
        return oclass == null ? "" : getShortClassName(oclass.getName());
    }

    public static String getShortClassName(String s) {
        if (StringUtils.isEmpty(s)) {
            return "";
        } else {
            StringBuilder stringbuilder = new StringBuilder();

            if (s.startsWith("[")) {
                while (s.charAt(0) == 91) {
                    s = s.substring(1);
                    stringbuilder.append("[]");
                }

                if (s.charAt(0) == 76 && s.charAt(s.length() - 1) == 59) {
                    s = s.substring(1, s.length() - 1);
                }

                if (ClassUtils.reverseAbbreviationMap.containsKey(s)) {
                    s = (String) ClassUtils.reverseAbbreviationMap.get(s);
                }
            }

            int i = s.lastIndexOf(46);
            int j = s.indexOf(36, i == -1 ? 0 : i + 1);
            String s1 = s.substring(i + 1);

            if (j != -1) {
                s1 = s1.replace('$', '.');
            }

            return s1 + stringbuilder;
        }
    }

    public static String getSimpleName(Class oclass) {
        return oclass == null ? "" : oclass.getSimpleName();
    }

    public static String getSimpleName(Object object, String s) {
        return object == null ? s : getSimpleName(object.getClass());
    }

    public static String getPackageName(Object object, String s) {
        return object == null ? s : getPackageName(object.getClass());
    }

    public static String getPackageName(Class oclass) {
        return oclass == null ? "" : getPackageName(oclass.getName());
    }

    public static String getPackageName(String s) {
        if (StringUtils.isEmpty(s)) {
            return "";
        } else {
            while (s.charAt(0) == 91) {
                s = s.substring(1);
            }

            if (s.charAt(0) == 76 && s.charAt(s.length() - 1) == 59) {
                s = s.substring(1);
            }

            int i = s.lastIndexOf(46);

            return i == -1 ? "" : s.substring(0, i);
        }
    }

    public static List getAllSuperclasses(Class oclass) {
        if (oclass == null) {
            return null;
        } else {
            ArrayList arraylist = new ArrayList();

            for (Class oclass1 = oclass.getSuperclass(); oclass1 != null; oclass1 = oclass1.getSuperclass()) {
                arraylist.add(oclass1);
            }

            return arraylist;
        }
    }

    public static List getAllInterfaces(Class oclass) {
        if (oclass == null) {
            return null;
        } else {
            LinkedHashSet linkedhashset = new LinkedHashSet();

            getAllInterfaces(oclass, linkedhashset);
            return new ArrayList(linkedhashset);
        }
    }

    private static void getAllInterfaces(Class oclass, HashSet hashset) {
        while (oclass != null) {
            Class[] aclass = oclass.getInterfaces();
            Class[] aclass1 = aclass;
            int i = aclass.length;

            for (int j = 0; j < i; ++j) {
                Class oclass1 = aclass1[j];

                if (hashset.add(oclass1)) {
                    getAllInterfaces(oclass1, hashset);
                }
            }

            oclass = oclass.getSuperclass();
        }

    }

    public static List convertClassNamesToClasses(List list) {
        if (list == null) {
            return null;
        } else {
            ArrayList arraylist = new ArrayList(list.size());
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                try {
                    arraylist.add(Class.forName(s));
                } catch (Exception exception) {
                    arraylist.add((Object) null);
                }
            }

            return arraylist;
        }
    }

    public static List convertClassesToClassNames(List list) {
        if (list == null) {
            return null;
        } else {
            ArrayList arraylist = new ArrayList(list.size());
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Class oclass = (Class) iterator.next();

                if (oclass == null) {
                    arraylist.add((Object) null);
                } else {
                    arraylist.add(oclass.getName());
                }
            }

            return arraylist;
        }
    }

    public static boolean isAssignable(Class[] aclass, Class... aclass1) {
        return isAssignable(aclass, aclass1, SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_5));
    }

    public static boolean isAssignable(Class[] aclass, Class[] aclass1, boolean flag) {
        if (!ArrayUtils.isSameLength((Object[]) aclass, (Object[]) aclass1)) {
            return false;
        } else {
            if (aclass == null) {
                aclass = ArrayUtils.EMPTY_CLASS_ARRAY;
            }

            if (aclass1 == null) {
                aclass1 = ArrayUtils.EMPTY_CLASS_ARRAY;
            }

            for (int i = 0; i < aclass.length; ++i) {
                if (!isAssignable(aclass[i], aclass1[i], flag)) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isPrimitiveOrWrapper(Class oclass) {
        return oclass == null ? false : oclass.isPrimitive() || isPrimitiveWrapper(oclass);
    }

    public static boolean isPrimitiveWrapper(Class oclass) {
        return ClassUtils.wrapperPrimitiveMap.containsKey(oclass);
    }

    public static boolean isAssignable(Class oclass, Class oclass1) {
        return isAssignable(oclass, oclass1, SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_5));
    }

    public static boolean isAssignable(Class oclass, Class oclass1, boolean flag) {
        if (oclass1 == null) {
            return false;
        } else if (oclass == null) {
            return !oclass1.isPrimitive();
        } else {
            if (flag) {
                if (oclass.isPrimitive() && !oclass1.isPrimitive()) {
                    oclass = primitiveToWrapper(oclass);
                    if (oclass == null) {
                        return false;
                    }
                }

                if (oclass1.isPrimitive() && !oclass.isPrimitive()) {
                    oclass = wrapperToPrimitive(oclass);
                    if (oclass == null) {
                        return false;
                    }
                }
            }

            return oclass.equals(oclass1) ? true : (oclass.isPrimitive() ? (!oclass1.isPrimitive() ? false : (Integer.TYPE.equals(oclass) ? Long.TYPE.equals(oclass1) || Float.TYPE.equals(oclass1) || Double.TYPE.equals(oclass1) : (Long.TYPE.equals(oclass) ? Float.TYPE.equals(oclass1) || Double.TYPE.equals(oclass1) : (Boolean.TYPE.equals(oclass) ? false : (Double.TYPE.equals(oclass) ? false : (Float.TYPE.equals(oclass) ? Double.TYPE.equals(oclass1) : (Character.TYPE.equals(oclass) ? Integer.TYPE.equals(oclass1) || Long.TYPE.equals(oclass1) || Float.TYPE.equals(oclass1) || Double.TYPE.equals(oclass1) : (Short.TYPE.equals(oclass) ? Integer.TYPE.equals(oclass1) || Long.TYPE.equals(oclass1) || Float.TYPE.equals(oclass1) || Double.TYPE.equals(oclass1) : (!Byte.TYPE.equals(oclass) ? false : Short.TYPE.equals(oclass1) || Integer.TYPE.equals(oclass1) || Long.TYPE.equals(oclass1) || Float.TYPE.equals(oclass1) || Double.TYPE.equals(oclass1)))))))))) : oclass1.isAssignableFrom(oclass));
        }
    }

    public static Class primitiveToWrapper(Class oclass) {
        Class oclass1 = oclass;

        if (oclass != null && oclass.isPrimitive()) {
            oclass1 = (Class) ClassUtils.primitiveWrapperMap.get(oclass);
        }

        return oclass1;
    }

    public static Class[] primitivesToWrappers(Class... aclass) {
        if (aclass == null) {
            return null;
        } else if (aclass.length == 0) {
            return aclass;
        } else {
            Class[] aclass1 = new Class[aclass.length];

            for (int i = 0; i < aclass.length; ++i) {
                aclass1[i] = primitiveToWrapper(aclass[i]);
            }

            return aclass1;
        }
    }

    public static Class wrapperToPrimitive(Class oclass) {
        return (Class) ClassUtils.wrapperPrimitiveMap.get(oclass);
    }

    public static Class[] wrappersToPrimitives(Class... aclass) {
        if (aclass == null) {
            return null;
        } else if (aclass.length == 0) {
            return aclass;
        } else {
            Class[] aclass1 = new Class[aclass.length];

            for (int i = 0; i < aclass.length; ++i) {
                aclass1[i] = wrapperToPrimitive(aclass[i]);
            }

            return aclass1;
        }
    }

    public static boolean isInnerClass(Class oclass) {
        return oclass != null && oclass.getEnclosingClass() != null;
    }

    public static Class getClass(ClassLoader classloader, String s, boolean flag) throws ClassNotFoundException {
        try {
            Class oclass;

            if (ClassUtils.abbreviationMap.containsKey(s)) {
                String s1 = "[" + (String) ClassUtils.abbreviationMap.get(s);

                oclass = Class.forName(s1, flag, classloader).getComponentType();
            } else {
                oclass = Class.forName(toCanonicalName(s), flag, classloader);
            }

            return oclass;
        } catch (ClassNotFoundException classnotfoundexception) {
            int i = s.lastIndexOf(46);

            if (i != -1) {
                try {
                    return getClass(classloader, s.substring(0, i) + '$' + s.substring(i + 1), flag);
                } catch (ClassNotFoundException classnotfoundexception1) {
                    ;
                }
            }

            throw classnotfoundexception;
        }
    }

    public static Class getClass(ClassLoader classloader, String s) throws ClassNotFoundException {
        return getClass(classloader, s, true);
    }

    public static Class getClass(String s) throws ClassNotFoundException {
        return getClass(s, true);
    }

    public static Class getClass(String s, boolean flag) throws ClassNotFoundException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        ClassLoader classloader1 = classloader == null ? ClassUtils.class.getClassLoader() : classloader;

        return getClass(classloader1, s, flag);
    }

    public static Method getPublicMethod(Class oclass, String s, Class... aclass) throws SecurityException, NoSuchMethodException {
        Method method = oclass.getMethod(s, aclass);

        if (Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
            return method;
        } else {
            ArrayList arraylist = new ArrayList();

            arraylist.addAll(getAllInterfaces(oclass));
            arraylist.addAll(getAllSuperclasses(oclass));
            Iterator iterator = arraylist.iterator();

            while (iterator.hasNext()) {
                Class oclass1 = (Class) iterator.next();

                if (Modifier.isPublic(oclass1.getModifiers())) {
                    Method method1;

                    try {
                        method1 = oclass1.getMethod(s, aclass);
                    } catch (NoSuchMethodException nosuchmethodexception) {
                        continue;
                    }

                    if (Modifier.isPublic(method1.getDeclaringClass().getModifiers())) {
                        return method1;
                    }
                }
            }

            throw new NoSuchMethodException("Can\'t find a public method for " + s + " " + ArrayUtils.toString(aclass));
        }
    }

    private static String toCanonicalName(String s) {
        s = StringUtils.deleteWhitespace(s);
        if (s == null) {
            throw new NullPointerException("className must not be null.");
        } else {
            if (s.endsWith("[]")) {
                StringBuilder stringbuilder = new StringBuilder();

                while (s.endsWith("[]")) {
                    s = s.substring(0, s.length() - 2);
                    stringbuilder.append("[");
                }

                String s1 = (String) ClassUtils.abbreviationMap.get(s);

                if (s1 != null) {
                    stringbuilder.append(s1);
                } else {
                    stringbuilder.append("L").append(s).append(";");
                }

                s = stringbuilder.toString();
            }

            return s;
        }
    }

    public static Class[] toClass(Object... aobject) {
        if (aobject == null) {
            return null;
        } else if (aobject.length == 0) {
            return ArrayUtils.EMPTY_CLASS_ARRAY;
        } else {
            Class[] aclass = new Class[aobject.length];

            for (int i = 0; i < aobject.length; ++i) {
                aclass[i] = aobject[i] == null ? null : aobject[i].getClass();
            }

            return aclass;
        }
    }

    public static String getShortCanonicalName(Object object, String s) {
        return object == null ? s : getShortCanonicalName(object.getClass().getName());
    }

    public static String getShortCanonicalName(Class oclass) {
        return oclass == null ? "" : getShortCanonicalName(oclass.getName());
    }

    public static String getShortCanonicalName(String s) {
        return getShortClassName(getCanonicalName(s));
    }

    public static String getPackageCanonicalName(Object object, String s) {
        return object == null ? s : getPackageCanonicalName(object.getClass().getName());
    }

    public static String getPackageCanonicalName(Class oclass) {
        return oclass == null ? "" : getPackageCanonicalName(oclass.getName());
    }

    public static String getPackageCanonicalName(String s) {
        return getPackageName(getCanonicalName(s));
    }

    private static String getCanonicalName(String s) {
        s = StringUtils.deleteWhitespace(s);
        if (s == null) {
            return null;
        } else {
            int i;

            for (i = 0; s.startsWith("["); s = s.substring(1)) {
                ++i;
            }

            if (i < 1) {
                return s;
            } else {
                if (s.startsWith("L")) {
                    s = s.substring(1, s.endsWith(";") ? s.length() - 1 : s.length());
                } else if (s.length() > 0) {
                    s = (String) ClassUtils.reverseAbbreviationMap.get(s.substring(0, 1));
                }

                StringBuilder stringbuilder = new StringBuilder(s);

                for (int j = 0; j < i; ++j) {
                    stringbuilder.append("[]");
                }

                return stringbuilder.toString();
            }
        }
    }

    public static Iterable hierarchy(Class oclass) {
        return hierarchy(oclass, ClassUtils.Interfaces.EXCLUDE);
    }

    public static Iterable hierarchy(final Class oclass, ClassUtils.Interfaces classutils_interfaces) {
        final Iterable iterable = new Iterable() {
            public Iterator iterator() {
                final MutableObject mutableobject = new MutableObject(oclass);

                return new Iterator() {
                    public boolean hasNext() {
                        return mutableobject.getValue() != null;
                    }

                    public Class next() {
                        Class oclass = (Class) mutableobject.getValue();

                        mutableobject.setValue(oclass.getSuperclass());
                        return oclass;
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };

        return classutils_interfaces != ClassUtils.Interfaces.INCLUDE ? iterable : new Iterable() {
            public Iterator iterator() {
                final HashSet hashset = new HashSet();
                final Iterator iterator = iterable.iterator();

                return new Iterator() {
                    Iterator interfaces = Collections.emptySet().iterator();

                    public boolean hasNext() {
                        return this.interfaces.hasNext() || iterator.hasNext();
                    }

                    public Class next() {
                        Class oclass;

                        if (this.interfaces.hasNext()) {
                            oclass = (Class) this.interfaces.next();
                            hashset.add(oclass);
                            return oclass;
                        } else {
                            oclass = (Class) iterator.next();
                            LinkedHashSet linkedhashset = new LinkedHashSet();

                            this.walkInterfaces(linkedhashset, oclass);
                            this.interfaces = linkedhashset.iterator();
                            return oclass;
                        }
                    }

                    private void walkInterfaces(Set set, Class oclass) {
                        Class[] aclass = oclass.getInterfaces();
                        int i = aclass.length;

                        for (int j = 0; j < i; ++j) {
                            Class oclass1 = aclass[j];

                            if (!hashset.contains(oclass1)) {
                                set.add(oclass1);
                            }

                            this.walkInterfaces(set, oclass1);
                        }

                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    static {
        ClassUtils.primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
        ClassUtils.primitiveWrapperMap.put(Byte.TYPE, Byte.class);
        ClassUtils.primitiveWrapperMap.put(Character.TYPE, Character.class);
        ClassUtils.primitiveWrapperMap.put(Short.TYPE, Short.class);
        ClassUtils.primitiveWrapperMap.put(Integer.TYPE, Integer.class);
        ClassUtils.primitiveWrapperMap.put(Long.TYPE, Long.class);
        ClassUtils.primitiveWrapperMap.put(Double.TYPE, Double.class);
        ClassUtils.primitiveWrapperMap.put(Float.TYPE, Float.class);
        ClassUtils.primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
        wrapperPrimitiveMap = new HashMap();
        Iterator iterator = ClassUtils.primitiveWrapperMap.keySet().iterator();

        while (iterator.hasNext()) {
            Class oclass = (Class) iterator.next();
            Class oclass1 = (Class) ClassUtils.primitiveWrapperMap.get(oclass);

            if (!oclass.equals(oclass1)) {
                ClassUtils.wrapperPrimitiveMap.put(oclass1, oclass);
            }
        }

        HashMap hashmap = new HashMap();

        hashmap.put("int", "I");
        hashmap.put("boolean", "Z");
        hashmap.put("float", "F");
        hashmap.put("long", "J");
        hashmap.put("short", "S");
        hashmap.put("byte", "B");
        hashmap.put("double", "D");
        hashmap.put("char", "C");
        hashmap.put("void", "V");
        HashMap hashmap1 = new HashMap();
        Iterator iterator1 = hashmap.entrySet().iterator();

        while (iterator1.hasNext()) {
            Entry entry = (Entry) iterator1.next();

            hashmap1.put(entry.getValue(), entry.getKey());
        }

        abbreviationMap = Collections.unmodifiableMap(hashmap);
        reverseAbbreviationMap = Collections.unmodifiableMap(hashmap1);
    }

    public static enum Interfaces {

        INCLUDE, EXCLUDE;
    }
}
