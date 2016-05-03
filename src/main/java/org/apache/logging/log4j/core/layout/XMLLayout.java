package org.apache.logging.log4j.core.layout;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.helpers.Throwables;
import org.apache.logging.log4j.core.helpers.Transform;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MultiformatMessage;

@Plugin(
    name = "XMLLayout",
    category = "Core",
    elementType = "layout",
    printObject = true
)
public class XMLLayout extends AbstractStringLayout {

    private static final String XML_NAMESPACE = "http://logging.apache.org/log4j/2.0/events";
    private static final String ROOT_TAG = "Events";
    private static final int DEFAULT_SIZE = 256;
    private static final String DEFAULT_EOL = "\r\n";
    private static final String COMPACT_EOL = "";
    private static final String DEFAULT_INDENT = "  ";
    private static final String COMPACT_INDENT = "";
    private static final String DEFAULT_NS_PREFIX = "log4j";
    private static final String[] FORMATS = new String[] { "xml"};
    private final boolean locationInfo;
    private final boolean properties;
    private final boolean complete;
    private final String namespacePrefix;
    private final String eol;
    private final String indent1;
    private final String indent2;
    private final String indent3;

    protected XMLLayout(boolean flag, boolean flag1, boolean flag2, boolean flag3, String s, Charset charset) {
        super(charset);
        this.locationInfo = flag;
        this.properties = flag1;
        this.complete = flag2;
        this.eol = flag3 ? "" : "\r\n";
        this.indent1 = flag3 ? "" : "  ";
        this.indent2 = this.indent1 + this.indent1;
        this.indent3 = this.indent2 + this.indent1;
        this.namespacePrefix = (Strings.isEmpty(s) ? "log4j" : s) + ":";
    }

    public String toSerializable(LogEvent logevent) {
        StringBuilder stringbuilder = new StringBuilder(256);

        stringbuilder.append(this.indent1);
        stringbuilder.append('<');
        if (!this.complete) {
            stringbuilder.append(this.namespacePrefix);
        }

        stringbuilder.append("Event logger=\"");
        String s = logevent.getLoggerName();

        if (s.isEmpty()) {
            s = "root";
        }

        stringbuilder.append(Transform.escapeHtmlTags(s));
        stringbuilder.append("\" timestamp=\"");
        stringbuilder.append(logevent.getMillis());
        stringbuilder.append("\" level=\"");
        stringbuilder.append(Transform.escapeHtmlTags(String.valueOf(logevent.getLevel())));
        stringbuilder.append("\" thread=\"");
        stringbuilder.append(Transform.escapeHtmlTags(logevent.getThreadName()));
        stringbuilder.append("\">");
        stringbuilder.append(this.eol);
        Message message = logevent.getMessage();

        if (message != null) {
            boolean flag = false;

            if (message instanceof MultiformatMessage) {
                String[] astring = ((MultiformatMessage) message).getFormats();
                String[] astring1 = astring;
                int i = astring.length;

                for (int j = 0; j < i; ++j) {
                    String s1 = astring1[j];

                    if (s1.equalsIgnoreCase("XML")) {
                        flag = true;
                        break;
                    }
                }
            }

            stringbuilder.append(this.indent2);
            stringbuilder.append('<');
            if (!this.complete) {
                stringbuilder.append(this.namespacePrefix);
            }

            stringbuilder.append("Message>");
            if (flag) {
                stringbuilder.append(((MultiformatMessage) message).getFormattedMessage(XMLLayout.FORMATS));
            } else {
                stringbuilder.append("<![CDATA[");
                Transform.appendEscapingCDATA(stringbuilder, logevent.getMessage().getFormattedMessage());
                stringbuilder.append("]]>");
            }

            stringbuilder.append("</");
            if (!this.complete) {
                stringbuilder.append(this.namespacePrefix);
            }

            stringbuilder.append("Message>");
            stringbuilder.append(this.eol);
        }

        if (logevent.getContextStack().getDepth() > 0) {
            stringbuilder.append(this.indent2);
            stringbuilder.append('<');
            if (!this.complete) {
                stringbuilder.append(this.namespacePrefix);
            }

            stringbuilder.append("NDC><![CDATA[");
            Transform.appendEscapingCDATA(stringbuilder, logevent.getContextStack().toString());
            stringbuilder.append("]]></");
            if (!this.complete) {
                stringbuilder.append(this.namespacePrefix);
            }

            stringbuilder.append("NDC>");
            stringbuilder.append(this.eol);
        }

        Throwable throwable = logevent.getThrown();

        if (throwable != null) {
            List list = Throwables.toStringList(throwable);

            stringbuilder.append(this.indent2);
            stringbuilder.append('<');
            if (!this.complete) {
                stringbuilder.append(this.namespacePrefix);
            }

            stringbuilder.append("Throwable><![CDATA[");
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                String s2 = (String) iterator.next();

                Transform.appendEscapingCDATA(stringbuilder, s2);
                stringbuilder.append(this.eol);
            }

            stringbuilder.append("]]></");
            if (!this.complete) {
                stringbuilder.append(this.namespacePrefix);
            }

            stringbuilder.append("Throwable>");
            stringbuilder.append(this.eol);
        }

