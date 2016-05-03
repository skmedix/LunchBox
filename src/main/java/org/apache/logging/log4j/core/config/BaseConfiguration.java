package org.apache.logging.log4j.core.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginManager;
import org.apache.logging.log4j.core.config.plugins.PluginNode;
import org.apache.logging.log4j.core.config.plugins.PluginType;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.core.filter.AbstractFilterable;
import org.apache.logging.log4j.core.helpers.NameUtil;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.lookup.Interpolator;
import org.apache.logging.log4j.core.lookup.MapLookup;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public class BaseConfiguration extends AbstractFilterable implements Configuration {

    protected static final Logger LOGGER = StatusLogger.getLogger();
    protected Node rootNode;
    protected final List listeners = new CopyOnWriteArrayList();
    protected ConfigurationMonitor monitor = new DefaultConfigurationMonitor();
    private Advertiser advertiser = new DefaultAdvertiser();
    protected Map advertisedConfiguration;
    private Node advertiserNode = null;
    private Object advertisement;
    protected boolean isShutdownHookEnabled = true;
    private String name;
    private ConcurrentMap appenders = new ConcurrentHashMap();
    private ConcurrentMap loggers = new ConcurrentHashMap();
    private final StrLookup tempLookup = new Interpolator();
    private final StrSubstitutor subst;
    private LoggerConfig root;
    private final boolean started;
    private final ConcurrentMap componentMap;
    protected PluginManager pluginManager;

    protected BaseConfiguration() {
        this.subst = new StrSubstitutor(this.tempLookup);
        this.root = new LoggerConfig();
        this.started = false;
        this.componentMap = new ConcurrentHashMap();
        this.pluginManager = new PluginManager("Core");
        this.rootNode = new Node();
    }

    public Map getProperties() {
        return (Map) this.componentMap.get("ContextProperties");
    }

    public void start() {
        this.pluginManager.collectPlugins();
        this.setup();
        this.setupAdvertisement();
        this.doConfigure();
        Iterator iterator = this.loggers.values().iterator();

        while (iterator.hasNext()) {
            LoggerConfig loggerconfig = (LoggerConfig) iterator.next();

            loggerconfig.startFilter();
        }

        iterator = this.appenders.values().iterator();

        while (iterator.hasNext()) {
            Appender appender = (Appender) iterator.next();

            appender.start();
        }

        this.root.startFilter();
        this.startFilter();
    }

    public void stop() {
        Appender[] aappender = (Appender[]) this.appenders.values().toArray(new Appender[this.appenders.size()]);

        for (int i = aappender.length - 1; i >= 0; --i) {
            aappender[i].stop();
        }

        Iterator iterator = this.loggers.values().iterator();

        while (iterator.hasNext()) {
            LoggerConfig loggerconfig = (LoggerConfig) iterator.next();

            loggerconfig.clearAppenders();
            loggerconfig.stopFilter();
        }

        this.root.stopFilter();
        this.stopFilter();
        if (this.advertiser != null && this.advertisement != null) {
            this.advertiser.unadvertise(this.advertisement);
        }

    }

    public boolean isShutdownHookEnabled() {
        return this.isShutdownHookEnabled;
    }

    protected void setup() {}

    protected Level getDefaultStatus() {
        String s = PropertiesUtil.getProperties().getStringProperty("Log4jDefaultStatusLevel", Level.ERROR.name());

        try {
            return Level.toLevel(s);
        } catch (Exception exception) {
            return Level.ERROR;
        }
    }

    protected void createAdvertiser(String s, ConfigurationFactory.ConfigurationSource configurationfactory_configurationsource, byte[] abyte, String s1) {
        if (s != null) {
            Node node = new Node((Node) null, s, (PluginType) null);
            Map map = node.getAttributes();

            map.put("content", new String(abyte));
            map.put("contentType", s1);
            map.put("name", "configuration");
            if (configurationfactory_configurationsource.getLocation() != null) {
                map.put("location", configurationfactory_configurationsource.getLocation());
            }

            this.advertiserNode = node;
        }

    }

    private void setupAdvertisement() {
        if (this.advertiserNode != null) {
            String s = this.advertiserNode.getName();
            PluginType plugintype = this.pluginManager.getPluginType(s);

            if (plugintype != null) {
                Class oclass = plugintype.getPluginClass();

                try {
                    this.advertiser = (Advertiser) oclass.newInstance();
                    this.advertisement = this.advertiser.advertise(this.advertiserNode.getAttributes());
                } catch (InstantiationException instantiationexception) {
                    System.err.println("InstantiationException attempting to instantiate advertiser: " + s);
                } catch (IllegalAccessException illegalaccessexception) {
                    System.err.println("IllegalAccessException attempting to instantiate advertiser: " + s);
                }
            }
        }

    }

    public Object getComponent(String s) {
        return this.componentMap.get(s);
    }

    public void addComponent(String s, Object object) {
        this.componentMap.putIfAbsent(s, object);
    }

    protected void doConfigure() {
        boolean flag = false;
        boolean flag1 = false;
        Iterator iterator = this.rootNode.getChildren().iterator();

        while (iterator.hasNext()) {
            Node node = (Node) iterator.next();

            this.createConfiguration(node, (LogEvent) null);
            if (node.getObject() != null) {
                if (node.getName().equalsIgnoreCase("Properties")) {
                    if (this.tempLookup == this.subst.getVariableResolver()) {
                        this.subst.setVariableResolver((StrLookup) node.getObject());
                    } else {
                        BaseConfiguration.LOGGER.error("Properties declaration must be the first element in the configuration");
                    }
                } else {
                    if (this.tempLookup == this.subst.getVariableResolver()) {
                        Map map = (Map) this.componentMap.get("ContextProperties");
                        MapLookup maplookup = map == null ? null : new MapLookup(map);

                        this.subst.setVariableResolver(new Interpolator(maplookup));
                    }

                    if (node.getName().equalsIgnoreCase("Appenders")) {
                        this.appenders = (ConcurrentMap) node.getObject();
                    } else if (node.getObject() instanceof Filter) {
                        this.addFilter((Filter) node.getObject());
                    } else if (node.getName().equalsIgnoreCase("Loggers")) {
                        Loggers loggers = (Loggers) node.getObject();

                        this.loggers = loggers.getMap();
                        flag1 = true;
                        if (loggers.getRoot() != null) {
                            this.root = loggers.getRoot();
                            flag = true;
                        }
                    } else {
                        BaseConfiguration.LOGGER.error("Unknown object \"" + node.getName() + "\" of type " + node.getObject().getClass().getName() + " is ignored");
                    }
                }
            }
        }

        if (!flag1) {
            BaseConfiguration.LOGGER.warn("No Loggers were configured, using default. Is the Loggers element missing?");
            this.setToDefault();
        } else {
            if (!flag) {
                BaseConfiguration.LOGGER.warn("No Root logger was configured, creating default ERROR-level Root logger with Console appender");
                this.setToDefault();
            }

            iterator = this.loggers.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                LoggerConfig loggerconfig = (LoggerConfig) entry.getValue();
                Iterator iterator1 = loggerconfig.getAppenderRefs().iterator();

                while (iterator1.hasNext()) {
                    AppenderRef appenderref = (AppenderRef) iterator1.next();
                    Appender appender = (Appender) this.appenders.get(appenderref.getRef());

                    if (appender != null) {
                        loggerconfig.addAppender(appender, appenderref.getLevel(), appenderref.getFilter());
                    } else {
                        BaseConfiguration.LOGGER.error("Unable to locate appender " + appenderref.getRef() + " for logger " + loggerconfig.getName());
                    }
                }
            }

            this.setParents();
        }
    }

    private void setToDefault() {
        this.setName("Default");
        PatternLayout patternlayout = PatternLayout.createLayout("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n", (Configuration) null, (RegexReplacement) null, (String) null, (String) null);
        ConsoleAppender consoleappender = ConsoleAppender.createAppender(patternlayout, (Filter) null, "SYSTEM_OUT", "Console", "false", "true");

        consoleappender.start();
        this.addAppender(consoleappender);
        LoggerConfig loggerconfig = this.getRootLogger();

        loggerconfig.addAppender(consoleappender, (Level) null, (Filter) null);
        String s = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.level");
        Level level = s != null && Level.valueOf(s) != null ? Level.valueOf(s) : Level.ERROR;

        loggerconfig.setLevel(level);
    }

    public void setName(String s) {
        this.name = s;
    }

    public String getName() {
        return this.name;
    }

    public void addListener(ConfigurationListener configurationlistener) {
        this.listeners.add(configurationlistener);
    }

    public void removeListener(ConfigurationListener configurationlistener) {
        this.listeners.remove(configurationlistener);
    }

    public Appender getAppender(String s) {
        return (Appender) this.appenders.get(s);
    }

    public Map getAppenders() {
        return this.appenders;
    }

    public void addAppender(Appender appender) {
        this.appenders.put(appender.getName(), appender);
    }

    public StrSubstitutor getStrSubstitutor() {
        return this.subst;
    }

    public void setConfigurationMonitor(ConfigurationMonitor configurationmonitor) {
        this.monitor = configurationmonitor;
    }

    public ConfigurationMonitor getConfigurationMonitor() {
        return this.monitor;
    }

    public void setAdvertiser(Advertiser advertiser) {
        this.advertiser = advertiser;
    }

    public Advertiser getAdvertiser() {
        return this.advertiser;
    }

    public synchronized void addLoggerAppender(org.apache.logging.log4j.core.Logger org_apache_logging_log4j_core_logger, Appender appender) {
        String s = org_apache_logging_log4j_core_logger.getName();

        this.appenders.putIfAbsent(appender.getName(), appender);
        LoggerConfig loggerconfig = this.getLoggerConfig(s);

        if (loggerconfig.getName().equals(s)) {
            loggerconfig.addAppender(appender, (Level) null, (Filter) null);
        } else {
            LoggerConfig loggerconfig1 = new LoggerConfig(s, loggerconfig.getLevel(), loggerconfig.isAdditive());

            loggerconfig1.addAppender(appender, (Level) null, (Filter) null);
            loggerconfig1.setParent(loggerconfig);
            this.loggers.putIfAbsent(s, loggerconfig1);
            this.setParents();
            org_apache_logging_log4j_core_logger.getContext().updateLoggers();
        }

    }

    public synchronized void addLoggerFilter(org.apache.logging.log4j.core.Logger org_apache_logging_log4j_core_logger, Filter filter) {
        String s = org_apache_logging_log4j_core_logger.getName();
        LoggerConfig loggerconfig = this.getLoggerConfig(s);

        if (loggerconfig.getName().equals(s)) {
            loggerconfig.addFilter(filter);
        } else {
            LoggerConfig loggerconfig1 = new LoggerConfig(s, loggerconfig.getLevel(), loggerconfig.isAdditive());

            loggerconfig1.addFilter(filter);
            loggerconfig1.setParent(loggerconfig);
            this.loggers.putIfAbsent(s, loggerconfig1);
            this.setParents();
            org_apache_logging_log4j_core_logger.getContext().updateLoggers();
        }

    }

    public synchronized void setLoggerAdditive(org.apache.logging.log4j.core.Logger org_apache_logging_log4j_core_logger, boolean flag) {
        String s = org_apache_logging_log4j_core_logger.getName();
        LoggerConfig loggerconfig = this.getLoggerConfig(s);

        if (loggerconfig.getName().equals(s)) {
            loggerconfig.setAdditive(flag);
        } else {
            LoggerConfig loggerconfig1 = new LoggerConfig(s, loggerconfig.getLevel(), flag);

            loggerconfig1.setParent(loggerconfig);
            this.loggers.putIfAbsent(s, loggerconfig1);
            this.setParents();
            org_apache_logging_log4j_core_logger.getContext().updateLoggers();
        }

    }

    public synchronized void removeAppender(String s) {
        Iterator iterator = this.loggers.values().iterator();

        while (iterator.hasNext()) {
            LoggerConfig loggerconfig = (LoggerConfig) iterator.next();

            loggerconfig.removeAppender(s);
        }

        Appender appender = (Appender) this.appenders.remove(s);

        if (appender != null) {
            appender.stop();
        }

    }

    public LoggerConfig getLoggerConfig(String s) {
        if (this.loggers.containsKey(s)) {
            return (LoggerConfig) this.loggers.get(s);
        } else {
            String s1 = s;

            do {
                if ((s1 = NameUtil.getSubName(s1)) == null) {
                    return this.root;
                }
            } while (!this.loggers.containsKey(s1));

            return (LoggerConfig) this.loggers.get(s1);
        }
    }

    public LoggerConfig getRootLogger() {
        return this.root;
    }

    public Map getLoggers() {
        return Collections.unmodifiableMap(this.loggers);
    }

    public LoggerConfig getLogger(String s) {
        return (LoggerConfig) this.loggers.get(s);
    }

    public void addLogger(String s, LoggerConfig loggerconfig) {
        this.loggers.put(s, loggerconfig);
        this.setParents();
    }

    public void removeLogger(String s) {
        this.loggers.remove(s);
        this.setParents();
    }

    public void createConfiguration(Node node, LogEvent logevent) {
        PluginType plugintype = node.getType();

        if (plugintype != null && plugintype.isDeferChildren()) {
            node.setObject(this.createPluginObject(plugintype, node, logevent));
        } else {
            Iterator iterator = node.getChildren().iterator();

            while (iterator.hasNext()) {
                Node node1 = (Node) iterator.next();

                this.createConfiguration(node1, logevent);
            }

            if (plugintype == null) {
                if (node.getParent() != null) {
                    BaseConfiguration.LOGGER.error("Unable to locate plugin for " + node.getName());
                }
            } else {
                node.setObject(this.createPluginObject(plugintype, node, logevent));
            }
        }

    }

    private Object createPluginObject(PluginType plugintype, Node node, LogEvent logevent) {
        Class oclass = plugintype.getPluginClass();
        Iterator iterator;
        Node node1;

        if (Map.class.isAssignableFrom(oclass)) {
            try {
                Map map = (Map) oclass.newInstance();

                iterator = node.getChildren().iterator();

                while (iterator.hasNext()) {
                    node1 = (Node) iterator.next();
                    map.put(node1.getName(), node1.getObject());
                }

                return map;
            } catch (Exception exception) {
                BaseConfiguration.LOGGER.warn("Unable to create Map for " + plugintype.getElementName() + " of class " + oclass);
            }
        }

        if (List.class.isAssignableFrom(oclass)) {
            try {
                List list = (List) oclass.newInstance();

                iterator = node.getChildren().iterator();

                while (iterator.hasNext()) {
                    node1 = (Node) iterator.next();
                    list.add(node1.getObject());
                }

                return list;
            } catch (Exception exception1) {
                BaseConfiguration.LOGGER.warn("Unable to create List for " + plugintype.getElementName() + " of class " + oclass);
            }
        }

        Method method = null;
        Method[] amethod = oclass.getMethods();
        int i = amethod.length;

        for (int j = 0; j < i; ++j) {
            Method method1 = amethod[j];

            if (method1.isAnnotationPresent(PluginFactory.class)) {
                method = method1;
                break;
            }
        }

        if (method == null) {
            return null;
        } else {
            Annotation[][] aannotation = method.getParameterAnnotations();
            Class[] aclass = method.getParameterTypes();

            if (aannotation.length != aclass.length) {
                BaseConfiguration.LOGGER.error("Number of parameter annotations does not equal the number of paramters");
            }

            Object[] aobject = new Object[aclass.length];
            int k = 0;
            Map map1 = node.getAttributes();
            List list1 = node.getChildren();
            StringBuilder stringbuilder = new StringBuilder();
            ArrayList arraylist = new ArrayList();
            Annotation[][] aannotation1 = aannotation;
            int l = aannotation.length;

            for (int i1 = 0; i1 < l; ++i1) {
                Annotation[] aannotation2 = aannotation1[i1];
                String[] astring = null;
                Annotation[] aannotation3 = aannotation2;
                int j1 = aannotation2.length;

                int k1;
                Annotation annotation;

                for (k1 = 0; k1 < j1; ++k1) {
                    annotation = aannotation3[k1];
                    if (annotation instanceof PluginAliases) {
                        astring = ((PluginAliases) annotation).value();
                    }
                }

                aannotation3 = aannotation2;
                j1 = aannotation2.length;

                for (k1 = 0; k1 < j1; ++k1) {
                    annotation = aannotation3[k1];
                    if (!(annotation instanceof PluginAliases)) {
                        if (stringbuilder.length() == 0) {
                            stringbuilder.append(" with params(");
                        } else {
                            stringbuilder.append(", ");
                        }

                        if (annotation instanceof PluginNode) {
                            aobject[k] = node;
                            stringbuilder.append("Node=").append(node.getName());
                        } else if (annotation instanceof PluginConfiguration) {
                            aobject[k] = this;
                            if (this.name != null) {
                                stringbuilder.append("Configuration(").append(this.name).append(")");
                            } else {
                                stringbuilder.append("Configuration");
                            }
                        } else {
                            String s;
                            String s1;

                            if (annotation instanceof PluginValue) {
                                String s2 = ((PluginValue) annotation).value();

                                s = node.getValue();
                                if (s == null) {
                                    s = this.getAttrValue("value", (String[]) null, map1);
                                }

                                s1 = this.subst.replace(logevent, s);
                                stringbuilder.append(s2).append("=\"").append(s1).append("\"");
                                aobject[k] = s1;
                            } else if (annotation instanceof PluginAttribute) {
                                PluginAttribute pluginattribute = (PluginAttribute) annotation;

                                s = pluginattribute.value();
                                s1 = this.subst.replace(logevent, this.getAttrValue(s, astring, map1));
                                stringbuilder.append(s).append("=\"").append(s1).append("\"");
                                aobject[k] = s1;
                            } else if (annotation instanceof PluginElement) {
                                PluginElement pluginelement = (PluginElement) annotation;

                                s = pluginelement.value();
                                Class oclass1;

                                if (aclass[k].isArray()) {
                                    oclass1 = aclass[k].getComponentType();
                                    ArrayList arraylist1 = new ArrayList();

                                    stringbuilder.append(s).append("={");
                                    boolean flag = true;
                                    Iterator iterator1 = list1.iterator();

                                    Object object;

                                    while (iterator1.hasNext()) {
                                        Node node2 = (Node) iterator1.next();
                                        PluginType plugintype1 = node2.getType();

                                        if (pluginelement.value().equalsIgnoreCase(plugintype1.getElementName()) || oclass1.isAssignableFrom(plugintype1.getPluginClass())) {
                                            arraylist.add(node2);
                                            if (!flag) {
                                                stringbuilder.append(", ");
                                            }

                                            flag = false;
                                            object = node2.getObject();
                                            if (object == null) {
                                                BaseConfiguration.LOGGER.error("Null object returned for " + node2.getName() + " in " + node.getName());
                                            } else {
                                                if (object.getClass().isArray()) {
                                                    this.printArray(stringbuilder, (Object[]) ((Object[]) object));
                                                    aobject[k] = object;
                                                    break;
                                                }

                                                stringbuilder.append(node2.toString());
                                                arraylist1.add(object);
                                            }
                                        }
                                    }

                                    stringbuilder.append("}");
                                    if (aobject[k] != null) {
                                        break;
                                    }

                                    if (arraylist1.size() > 0 && !oclass1.isAssignableFrom(arraylist1.get(0).getClass())) {
                                        BaseConfiguration.LOGGER.error("Attempted to assign List containing class " + arraylist1.get(0).getClass().getName() + " to array of type " + oclass1 + " for attribute " + s);
                                        break;
                                    }

                                    Object[] aobject1 = (Object[]) ((Object[]) Array.newInstance(oclass1, arraylist1.size()));
                                    int l1 = 0;

                                    for (Iterator iterator2 = arraylist1.iterator(); iterator2.hasNext(); ++l1) {
                                        object = iterator2.next();
                                        aobject1[l1] = object;
                                    }

                                    aobject[k] = aobject1;
                                } else {
                                    oclass1 = aclass[k];
                                    boolean flag1 = false;
                                    Iterator iterator3 = list1.iterator();

                                    while (iterator3.hasNext()) {
                                        Node node3 = (Node) iterator3.next();
                                        PluginType plugintype2 = node3.getType();

                                        if (pluginelement.value().equals(plugintype2.getElementName()) || oclass1.isAssignableFrom(plugintype2.getPluginClass())) {
                                            stringbuilder.append(node3.getName()).append("(").append(node3.toString()).append(")");
                                            flag1 = true;
                                            arraylist.add(node3);
                                            aobject[k] = node3.getObject();
                                            break;
                                        }
                                    }

                                    if (!flag1) {
                                        stringbuilder.append("null");
                                    }
                                }
                            }
                        }
                    }
                }

                ++k;
            }

            if (stringbuilder.length() > 0) {
                stringbuilder.append(")");
            }

            String s3;

            if (map1.size() > 0) {
                StringBuilder stringbuilder1 = new StringBuilder();
                Iterator iterator4 = map1.keySet().iterator();

                while (iterator4.hasNext()) {
                    s3 = (String) iterator4.next();
                    if (stringbuilder1.length() == 0) {
                        stringbuilder1.append(node.getName());
                        stringbuilder1.append(" contains ");
                        if (map1.size() == 1) {
                            stringbuilder1.append("an invalid element or attribute ");
                        } else {
                            stringbuilder1.append("invalid attributes ");
                        }
                    } else {
                        stringbuilder1.append(", ");
                    }

                    stringbuilder1.append("\"");
                    stringbuilder1.append(s3);
                    stringbuilder1.append("\"");
                }

                BaseConfiguration.LOGGER.error(stringbuilder1.toString());
            }

            if (!plugintype.isDeferChildren() && arraylist.size() != list1.size()) {
                Iterator iterator5 = list1.iterator();

                while (iterator5.hasNext()) {
                    Node node4 = (Node) iterator5.next();

                    if (!arraylist.contains(node4)) {
                        s3 = node.getType().getElementName();
                        String s4 = s3.equals(node.getName()) ? node.getName() : s3 + " " + node.getName();

                        BaseConfiguration.LOGGER.error(s4 + " has no parameter that matches element " + node4.getName());
                    }
                }
            }

            try {
                int i2 = method.getModifiers();

                if (!Modifier.isStatic(i2)) {
                    BaseConfiguration.LOGGER.error(method.getName() + " method is not static on class " + oclass.getName() + " for element " + node.getName());
                    return null;
                } else {
                    BaseConfiguration.LOGGER.debug("Calling {} on class {} for element {}{}", new Object[] { method.getName(), oclass.getName(), node.getName(), stringbuilder.toString()});
                    return method.invoke((Object) null, aobject);
                }
            } catch (Exception exception2) {
                BaseConfiguration.LOGGER.error("Unable to invoke method " + method.getName() + " in class " + oclass.getName() + " for element " + node.getName(), (Throwable) exception2);
                return null;
            }
        }
    }

    private void printArray(StringBuilder stringbuilder, Object... aobject) {
        boolean flag = true;
        Object[] aobject1 = aobject;
        int i = aobject.length;

        for (int j = 0; j < i; ++j) {
            Object object = aobject1[j];

            if (!flag) {
                stringbuilder.append(", ");
            }

            stringbuilder.append(object.toString());
            flag = false;
        }

    }

    private String getAttrValue(String s, String[] astring, Map map) {
        Iterator iterator = map.keySet().iterator();

        while (iterator.hasNext()) {
            String s1 = (String) iterator.next();

            if (s1.equalsIgnoreCase(s)) {
                String s2 = (String) map.get(s1);

                map.remove(s1);
                return s2;
            }

            if (astring != null) {
                String[] astring1 = astring;
                int i = astring.length;

                for (int j = 0; j < i; ++j) {
                    String s3 = astring1[j];

                    if (s1.equalsIgnoreCase(s3)) {
                        String s4 = (String) map.get(s1);

                        map.remove(s1);
                        return s4;
                    }
                }
            }
        }

        return null;
    }

    private void setParents() {
        Iterator iterator = this.loggers.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            LoggerConfig loggerconfig = (LoggerConfig) entry.getValue();
            String s = (String) entry.getKey();

            if (!s.equals("")) {
                int i = s.lastIndexOf(46);

                if (i > 0) {
                    s = s.substring(0, i);
                    LoggerConfig loggerconfig1 = this.getLoggerConfig(s);

                    if (loggerconfig1 == null) {
                        loggerconfig1 = this.root;
                    }

                    loggerconfig.setParent(loggerconfig1);
                } else {
                    loggerconfig.setParent(this.root);
                }
            }
        }

    }
}
