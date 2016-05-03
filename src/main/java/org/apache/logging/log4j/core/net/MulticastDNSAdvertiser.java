package org.apache.logging.log4j.core.net;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.helpers.Integers;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
    name = "multicastdns",
    category = "Core",
    elementType = "advertiser",
    printObject = false
)
public class MulticastDNSAdvertiser implements Advertiser {

    protected static final Logger LOGGER = StatusLogger.getLogger();
    private static Object jmDNS = initializeJMDNS();
    private static Class jmDNSClass;
    private static Class serviceInfoClass;

    public Object advertise(Map map) {
        HashMap hashmap = new HashMap();
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (((String) entry.getKey()).length() <= 255 && ((String) entry.getValue()).length() <= 255) {
                hashmap.put(entry.getKey(), entry.getValue());
            }
        }

        String s = (String) hashmap.get("protocol");
        String s1 = "._log4j._" + (s != null ? s : "tcp") + ".local.";
        String s2 = (String) hashmap.get("port");
        int i = Integers.parseInt(s2, 4555);
        String s3 = (String) hashmap.get("name");

        if (MulticastDNSAdvertiser.jmDNS != null) {
            boolean flag = false;

            try {
                MulticastDNSAdvertiser.jmDNSClass.getMethod("create", (Class[]) null);
                flag = true;
            } catch (NoSuchMethodException nosuchmethodexception) {
                ;
            }

            Object object;

            if (flag) {
                object = this.buildServiceInfoVersion3(s1, i, s3, hashmap);
            } else {
                object = this.buildServiceInfoVersion1(s1, i, s3, hashmap);
            }

            try {
                Method method = MulticastDNSAdvertiser.jmDNSClass.getMethod("registerService", new Class[] { MulticastDNSAdvertiser.serviceInfoClass});

                method.invoke(MulticastDNSAdvertiser.jmDNS, new Object[] { object});
            } catch (IllegalAccessException illegalaccessexception) {
                MulticastDNSAdvertiser.LOGGER.warn("Unable to invoke registerService method", (Throwable) illegalaccessexception);
            } catch (NoSuchMethodException nosuchmethodexception1) {
                MulticastDNSAdvertiser.LOGGER.warn("No registerService method", (Throwable) nosuchmethodexception1);
            } catch (InvocationTargetException invocationtargetexception) {
                MulticastDNSAdvertiser.LOGGER.warn("Unable to invoke registerService method", (Throwable) invocationtargetexception);
            }

            return object;
        } else {
            MulticastDNSAdvertiser.LOGGER.warn("JMDNS not available - will not advertise ZeroConf support");
            return null;
        }
    }

    public void unadvertise(Object object) {
        if (MulticastDNSAdvertiser.jmDNS != null) {
            try {
                Method method = MulticastDNSAdvertiser.jmDNSClass.getMethod("unregisterService", new Class[] { MulticastDNSAdvertiser.serviceInfoClass});

                method.invoke(MulticastDNSAdvertiser.jmDNS, new Object[] { object});
            } catch (IllegalAccessException illegalaccessexception) {
                MulticastDNSAdvertiser.LOGGER.warn("Unable to invoke unregisterService method", (Throwable) illegalaccessexception);
            } catch (NoSuchMethodException nosuchmethodexception) {
                MulticastDNSAdvertiser.LOGGER.warn("No unregisterService method", (Throwable) nosuchmethodexception);
            } catch (InvocationTargetException invocationtargetexception) {
                MulticastDNSAdvertiser.LOGGER.warn("Unable to invoke unregisterService method", (Throwable) invocationtargetexception);
            }
        }

    }

    private static Object createJmDNSVersion1() {
        try {
            return MulticastDNSAdvertiser.jmDNSClass.newInstance();
        } catch (InstantiationException instantiationexception) {
            MulticastDNSAdvertiser.LOGGER.warn("Unable to instantiate JMDNS", (Throwable) instantiationexception);
        } catch (IllegalAccessException illegalaccessexception) {
            MulticastDNSAdvertiser.LOGGER.warn("Unable to instantiate JMDNS", (Throwable) illegalaccessexception);
        }

        return null;
    }

    private static Object createJmDNSVersion3() {
        try {
            Method method = MulticastDNSAdvertiser.jmDNSClass.getMethod("create", (Class[]) null);

            return method.invoke((Object) null, (Object[]) null);
        } catch (IllegalAccessException illegalaccessexception) {
            MulticastDNSAdvertiser.LOGGER.warn("Unable to instantiate jmdns class", (Throwable) illegalaccessexception);
        } catch (NoSuchMethodException nosuchmethodexception) {
            MulticastDNSAdvertiser.LOGGER.warn("Unable to access constructor", (Throwable) nosuchmethodexception);
        } catch (InvocationTargetException invocationtargetexception) {
            MulticastDNSAdvertiser.LOGGER.warn("Unable to call constructor", (Throwable) invocationtargetexception);
        }

        return null;
    }

    private Object buildServiceInfoVersion1(String s, int i, String s1, Map map) {
        Hashtable hashtable = new Hashtable(map);

        try {
            Class[] aclass = new Class[] { String.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Hashtable.class};
            Constructor constructor = MulticastDNSAdvertiser.serviceInfoClass.getConstructor(aclass);
            Object[] aobject = new Object[] { s, s1, Integer.valueOf(i), Integer.valueOf(0), Integer.valueOf(0), hashtable};

            return constructor.newInstance(aobject);
        } catch (IllegalAccessException illegalaccessexception) {
            MulticastDNSAdvertiser.LOGGER.warn("Unable to construct ServiceInfo instance", (Throwable) illegalaccessexception);
        } catch (NoSuchMethodException nosuchmethodexception) {
            MulticastDNSAdvertiser.LOGGER.warn("Unable to get ServiceInfo constructor", (Throwable) nosuchmethodexception);
        } catch (InstantiationException instantiationexception) {
            MulticastDNSAdvertiser.LOGGER.warn("Unable to construct ServiceInfo instance", (Throwable) instantiationexception);
        } catch (InvocationTargetException invocationtargetexception) {
            MulticastDNSAdvertiser.LOGGER.warn("Unable to construct ServiceInfo instance", (Throwable) invocationtargetexception);
        }

        return null;
    }

    private Object buildServiceInfoVersion3(String s, int i, String s1, Map map) {
        try {
            Class[] aclass = new Class[] { String.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Map.class};
            Method method = MulticastDNSAdvertiser.serviceInfoClass.getMethod("create", aclass);
            Object[] aobject = new Object[] { s, s1, Integer.valueOf(i), Integer.valueOf(0), Integer.valueOf(0), map};

            return method.invoke((Object) null, aobject);
        } catch (IllegalAccessException illegalaccessexception) {
            MulticastDNSAdvertiser.LOGGER.warn("Unable to invoke create method", (Throwable) illegalaccessexception);
        } catch (NoSuchMethodException nosuchmethodexception) {
            MulticastDNSAdvertiser.LOGGER.warn("Unable to find create method", (Throwable) nosuchmethodexception);
        } catch (InvocationTargetException invocationtargetexception) {
            MulticastDNSAdvertiser.LOGGER.warn("Unable to invoke create method", (Throwable) invocationtargetexception);
        }

        return null;
    }

    private static Object initializeJMDNS() {
        try {
            MulticastDNSAdvertiser.jmDNSClass = Class.forName("javax.jmdns.JmDNS");
            MulticastDNSAdvertiser.serviceInfoClass = Class.forName("javax.jmdns.ServiceInfo");
            boolean flag = false;

            try {
                MulticastDNSAdvertiser.jmDNSClass.getMethod("create", (Class[]) null);
                flag = true;
            } catch (NoSuchMethodException nosuchmethodexception) {
                ;
            }

            if (flag) {
                return createJmDNSVersion3();
            }

            return createJmDNSVersion1();
        } catch (ClassNotFoundException classnotfoundexception) {
            MulticastDNSAdvertiser.LOGGER.warn("JmDNS or serviceInfo class not found", (Throwable) classnotfoundexception);
        } catch (ExceptionInInitializerError exceptionininitializererror) {
            MulticastDNSAdvertiser.LOGGER.warn("JmDNS or serviceInfo class not found", (Throwable) exceptionininitializererror);
        }

        return null;
    }
}
