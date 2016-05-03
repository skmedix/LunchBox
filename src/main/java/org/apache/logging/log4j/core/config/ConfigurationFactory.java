package org.apache.logging.log4j.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.PluginManager;
import org.apache.logging.log4j.core.config.plugins.PluginType;
import org.apache.logging.log4j.core.helpers.FileUtils;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.core.lookup.Interpolator;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public abstract class ConfigurationFactory {

    public static final String CONFIGURATION_FACTORY_PROPERTY = "log4j.configurationFactory";
    public static final String CONFIGURATION_FILE_PROPERTY = "log4j.configurationFile";
    protected static final Logger LOGGER = StatusLogger.getLogger();
    protected static final String TEST_PREFIX = "log4j2-test";
    protected static final String DEFAULT_PREFIX = "log4j2";
    private static final String CLASS_LOADER_SCHEME = "classloader";
    private static final int CLASS_LOADER_SCHEME_LENGTH = "classloader".length() + 1;
    private static final String CLASS_PATH_SCHEME = "classpath";
    private static final int CLASS_PATH_SCHEME_LENGTH = "classpath".length() + 1;
    private static volatile List factories = null;
    private static ConfigurationFactory configFactory = new ConfigurationFactory.Factory((ConfigurationFactory.SyntheticClass_1) null);
    protected final StrSubstitutor substitutor = new StrSubstitutor(new Interpolator());

    public static ConfigurationFactory getInstance() {
        if (ConfigurationFactory.factories == null) {
            String s = "log4j2-test";

            synchronized ("log4j2-test") {
                if (ConfigurationFactory.factories == null) {
                    ArrayList arraylist = new ArrayList();
                    String s1 = PropertiesUtil.getProperties().getStringProperty("log4j.configurationFactory");

                    if (s1 != null) {
                        addFactory(arraylist, s1);
                    }

                    PluginManager pluginmanager = new PluginManager("ConfigurationFactory");

                    pluginmanager.collectPlugins();
                    Map map = pluginmanager.getPlugins();
                    TreeSet treeset = new TreeSet();
                    Iterator iterator = map.values().iterator();

                    while (iterator.hasNext()) {
                        PluginType plugintype = (PluginType) iterator.next();

                        try {
                            Class oclass = plugintype.getPluginClass();
                            Order order = (Order) oclass.getAnnotation(Order.class);

                            if (order != null) {
                                int i = order.value();

                                treeset.add(new ConfigurationFactory.WeightedFactory(i, oclass));
                            }
                        } catch (Exception exception) {
                            ConfigurationFactory.LOGGER.warn("Unable to add class " + plugintype.getPluginClass());
                        }
                    }

                    iterator = treeset.iterator();

                    while (iterator.hasNext()) {
                        ConfigurationFactory.WeightedFactory configurationfactory_weightedfactory = (ConfigurationFactory.WeightedFactory) iterator.next();

                        addFactory(arraylist, configurationfactory_weightedfactory.factoryClass);
                    }

                    ConfigurationFactory.factories = Collections.unmodifiableList(arraylist);
                }
            }
        }

        return ConfigurationFactory.configFactory;
    }

    private static void addFactory(List list, String s) {
        try {
            addFactory(list, Class.forName(s));
        } catch (ClassNotFoundException classnotfoundexception) {
            ConfigurationFactory.LOGGER.error("Unable to load class " + s, (Throwable) classnotfoundexception);
        } catch (Exception exception) {
            ConfigurationFactory.LOGGER.error("Unable to load class " + s, (Throwable) exception);
        }

    }

    private static void addFactory(List list, Class oclass) {
        try {
            list.add(oclass.newInstance());
        } catch (Exception exception) {
            ConfigurationFactory.LOGGER.error("Unable to create instance of " + oclass.getName(), (Throwable) exception);
        }

    }

    public static void setConfigurationFactory(ConfigurationFactory configurationfactory) {
        ConfigurationFactory.configFactory = configurationfactory;
    }

    public static void resetConfigurationFactory() {
        ConfigurationFactory.configFactory = new ConfigurationFactory.Factory((ConfigurationFactory.SyntheticClass_1) null);
    }

    public static void removeConfigurationFactory(ConfigurationFactory configurationfactory) {
        if (ConfigurationFactory.configFactory == configurationfactory) {
            ConfigurationFactory.configFactory = new ConfigurationFactory.Factory((ConfigurationFactory.SyntheticClass_1) null);
        }

    }

    protected abstract String[] getSupportedTypes();

    protected boolean isActive() {
        return true;
    }

    public abstract Configuration getConfiguration(ConfigurationFactory.ConfigurationSource configurationfactory_configurationsource);

    public Configuration getConfiguration(String s, URI uri) {
        if (!this.isActive()) {
            return null;
        } else {
            if (uri != null) {
                ConfigurationFactory.ConfigurationSource configurationfactory_configurationsource = this.getInputFromURI(uri);

                if (configurationfactory_configurationsource != null) {
                    return this.getConfiguration(configurationfactory_configurationsource);
                }
            }

            return null;
        }
    }

    protected ConfigurationFactory.ConfigurationSource getInputFromURI(URI uri) {
        File file = FileUtils.fileFromURI(uri);

        if (file != null && file.exists() && file.canRead()) {
            try {
                return new ConfigurationFactory.ConfigurationSource(new FileInputStream(file), file);
            } catch (FileNotFoundException filenotfoundexception) {
                ConfigurationFactory.LOGGER.error("Cannot locate file " + uri.getPath(), (Throwable) filenotfoundexception);
            }
        }

        String s = uri.getScheme();
        boolean flag = s != null && s.equals("classloader");
        boolean flag1 = s != null && !flag && s.equals("classpath");

        if (s == null || flag || flag1) {
            ClassLoader classloader = this.getClass().getClassLoader();
            String s1;

            if (flag) {
                s1 = uri.toString().substring(ConfigurationFactory.CLASS_LOADER_SCHEME_LENGTH);
            } else if (flag1) {
                s1 = uri.toString().substring(ConfigurationFactory.CLASS_PATH_SCHEME_LENGTH);
            } else {
                s1 = uri.getPath();
            }

            ConfigurationFactory.ConfigurationSource configurationfactory_configurationsource = this.getInputFromResource(s1, classloader);

            if (configurationfactory_configurationsource != null) {
                return configurationfactory_configurationsource;
            }
        }

        try {
            return new ConfigurationFactory.ConfigurationSource(uri.toURL().openStream(), uri.getPath());
        } catch (MalformedURLException malformedurlexception) {
            ConfigurationFactory.LOGGER.error("Invalid URL " + uri.toString(), (Throwable) malformedurlexception);
        } catch (IOException ioexception) {
            ConfigurationFactory.LOGGER.error("Unable to access " + uri.toString(), (Throwable) ioexception);
        } catch (Exception exception) {
            ConfigurationFactory.LOGGER.error("Unable to access " + uri.toString(), (Throwable) exception);
        }

        return null;
    }

    protected ConfigurationFactory.ConfigurationSource getInputFromString(String s, ClassLoader classloader) {
        try {
            URL url = new URL(s);

            return new ConfigurationFactory.ConfigurationSource(url.openStream(), FileUtils.fileFromURI(url.toURI()));
        } catch (Exception exception) {
            ConfigurationFactory.ConfigurationSource configurationfactory_configurationsource = this.getInputFromResource(s, classloader);

            if (configurationfactory_configurationsource == null) {
                try {
                    File file = new File(s);

                    return new ConfigurationFactory.ConfigurationSource(new FileInputStream(file), file);
                } catch (FileNotFoundException filenotfoundexception) {
                    ;
                }
            }

            return configurationfactory_configurationsource;
        }
    }

    protected ConfigurationFactory.ConfigurationSource getInputFromResource(String s, ClassLoader classloader) {
        URL url = Loader.getResource(s, classloader);

        if (url == null) {
            return null;
        } else {
            InputStream inputstream = null;

            try {
                inputstream = url.openStream();
            } catch (IOException ioexception) {
                return null;
            }

            if (inputstream == null) {
                return null;
            } else {
                if (FileUtils.isFile(url)) {
                    try {
                        return new ConfigurationFactory.ConfigurationSource(inputstream, FileUtils.fileFromURI(url.toURI()));
                    } catch (URISyntaxException urisyntaxexception) {
                        ;
                    }
                }

                return new ConfigurationFactory.ConfigurationSource(inputstream, s);
            }
        }
    }

    static class SyntheticClass_1 {    }

    public static class ConfigurationSource {

        private File file;
        private String location;
        private InputStream stream;

        public ConfigurationSource() {}

        public ConfigurationSource(InputStream inputstream) {
            this.stream = inputstream;
            this.file = null;
            this.location = null;
        }

        public ConfigurationSource(InputStream inputstream, File file) {
            this.stream = inputstream;
            this.file = file;
            this.location = file.getAbsolutePath();
        }

        public ConfigurationSource(InputStream inputstream, String s) {
            this.stream = inputstream;
            this.location = s;
            this.file = null;
        }

        public File getFile() {
            return this.file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public String getLocation() {
            return this.location;
        }

        public void setLocation(String s) {
            this.location = s;
        }

        public InputStream getInputStream() {
            return this.stream;
        }

        public void setInputStream(InputStream inputstream) {
            this.stream = inputstream;
        }
    }

    private static class Factory extends ConfigurationFactory {

        private Factory() {}

        public Configuration getConfiguration(String s, URI uri) {
            if (uri == null) {
                String s1 = this.substitutor.replace(PropertiesUtil.getProperties().getStringProperty("log4j.configurationFile"));

                if (s1 != null) {
                    ConfigurationFactory.ConfigurationSource configurationfactory_configurationsource = null;

                    try {
                        configurationfactory_configurationsource = this.getInputFromURI(new URI(s1));
                    } catch (Exception exception) {
                        ;
                    }

                    if (configurationfactory_configurationsource == null) {
                        ClassLoader classloader = this.getClass().getClassLoader();

                        configurationfactory_configurationsource = this.getInputFromString(s1, classloader);
                    }

                    if (configurationfactory_configurationsource != null) {
                        Iterator iterator = ConfigurationFactory.factories.iterator();

                        while (iterator.hasNext()) {
                            ConfigurationFactory configurationfactory = (ConfigurationFactory) iterator.next();
                            String[] astring = configurationfactory.getSupportedTypes();

                            if (astring != null) {
                                String[] astring1 = astring;
                                int i = astring.length;

                                for (int j = 0; j < i; ++j) {
                                    String s2 = astring1[j];

                                    if (s2.equals("*") || s1.endsWith(s2)) {
                                        Configuration configuration = configurationfactory.getConfiguration(configurationfactory_configurationsource);

                                        if (configuration != null) {
                                            return configuration;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Iterator iterator1 = ConfigurationFactory.factories.iterator();

                while (iterator1.hasNext()) {
                    ConfigurationFactory configurationfactory1 = (ConfigurationFactory) iterator1.next();
                    String[] astring2 = configurationfactory1.getSupportedTypes();

                    if (astring2 != null) {
                        String[] astring3 = astring2;
                        int k = astring2.length;

                        for (int l = 0; l < k; ++l) {
                            String s3 = astring3[l];

                            if (s3.equals("*") || uri.toString().endsWith(s3)) {
                                Configuration configuration1 = configurationfactory1.getConfiguration(s, uri);

                                if (configuration1 != null) {
                                    return configuration1;
                                }
                            }
                        }
                    }
                }
            }

            Configuration configuration2 = this.getConfiguration(true, s);

            if (configuration2 == null) {
                configuration2 = this.getConfiguration(true, (String) null);
                if (configuration2 == null) {
                    configuration2 = this.getConfiguration(false, s);
                    if (configuration2 == null) {
                        configuration2 = this.getConfiguration(false, (String) null);
                    }
                }
            }

            return (Configuration) (configuration2 != null ? configuration2 : new DefaultConfiguration());
        }

        private Configuration getConfiguration(boolean flag, String s) {
            boolean flag1 = s != null && s.length() > 0;
            ClassLoader classloader = this.getClass().getClassLoader();
            Iterator iterator = ConfigurationFactory.factories.iterator();

            while (iterator.hasNext()) {
                ConfigurationFactory configurationfactory = (ConfigurationFactory) iterator.next();
                String s1 = flag ? "log4j2-test" : "log4j2";
                String[] astring = configurationfactory.getSupportedTypes();

                if (astring != null) {
                    String[] astring1 = astring;
                    int i = astring.length;

                    for (int j = 0; j < i; ++j) {
                        String s2 = astring1[j];

                        if (!s2.equals("*")) {
                            String s3 = flag1 ? s1 + s + s2 : s1 + s2;
                            ConfigurationFactory.ConfigurationSource configurationfactory_configurationsource = this.getInputFromResource(s3, classloader);

                            if (configurationfactory_configurationsource != null) {
                                return configurationfactory.getConfiguration(configurationfactory_configurationsource);
                            }
                        }
                    }
                }
            }

            return null;
        }

        public String[] getSupportedTypes() {
            return null;
        }

        public Configuration getConfiguration(ConfigurationFactory.ConfigurationSource configurationfactory_configurationsource) {
            if (configurationfactory_configurationsource != null) {
                String s = configurationfactory_configurationsource.getLocation();
                Iterator iterator = ConfigurationFactory.factories.iterator();

                while (iterator.hasNext()) {
                    ConfigurationFactory configurationfactory = (ConfigurationFactory) iterator.next();
                    String[] astring = configurationfactory.getSupportedTypes();

                    if (astring != null) {
                        String[] astring1 = astring;
                        int i = astring.length;

                        for (int j = 0; j < i; ++j) {
                            String s1 = astring1[j];

                            if (s1.equals("*") || s != null && s.endsWith(s1)) {
                                Configuration configuration = configurationfactory.getConfiguration(configurationfactory_configurationsource);

                                if (configuration != null) {
                                    return configuration;
                                }

                                ConfigurationFactory.Factory.LOGGER.error("Cannot determine the ConfigurationFactory to use for {}", new Object[] { s});
                                return null;
                            }
                        }
                    }
                }
            }

            ConfigurationFactory.Factory.LOGGER.error("Cannot process configuration, input source is null");
            return null;
        }

        Factory(ConfigurationFactory.SyntheticClass_1 configurationfactory_syntheticclass_1) {
            this();
        }
    }

    private static class WeightedFactory implements Comparable {

        private final int weight;
        private final Class factoryClass;

        public WeightedFactory(int i, Class oclass) {
            this.weight = i;
            this.factoryClass = oclass;
        }

        public int compareTo(ConfigurationFactory.WeightedFactory configurationfactory_weightedfactory) {
            int i = configurationfactory_weightedfactory.weight;

            return this.weight == i ? 0 : (this.weight > i ? -1 : 1);
        }
    }
}
