package org.apache.logging.log4j.core.jmx;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.management.ManagementFactory;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.management.InstanceAlreadyExistsException;
import javax.management.JMException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.QueryExp;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.selector.ContextSelector;
import org.apache.logging.log4j.status.StatusLogger;

public final class Server {

    private static final String PROPERTY_DISABLE_JMX = "log4j2.disable.jmx";

    public static String escape(String s) {
        StringBuilder stringbuilder = new StringBuilder(s.length() * 2);
        boolean flag = false;
        int i = 0;

        while (i < s.length()) {
            char c0 = s.charAt(i);

            switch (c0) {
            case '*':
            case ',':
            case ':':
            case '=':
            case '?':
            case '\\':
                stringbuilder.append('\\');
                flag = true;

            default:
                stringbuilder.append(c0);
                ++i;
            }
        }

        if (flag) {
            stringbuilder.insert(0, '\"');
            stringbuilder.append('\"');
        }

        return stringbuilder.toString();
    }

    public static void registerMBeans(ContextSelector contextselector) throws JMException {
        if (Boolean.getBoolean("log4j2.disable.jmx")) {
            StatusLogger.getLogger().debug("JMX disabled for log4j2. Not registering MBeans.");
        } else {
            MBeanServer mbeanserver = ManagementFactory.getPlatformMBeanServer();

            registerMBeans(contextselector, mbeanserver);
        }
    }

    public static void registerMBeans(ContextSelector contextselector, final MBeanServer mbeanserver) throws JMException {
        if (Boolean.getBoolean("log4j2.disable.jmx")) {
            StatusLogger.getLogger().debug("JMX disabled for log4j2. Not registering MBeans.");
        } else {
            final ExecutorService executorservice = Executors.newFixedThreadPool(1);

            registerStatusLogger(mbeanserver, executorservice);
            registerContextSelector(contextselector, mbeanserver, executorservice);
            List list = contextselector.getLoggerContexts();

            registerContexts(list, mbeanserver, executorservice);
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                final LoggerContext loggercontext = (LoggerContext) iterator.next();

                loggercontext.addPropertyChangeListener(new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent propertychangeevent) {
                        if ("config".equals(propertychangeevent.getPropertyName())) {
                            Server.unregisterLoggerConfigs(loggercontext, mbeanserver);
                            Server.unregisterAppenders(loggercontext, mbeanserver);

                            try {
                                Server.registerLoggerConfigs(loggercontext, mbeanserver, executorservice);
                                Server.registerAppenders(loggercontext, mbeanserver, executorservice);
                            } catch (Exception exception) {
                                StatusLogger.getLogger().error("Could not register mbeans", (Throwable) exception);
                            }

                        }
                    }
                });
            }

        }
    }

    private static void registerStatusLogger(MBeanServer mbeanserver, Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        StatusLoggerAdmin statusloggeradmin = new StatusLoggerAdmin(executor);

        mbeanserver.registerMBean(statusloggeradmin, statusloggeradmin.getObjectName());
    }

    private static void registerContextSelector(ContextSelector contextselector, MBeanServer mbeanserver, Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        ContextSelectorAdmin contextselectoradmin = new ContextSelectorAdmin(contextselector);

        mbeanserver.registerMBean(contextselectoradmin, contextselectoradmin.getObjectName());
    }

    private static void registerContexts(List list, MBeanServer mbeanserver, Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            LoggerContext loggercontext = (LoggerContext) iterator.next();
            LoggerContextAdmin loggercontextadmin = new LoggerContextAdmin(loggercontext, executor);

            mbeanserver.registerMBean(loggercontextadmin, loggercontextadmin.getObjectName());
        }

    }

    private static void unregisterLoggerConfigs(LoggerContext loggercontext, MBeanServer mbeanserver) {
        String s = "org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=LoggerConfig,name=%s";
        String s1 = String.format("org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=LoggerConfig,name=%s", new Object[] { loggercontext.getName(), "*"});

        unregisterAllMatching(s1, mbeanserver);
    }

    private static void unregisterAppenders(LoggerContext loggercontext, MBeanServer mbeanserver) {
        String s = "org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=Appender,name=%s";
        String s1 = String.format("org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=Appender,name=%s", new Object[] { loggercontext.getName(), "*"});

        unregisterAllMatching(s1, mbeanserver);
    }

    private static void unregisterAllMatching(String s, MBeanServer mbeanserver) {
        try {
            ObjectName objectname = new ObjectName(s);
            Set set = mbeanserver.queryNames(objectname, (QueryExp) null);
            Iterator iterator = set.iterator();

            while (iterator.hasNext()) {
                ObjectName objectname1 = (ObjectName) iterator.next();

                mbeanserver.unregisterMBean(objectname1);
            }
        } catch (Exception exception) {
            StatusLogger.getLogger().error("Could not unregister " + s, (Throwable) exception);
        }

    }

    private static void registerLoggerConfigs(LoggerContext loggercontext, MBeanServer mbeanserver, Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        Map map = loggercontext.getConfiguration().getLoggers();
        Iterator iterator = map.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            LoggerConfig loggerconfig = (LoggerConfig) map.get(s);
            LoggerConfigAdmin loggerconfigadmin = new LoggerConfigAdmin(loggercontext.getName(), loggerconfig);

            mbeanserver.registerMBean(loggerconfigadmin, loggerconfigadmin.getObjectName());
        }

    }

    private static void registerAppenders(LoggerContext loggercontext, MBeanServer mbeanserver, Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        Map map = loggercontext.getConfiguration().getAppenders();
        Iterator iterator = map.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            Appender appender = (Appender) map.get(s);
            AppenderAdmin appenderadmin = new AppenderAdmin(loggercontext.getName(), appender);

            mbeanserver.registerMBean(appenderadmin, appenderadmin.getObjectName());
        }

    }
}
