package org.apache.logging.log4j.core.appender;

import java.util.HashMap;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
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
    name = "RandomAccessFile",
    category = "Core",
    elementType = "appender",
    printObject = true
)
public final class RandomAccessFileAppender extends AbstractOutputStreamAppender {

    private final String fileName;
    private Object advertisement;
    private final Advertiser advertiser;

    private RandomAccessFileAppender(String s, Layout layout, Filter filter, RandomAccessFileManager randomaccessfilemanager, String s1, boolean flag, boolean flag1, Advertiser advertiser) {
        super(s, layout, filter, flag, flag1, randomaccessfilemanager);
        if (advertiser != null) {
            HashMap hashmap = new HashMap(layout.getContentFormat());

            hashmap.putAll(randomaccessfilemanager.getContentFormat());
            hashmap.put("contentType", layout.getContentType());
            hashmap.put("name", s);
            this.advertisement = advertiser.advertise(hashmap);
        }

        this.fileName = s1;
        this.advertiser = advertiser;
    }

    public void stop() {
        super.stop();
        if (this.advertiser != null) {
            this.advertiser.unadvertise(this.advertisement);
        }

    }

    public void append(LogEvent logevent) {
        ((RandomAccessFileManager) this.getManager()).setEndOfBatch(logevent.isEndOfBatch());
        super.append(logevent);
    }

    public String getFileName() {
        return this.fileName;
    }

    @PluginFactory
    public static RandomAccessFileAppender createAppender(@PluginAttribute("fileName") String s, @PluginAttribute("append") String s1, @PluginAttribute("name") String s2, @PluginAttribute("immediateFlush") String s3, @PluginAttribute("ignoreExceptions") String s4, @PluginElement("Layout") Layout layout, @PluginElement("Filters") Filter filter, @PluginAttribute("advertise") String s5, @PluginAttribute("advertiseURI") String s6, @PluginConfiguration Configuration configuration) {
        boolean flag = Booleans.parseBoolean(s1, true);
        boolean flag1 = Booleans.parseBoolean(s3, true);
        boolean flag2 = Booleans.parseBoolean(s4, true);
        boolean flag3 = Boolean.parseBoolean(s5);

        if (s2 == null) {
            RandomAccessFileAppender.LOGGER.error("No name provided for FileAppender");
            return null;
        } else if (s == null) {
            RandomAccessFileAppender.LOGGER.error("No filename provided for FileAppender with name " + s2);
            return null;
        } else {
            if (layout == null) {
                layout = PatternLayout.createLayout((String) null, (Configuration) null, (RegexReplacement) null, (String) null, (String) null);
            }

            RandomAccessFileManager randomaccessfilemanager = RandomAccessFileManager.getFileManager(s, flag, flag1, s6, (Layout) layout);

            return randomaccessfilemanager == null ? null : new RandomAccessFileAppender(s2, (Layout) layout, filter, randomaccessfilemanager, s, flag2, flag1, flag3 ? configuration.getAdvertiser() : null);
        }
    }
}
