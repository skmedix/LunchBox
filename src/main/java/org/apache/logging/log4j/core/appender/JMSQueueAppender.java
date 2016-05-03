package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.layout.SerializedLayout;
import org.apache.logging.log4j.core.net.JMSQueueManager;

@Plugin(
    name = "JMSQueue",
    category = "Core",
    elementType = "appender",
    printObject = true
)
public final class JMSQueueAppender extends AbstractAppender {

    private final JMSQueueManager manager;

    private JMSQueueAppender(String s, Filter filter, Layout layout, JMSQueueManager jmsqueuemanager, boolean flag) {
        super(s, filter, layout, flag);
        this.manager = jmsqueuemanager;
    }

    public void append(LogEvent logevent) {
        try {
            this.manager.send(this.getLayout().toSerializable(logevent));
        } catch (Exception exception) {
            throw new AppenderLoggingException(exception);
        }
    }

    @PluginFactory
    public static JMSQueueAppender createAppender(@PluginAttribute("name") String s, @PluginAttribute("factoryName") String s1, @PluginAttribute("providerURL") String s2, @PluginAttribute("urlPkgPrefixes") String s3, @PluginAttribute("securityPrincipalName") String s4, @PluginAttribute("securityCredentials") String s5, @PluginAttribute("factoryBindingName") String s6, @PluginAttribute("queueBindingName") String s7, @PluginAttribute("userName") String s8, @PluginAttribute("password") String s9, @PluginElement("Layout") Layout layout, @PluginElement("Filter") Filter filter, @PluginAttribute("ignoreExceptions") String s10) {
        if (s == null) {
            JMSQueueAppender.LOGGER.error("No name provided for JMSQueueAppender");
            return null;
        } else {
            boolean flag = Booleans.parseBoolean(s10, true);
            JMSQueueManager jmsqueuemanager = JMSQueueManager.getJMSQueueManager(s1, s2, s3, s4, s5, s6, s7, s8, s9);

            if (jmsqueuemanager == null) {
                return null;
            } else {
                if (layout == null) {
                    layout = SerializedLayout.createLayout();
                }

                return new JMSQueueAppender(s, filter, (Layout) layout, jmsqueuemanager, flag);
            }
        }
    }
}
