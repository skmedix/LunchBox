package org.apache.logging.log4j.core.jmx;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.helpers.Assert;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.helpers.Closer;
import org.apache.logging.log4j.status.StatusLogger;

public class LoggerContextAdmin extends NotificationBroadcasterSupport implements LoggerContextAdminMBean, PropertyChangeListener {

    private static final int PAGE = 4096;
    private static final int TEXT_BUFFER = 65536;
    private static final int BUFFER_SIZE = 2048;
    private static final StatusLogger LOGGER = StatusLogger.getLogger();
    private final AtomicLong sequenceNo = new AtomicLong();
    private final ObjectName objectName;
    private final LoggerContext loggerContext;
    private String customConfigText;

    public LoggerContextAdmin(LoggerContext loggercontext, Executor executor) {
        super(executor, new MBeanNotificationInfo[] { createNotificationInfo()});
        this.loggerContext = (LoggerContext) Assert.isNotNull(loggercontext, "loggerContext");

        try {
            String s = Server.escape(loggercontext.getName());
            String s1 = String.format("org.apache.logging.log4j2:type=LoggerContext,ctx=%s", new Object[] { s});

            this.objectName = new ObjectName(s1);
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }

        loggercontext.addPropertyChangeListener(this);
    }

    private static MBeanNotificationInfo createNotificationInfo() {
        String[] astring = new String[] { "com.apache.logging.log4j.core.jmx.config.reconfigured"};
        String s = Notification.class.getName();
        String s1 = "Configuration reconfigured";

        return new MBeanNotificationInfo(astring, s, "Configuration reconfigured");
    }

    public String getStatus() {
        return this.loggerContext.getStatus().toString();
    }

    public String getName() {
        return this.loggerContext.getName();
    }

    private Configuration getConfig() {
        return this.loggerContext.getConfiguration();
    }

    public String getConfigLocationURI() {
        return this.loggerContext.getConfigLocation() != null ? String.valueOf(this.loggerContext.getConfigLocation()) : (this.getConfigName() != null ? String.valueOf((new File(this.getConfigName())).toURI()) : "");
    }

    public void setConfigLocationURI(String s) throws URISyntaxException, IOException {
        LoggerContextAdmin.LOGGER.debug("---------");
        LoggerContextAdmin.LOGGER.debug("Remote request to reconfigure using location " + s);
        URI uri = new URI(s);

        uri.toURL().openStream().close();
        this.loggerContext.setConfigLocation(uri);
        LoggerContextAdmin.LOGGER.debug("Completed remote request to reconfigure.");
    }

    public void propertyChange(PropertyChangeEvent propertychangeevent) {
        if ("config".equals(propertychangeevent.getPropertyName())) {
            if (this.loggerContext.getConfiguration().getName() != null) {
                this.customConfigText = null;
            }

            Notification notification = new Notification("com.apache.logging.log4j.core.jmx.config.reconfigured", this.getObjectName(), this.nextSeqNo(), this.now(), (String) null);

            this.sendNotification(notification);
        }
    }

    public String getConfigText() throws IOException {
        return this.getConfigText(Charsets.UTF_8.name());
    }

    public String getConfigText(String s) throws IOException {
        if (this.customConfigText != null) {
            return this.customConfigText;
        } else {
            try {
                Charset charset = Charset.forName(s);

                return this.readContents(new URI(this.getConfigLocationURI()), charset);
            } catch (Exception exception) {
                StringWriter stringwriter = new StringWriter(2048);

                exception.printStackTrace(new PrintWriter(stringwriter));
                return stringwriter.toString();
            }
        }
    }

    public void setConfigText(String s, String s1) {
        String s2 = this.customConfigText;

        this.customConfigText = (String) Assert.isNotNull(s, "configText");
        LoggerContextAdmin.LOGGER.debug("---------");
        LoggerContextAdmin.LOGGER.debug("Remote request to reconfigure from config text.");

        try {
            ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(s.getBytes(s1));
            ConfigurationFactory.ConfigurationSource configurationfactory_configurationsource = new ConfigurationFactory.ConfigurationSource(bytearrayinputstream);
            Configuration configuration = ConfigurationFactory.getInstance().getConfiguration(configurationfactory_configurationsource);

            this.loggerContext.start(configuration);
            LoggerContextAdmin.LOGGER.debug("Completed remote request to reconfigure from config text.");
        } catch (Exception exception) {
            this.customConfigText = s2;
            String s3 = "Could not reconfigure from config text";

            LoggerContextAdmin.LOGGER.error("Could not reconfigure from config text", (Throwable) exception);
            throw new IllegalArgumentException("Could not reconfigure from config text", exception);
        }
    }

    private String readContents(URI uri, Charset charset) throws IOException {
        InputStream inputstream = null;
        InputStreamReader inputstreamreader = null;

        try {
            inputstream = uri.toURL().openStream();
            inputstreamreader = new InputStreamReader(inputstream, charset);
            StringBuilder stringbuilder = new StringBuilder(65536);
            char[] achar = new char[4096];
            boolean flag = true;

            int i;

            while ((i = inputstreamreader.read(achar)) >= 0) {
                stringbuilder.append(achar, 0, i);
            }

            String s = stringbuilder.toString();

            return s;
        } finally {
            Closer.closeSilent((Closeable) inputstream);
            Closer.closeSilent((Closeable) inputstreamreader);
        }
    }

    public String getConfigName() {
        return this.getConfig().getName();
    }

    public String getConfigClassName() {
        return this.getConfig().getClass().getName();
    }

    public String getConfigFilter() {
        return String.valueOf(this.getConfig().getFilter());
    }

    public String getConfigMonitorClassName() {
        return this.getConfig().getConfigurationMonitor().getClass().getName();
    }

    public Map getConfigProperties() {
        return this.getConfig().getProperties();
    }

    public ObjectName getObjectName() {
        return this.objectName;
    }

    private long nextSeqNo() {
        return this.sequenceNo.getAndIncrement();
    }

    private long now() {
        return System.currentTimeMillis();
    }
}
