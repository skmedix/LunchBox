package org.apache.logging.log4j.core.layout;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.TLSSyslogFrame;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.helpers.Integers;
import org.apache.logging.log4j.core.helpers.NetUtils;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.net.Facility;
import org.apache.logging.log4j.core.net.Priority;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;
import org.apache.logging.log4j.core.pattern.ThrowablePatternConverter;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.StructuredDataId;
import org.apache.logging.log4j.message.StructuredDataMessage;

@Plugin(
    name = "RFC5424Layout",
    category = "Core",
    elementType = "layout",
    printObject = true
)
public class RFC5424Layout extends AbstractStringLayout {

    private static final String LF = "\n";
    public static final int DEFAULT_ENTERPRISE_NUMBER = 18060;
    public static final String DEFAULT_ID = "Audit";
    public static final Pattern NEWLINE_PATTERN = Pattern.compile("\\r?\\n");
    public static final Pattern PARAM_VALUE_ESCAPE_PATTERN = Pattern.compile("[\\\"\\]\\\\]");
    protected static final String DEFAULT_MDCID = "mdc";
    private static final int TWO_DIGITS = 10;
    private static final int THREE_DIGITS = 100;
    private static final int MILLIS_PER_MINUTE = 60000;
    private static final int MINUTES_PER_HOUR = 60;
    private static final String COMPONENT_KEY = "RFC5424-Converter";
    private final Facility facility;
    private final String defaultId;
    private final int enterpriseNumber;
    private final boolean includeMDC;
    private final String mdcId;
    private final StructuredDataId mdcSDID;
    private final String localHostName;
    private final String appName;
    private final String messageId;
    private final String configName;
    private final String mdcPrefix;
    private final String eventPrefix;
    private final List mdcExcludes;
    private final List mdcIncludes;
    private final List mdcRequired;
    private final RFC5424Layout.ListChecker checker;
    private final RFC5424Layout.ListChecker noopChecker = new RFC5424Layout.NoopChecker((RFC5424Layout.SyntheticClass_1) null);
    private final boolean includeNewLine;
    private final String escapeNewLine;
    private final boolean useTLSMessageFormat;
    private long lastTimestamp = -1L;
    private String timestamppStr;
    private final List exceptionFormatters;
    private final Map fieldFormatters;

    private RFC5424Layout(Configuration configuration, Facility facility, String s, int i, boolean flag, boolean flag1, String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8, String s9, Charset charset, String s10, boolean flag2, LoggerFields[] aloggerfields) {
        super(charset);
        PatternParser patternparser = createPatternParser(configuration, ThrowablePatternConverter.class);

        this.exceptionFormatters = s10 == null ? null : patternparser.parse(s10, false);
        this.facility = facility;
        this.defaultId = s == null ? "Audit" : s;
        this.enterpriseNumber = i;
        this.includeMDC = flag;
        this.includeNewLine = flag1;
        this.escapeNewLine = s1 == null ? null : Matcher.quoteReplacement(s1);
        this.mdcId = s2;
        this.mdcSDID = new StructuredDataId(s2, this.enterpriseNumber, (String[]) null, (String[]) null);
        this.mdcPrefix = s3;
        this.eventPrefix = s4;
        this.appName = s5;
        this.messageId = s6;
        this.useTLSMessageFormat = flag2;
        this.localHostName = NetUtils.getLocalHostname();
        Object object = null;
        String[] astring;
        String[] astring1;
        int j;
        int k;
        String s11;

        if (s7 != null) {
            astring = s7.split(",");
            if (astring.length > 0) {
                object = new RFC5424Layout.ExcludeChecker((RFC5424Layout.SyntheticClass_1) null);
                this.mdcExcludes = new ArrayList(astring.length);
                astring1 = astring;
                j = astring.length;

                for (k = 0; k < j; ++k) {
                    s11 = astring1[k];
                    this.mdcExcludes.add(s11.trim());
                }
            } else {
                this.mdcExcludes = null;
            }
        } else {
            this.mdcExcludes = null;
        }

        if (s8 != null) {
            astring = s8.split(",");
            if (astring.length > 0) {
                object = new RFC5424Layout.IncludeChecker((RFC5424Layout.SyntheticClass_1) null);
                this.mdcIncludes = new ArrayList(astring.length);
                astring1 = astring;
                j = astring.length;

                for (k = 0; k < j; ++k) {
                    s11 = astring1[k];
                    this.mdcIncludes.add(s11.trim());
                }
            } else {
                this.mdcIncludes = null;
            }
        } else {
            this.mdcIncludes = null;
        }

        if (s9 != null) {
            astring = s9.split(",");
            if (astring.length > 0) {
                this.mdcRequired = new ArrayList(astring.length);
                astring1 = astring;
                j = astring.length;

                for (k = 0; k < j; ++k) {
                    s11 = astring1[k];
                    this.mdcRequired.add(s11.trim());
                }
            } else {
                this.mdcRequired = null;
            }
        } else {
            this.mdcRequired = null;
        }

        this.checker = (RFC5424Layout.ListChecker) (object != null ? object : this.noopChecker);
        String s12 = configuration == null ? null : configuration.getName();

        this.configName = s12 != null && s12.length() > 0 ? s12 : null;
        this.fieldFormatters = this.createFieldFormatters(aloggerfields, configuration);
    }

