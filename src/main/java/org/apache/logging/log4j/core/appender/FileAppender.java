package org.apache.logging.log4j.core.appender;

import java.util.HashMap;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
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
    name = "File",
    category = "Core",
    elementType = "appender",
    printObject = true
)
public final class FileAppender extends AbstractOutputStreamAppender {

    private final String fileName;
    private final Advertiser advertiser;
    private Object advertisement;

    private FileAppender(String s, Layout layout, Filter filter, FileManager filemanager, String s1, boolean flag, boolean flag1, Advertiser advertiser) {
        super(s, layout, filter, flag, flag1, filemanager);
        if (advertiser != null) {
            HashMap hashmap = new HashMap(layout.getContentFormat());

            hashmap.putAll(filemanager.getContentFormat());
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

    public String getFileName() {
        return this.fileName;
    }

    @PluginFactory
    public static FileAppender createAppender(@PluginAttribute("fileName") String s, @PluginAttribute("append") String s1, @PluginAttribute("locking") String s2, @PluginAttribute("name") String s3, @PluginAttribute("immediateFlush") String s4, @PluginAttribute("ignoreExceptions") String s5, @PluginAttribute("bufferedIO") String s6, @PluginElement("Layout") Layout layout, @PluginElement("Filters") Filter filter, @PluginAttribute("advertise") String s7, @PluginAttribute("advertiseURI") String s8, @PluginConfiguration Configuration configuration) {
        boolean flag = Booleans.parseBoolean(s1, true);
        boolean flag1 = Boolean.parseBoolean(s2);
        boolean flag2 = Booleans.parseBoolean(s6, true);
        boolean flag3 = Boolean.parseBoolean(s7);

        if (flag1 && flag2) {
            if (s6 != null) {
                FileAppender.LOGGER.warn("Locking and buffering are mutually exclusive. No buffering will occur for " + s);
            }

            flag2 = false;
        }

        boolean flag4 = Booleans.parseBoolean(s4, true);
        boolean flag5 = Booleans.parseBoolean(s5, true);

        if (s3 == null) {
            FileAppender.LOGGER.error("No name provided for FileAppender");
            return null;
        } else if (s == null) {
            FileAppender.LOGGER.error("No filename provided for FileAppender with name " + s3);
            return null;
        } else {
            if (layout == null) {
                layout = PatternLayout.createLayout((String) null, (Configuration) null, (RegexReplacement) null, (String) null, (String) null);
            }

            FileManager filemanager = FileManager.getFileManager(s, flag, flag1, flag2, s8, (Layout) layout);

            return filemanager == null ? null : new FileAppender(s3, (Layout) layout, filter, filemanager, s, flag5, flag4, flag3 ? configuration.getAdvertiser() : null);
        }
    }
}
