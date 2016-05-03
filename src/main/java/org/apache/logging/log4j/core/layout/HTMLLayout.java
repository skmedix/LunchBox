package org.apache.logging.log4j.core.layout;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.helpers.Constants;
import org.apache.logging.log4j.core.helpers.Transform;

@Plugin(
    name = "HTMLLayout",
    category = "Core",
    elementType = "layout",
    printObject = true
)
public final class HTMLLayout extends AbstractStringLayout {

    private static final int BUF_SIZE = 256;
    private static final String TRACE_PREFIX = "<br />&nbsp;&nbsp;&nbsp;&nbsp;";
    private static final String REGEXP = Constants.LINE_SEP.equals("\n") ? "\n" : Constants.LINE_SEP + "|\n";
    private static final String DEFAULT_TITLE = "Log4j Log Messages";
    private static final String DEFAULT_CONTENT_TYPE = "text/html";
    private final long jvmStartTime = ManagementFactory.getRuntimeMXBean().getStartTime();
    private final boolean locationInfo;
    private final String title;
    private final String contentType;
    private final String font;
    private final String fontSize;
    private final String headerSize;

    private HTMLLayout(boolean flag, String s, String s1, Charset charset, String s2, String s3, String s4) {
        super(charset);
        this.locationInfo = flag;
        this.title = s;
        this.contentType = s1;
        this.font = s2;
        this.fontSize = s3;
        this.headerSize = s4;
    }

    public String toSerializable(LogEvent logevent) {
        StringBuilder stringbuilder = new StringBuilder(256);

        stringbuilder.append(Constants.LINE_SEP).append("<tr>").append(Constants.LINE_SEP);
        stringbuilder.append("<td>");
        stringbuilder.append(logevent.getMillis() - this.jvmStartTime);
        stringbuilder.append("</td>").append(Constants.LINE_SEP);
        String s = Transform.escapeHtmlTags(logevent.getThreadName());

        stringbuilder.append("<td title=\"").append(s).append(" thread\">");
        stringbuilder.append(s);
        stringbuilder.append("</td>").append(Constants.LINE_SEP);
        stringbuilder.append("<td title=\"Level\">");
        if (logevent.getLevel().equals(Level.DEBUG)) {
            stringbuilder.append("<font color=\"#339933\">");
            stringbuilder.append(Transform.escapeHtmlTags(String.valueOf(logevent.getLevel())));
            stringbuilder.append("</font>");
        } else if (logevent.getLevel().isAtLeastAsSpecificAs(Level.WARN)) {
            stringbuilder.append("<font color=\"#993300\"><strong>");
            stringbuilder.append(Transform.escapeHtmlTags(String.valueOf(logevent.getLevel())));
            stringbuilder.append("</strong></font>");
        } else {
            stringbuilder.append(Transform.escapeHtmlTags(String.valueOf(logevent.getLevel())));
        }

        stringbuilder.append("</td>").append(Constants.LINE_SEP);
        String s1 = Transform.escapeHtmlTags(logevent.getLoggerName());

        if (s1.isEmpty()) {
            s1 = "root";
        }

        stringbuilder.append("<td title=\"").append(s1).append(" logger\">");
        stringbuilder.append(s1);
        stringbuilder.append("</td>").append(Constants.LINE_SEP);
        if (this.locationInfo) {
            StackTraceElement stacktraceelement = logevent.getSource();

            stringbuilder.append("<td>");
            stringbuilder.append(Transform.escapeHtmlTags(stacktraceelement.getFileName()));
            stringbuilder.append(':');
            stringbuilder.append(stacktraceelement.getLineNumber());
            stringbuilder.append("</td>").append(Constants.LINE_SEP);
        }

        stringbuilder.append("<td title=\"Message\">");
        stringbuilder.append(Transform.escapeHtmlTags(logevent.getMessage().getFormattedMessage()).replaceAll(HTMLLayout.REGEXP, "<br />"));
        stringbuilder.append("</td>").append(Constants.LINE_SEP);
        stringbuilder.append("</tr>").append(Constants.LINE_SEP);
        if (logevent.getContextStack().getDepth() > 0) {
            stringbuilder.append("<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : ").append(this.fontSize);
            stringbuilder.append(";\" colspan=\"6\" ");
            stringbuilder.append("title=\"Nested Diagnostic Context\">");
            stringbuilder.append("NDC: ").append(Transform.escapeHtmlTags(logevent.getContextStack().toString()));
            stringbuilder.append("</td></tr>").append(Constants.LINE_SEP);
        }

        if (logevent.getContextMap().size() > 0) {
            stringbuilder.append("<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : ").append(this.fontSize);
            stringbuilder.append(";\" colspan=\"6\" ");
            stringbuilder.append("title=\"Mapped Diagnostic Context\">");
            stringbuilder.append("MDC: ").append(Transform.escapeHtmlTags(logevent.getContextMap().toString()));
            stringbuilder.append("</td></tr>").append(Constants.LINE_SEP);
        }

        Throwable throwable = logevent.getThrown();

        if (throwable != null) {
            stringbuilder.append("<tr><td bgcolor=\"#993300\" style=\"color:White; font-size : ").append(this.fontSize);
            stringbuilder.append(";\" colspan=\"6\">");
            this.appendThrowableAsHTML(throwable, stringbuilder);
            stringbuilder.append("</td></tr>").append(Constants.LINE_SEP);
        }

        return stringbuilder.toString();
    }