    private Map createFieldFormatters(LoggerFields[] aloggerfields, Configuration configuration) {
        HashMap hashmap = new HashMap();

        if (aloggerfields != null) {
            LoggerFields[] aloggerfields1 = aloggerfields;
            int i = aloggerfields.length;

            for (int j = 0; j < i; ++j) {
                LoggerFields loggerfields = aloggerfields1[j];
                StructuredDataId structureddataid = loggerfields.getSdId() == null ? this.mdcSDID : loggerfields.getSdId();
                HashMap hashmap1 = new HashMap();
                Map map = loggerfields.getMap();

                if (!map.isEmpty()) {
                    PatternParser patternparser = createPatternParser(configuration, (Class) null);
                    Iterator iterator = map.entrySet().iterator();

                    while (iterator.hasNext()) {
                        Entry entry = (Entry) iterator.next();
                        List list = patternparser.parse((String) entry.getValue(), false);

                        hashmap1.put(entry.getKey(), list);
                    }

                    RFC5424Layout.FieldFormatter rfc5424layout_fieldformatter = new RFC5424Layout.FieldFormatter(hashmap1, loggerfields.getDiscardIfAllFieldsAreEmpty());

                    hashmap.put(structureddataid.toString(), rfc5424layout_fieldformatter);
                }
            }
        }

        return hashmap.size() > 0 ? hashmap : null;
    }

    private static PatternParser createPatternParser(Configuration configuration, Class oclass) {
        if (configuration == null) {
            return new PatternParser(configuration, "Converter", LogEventPatternConverter.class, oclass);
        } else {
            PatternParser patternparser = (PatternParser) configuration.getComponent("RFC5424-Converter");

            if (patternparser == null) {
                patternparser = new PatternParser(configuration, "Converter", ThrowablePatternConverter.class);
                configuration.addComponent("RFC5424-Converter", patternparser);
                patternparser = (PatternParser) configuration.getComponent("RFC5424-Converter");
            }

            return patternparser;
        }
    }

    public Map getContentFormat() {
        HashMap hashmap = new HashMap();

        hashmap.put("structured", "true");
        hashmap.put("formatType", "RFC5424");
        return hashmap;
    }

    public String toSerializable(LogEvent logevent) {
        StringBuilder stringbuilder = new StringBuilder();

        this.appendPriority(stringbuilder, logevent.getLevel());
        this.appendTimestamp(stringbuilder, logevent.getMillis());
        this.appendSpace(stringbuilder);
        this.appendHostName(stringbuilder);
        this.appendSpace(stringbuilder);
        this.appendAppName(stringbuilder);
        this.appendSpace(stringbuilder);
        this.appendProcessId(stringbuilder);
        this.appendSpace(stringbuilder);
        this.appendMessageId(stringbuilder, logevent.getMessage());
        this.appendSpace(stringbuilder);
        this.appendStructuredElements(stringbuilder, logevent);
        this.appendMessage(stringbuilder, logevent);
        return this.useTLSMessageFormat ? (new TLSSyslogFrame(stringbuilder.toString())).toString() : stringbuilder.toString();
    }

