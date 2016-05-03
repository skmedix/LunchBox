package org.apache.logging.log4j.core.layout;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.helpers.Throwables;
import org.apache.logging.log4j.core.helpers.Transform;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MultiformatMessage;

@Plugin(
    name = "JSONLayout",
    category = "Core",
    elementType = "layout",
    printObject = true
)
public class JSONLayout extends AbstractStringLayout {

    private static final int DEFAULT_SIZE = 256;
    private static final String DEFAULT_EOL = "\r\n";
    private static final String COMPACT_EOL = "";
    private static final String DEFAULT_INDENT = "  ";
    private static final String COMPACT_INDENT = "";
    private static final String[] FORMATS = new String[] { "json"};
    private final boolean locationInfo;
    private final boolean properties;
    private final boolean complete;
    private final String eol;
    private final String indent1;
    private final String indent2;
    private final String indent3;
    private final String indent4;
    private volatile boolean firstLayoutDone;

    protected JSONLayout(boolean flag, boolean flag1, boolean flag2, boolean flag3, Charset charset) {
        super(charset);
        this.locationInfo = flag;
        this.properties = flag1;
        this.complete = flag2;
        this.eol = flag3 ? "" : "\r\n";
        this.indent1 = flag3 ? "" : "  ";
        this.indent2 = this.indent1 + this.indent1;
        this.indent3 = this.indent2 + this.indent1;
        this.indent4 = this.indent3 + this.indent1;
    }

    public String toSerializable(LogEvent logevent) {
        StringBuilder stringbuilder = new StringBuilder(256);
        boolean flag = this.firstLayoutDone;

        if (!this.firstLayoutDone) {
            synchronized (this) {
                flag = this.firstLayoutDone;
                if (!flag) {
                    this.firstLayoutDone = true;
                } else {
                    stringbuilder.append(',');
                    stringbuilder.append(this.eol);
                }
            }
        } else {
            stringbuilder.append(',');
            stringbuilder.append(this.eol);
        }

        stringbuilder.append(this.indent1);
        stringbuilder.append('{');
        stringbuilder.append(this.eol);
        stringbuilder.append(this.indent2);
        stringbuilder.append("\"logger\":\"");
        String s = logevent.getLoggerName();

        if (s.isEmpty()) {
            s = "root";
        }

        stringbuilder.append(Transform.escapeJsonControlCharacters(s));
        stringbuilder.append("\",");
        stringbuilder.append(this.eol);
        stringbuilder.append(this.indent2);
        stringbuilder.append("\"timestamp\":\"");
        stringbuilder.append(logevent.getMillis());
        stringbuilder.append("\",");
        stringbuilder.append(this.eol);
        stringbuilder.append(this.indent2);
        stringbuilder.append("\"level\":\"");
        stringbuilder.append(Transform.escapeJsonControlCharacters(String.valueOf(logevent.getLevel())));
        stringbuilder.append("\",");
        stringbuilder.append(this.eol);
        stringbuilder.append(this.indent2);
        stringbuilder.append("\"thread\":\"");
        stringbuilder.append(Transform.escapeJsonControlCharacters(logevent.getThreadName()));
        stringbuilder.append("\",");
        stringbuilder.append(this.eol);
        Message message = logevent.getMessage();

        if (message != null) {
            boolean flag1 = false;

            if (message instanceof MultiformatMessage) {
                String[] astring = ((MultiformatMessage) message).getFormats();
                String[] astring1 = astring;
                int i = astring.length;

                for (int j = 0; j < i; ++j) {
                    String s1 = astring1[j];

                    if (s1.equalsIgnoreCase("JSON")) {
                        flag1 = true;
                        break;
                    }
                }
            }

            stringbuilder.append(this.indent2);
            stringbuilder.append("\"message\":\"");
            if (flag1) {
                stringbuilder.append(((MultiformatMessage) message).getFormattedMessage(JSONLayout.FORMATS));
            } else {
                Transform.appendEscapingCDATA(stringbuilder, logevent.getMessage().getFormattedMessage());
            }

            stringbuilder.append('\"');
        }

        if (logevent.getContextStack().getDepth() > 0) {
            stringbuilder.append(",");
            stringbuilder.append(this.eol);
            stringbuilder.append("\"ndc\":");
            Transform.appendEscapingCDATA(stringbuilder, logevent.getContextStack().toString());
            stringbuilder.append("\"");
        }

        Throwable throwable = logevent.getThrown();

        if (throwable != null) {
            stringbuilder.append(",");
            stringbuilder.append(this.eol);
            stringbuilder.append(this.indent2);
            stringbuilder.append("\"throwable\":\"");
            List list = Throwables.toStringList(throwable);
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                String s2 = (String) iterator.next();

                stringbuilder.append(Transform.escapeJsonControlCharacters(s2));
                stringbuilder.append("\\\\n");
            }

            stringbuilder.append("\"");
        }

