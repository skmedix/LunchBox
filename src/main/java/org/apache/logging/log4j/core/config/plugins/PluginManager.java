package org.apache.logging.log4j.core.config.plugins;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.helpers.Closer;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.status.StatusLogger;

public class PluginManager {

    private static final long NANOS_PER_SECOND = 1000000000L;
    private static ConcurrentMap pluginTypeMap = new ConcurrentHashMap();
    private static final CopyOnWriteArrayList PACKAGES = new CopyOnWriteArrayList();
    private static final String PATH = "org/apache/logging/log4j/core/config/plugins/";
    private static final String FILENAME = "Log4j2Plugins.dat";
    private static final String LOG4J_PACKAGES = "org.apache.logging.log4j.core";
    private static final Logger LOGGER = StatusLogger.getLogger();
    private static String rootDir;
    private Map plugins = new HashMap();
    private final String type;
    private final Class clazz;

    public PluginManager(String s) {
        this.type = s;
        this.clazz = null;
    }

    public PluginManager(String s, Class oclass) {
        this.type = s;
        this.clazz = oclass;
    }

    public static void main(String[] astring) throws Exception {
        if (astring == null || astring.length < 1) {
            System.err.println("A target directory must be specified");
            System.exit(-1);
        }

        PluginManager.rootDir = !astring[0].endsWith("/") && !astring[0].endsWith("\\") ? astring[0] + "/" : astring[0];
        PluginManager pluginmanager = new PluginManager("Core");
        String s = astring.length == 2 ? astring[1] : null;

        pluginmanager.collectPlugins(false, s);
        encode(PluginManager.pluginTypeMap);
    }

    public static void addPackage(String s) {
        if (PluginManager.PACKAGES.addIfAbsent(s)) {
            PluginManager.pluginTypeMap.clear();
        }

    }

    public PluginType getPluginType(String s) {
        return (PluginType) this.plugins.get(s.toLowerCase());
    }

    public Map getPlugins() {
        return this.plugins;
    }

    public void collectPlugins() {
        this.collectPlugins(true, (String) null);
    }

    public void collectPlugins(boolean flag, String s) {
        if (PluginManager.pluginTypeMap.containsKey(this.type)) {
            this.plugins = (Map) PluginManager.pluginTypeMap.get(this.type);
            flag = false;
        }

        long i = System.nanoTime();
        ResolverUtil resolverutil = new ResolverUtil();
        ClassLoader classloader = Loader.getClassLoader();

        if (classloader != null) {
            resolverutil.setClassLoader(classloader);
        }

        if (flag) {
            ConcurrentMap concurrentmap = decode(classloader);

            if (concurrentmap != null) {
                PluginManager.pluginTypeMap = concurrentmap;
                this.plugins = (Map) concurrentmap.get(this.type);
            } else {
                PluginManager.LOGGER.warn("Plugin preloads not available from class loader {}", new Object[] { classloader});
            }
        }

        String s1;

        if (this.plugins == null || this.plugins.size() == 0) {
            if (s == null) {
                if (!PluginManager.PACKAGES.contains("org.apache.logging.log4j.core")) {
                    PluginManager.PACKAGES.add("org.apache.logging.log4j.core");
                }
            } else {
                String[] astring = s.split(",");
                String[] astring1 = astring;
                int j = astring.length;

                for (int k = 0; k < j; ++k) {
                    s1 = astring1[k];
                    PluginManager.PACKAGES.add(s1);
                }
            }
        }

        PluginManager.PluginTest pluginmanager_plugintest = new PluginManager.PluginTest(this.clazz);
        Iterator iterator = PluginManager.PACKAGES.iterator();

        while (iterator.hasNext()) {
            String s2 = (String) iterator.next();

            resolverutil.findInPackage(pluginmanager_plugintest, s2);
        }

        iterator = resolverutil.getClasses().iterator();

        while (iterator.hasNext()) {
            Class oclass = (Class) iterator.next();
            Plugin plugin = (Plugin) oclass.getAnnotation(Plugin.class);

            s1 = plugin.category();
            if (!PluginManager.pluginTypeMap.containsKey(s1)) {
                PluginManager.pluginTypeMap.putIfAbsent(s1, new ConcurrentHashMap());
            }

            Map map = (Map) PluginManager.pluginTypeMap.get(s1);
            String s3 = plugin.elementType().equals("") ? plugin.name() : plugin.elementType();
            PluginType plugintype = new PluginType(oclass, s3, plugin.printObject(), plugin.deferChildren());

            map.put(plugin.name().toLowerCase(), plugintype);
            PluginAliases pluginaliases = (PluginAliases) oclass.getAnnotation(PluginAliases.class);

            if (pluginaliases != null) {
                String[] astring2 = pluginaliases.value();
                int l = astring2.length;

                for (int i1 = 0; i1 < l; ++i1) {
                    String s4 = astring2[i1];

                    s3 = plugin.elementType().equals("") ? s4 : plugin.elementType();
                    plugintype = new PluginType(oclass, s3, plugin.printObject(), plugin.deferChildren());
                    map.put(s4.trim().toLowerCase(), plugintype);
                }
            }
        }

        long j1 = System.nanoTime() - i;

        this.plugins = (Map) PluginManager.pluginTypeMap.get(this.type);
        StringBuilder stringbuilder = new StringBuilder("Generated plugins");

        stringbuilder.append(" in ");
        DecimalFormat decimalformat = new DecimalFormat("#0");
        long k1 = j1 / 1000000000L;

        j1 %= 1000000000L;
        stringbuilder.append(decimalformat.format(k1)).append('.');
        decimalformat = new DecimalFormat("000000000");
        stringbuilder.append(decimalformat.format(j1)).append(" seconds");
        PluginManager.LOGGER.debug(stringbuilder.toString());
    }