    private void appendPriority(StringBuilder stringbuilder, Level level) {
        stringbuilder.append("<");
        stringbuilder.append(Priority.getPriority(this.facility, level));
        stringbuilder.append(">1 ");
    }

    private void appendTimestamp(StringBuilder stringbuilder, long i) {
        stringbuilder.append(this.computeTimeStampString(i));
    }

    private void appendSpace(StringBuilder stringbuilder) {
        stringbuilder.append(" ");
    }

    private void appendHostName(StringBuilder stringbuilder) {
        stringbuilder.append(this.localHostName);
    }

    private void appendAppName(StringBuilder stringbuilder) {
        if (this.appName != null) {
            stringbuilder.append(this.appName);
        } else if (this.configName != null) {
            stringbuilder.append(this.configName);
        } else {
            stringbuilder.append("-");
        }

    }

    private void appendProcessId(StringBuilder stringbuilder) {
        stringbuilder.append(this.getProcId());
    }

    private void appendMessageId(StringBuilder stringbuilder, Message message) {
        boolean flag = message instanceof StructuredDataMessage;
        String s = flag ? ((StructuredDataMessage) message).getType() : null;

        if (s != null) {
            stringbuilder.append(s);
        } else if (this.messageId != null) {
            stringbuilder.append(this.messageId);
        } else {
            stringbuilder.append("-");
        }

    }

    private void appendMessage(StringBuilder stringbuilder, LogEvent logevent) {
        Message message = logevent.getMessage();
        String s = message.getFormat();

        if (s != null && s.length() > 0) {
            stringbuilder.append(" ").append(this.escapeNewlines(s, this.escapeNewLine));
        }

        if (this.exceptionFormatters != null && logevent.getThrown() != null) {
            StringBuilder stringbuilder1 = new StringBuilder("\n");
            Iterator iterator = this.exceptionFormatters.iterator();

            while (iterator.hasNext()) {
                PatternFormatter patternformatter = (PatternFormatter) iterator.next();

                patternformatter.format(logevent, stringbuilder1);
            }

            stringbuilder.append(this.escapeNewlines(stringbuilder1.toString(), this.escapeNewLine));
        }

        if (this.includeNewLine) {
            stringbuilder.append("\n");
        }

    }

