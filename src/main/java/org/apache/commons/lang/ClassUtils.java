package org.apache.commons.lang;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.text.StrBuilder;

public class ClassUtils {

    public static final char PACKAGE_SEPARATOR_CHAR = '.';
    public static final String PACKAGE_SEPARATOR = String.valueOf('.');
    public static final char INNER_CLASS_SEPARATOR_CHAR = '$';
    public static final String INNER_CLASS_SEPARATOR = String.valueOf('$');
    private static final Map primitiveWrapperMap = new HashMap();
    private static final Map wrapperPrimitiveMap;
    private static final Map abbreviationMap;
    private static final Map reverseAbbreviationMap;
    static Class class$java$lang$Boolean;
    static Class class$java$lang$Byte;
    static Class class$java$lang$Character;
    static Class class$java$lang$Short;
    static Class class$java$lang$Integer;
    static Class class$java$lang$Long;
    static Class class$java$lang$Double;
    static Class class$java$lang$Float;
    static Class class$org$apache$commons$lang$ClassUtils;

    private static void addAbbreviation(String primitive, String abbreviation) {
        ClassUtils.abbreviationMap.put(primitive, abbreviation);
        ClassUtils.reverseAbbreviationMap.put(abbreviation, primitive);
    }

    public static String getShortClassName(Object object, String valueIfNull) {
        return object == null ? valueIfNull : getShortClassName(object.getClass());
    }

    public static String getShortClassName(Class cls) {
        return cls == null ? "" : getShortClassName(cls.getName());
    }

    public static String getShortClassName(String className) {
        if (className == null) {
            return "";
        } else if (className.length() == 0) {
            return "";
        } else {
            StrBuilder arrayPrefix = new StrBuilder();

            if (className.startsWith("[")) {
                while (className.charAt(0) == 91) {
                    className = className.substring(1);
                    arrayPrefix.append("[]");
                }

                if (className.charAt(0) == 76 && className.charAt(className.length() - 1) == 59) {
                    className = className.substring(1, className.length() - 1);
                }
            }

            if (ClassUtils.reverseAbbreviationMap.containsKey(className)) {
                className = (String) ClassUtils.reverseAbbreviationMap.get(className);
            }

            int lastDotIdx = className.lastIndexOf(46);
            int innerIdx = className.indexOf(36, lastDotIdx == -1 ? 0 : lastDotIdx + 1);
            String out = className.substring(lastDotIdx + 1);

            if (innerIdx != -1) {
                out = out.replace('$', '.');
            }

            return out + arrayPrefix;
        }
    }

    public static String getPackageName(Object object, String valueIfNull) {
        return object == null ? valueIfNull : getPackageName(object.getClass());
    }

    public static String getPackageName(Class cls) {
        return cls == null ? "" : getPackageName(cls.getName());
    }

    public static String getPackageName(String className) {
        if (className != null && className.length() != 0) {
            while (className.charAt(0) == 91) {
                className = className.substring(1);
            }

            if (className.charAt(0) == 76 && className.charAt(className.length() - 1) == 59) {
                className = className.substring(1);
            }

            int i = className.lastIndexOf(46);

            return i == -1 ? "" : className.substring(0, i);
        } else {
            return "";
        }
    }

    public static List getAllSuperclasses(Class cls) {
        if (cls == null) {
            return null;
        } else {
            ArrayList classes = new ArrayList();

            for (Class superclass = cls.getSuperclass(); superclass != null; superclass = superclass.getSuperclass()) {
                classes.add(superclass);
            }

            return classes;
        }
    }

    public static List getAllInterfaces(Class cls) {
        if (cls == null) {
            return null;
        } else {
            ArrayList interfacesFound = new ArrayList();

            getAllInterfaces(cls, interfacesFound);
            return interfacesFound;
        }
    }

    private static void getAllInterfaces(Class cls, List interfacesFound) {
        while (cls != null) {
            Class[] interfaces = cls.getInterfaces();

            for (int i = 0; i < interfaces.length; ++i) {
                if (!interfacesFound.contains(interfaces[i])) {
                    interfacesFound.add(interfaces[i]);
                    getAllInterfaces(interfaces[i], interfacesFound);
                }
            }

            cls = cls.getSuperclass();
        }

    }

