package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.layout.LoggerFields;
import org.apache.logging.log4j.core.layout.RFC5424Layout;
import org.apache.logging.log4j.core.layout.SyslogLayout;
import org.apache.logging.log4j.core.net.AbstractSocketManager;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.net.TLSSocketManager;
import org.apache.logging.log4j.core.net.ssl.SSLConfiguration;

@Plugin(
    name = "TLSSyslog",
    category = "Core",
    elementType = "appender",
    printObject = true
)
public final class TLSSyslogAppender extends SyslogAppender {

    protected TLSSyslogAppender(String s, Layout layout, Filter filter, boolean flag, boolean flag1, AbstractSocketManager abstractsocketmanager, Advertiser advertiser) {
        super(s, layout, filter, flag, flag1, abstractsocketmanager, advertiser);
    }

    @PluginFactory
    public static TLSSyslogAppender createAppender(@PluginAttribute("host") String s, @PluginAttribute("port") String s1, @PluginElement("ssl") SSLConfiguration sslconfiguration, @PluginAttribute("reconnectionDelay") String s2, @PluginAttribute("immediateFail") String s3, @PluginAttribute("name") String s4, @PluginAttribute("immediateFlush") String s5, @PluginAttribute("ignoreExceptions") String s6, @PluginAttribute("facility") String s7, @PluginAttribute("id") String s8, @PluginAttribute("enterpriseNumber") String s9, @PluginAttribute("includeMDC") String s10, @PluginAttribute("mdcId") String s11, @PluginAttribute("mdcPrefix") String s12, @PluginAttribute("eventPrefix") String s13, @PluginAttribute("newLine") String s14, @PluginAttribute("newLineEscape") String s15, @PluginAttribute("appName") String s16, @PluginAttribute("messageId") String s17, @PluginAttribute("mdcExcludes") String s18, @PluginAttribute("mdcIncludes") String s19, @PluginAttribute("mdcRequired") String s20, @PluginAttribute("format") String s21, @PluginElement("filters") Filter filter, @PluginConfiguration Configuration configuration, @PluginAttribute("charset") String s22, @PluginAttribute("exceptionPattern") String s23, @PluginElement("LoggerFields") LoggerFields[] aloggerfields, @PluginAttribute("advertise") String s24) {
        boolean flag = Booleans.parseBoolean(s5, true);
        boolean flag1 = Booleans.parseBoolean(s6, true);
        int i = AbstractAppender.parseInt(s2, 0);
        boolean flag2 = Booleans.parseBoolean(s3, true);
        int j = AbstractAppender.parseInt(s1, 0);
        boolean flag3 = Boolean.parseBoolean(s24);
        Object object = "RFC5424".equalsIgnoreCase(s21) ? RFC5424Layout.createLayout(s7, s8, s9, s10, s11, s12, s13, s14, s15, s16, s17, s18, s19, s20, s23, "true", aloggerfields, configuration) : SyslogLayout.createLayout(s7, s14, s15, s22);

        if (s4 == null) {
            TLSSyslogAppender.LOGGER.error("No name provided for TLSSyslogAppender");
            return null;
        } else {
            AbstractSocketManager abstractsocketmanager = createSocketManager(sslconfiguration, s, j, i, flag2, (Layout) object);

            return abstractsocketmanager == null ? null : new TLSSyslogAppender(s4, (Layout) object, filter, flag1, flag, abstractsocketmanager, flag3 ? configuration.getAdvertiser() : null);
        }
    }

    public static AbstractSocketManager createSocketManager(SSLConfiguration sslconfiguration, String s, int i, int j, boolean flag, Layout layout) {
        return TLSSocketManager.getSocketManager(sslconfiguration, s, i, j, flag, layout);
    }
}