    private void appendStructuredElements(StringBuilder stringbuilder, LogEvent logevent) {
        Message message = logevent.getMessage();
        boolean flag = message instanceof StructuredDataMessage;

        if (!flag && this.fieldFormatters != null && this.fieldFormatters.size() == 0 && !this.includeMDC) {
            stringbuilder.append("-");
        } else {
            HashMap hashmap = new HashMap();
            Map map = logevent.getContextMap();

            if (this.mdcRequired != null) {
                this.checkRequired(map);
            }

            Iterator iterator;
            Entry entry;
            RFC5424Layout.StructuredDataElement rfc5424layout_structureddataelement;

            if (this.fieldFormatters != null) {
                iterator = this.fieldFormatters.entrySet().iterator();

                while (iterator.hasNext()) {
                    entry = (Entry) iterator.next();
                    String s = (String) entry.getKey();

                    rfc5424layout_structureddataelement = ((RFC5424Layout.FieldFormatter) entry.getValue()).format(logevent);
                    hashmap.put(s, rfc5424layout_structureddataelement);
                }
            }

            if (this.includeMDC && map.size() > 0) {
                RFC5424Layout.StructuredDataElement rfc5424layout_structureddataelement1;

                if (hashmap.containsKey(this.mdcSDID.toString())) {
                    rfc5424layout_structureddataelement1 = (RFC5424Layout.StructuredDataElement) hashmap.get(this.mdcSDID.toString());
                    rfc5424layout_structureddataelement1.union(map);
                    hashmap.put(this.mdcSDID.toString(), rfc5424layout_structureddataelement1);
                } else {
                    rfc5424layout_structureddataelement1 = new RFC5424Layout.StructuredDataElement(map, false);
                    hashmap.put(this.mdcSDID.toString(), rfc5424layout_structureddataelement1);
                }
            }

            if (flag) {
                StructuredDataMessage structureddatamessage = (StructuredDataMessage) message;
                Map map1 = structureddatamessage.getData();
                StructuredDataId structureddataid = structureddatamessage.getId();

                if (hashmap.containsKey(structureddataid.toString())) {
                    rfc5424layout_structureddataelement = (RFC5424Layout.StructuredDataElement) hashmap.get(structureddataid.toString());
                    rfc5424layout_structureddataelement.union(map1);
                    hashmap.put(structureddataid.toString(), rfc5424layout_structureddataelement);
                } else {
                    rfc5424layout_structureddataelement = new RFC5424Layout.StructuredDataElement(map1, false);
                    hashmap.put(structureddataid.toString(), rfc5424layout_structureddataelement);
                }
            }

            if (hashmap.size() == 0) {
                stringbuilder.append("-");
            } else {
                iterator = hashmap.entrySet().iterator();

                while (iterator.hasNext()) {
                    entry = (Entry) iterator.next();
                    this.formatStructuredElement((String) entry.getKey(), this.mdcPrefix, (RFC5424Layout.StructuredDataElement) entry.getValue(), stringbuilder, this.checker);
                }

            }
        }
    }

    private String escapeNewlines(String s, String s1) {
        return null == s1 ? s : RFC5424Layout.NEWLINE_PATTERN.matcher(s).replaceAll(s1);
    }

    protected String getProcId() {
        return "-";
    }

    protected List getMdcExcludes() {
        return this.mdcExcludes;
    }

    protected List getMdcIncludes() {
        return this.mdcIncludes;
    }

    private String computeTimeStampString(long i) {
        long j;

        synchronized (this) {
            j = this.lastTimestamp;
            if (i == this.lastTimestamp) {
                return this.timestamppStr;
            }
        }

        StringBuilder stringbuilder = new StringBuilder();
        GregorianCalendar gregoriancalendar = new GregorianCalendar();

        gregoriancalendar.setTimeInMillis(i);
        stringbuilder.append(Integer.toString(gregoriancalendar.get(1)));
        stringbuilder.append("-");
        this.pad(gregoriancalendar.get(2) + 1, 10, stringbuilder);
        stringbuilder.append("-");
        this.pad(gregoriancalendar.get(5), 10, stringbuilder);
        stringbuilder.append("T");
        this.pad(gregoriancalendar.get(11), 10, stringbuilder);
        stringbuilder.append(":");
        this.pad(gregoriancalendar.get(12), 10, stringbuilder);
        stringbuilder.append(":");
        this.pad(gregoriancalendar.get(13), 10, stringbuilder);
        int k = gregoriancalendar.get(14);

        if (k != 0) {
            stringbuilder.append('.');
            this.pad(k, 100, stringbuilder);
        }

        int l = (gregoriancalendar.get(15) + gregoriancalendar.get(16)) / '\uea60';

        if (l == 0) {
            stringbuilder.append("Z");
        } else {
            if (l < 0) {
                l = -l;
                stringbuilder.append("-");
            } else {
                stringbuilder.append("+");
            }

            int i1 = l / 60;

            l -= i1 * 60;
            this.pad(i1, 10, stringbuilder);
            stringbuilder.append(":");
            this.pad(l, 10, stringbuilder);
        }

        synchronized (this) {
            if (j == this.lastTimestamp) {
                this.lastTimestamp = i;
                this.timestamppStr = stringbuilder.toString();
            }
        }

        return stringbuilder.toString();
    }

