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
import org.apache.logging.log4j.core.net.JMSTopicManager;

@Plugin(
    name = "JMSTopic",
    category = "Core",
    elementType = "appender",
    printObject = true
)
public final class JMSTopicAppender extends AbstractAppender {

    private final JMSTopicManager manager;

    private JMSTopicAppender(String s, Filter filter, Layout layout, JMSTopicManager jmstopicmanager, boolean flag) {
        super(s, filter, layout, flag);
        this.manager = jmstopicmanager;
    }

    public void append(LogEvent logevent) {
        try {
            this.manager.send(this.getLayout().toSerializable(logevent));
        } catch (Exception exception) {
            throw new AppenderLoggingException(exception);
        }
    }

    @PluginFactory
    public static JMSTopicAppender createAppender(@PluginAttribute("name") String s, @PluginAttribute("factoryName") String s1, @PluginAttribute("providerURL") String s2, @PluginAttribute("urlPkgPrefixes") String s3, @PluginAttribute("securityPrincipalName") String s4, @PluginAttribute("securityCredentials") String s5, @PluginAttribute("factoryBindingName") String s6, @PluginAttribute("topicBindingName") String s7, @PluginAttribute("userName") String s8, @PluginAttribute("password") String s9, @PluginElement("Layout") Layout layout, @PluginElement("Filters") Filter filter, @PluginAttribute("ignoreExceptions") String s10) {
        if (s == null) {
            JMSTopicAppender.LOGGER.error("No name provided for JMSQueueAppender");
            return null;
        } else {
            boolean flag = Booleans.parseBoolean(s10, true);
            JMSTopicManager jmstopicmanager = JMSTopicManager.getJMSTopicManager(s1, s2, s3, s4, s5, s6, s7, s8, s9);

            if (jmstopicmanager == null) {
                return null;
            } else {
                if (layout == null) {
                    layout = SerializedLayout.createLayout();
                }

                return new JMSTopicAppender(s, filter, (Layout) layout, jmstopicmanager, flag);
            }
        }
    }
}
