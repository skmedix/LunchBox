package org.apache.commons.lang3.event;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.reflect.MethodUtils;

public class EventUtils {

    public static void addEventListener(Object object, Class oclass, Object object1) {
        try {
            MethodUtils.invokeMethod(object, "add" + oclass.getSimpleName(), new Object[] { object1});
        } catch (NoSuchMethodException nosuchmethodexception) {
            throw new IllegalArgumentException("Class " + object.getClass().getName() + " does not have a public add" + oclass.getSimpleName() + " method which takes a parameter of type " + oclass.getName() + ".");
        } catch (IllegalAccessException illegalaccessexception) {
            throw new IllegalArgumentException("Class " + object.getClass().getName() + " does not have an accessible add" + oclass.getSimpleName() + " method which takes a parameter of type " + oclass.getName() + ".");
        } catch (InvocationTargetException invocationtargetexception) {
            throw new RuntimeException("Unable to add listener.", invocationtargetexception.getCause());
        }
    }

    public static void bindEventsToMethod(Object object, String s, Object object1, Class oclass, String... astring) {
        Object object2 = oclass.cast(Proxy.newProxyInstance(object.getClass().getClassLoader(), new Class[] { oclass}, new EventUtils.EventBindingInvocationHandler(object, s, astring)));

        addEventListener(object1, oclass, object2);
    }

    private static class EventBindingInvocationHandler implements InvocationHandler {

        private final Object target;
        private final String methodName;
        private final Set eventTypes;

        EventBindingInvocationHandler(Object object, String s, String[] astring) {
            this.target = object;
            this.methodName = s;
            this.eventTypes = new HashSet(Arrays.asList(astring));
        }

        public Object invoke(Object object, Method method, Object[] aobject) throws Throwable {
            return !this.eventTypes.isEmpty() && !this.eventTypes.contains(method.getName()) ? null : (this.hasMatchingParametersMethod(method) ? MethodUtils.invokeMethod(this.target, this.methodName, aobject) : MethodUtils.invokeMethod(this.target, this.methodName, new Object[0]));
        }

        private boolean hasMatchingParametersMethod(Method method) {
            return MethodUtils.getAccessibleMethod(this.target.getClass(), this.methodName, method.getParameterTypes()) != null;
        }
    }
}