    private void pad(int i, int j, StringBuilder stringbuilder) {
        for (; j > 1; j /= 10) {
            if (i < j) {
                stringbuilder.append("0");
            }
        }

        stringbuilder.append(Integer.toString(i));
    }

    private void formatStructuredElement(String s, String s1, RFC5424Layout.StructuredDataElement rfc5424layout_structureddataelement, StringBuilder stringbuilder, RFC5424Layout.ListChecker rfc5424layout_listchecker) {
        if ((s != null || this.defaultId != null) && !rfc5424layout_structureddataelement.discard()) {
            stringbuilder.append("[");
            stringbuilder.append(s);
            if (!this.mdcSDID.toString().equals(s)) {
                this.appendMap(s1, rfc5424layout_structureddataelement.getFields(), stringbuilder, this.noopChecker);
            } else {
                this.appendMap(s1, rfc5424layout_structureddataelement.getFields(), stringbuilder, rfc5424layout_listchecker);
            }

            stringbuilder.append("]");
        }
    }

    private String getId(StructuredDataId structureddataid) {
        StringBuilder stringbuilder = new StringBuilder();

        if (structureddataid != null && structureddataid.getName() != null) {
            stringbuilder.append(structureddataid.getName());
        } else {
            stringbuilder.append(this.defaultId);
        }

        int i = structureddataid != null ? structureddataid.getEnterpriseNumber() : this.enterpriseNumber;

        if (i < 0) {
            i = this.enterpriseNumber;
        }

        if (i >= 0) {
            stringbuilder.append("@").append(i);
        }

        return stringbuilder.toString();
    }

    private void checkRequired(Map map) {
        Iterator iterator = this.mdcRequired.iterator();

        String s;
        String s1;

        do {
            if (!iterator.hasNext()) {
                return;
            }

            s = (String) iterator.next();
            s1 = (String) map.get(s);
        } while (s1 != null);

        throw new LoggingException("Required key " + s + " is missing from the " + this.mdcId);
    }