    public Map getContentFormat() {
        return new HashMap();
    }

    public String getContentType() {
        return this.contentType;
    }

    private void appendThrowableAsHTML(Throwable throwable, StringBuilder stringbuilder) {
        StringWriter stringwriter = new StringWriter();
        PrintWriter printwriter = new PrintWriter(stringwriter);

        try {
            throwable.printStackTrace(printwriter);
        } catch (RuntimeException runtimeexception) {
            ;
        }

        printwriter.flush();
        LineNumberReader linenumberreader = new LineNumberReader(new StringReader(stringwriter.toString()));
        ArrayList arraylist = new ArrayList();

        try {
            for (String s = linenumberreader.readLine(); s != null; s = linenumberreader.readLine()) {
                arraylist.add(s);
            }
        } catch (IOException ioexception) {
            if (ioexception instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }

            arraylist.add(ioexception.toString());
        }

        boolean flag = true;
        Iterator iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            String s1 = (String) iterator.next();

            if (!flag) {
                stringbuilder.append("<br />&nbsp;&nbsp;&nbsp;&nbsp;");
            } else {
                flag = false;
            }

            stringbuilder.append(Transform.escapeHtmlTags(s1));
            stringbuilder.append(Constants.LINE_SEP);
        }

    }

    public byte[] getHeader() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" ");
        stringbuilder.append("\"http://www.w3.org/TR/html4/loose.dtd\">");
        stringbuilder.append(Constants.LINE_SEP);
        stringbuilder.append("<html>").append(Constants.LINE_SEP);
        stringbuilder.append("<head>").append(Constants.LINE_SEP);
        stringbuilder.append("<meta charset=\"").append(this.getCharset()).append("\"/>").append(Constants.LINE_SEP);
        stringbuilder.append("<title>").append(this.title).append("</title>").append(Constants.LINE_SEP);
        stringbuilder.append("<style type=\"text/css\">").append(Constants.LINE_SEP);
        stringbuilder.append("<!--").append(Constants.LINE_SEP);
        stringbuilder.append("body, table {font-family:").append(this.font).append("; font-size: ");
        stringbuilder.append(this.headerSize).append(";}").append(Constants.LINE_SEP);
        stringbuilder.append("th {background: #336699; color: #FFFFFF; text-align: left;}").append(Constants.LINE_SEP);
        stringbuilder.append("-->").append(Constants.LINE_SEP);
        stringbuilder.append("</style>").append(Constants.LINE_SEP);
        stringbuilder.append("</head>").append(Constants.LINE_SEP);
        stringbuilder.append("<body bgcolor=\"#FFFFFF\" topmargin=\"6\" leftmargin=\"6\">").append(Constants.LINE_SEP);
        stringbuilder.append("<hr size=\"1\" noshade>").append(Constants.LINE_SEP);
        stringbuilder.append("Log session start time " + new Date() + "<br>").append(Constants.LINE_SEP);
        stringbuilder.append("<br>").append(Constants.LINE_SEP);
        stringbuilder.append("<table cellspacing=\"0\" cellpadding=\"4\" border=\"1\" bordercolor=\"#224466\" width=\"100%\">");
        stringbuilder.append(Constants.LINE_SEP);
        stringbuilder.append("<tr>").append(Constants.LINE_SEP);
        stringbuilder.append("<th>Time</th>").append(Constants.LINE_SEP);
        stringbuilder.append("<th>Thread</th>").append(Constants.LINE_SEP);
        stringbuilder.append("<th>Level</th>").append(Constants.LINE_SEP);
        stringbuilder.append("<th>Logger</th>").append(Constants.LINE_SEP);
        if (this.locationInfo) {
            stringbuilder.append("<th>File:Line</th>").append(Constants.LINE_SEP);
        }

        stringbuilder.append("<th>Message</th>").append(Constants.LINE_SEP);
        stringbuilder.append("</tr>").append(Constants.LINE_SEP);
        return stringbuilder.toString().getBytes(this.getCharset());
    }

    public byte[] getFooter() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append("</table>").append(Constants.LINE_SEP);
        stringbuilder.append("<br>").append(Constants.LINE_SEP);
        stringbuilder.append("</body></html>");
        return stringbuilder.toString().getBytes(this.getCharset());
    }

    @PluginFactory
    public static HTMLLayout createLayout(@PluginAttribute("locationInfo") String s, @PluginAttribute("title") String s1, @PluginAttribute("contentType") String s2, @PluginAttribute("charset") String s3, @PluginAttribute("fontSize") String s4, @PluginAttribute("fontName") String s5) {
        Charset charset = Charsets.getSupportedCharset(s3, Charsets.UTF_8);

        if (s5 == null) {
            s5 = "arial,sans-serif";
        }

        HTMLLayout.FontSize htmllayout_fontsize = HTMLLayout.FontSize.getFontSize(s4);

        s4 = htmllayout_fontsize.getFontSize();
        String s6 = htmllayout_fontsize.larger().getFontSize();
        boolean flag = Boolean.parseBoolean(s);

        if (s1 == null) {
            s1 = "Log4j Log Messages";
        }

        if (s2 == null) {
            s2 = "text/html; charset=" + charset;
        }

        return new HTMLLayout(flag, s1, s2, charset, s5, s4, s6);
    }

    private static enum FontSize {

        SMALLER("smaller"), XXSMALL("xx-small"), XSMALL("x-small"), SMALL("small"), MEDIUM("medium"), LARGE("large"), XLARGE("x-large"), XXLARGE("xx-large"), LARGER("larger");

        private final String size;

        private FontSize(String s) {
            this.size = s;
        }

        public String getFontSize() {
            return this.size;
        }

        public static HTMLLayout.FontSize getFontSize(String s) {
            HTMLLayout.FontSize[] ahtmllayout_fontsize = values();
            int i = ahtmllayout_fontsize.length;

            for (int j = 0; j < i; ++j) {
                HTMLLayout.FontSize htmllayout_fontsize = ahtmllayout_fontsize[j];

                if (htmllayout_fontsize.size.equals(s)) {
                    return htmllayout_fontsize;
                }
            }

            return HTMLLayout.FontSize.SMALL;
        }

        public HTMLLayout.FontSize larger() {
            return this.ordinal() < HTMLLayout.FontSize.XXLARGE.ordinal() ? values()[this.ordinal() + 1] : this;
        }
    }
}