    public static List convertClassNamesToClasses(List classNames) {
        if (classNames == null) {
            return null;
        } else {
            ArrayList classes = new ArrayList(classNames.size());
            Iterator it = classNames.iterator();

            while (it.hasNext()) {
                String className = (String) it.next();

                try {
                    classes.add(Class.forName(className));
                } catch (Exception exception) {
                    classes.add((Object) null);
                }
            }

            return classes;
        }
    }

    public static List convertClassesToClassNames(List classes) {
        if (classes == null) {
            return null;
        } else {
            ArrayList classNames = new ArrayList(classes.size());
            Iterator it = classes.iterator();

            while (it.hasNext()) {
                Class cls = (Class) it.next();

                if (cls == null) {
                    classNames.add((Object) null);
                } else {
                    classNames.add(cls.getName());
                }
            }

            return classNames;
        }
    }

    public static boolean isAssignable(Class[] classArray, Class[] toClassArray) {
        return isAssignable(classArray, toClassArray, false);
    }

    public static boolean isAssignable(Class[] classArray, Class[] toClassArray, boolean autoboxing) {
        if (!ArrayUtils.isSameLength((Object[]) classArray, (Object[]) toClassArray)) {
            return false;
        } else {
            if (classArray == null) {
                classArray = ArrayUtils.EMPTY_CLASS_ARRAY;
            }

            if (toClassArray == null) {
                toClassArray = ArrayUtils.EMPTY_CLASS_ARRAY;
            }

            for (int i = 0; i < classArray.length; ++i) {
                if (!isAssignable(classArray[i], toClassArray[i], autoboxing)) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isAssignable(Class cls, Class toClass) {
        return isAssignable(cls, toClass, false);
    }

    public static boolean isAssignable(Class cls, Class toClass, boolean autoboxing) {
        if (toClass == null) {
            return false;
        } else if (cls == null) {
            return !toClass.isPrimitive();
        } else {
            if (autoboxing) {
                if (cls.isPrimitive() && !toClass.isPrimitive()) {
                    cls = primitiveToWrapper(cls);
                    if (cls == null) {
                        return false;
                    }
                }

                if (toClass.isPrimitive() && !cls.isPrimitive()) {
                    cls = wrapperToPrimitive(cls);
                    if (cls == null) {
                        return false;
                    }
                }
            }

            return cls.equals(toClass) ? true : (cls.isPrimitive() ? (!toClass.isPrimitive() ? false : (Integer.TYPE.equals(cls) ? Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass) : (Long.TYPE.equals(cls) ? Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass) : (Boolean.TYPE.equals(cls) ? false : (Double.TYPE.equals(cls) ? false : (Float.TYPE.equals(cls) ? Double.TYPE.equals(toClass) : (Character.TYPE.equals(cls) ? Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass) : (Short.TYPE.equals(cls) ? Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass) : (!Byte.TYPE.equals(cls) ? false : Short.TYPE.equals(toClass) || Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass)))))))))) : toClass.isAssignableFrom(cls));
        }
    }

    public static Class primitiveToWrapper(Class cls) {
        Class convertedClass = cls;

        if (cls != null && cls.isPrimitive()) {
            convertedClass = (Class) ClassUtils.primitiveWrapperMap.get(cls);
        }

        return convertedClass;
    }

    public static Class[] primitivesToWrappers(Class[] classes) {
        if (classes == null) {
            return null;
        } else if (classes.length == 0) {
            return classes;
        } else {
            Class[] convertedClasses = new Class[classes.length];

            for (int i = 0; i < classes.length; ++i) {
                convertedClasses[i] = primitiveToWrapper(classes[i]);
            }

            return convertedClasses;
        }
    }

    public static Class wrapperToPrimitive(Class cls) {
        return (Class) ClassUtils.wrapperPrimitiveMap.get(cls);
    }

    public static Class[] wrappersToPrimitives(Class[] classes) {
        if (classes == null) {
            return null;
        } else if (classes.length == 0) {
            return classes;
        } else {
            Class[] convertedClasses = new Class[classes.length];

            for (int i = 0; i < classes.length; ++i) {
                convertedClasses[i] = wrapperToPrimitive(classes[i]);
            }

            return convertedClasses;
        }
    }

    public static boolean isInnerClass(Class cls) {
        return cls == null ? false : cls.getName().indexOf(36) >= 0;
    }

    public static Class getClass(ClassLoader classLoader, String className, boolean initialize) throws ClassNotFoundException {
        try {
            Class ex;

            if (ClassUtils.abbreviationMap.containsKey(className)) {
                String lastDotIndex1 = "[" + ClassUtils.abbreviationMap.get(className);

                ex = Class.forName(lastDotIndex1, initialize, classLoader).getComponentType();
            } else {
                ex = Class.forName(toCanonicalName(className), initialize, classLoader);
            }

            return ex;
        } catch (ClassNotFoundException classnotfoundexception) {
            int lastDotIndex = className.lastIndexOf(46);

            if (lastDotIndex != -1) {
                try {
                    return getClass(classLoader, className.substring(0, lastDotIndex) + '$' + className.substring(lastDotIndex + 1), initialize);
                } catch (ClassNotFoundException classnotfoundexception1) {
                    ;
                }
            }

            throw classnotfoundexception;
        }
    }

    public static Class getClass(ClassLoader classLoader, String className) throws ClassNotFoundException {
        return getClass(classLoader, className, true);
    }

    public static Class getClass(String className) throws ClassNotFoundException {
        return getClass(className, true);
    }

    public static Class getClass(String className, boolean initialize) throws ClassNotFoundException {
        ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
        ClassLoader loader = contextCL == null ? (ClassUtils.class$org$apache$commons$lang$ClassUtils == null ? (ClassUtils.class$org$apache$commons$lang$ClassUtils = class$("org.apache.commons.lang.ClassUtils")) : ClassUtils.class$org$apache$commons$lang$ClassUtils).getClassLoader() : contextCL;

        return getClass(loader, className, initialize);
    }

    public static Method getPublicMethod(Class cls, String methodName, Class[] parameterTypes) throws SecurityException, NoSuchMethodException {
        Method declaredMethod = cls.getMethod(methodName, parameterTypes);

        if (Modifier.isPublic(declaredMethod.getDeclaringClass().getModifiers())) {
            return declaredMethod;
        } else {
            ArrayList candidateClasses = new ArrayList();

            candidateClasses.addAll(getAllInterfaces(cls));
            candidateClasses.addAll(getAllSuperclasses(cls));
            Iterator it = candidateClasses.iterator();

            while (it.hasNext()) {
                Class candidateClass = (Class) it.next();

                if (Modifier.isPublic(candidateClass.getModifiers())) {
                    Method candidateMethod;

                    try {
                        candidateMethod = candidateClass.getMethod(methodName, parameterTypes);
                    } catch (NoSuchMethodException nosuchmethodexception) {
                        continue;
                    }

                    if (Modifier.isPublic(candidateMethod.getDeclaringClass().getModifiers())) {
                        return candidateMethod;
                    }
                }
            }

            throw new NoSuchMethodException("Can\'t find a public method for " + methodName + " " + ArrayUtils.toString(parameterTypes));
        }
    }

    private static String toCanonicalName(String className) {
        className = StringUtils.deleteWhitespace(className);
        if (className == null) {
            throw new NullArgumentException("className");
        } else {
            if (className.endsWith("[]")) {
                StrBuilder classNameBuffer = new StrBuilder();

                while (className.endsWith("[]")) {
                    className = className.substring(0, className.length() - 2);
                    classNameBuffer.append("[");
                }

                String abbreviation = (String) ClassUtils.abbreviationMap.get(className);

                if (abbreviation != null) {
                    classNameBuffer.append(abbreviation);
                } else {
                    classNameBuffer.append("L").append(className).append(";");
                }

                className = classNameBuffer.toString();
            }

            return className;
        }
    }

    public static Class[] toClass(Object[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return ArrayUtils.EMPTY_CLASS_ARRAY;
        } else {
            Class[] classes = new Class[array.length];

            for (int i = 0; i < array.length; ++i) {
                classes[i] = array[i] == null ? null : array[i].getClass();
            }

            return classes;
        }
    }

    public static String getShortCanonicalName(Object object, String valueIfNull) {
        return object == null ? valueIfNull : getShortCanonicalName(object.getClass().getName());
    }

    public static String getShortCanonicalName(Class cls) {
        return cls == null ? "" : getShortCanonicalName(cls.getName());
    }

    public static String getShortCanonicalName(String canonicalName) {
        return getShortClassName(getCanonicalName(canonicalName));
    }

    public static String getPackageCanonicalName(Object object, String valueIfNull) {
        return object == null ? valueIfNull : getPackageCanonicalName(object.getClass().getName());
    }

    public static String getPackageCanonicalName(Class cls) {
        return cls == null ? "" : getPackageCanonicalName(cls.getName());
    }

    public static String getPackageCanonicalName(String canonicalName) {
        return getPackageName(getCanonicalName(canonicalName));
    }

    private static String getCanonicalName(String className) {
        className = StringUtils.deleteWhitespace(className);
        if (className == null) {
            return null;
        } else {
            int dim;

            for (dim = 0; className.startsWith("["); className = className.substring(1)) {
                ++dim;
            }

            if (dim < 1) {
                return className;
            } else {
                if (className.startsWith("L")) {
                    className = className.substring(1, className.endsWith(";") ? className.length() - 1 : className.length());
                } else if (className.length() > 0) {
                    className = (String) ClassUtils.reverseAbbreviationMap.get(className.substring(0, 1));
                }

                StrBuilder canonicalClassNameBuffer = new StrBuilder(className);

                for (int i = 0; i < dim; ++i) {
                    canonicalClassNameBuffer.append("[]");
                }

                return canonicalClassNameBuffer.toString();
            }
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException classnotfoundexception) {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    static {
        ClassUtils.primitiveWrapperMap.put(Boolean.TYPE, ClassUtils.class$java$lang$Boolean == null ? (ClassUtils.class$java$lang$Boolean = class$("java.lang.Boolean")) : ClassUtils.class$java$lang$Boolean);
        ClassUtils.primitiveWrapperMap.put(Byte.TYPE, ClassUtils.class$java$lang$Byte == null ? (ClassUtils.class$java$lang$Byte = class$("java.lang.Byte")) : ClassUtils.class$java$lang$Byte);
        ClassUtils.primitiveWrapperMap.put(Character.TYPE, ClassUtils.class$java$lang$Character == null ? (ClassUtils.class$java$lang$Character = class$("java.lang.Character")) : ClassUtils.class$java$lang$Character);
        ClassUtils.primitiveWrapperMap.put(Short.TYPE, ClassUtils.class$java$lang$Short == null ? (ClassUtils.class$java$lang$Short = class$("java.lang.Short")) : ClassUtils.class$java$lang$Short);
        ClassUtils.primitiveWrapperMap.put(Integer.TYPE, ClassUtils.class$java$lang$Integer == null ? (ClassUtils.class$java$lang$Integer = class$("java.lang.Integer")) : ClassUtils.class$java$lang$Integer);
        ClassUtils.primitiveWrapperMap.put(Long.TYPE, ClassUtils.class$java$lang$Long == null ? (ClassUtils.class$java$lang$Long = class$("java.lang.Long")) : ClassUtils.class$java$lang$Long);
        ClassUtils.primitiveWrapperMap.put(Double.TYPE, ClassUtils.class$java$lang$Double == null ? (ClassUtils.class$java$lang$Double = class$("java.lang.Double")) : ClassUtils.class$java$lang$Double);
        ClassUtils.primitiveWrapperMap.put(Float.TYPE, ClassUtils.class$java$lang$Float == null ? (ClassUtils.class$java$lang$Float = class$("java.lang.Float")) : ClassUtils.class$java$lang$Float);
        ClassUtils.primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
        wrapperPrimitiveMap = new HashMap();
        Iterator it = ClassUtils.primitiveWrapperMap.keySet().iterator();

        while (it.hasNext()) {
            Class primitiveClass = (Class) it.next();
            Class wrapperClass = (Class) ClassUtils.primitiveWrapperMap.get(primitiveClass);

            if (!primitiveClass.equals(wrapperClass)) {
                ClassUtils.wrapperPrimitiveMap.put(wrapperClass, primitiveClass);
            }
        }

        abbreviationMap = new HashMap();
        reverseAbbreviationMap = new HashMap();
        addAbbreviation("int", "I");
        addAbbreviation("boolean", "Z");
        addAbbreviation("float", "F");
        addAbbreviation("long", "J");
        addAbbreviation("short", "S");
        addAbbreviation("byte", "B");
        addAbbreviation("double", "D");
        addAbbreviation("char", "C");
    }
}
