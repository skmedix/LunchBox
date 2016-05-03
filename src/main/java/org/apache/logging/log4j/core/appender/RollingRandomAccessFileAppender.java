package org.apache.logging.log4j.core.appender;

import java.util.HashMap;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
import org.apache.logging.log4j.core.appender.rolling.RollingRandomAccessFileManager;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.pattern.RegexReplacement;

@Plugin(
    name = "RollingRandomAccessFile",
    category = "Core",
    elementType = "appender",
    printObject = true
)
public final class RollingRandomAccessFileAppender extends AbstractOutputStreamAppender {

    private final String fileName;
    private final String filePattern;
    private Object advertisement;
    private final Advertiser advertiser;

    private RollingRandomAccessFileAppender(String s, Layout layout, Filter filter, RollingFileManager rollingfilemanager, String s1, String s2, boolean flag, boolean flag1, Advertiser advertiser) {
        super(s, layout, filter, flag, flag1, rollingfilemanager);
        if (advertiser != null) {
            HashMap hashmap = new HashMap(layout.getContentFormat());

            hashmap.put("contentType", layout.getContentType());
            hashmap.put("name", s);
            this.advertisement = advertiser.advertise(hashmap);
        }

        this.fileName = s1;
        this.filePattern = s2;
        this.advertiser = advertiser;
    }

    public void stop() {
        super.stop();
        if (this.advertiser != null) {
            this.advertiser.unadvertise(this.advertisement);
        }

    }

    public void append(LogEvent logevent) {
        RollingRandomAccessFileManager rollingrandomaccessfilemanager = (RollingRandomAccessFileManager) this.getManager();

        rollingrandomaccessfilemanager.checkRollover(logevent);
        rollingrandomaccessfilemanager.setEndOfBatch(logevent.isEndOfBatch());
        super.append(logevent);
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getFilePattern() {
        return this.filePattern;
    }

    @PluginFactory
    public static RollingRandomAccessFileAppender createAppender(@PluginAttribute("fileName") String s, @PluginAttribute("filePattern") String s1, @PluginAttribute("append") String s2, @PluginAttribute("name") String s3, @PluginAttribute("immediateFlush") String s4, @PluginElement("Policy") TriggeringPolicy triggeringpolicy, @PluginElement("Strategy") RolloverStrategy rolloverstrategy, @PluginElement("Layout") Layout layout, @PluginElement("Filter") Filter filter, @PluginAttribute("ignoreExceptions") String s5, @PluginAttribute("advertise") String s6, @PluginAttribute("advertiseURI") String s7, @PluginConfiguration Configuration configuration) {
        boolean flag = Booleans.parseBoolean(s2, true);
        boolean flag1 = Booleans.parseBoolean(s5, true);
        boolean flag2 = Booleans.parseBoolean(s4, true);
        boolean flag3 = Boolean.parseBoolean(s6);

        if (s3 == null) {
            RollingRandomAccessFileAppender.LOGGER.error("No name provided for FileAppender");
            return null;
        } else if (s == null) {
            RollingRandomAccessFileAppender.LOGGER.error("No filename was provided for FileAppender with name " + s3);
            return null;
        } else if (s1 == null) {
            RollingRandomAccessFileAppender.LOGGER.error("No filename pattern provided for FileAppender with name " + s3);
            return null;
        } else if (triggeringpolicy == null) {
            RollingRandomAccessFileAppender.LOGGER.error("A TriggeringPolicy must be provided");
            return null;
        } else {
            if (rolloverstrategy == null) {
                rolloverstrategy = DefaultRolloverStrategy.createStrategy((String) null, (String) null, (String) null, String.valueOf(-1), configuration);
            }

            if (layout == null) {
                layout = PatternLayout.createLayout((String) null, (Configuration) null, (RegexReplacement) null, (String) null, (String) null);
            }

            RollingRandomAccessFileManager rollingrandomaccessfilemanager = RollingRandomAccessFileManager.getRollingRandomAccessFileManager(s, s1, flag, flag2, triggeringpolicy, (RolloverStrategy) rolloverstrategy, s7, (Layout) layout);

            return rollingrandomaccessfilemanager == null ? null : new RollingRandomAccessFileAppender(s3, (Layout) layout, filter, rollingrandomaccessfilemanager, s, s1, flag1, flag2, flag3 ? configuration.getAdvertiser() : null);
        }
    }
}