    private void appendMap(String s, Map map, StringBuilder stringbuilder, RFC5424Layout.ListChecker rfc5424layout_listchecker) {
        TreeMap treemap = new TreeMap(map);
        Iterator iterator = treemap.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (rfc5424layout_listchecker.check((String) entry.getKey()) && entry.getValue() != null) {
                stringbuilder.append(" ");
                if (s != null) {
                    stringbuilder.append(s);
                }

                stringbuilder.append(this.escapeNewlines(this.escapeSDParams((String) entry.getKey()), this.escapeNewLine)).append("=\"").append(this.escapeNewlines(this.escapeSDParams((String) entry.getValue()), this.escapeNewLine)).append("\"");
            }
        }

    }

    private String escapeSDParams(String s) {
        return RFC5424Layout.PARAM_VALUE_ESCAPE_PATTERN.matcher(s).replaceAll("\\\\$0");
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append("facility=").append(this.facility.name());
        stringbuilder.append(" appName=").append(this.appName);
        stringbuilder.append(" defaultId=").append(this.defaultId);
        stringbuilder.append(" enterpriseNumber=").append(this.enterpriseNumber);
        stringbuilder.append(" newLine=").append(this.includeNewLine);
        stringbuilder.append(" includeMDC=").append(this.includeMDC);
        stringbuilder.append(" messageId=").append(this.messageId);
        return stringbuilder.toString();
    }

    @PluginFactory
    public static RFC5424Layout createLayout(@PluginAttribute("facility") String s, @PluginAttribute("id") String s1, @PluginAttribute("enterpriseNumber") String s2, @PluginAttribute("includeMDC") String s3, @PluginAttribute("mdcId") String s4, @PluginAttribute("mdcPrefix") String s5, @PluginAttribute("eventPrefix") String s6, @PluginAttribute("newLine") String s7, @PluginAttribute("newLineEscape") String s8, @PluginAttribute("appName") String s9, @PluginAttribute("messageId") String s10, @PluginAttribute("mdcExcludes") String s11, @PluginAttribute("mdcIncludes") String s12, @PluginAttribute("mdcRequired") String s13, @PluginAttribute("exceptionPattern") String s14, @PluginAttribute("useTLSMessageFormat") String s15, @PluginElement("LoggerFields") LoggerFields[] aloggerfields, @PluginConfiguration Configuration configuration) {
        Charset charset = Charsets.UTF_8;

        if (s12 != null && s11 != null) {
            RFC5424Layout.LOGGER.error("mdcIncludes and mdcExcludes are mutually exclusive. Includes wil be ignored");
            s12 = null;
        }

        Facility facility = Facility.toFacility(s, Facility.LOCAL0);
        int i = Integers.parseInt(s2, 18060);
        boolean flag = Booleans.parseBoolean(s3, true);
        boolean flag1 = Boolean.parseBoolean(s7);
        boolean flag2 = Booleans.parseBoolean(s15, false);

        if (s4 == null) {
            s4 = "mdc";
        }

        return new RFC5424Layout(configuration, facility, s1, i, flag, flag1, s8, s4, s5, s6, s9, s10, s11, s12, s13, charset, s14, flag2, aloggerfields);
    }

    static class SyntheticClass_1 {    }

    private class StructuredDataElement {

        private final Map fields;
        private final boolean discardIfEmpty;

        public StructuredDataElement(Map map, boolean flag) {
            this.discardIfEmpty = flag;
            this.fields = map;
        }

        boolean discard() {
            if (!this.discardIfEmpty) {
                return false;
            } else {
                boolean flag = false;
                Iterator iterator = this.fields.entrySet().iterator();

                while (iterator.hasNext()) {
                    Entry entry = (Entry) iterator.next();

                    if (Strings.isNotEmpty((CharSequence) entry.getValue())) {
                        flag = true;
                        break;
                    }
                }

                return !flag;
            }
        }

        void union(Map map) {
            this.fields.putAll(map);
        }

        Map getFields() {
            return this.fields;
        }
    }

    private class FieldFormatter {

        private final Map delegateMap;
        private final boolean discardIfEmpty;

        public FieldFormatter(Map map, boolean flag) {
            this.discardIfEmpty = flag;
            this.delegateMap = map;
        }

        public RFC5424Layout.StructuredDataElement format(LogEvent logevent) {
            HashMap hashmap = new HashMap();
            Iterator iterator = this.delegateMap.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                StringBuilder stringbuilder = new StringBuilder();
                Iterator iterator1 = ((List) entry.getValue()).iterator();

                while (iterator1.hasNext()) {
                    PatternFormatter patternformatter = (PatternFormatter) iterator1.next();

                    patternformatter.format(logevent, stringbuilder);
                }

                hashmap.put(entry.getKey(), stringbuilder.toString());
            }

            return RFC5424Layout.this.new StructuredDataElement(hashmap, this.discardIfEmpty);
        }
    }

    private class NoopChecker implements RFC5424Layout.ListChecker {

        private NoopChecker() {}

        public boolean check(String s) {
            return true;
        }

        NoopChecker(RFC5424Layout.SyntheticClass_1 rfc5424layout_syntheticclass_1) {
            this();
        }
    }

    private class ExcludeChecker implements RFC5424Layout.ListChecker {

        private ExcludeChecker() {}

        public boolean check(String s) {
            return !RFC5424Layout.this.mdcExcludes.contains(s);
        }

        ExcludeChecker(RFC5424Layout.SyntheticClass_1 rfc5424layout_syntheticclass_1) {
            this();
        }
    }

    private class IncludeChecker implements RFC5424Layout.ListChecker {

        private IncludeChecker() {}

        public boolean check(String s) {
            return RFC5424Layout.this.mdcIncludes.contains(s);
        }

        IncludeChecker(RFC5424Layout.SyntheticClass_1 rfc5424layout_syntheticclass_1) {
            this();
        }
    }

    private interface ListChecker {

        boolean check(String s);
    }
}