        if (this.locationInfo) {
            StackTraceElement stacktraceelement = logevent.getSource();

            stringbuilder.append(this.indent2);
            stringbuilder.append('<');
            if (!this.complete) {
                stringbuilder.append(this.namespacePrefix);
            }

            stringbuilder.append("LocationInfo class=\"");
            stringbuilder.append(Transform.escapeHtmlTags(stacktraceelement.getClassName()));
            stringbuilder.append("\" method=\"");
            stringbuilder.append(Transform.escapeHtmlTags(stacktraceelement.getMethodName()));
            stringbuilder.append("\" file=\"");
            stringbuilder.append(Transform.escapeHtmlTags(stacktraceelement.getFileName()));
            stringbuilder.append("\" line=\"");
            stringbuilder.append(stacktraceelement.getLineNumber());
            stringbuilder.append("\"/>");
            stringbuilder.append(this.eol);
        }

        if (this.properties && logevent.getContextMap().size() > 0) {
            stringbuilder.append(this.indent2);
            stringbuilder.append('<');
            if (!this.complete) {
                stringbuilder.append(this.namespacePrefix);
            }

            stringbuilder.append("Properties>");
            stringbuilder.append(this.eol);
            Iterator iterator1 = logevent.getContextMap().entrySet().iterator();

            while (iterator1.hasNext()) {
                Entry entry = (Entry) iterator1.next();

                stringbuilder.append(this.indent3);
                stringbuilder.append('<');
                if (!this.complete) {
                    stringbuilder.append(this.namespacePrefix);
                }

                stringbuilder.append("Data name=\"");
                stringbuilder.append(Transform.escapeHtmlTags((String) entry.getKey()));
                stringbuilder.append("\" value=\"");
                stringbuilder.append(Transform.escapeHtmlTags(String.valueOf(entry.getValue())));
                stringbuilder.append("\"/>");
                stringbuilder.append(this.eol);
            }

            stringbuilder.append(this.indent2);
            stringbuilder.append("</");
            if (!this.complete) {
                stringbuilder.append(this.namespacePrefix);
            }

            stringbuilder.append("Properties>");
            stringbuilder.append(this.eol);
        }

        stringbuilder.append(this.indent1);
        stringbuilder.append("</");
        if (!this.complete) {
            stringbuilder.append(this.namespacePrefix);
        }

        stringbuilder.append("Event>");
        stringbuilder.append(this.eol);
        return stringbuilder.toString();
    }

    public byte[] getHeader() {
        if (!this.complete) {
            return null;
        } else {
            StringBuilder stringbuilder = new StringBuilder();

            stringbuilder.append("<?xml version=\"1.0\" encoding=\"");
            stringbuilder.append(this.getCharset().name());
            stringbuilder.append("\"?>");
            stringbuilder.append(this.eol);
            stringbuilder.append('<');
            stringbuilder.append("Events");
            stringbuilder.append(" xmlns=\"http://logging.apache.org/log4j/2.0/events\">");
            stringbuilder.append(this.eol);
            return stringbuilder.toString().getBytes(this.getCharset());
        }
    }

    public byte[] getFooter() {
        return !this.complete ? null : ("</Events>" + this.eol).getBytes(this.getCharset());
    }

    public Map getContentFormat() {
        HashMap hashmap = new HashMap();

        hashmap.put("xsd", "log4j-events.xsd");
        hashmap.put("version", "2.0");
        return hashmap;
    }

    public String getContentType() {
        return "text/xml; charset=" + this.getCharset();
    }

    @PluginFactory
    public static XMLLayout createLayout(@PluginAttribute("locationInfo") String s, @PluginAttribute("properties") String s1, @PluginAttribute("complete") String s2, @PluginAttribute("compact") String s3, @PluginAttribute("namespacePrefix") String s4, @PluginAttribute("charset") String s5) {
        Charset charset = Charsets.getSupportedCharset(s5, Charsets.UTF_8);
        boolean flag = Boolean.parseBoolean(s);
        boolean flag1 = Boolean.parseBoolean(s1);
        boolean flag2 = Boolean.parseBoolean(s2);
        boolean flag3 = Boolean.parseBoolean(s3);

        return new XMLLayout(flag, flag1, flag2, flag3, s4, charset);
    }
}
