package org.apache.logging.log4j.core.layout;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.net.Facility;
import org.apache.logging.log4j.core.net.Priority;

@Plugin(
    name = "SyslogLayout",
    category = "Core",
    elementType = "layout",
    printObject = true
)
public class SyslogLayout extends AbstractStringLayout {

    public static final Pattern NEWLINE_PATTERN = Pattern.compile("\\r?\\n");
    private final Facility facility;
    private final boolean includeNewLine;
    private final String escapeNewLine;
    private final SimpleDateFormat dateFormat;
    private final String localHostname;

    protected SyslogLayout(Facility facility, boolean flag, String s, Charset charset) {
        super(charset);
        this.dateFormat = new SimpleDateFormat("MMM dd HH:mm:ss ", Locale.ENGLISH);
        this.localHostname = this.getLocalHostname();
        this.facility = facility;
        this.includeNewLine = flag;
        this.escapeNewLine = s == null ? null : Matcher.quoteReplacement(s);
    }

    public String toSerializable(LogEvent logevent) {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append("<");
        stringbuilder.append(Priority.getPriority(this.facility, logevent.getLevel()));
        stringbuilder.append(">");
        this.addDate(logevent.getMillis(), stringbuilder);
        stringbuilder.append(" ");
        stringbuilder.append(this.localHostname);
        stringbuilder.append(" ");
        String s = logevent.getMessage().getFormattedMessage();

        if (null != this.escapeNewLine) {
            s = SyslogLayout.NEWLINE_PATTERN.matcher(s).replaceAll(this.escapeNewLine);
        }

        stringbuilder.append(s);
        if (this.includeNewLine) {
            stringbuilder.append("\n");
        }

        return stringbuilder.toString();
    }

    private String getLocalHostname() {
        try {
            InetAddress inetaddress = InetAddress.getLocalHost();

            return inetaddress.getHostName();
        } catch (UnknownHostException unknownhostexception) {
            SyslogLayout.LOGGER.error("Could not determine local host name", (Throwable) unknownhostexception);
            return "UNKNOWN_LOCALHOST";
        }
    }

    private synchronized void addDate(long i, StringBuilder stringbuilder) {
        int j = stringbuilder.length() + 4;

        stringbuilder.append(this.dateFormat.format(new Date(i)));
        if (stringbuilder.charAt(j) == 48) {
            stringbuilder.setCharAt(j, ' ');
        }

    }

    public Map getContentFormat() {
        HashMap hashmap = new HashMap();

        hashmap.put("structured", "false");
        hashmap.put("formatType", "logfilepatternreceiver");
        hashmap.put("dateFormat", this.dateFormat.toPattern());
        hashmap.put("format", "<LEVEL>TIMESTAMP PROP(HOSTNAME) MESSAGE");
        return hashmap;
    }

    @PluginFactory
    public static SyslogLayout createLayout(@PluginAttribute("facility") String s, @PluginAttribute("newLine") String s1, @PluginAttribute("newLineEscape") String s2, @PluginAttribute("charset") String s3) {
        Charset charset = Charsets.getSupportedCharset(s3);
        boolean flag = Boolean.parseBoolean(s1);
        Facility facility = Facility.toFacility(s, Facility.LOCAL0);

        return new SyslogLayout(facility, flag, s2, charset);
    }
}
