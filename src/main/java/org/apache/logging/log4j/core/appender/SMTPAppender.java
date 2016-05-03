package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.layout.HTMLLayout;
import org.apache.logging.log4j.core.net.SMTPManager;

@Plugin(
    name = "SMTP",
    category = "Core",
    elementType = "appender",
    printObject = true
)
public final class SMTPAppender extends AbstractAppender {

    private static final int DEFAULT_BUFFER_SIZE = 512;
    protected final SMTPManager manager;

    private SMTPAppender(String s, Filter filter, Layout layout, SMTPManager smtpmanager, boolean flag) {
        super(s, filter, layout, flag);
        this.manager = smtpmanager;
    }

    @PluginFactory
    public static SMTPAppender createAppender(@PluginAttribute("name") String s, @PluginAttribute("to") String s1, @PluginAttribute("cc") String s2, @PluginAttribute("bcc") String s3, @PluginAttribute("from") String s4, @PluginAttribute("replyTo") String s5, @PluginAttribute("subject") String s6, @PluginAttribute("smtpProtocol") String s7, @PluginAttribute("smtpHost") String s8, @PluginAttribute("smtpPort") String s9, @PluginAttribute("smtpUsername") String s10, @PluginAttribute("smtpPassword") String s11, @PluginAttribute("smtpDebug") String s12, @PluginAttribute("bufferSize") String s13, @PluginElement("Layout") Layout layout, @PluginElement("Filter") Filter filter, @PluginAttribute("ignoreExceptions") String s14) {
        if (s == null) {
            SMTPAppender.LOGGER.error("No name provided for SMTPAppender");
            return null;
        } else {
            boolean flag = Booleans.parseBoolean(s14, true);
            int i = AbstractAppender.parseInt(s9, 0);
            boolean flag1 = Boolean.parseBoolean(s12);
            int j = s13 == null ? 512 : Integer.parseInt(s13);

            if (layout == null) {
                layout = HTMLLayout.createLayout((String) null, (String) null, (String) null, (String) null, (String) null, (String) null);
            }

            if (filter == null) {
                filter = ThresholdFilter.createFilter((String) null, (String) null, (String) null);
            }

            SMTPManager smtpmanager = SMTPManager.getSMTPManager(s1, s2, s3, s4, s5, s6, s7, s8, i, s10, s11, flag1, filter.toString(), j);

            return smtpmanager == null ? null : new SMTPAppender(s, (Filter) filter, (Layout) layout, smtpmanager, flag);
        }
    }

    public boolean isFiltered(LogEvent logevent) {
        boolean flag = super.isFiltered(logevent);

        if (flag) {
            this.manager.add(logevent);
        }

        return flag;
    }

    public void append(LogEvent logevent) {
        this.manager.sendEvents(this.getLayout(), logevent);
    }
}
