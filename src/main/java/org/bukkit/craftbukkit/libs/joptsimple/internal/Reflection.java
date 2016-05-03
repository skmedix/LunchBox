package org.bukkit.craftbukkit.libs.joptsimple.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.bukkit.craftbukkit.libs.joptsimple.ValueConverter;

public final class Reflection {

    public static ValueConverter findConverter(Class clazz) {
        ValueConverter valueOf = valueOfConverter(clazz);

        if (valueOf != null) {
            return valueOf;
        } else {
            ValueConverter constructor = constructorConverter(clazz);

            if (constructor != null) {
                return constructor;
            } else {
                throw new IllegalArgumentException(clazz + " is not a value type");
            }
        }
    }

    private static ValueConverter valueOfConverter(Class clazz) {
        try {
            Method ignored = clazz.getDeclaredMethod("valueOf", new Class[] { String.class});

            return !meetsConverterRequirements(ignored, clazz) ? null : new MethodInvokingValueConverter(ignored, clazz);
        } catch (NoSuchMethodException nosuchmethodexception) {
            return null;
        }
    }

    private static ValueConverter constructorConverter(Class clazz) {
        try {
            return new ConstructorInvokingValueConverter(clazz.getConstructor(new Class[] { String.class}));
        } catch (NoSuchMethodException nosuchmethodexception) {
            return null;
        }
    }

    public static Object instantiate(Constructor constructor, Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (Exception exception) {
            throw reflectionException(exception);
        }
    }

    public static Object invoke(Method method, Object... args) {
        try {
            return method.invoke((Object) null, args);
        } catch (Exception exception) {
            throw reflectionException(exception);
        }
    }

    private static boolean meetsConverterRequirements(Method method, Class expectedReturnType) {
        int modifiers = method.getModifiers();

        return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && expectedReturnType.equals(method.getReturnType());
    }

    private static RuntimeException reflectionException(Exception ex) {
        return (RuntimeException) (ex instanceof IllegalArgumentException ? new ReflectionException(ex) : (ex instanceof InvocationTargetException ? new ReflectionException(ex.getCause()) : (ex instanceof RuntimeException ? (RuntimeException) ex : new ReflectionException(ex))));
    }

    static {
        new Reflection();
    }
}