    private static ConcurrentMap decode(ClassLoader classloader) {
        Enumeration enumeration;

        try {
            enumeration = classloader.getResources("org/apache/logging/log4j/core/config/plugins/Log4j2Plugins.dat");
        } catch (IOException ioexception) {
            PluginManager.LOGGER.warn("Unable to preload plugins", (Throwable) ioexception);
            return null;
        }

        ConcurrentHashMap concurrenthashmap = new ConcurrentHashMap();

        label113:
        while (enumeration.hasMoreElements()) {
            DataInputStream datainputstream = null;

            InputStream inputstream;

            try {
                URL url = (URL) enumeration.nextElement();

                PluginManager.LOGGER.debug("Found Plugin Map at {}", new Object[] { url.toExternalForm()});
                inputstream = url.openStream();
                BufferedInputStream bufferedinputstream = new BufferedInputStream(inputstream);

                datainputstream = new DataInputStream(bufferedinputstream);
                int i = datainputstream.readInt();
                int j = 0;

                while (true) {
                    if (j >= i) {
                        continue label113;
                    }

                    String s = datainputstream.readUTF();
                    int k = datainputstream.readInt();
                    Object object = (ConcurrentMap) concurrenthashmap.get(s);

                    if (object == null) {
                        object = new ConcurrentHashMap(i);
                    }

                    for (int l = 0; l < k; ++l) {
                        String s1 = datainputstream.readUTF();
                        String s2 = datainputstream.readUTF();
                        String s3 = datainputstream.readUTF();
                        boolean flag = datainputstream.readBoolean();
                        boolean flag1 = datainputstream.readBoolean();
                        Class oclass = Class.forName(s2);

                        ((ConcurrentMap) object).put(s1, new PluginType(oclass, s3, flag, flag1));
                    }

                    concurrenthashmap.putIfAbsent(s, object);
                    ++j;
                }
            } catch (Exception exception) {
                PluginManager.LOGGER.warn("Unable to preload plugins", (Throwable) exception);
                inputstream = null;
            } finally {
                Closer.closeSilent((Closeable) datainputstream);
            }

            return inputstream;
        }

        return concurrenthashmap.size() == 0 ? null : concurrenthashmap;
    }

    private static void encode(ConcurrentMap concurrentmap) {
        String s = PluginManager.rootDir + "org/apache/logging/log4j/core/config/plugins/" + "Log4j2Plugins.dat";
        DataOutputStream dataoutputstream = null;

        try {
            File file = new File(PluginManager.rootDir + "org/apache/logging/log4j/core/config/plugins/");

            file.mkdirs();
            FileOutputStream fileoutputstream = new FileOutputStream(s);
            BufferedOutputStream bufferedoutputstream = new BufferedOutputStream(fileoutputstream);

            dataoutputstream = new DataOutputStream(bufferedoutputstream);
            dataoutputstream.writeInt(concurrentmap.size());
            Iterator iterator = concurrentmap.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();

                dataoutputstream.writeUTF((String) entry.getKey());
                dataoutputstream.writeInt(((ConcurrentMap) entry.getValue()).size());
                Iterator iterator1 = ((ConcurrentMap) entry.getValue()).entrySet().iterator();

                while (iterator1.hasNext()) {
                    Entry entry1 = (Entry) iterator1.next();

                    dataoutputstream.writeUTF((String) entry1.getKey());
                    PluginType plugintype = (PluginType) entry1.getValue();

                    dataoutputstream.writeUTF(plugintype.getPluginClass().getName());
                    dataoutputstream.writeUTF(plugintype.getElementName());
                    dataoutputstream.writeBoolean(plugintype.isObjectPrintable());
                    dataoutputstream.writeBoolean(plugintype.isDeferChildren());
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            Closer.closeSilent((Closeable) dataoutputstream);
        }

    }

    public static class PluginTest extends ResolverUtil.ClassTest {

        private final Class isA;

        public PluginTest(Class oclass) {
            this.isA = oclass;
        }

        public boolean matches(Class oclass) {
            return oclass != null && oclass.isAnnotationPresent(Plugin.class) && (this.isA == null || this.isA.isAssignableFrom(oclass));
        }

        public String toString() {
            StringBuilder stringbuilder = new StringBuilder("annotated with @" + Plugin.class.getSimpleName());

            if (this.isA != null) {
                stringbuilder.append(" is assignable to " + this.isA.getSimpleName());
            }

            return stringbuilder.toString();
        }
    }
}
