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
import org.apache.logging.log4j.core.net.Protocol;
import org.apache.logging.log4j.util.EnglishEnums;

@Plugin(
    name = "Syslog",
    category = "Core",
    elementType = "appender",
    printObject = true
)
public class SyslogAppender extends SocketAppender {

    protected static final String RFC5424 = "RFC5424";

    protected SyslogAppender(String s, Layout layout, Filter filter, boolean flag, boolean flag1, AbstractSocketManager abstractsocketmanager, Advertiser advertiser) {
        super(s, layout, filter, abstractsocketmanager, flag, flag1, advertiser);
    }

    @PluginFactory
    public static SyslogAppender createAppender(@PluginAttribute("host") String s, @PluginAttribute("port") String s1, @PluginAttribute("protocol") String s2, @PluginAttribute("reconnectionDelay") String s3, @PluginAttribute("immediateFail") String s4, @PluginAttribute("name") String s5, @PluginAttribute("immediateFlush") String s6, @PluginAttribute("ignoreExceptions") String s7, @PluginAttribute("facility") String s8, @PluginAttribute("id") String s9, @PluginAttribute("enterpriseNumber") String s10, @PluginAttribute("includeMDC") String s11, @PluginAttribute("mdcId") String s12, @PluginAttribute("mdcPrefix") String s13, @PluginAttribute("eventPrefix") String s14, @PluginAttribute("newLine") String s15, @PluginAttribute("newLineEscape") String s16, @PluginAttribute("appName") String s17, @PluginAttribute("messageId") String s18, @PluginAttribute("mdcExcludes") String s19, @PluginAttribute("mdcIncludes") String s20, @PluginAttribute("mdcRequired") String s21, @PluginAttribute("format") String s22, @PluginElement("Filters") Filter filter, @PluginConfiguration Configuration configuration, @PluginAttribute("charset") String s23, @PluginAttribute("exceptionPattern") String s24, @PluginElement("LoggerFields") LoggerFields[] aloggerfields, @PluginAttribute("advertise") String s25) {
        boolean flag = Booleans.parseBoolean(s6, true);
        boolean flag1 = Booleans.parseBoolean(s7, true);
        int i = AbstractAppender.parseInt(s3, 0);
        boolean flag2 = Booleans.parseBoolean(s4, true);
        int j = AbstractAppender.parseInt(s1, 0);
        boolean flag3 = Boolean.parseBoolean(s25);
        Object object = "RFC5424".equalsIgnoreCase(s22) ? RFC5424Layout.createLayout(s8, s9, s10, s11, s12, s13, s14, s15, s16, s17, s18, s19, s20, s21, s24, "false", aloggerfields, configuration) : SyslogLayout.createLayout(s8, s15, s16, s23);

        if (s5 == null) {
            SyslogAppender.LOGGER.error("No name provided for SyslogAppender");
            return null;
        } else {
            Protocol protocol = (Protocol) EnglishEnums.valueOf(Protocol.class, s2);
            AbstractSocketManager abstractsocketmanager = createSocketManager(protocol, s, j, i, flag2, (Layout) object);

            return abstractsocketmanager == null ? null : new SyslogAppender(s5, (Layout) object, filter, flag1, flag, abstractsocketmanager, flag3 ? configuration.getAdvertiser() : null);
        }
    }
}