        if (this.locationInfo) {
            StackTraceElement stacktraceelement = logevent.getSource();

            stringbuilder.append(",");
            stringbuilder.append(this.eol);
            stringbuilder.append(this.indent2);
            stringbuilder.append("\"LocationInfo\":{");
            stringbuilder.append(this.eol);
            stringbuilder.append(this.indent3);
            stringbuilder.append("\"class\":\"");
            stringbuilder.append(Transform.escapeJsonControlCharacters(stacktraceelement.getClassName()));
            stringbuilder.append("\",");
            stringbuilder.append(this.eol);
            stringbuilder.append(this.indent3);
            stringbuilder.append("\"method\":\"");
            stringbuilder.append(Transform.escapeJsonControlCharacters(stacktraceelement.getMethodName()));
            stringbuilder.append("\",");
            stringbuilder.append(this.eol);
            stringbuilder.append(this.indent3);
            stringbuilder.append("\"file\":\"");
            stringbuilder.append(Transform.escapeJsonControlCharacters(stacktraceelement.getFileName()));
            stringbuilder.append("\",");
            stringbuilder.append(this.eol);
            stringbuilder.append(this.indent3);
            stringbuilder.append("\"line\":\"");
            stringbuilder.append(stacktraceelement.getLineNumber());
            stringbuilder.append("\"");
            stringbuilder.append(this.eol);
            stringbuilder.append(this.indent2);
            stringbuilder.append("}");
        }

        if (this.properties && logevent.getContextMap().size() > 0) {
            stringbuilder.append(",");
            stringbuilder.append(this.eol);
            stringbuilder.append(this.indent2);
            stringbuilder.append("\"Properties\":[");
            stringbuilder.append(this.eol);
            Set set = logevent.getContextMap().entrySet();
            int k = 1;

            for (Iterator iterator1 = set.iterator(); iterator1.hasNext(); ++k) {
                Entry entry = (Entry) iterator1.next();

                stringbuilder.append(this.indent3);
                stringbuilder.append('{');
                stringbuilder.append(this.eol);
                stringbuilder.append(this.indent4);
                stringbuilder.append("\"name\":\"");
                stringbuilder.append(Transform.escapeJsonControlCharacters((String) entry.getKey()));
                stringbuilder.append("\",");
                stringbuilder.append(this.eol);
                stringbuilder.append(this.indent4);
                stringbuilder.append("\"value\":\"");
                stringbuilder.append(Transform.escapeJsonControlCharacters(String.valueOf(entry.getValue())));
                stringbuilder.append("\"");
                stringbuilder.append(this.eol);
                stringbuilder.append(this.indent3);
                stringbuilder.append("}");
                if (k < set.size()) {
                    stringbuilder.append(",");
                }

                stringbuilder.append(this.eol);
            }

            stringbuilder.append(this.indent2);
            stringbuilder.append("]");
        }

        stringbuilder.append(this.eol);
        stringbuilder.append(this.indent1);
        stringbuilder.append("}");
        return stringbuilder.toString();
    }

    public byte[] getHeader() {
        if (!this.complete) {
            return null;
        } else {
            StringBuilder stringbuilder = new StringBuilder();

            stringbuilder.append('[');
            stringbuilder.append(this.eol);
            return stringbuilder.toString().getBytes(this.getCharset());
        }
    }

    public byte[] getFooter() {
        return !this.complete ? null : (this.eol + "]" + this.eol).getBytes(this.getCharset());
    }

    public Map getContentFormat() {
        HashMap hashmap = new HashMap();

        hashmap.put("version", "2.0");
        return hashmap;
    }

    public String getContentType() {
        return "application/json; charset=" + this.getCharset();
    }

    @PluginFactory
    public static JSONLayout createLayout(@PluginAttribute("locationInfo") String s, @PluginAttribute("properties") String s1, @PluginAttribute("complete") String s2, @PluginAttribute("compact") String s3, @PluginAttribute("charset") String s4) {
        Charset charset = Charsets.getSupportedCharset(s4, Charsets.UTF_8);
        boolean flag = Boolean.parseBoolean(s);
        boolean flag1 = Boolean.parseBoolean(s1);
        boolean flag2 = Boolean.parseBoolean(s2);
        boolean flag3 = Boolean.parseBoolean(s3);

        return new JSONLayout(flag, flag1, flag2, flag3, charset);
    }
}
