package org.apache.commons.lang3;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.EntityArrays;
import org.apache.commons.lang3.text.translate.JavaUnicodeEscaper;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.text.translate.NumericEntityEscaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.OctalUnescaper;
import org.apache.commons.lang3.text.translate.UnicodeUnescaper;
import org.apache.commons.lang3.text.translate.UnicodeUnpairedSurrogateRemover;

public class StringEscapeUtils {

    public static final CharSequenceTranslator ESCAPE_JAVA = (new LookupTranslator(new String[][] { { "\"", "\\\""}, { "\\", "\\\\"}})).with(new CharSequenceTranslator[] { new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE())}).with(new CharSequenceTranslator[] { JavaUnicodeEscaper.outsideOf(32, 127)});
    public static final CharSequenceTranslator ESCAPE_ECMASCRIPT = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator(new String[][] { { "\'", "\\\'"}, { "\"", "\\\""}, { "\\", "\\\\"}, { "/", "\\/"}}), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE()), JavaUnicodeEscaper.outsideOf(32, 127)});
    public static final CharSequenceTranslator ESCAPE_JSON = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator(new String[][] { { "\"", "\\\""}, { "\\", "\\\\"}, { "/", "\\/"}}), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE()), JavaUnicodeEscaper.outsideOf(32, 127)});
    /** @deprecated */
    @Deprecated
    public static final CharSequenceTranslator ESCAPE_XML = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator(EntityArrays.BASIC_ESCAPE()), new LookupTranslator(EntityArrays.APOS_ESCAPE())});
    public static final CharSequenceTranslator ESCAPE_XML10 = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator(EntityArrays.BASIC_ESCAPE()), new LookupTranslator(EntityArrays.APOS_ESCAPE()), new LookupTranslator(new String[][] { { "\u0000", ""}, { "\u0001", ""}, { "\u0002", ""}, { "\u0003", ""}, { "\u0004", ""}, { "\u0005", ""}, { "\u0006", ""}, { "\u0007", ""}, { "\b", ""}, { "\u000b", ""}, { "\f", ""}, { "\u000e", ""}, { "\u000f", ""}, { "\u0010", ""}, { "\u0011", ""}, { "\u0012", ""}, { "\u0013", ""}, { "\u0014", ""}, { "\u0015", ""}, { "\u0016", ""}, { "\u0017", ""}, { "\u0018", ""}, { "\u0019", ""}, { "\u001a", ""}, { "\u001b", ""}, { "\u001c", ""}, { "\u001d", ""}, { "\u001e", ""}, { "\u001f", ""}, { "\ufffe", ""}, { "\uffff", ""}}), NumericEntityEscaper.between(127, 132), NumericEntityEscaper.between(134, 159), new UnicodeUnpairedSurrogateRemover()});
    public static final CharSequenceTranslator ESCAPE_XML11 = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator(EntityArrays.BASIC_ESCAPE()), new LookupTranslator(EntityArrays.APOS_ESCAPE()), new LookupTranslator(new String[][] { { "\u0000", ""}, { "\u000b", "&#11;"}, { "\f", "&#12;"}, { "\ufffe", ""}, { "\uffff", ""}}), NumericEntityEscaper.between(1, 8), NumericEntityEscaper.between(14, 31), NumericEntityEscaper.between(127, 132), NumericEntityEscaper.between(134, 159), new UnicodeUnpairedSurrogateRemover()});
    public static final CharSequenceTranslator ESCAPE_HTML3 = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator(EntityArrays.BASIC_ESCAPE()), new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE())});
    public static final CharSequenceTranslator ESCAPE_HTML4 = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator(EntityArrays.BASIC_ESCAPE()), new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE()), new LookupTranslator(EntityArrays.HTML40_EXTENDED_ESCAPE())});
    public static final CharSequenceTranslator ESCAPE_CSV = new StringEscapeUtils.CsvEscaper();
    public static final CharSequenceTranslator UNESCAPE_JAVA = new AggregateTranslator(new CharSequenceTranslator[] { new OctalUnescaper(), new UnicodeUnescaper(), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_UNESCAPE()), new LookupTranslator(new String[][] { { "\\\\", "\\"}, { "\\\"", "\""}, { "\\\'", "\'"}, { "\\", ""}})});
    public static final CharSequenceTranslator UNESCAPE_ECMASCRIPT = StringEscapeUtils.UNESCAPE_JAVA;
    public static final CharSequenceTranslator UNESCAPE_JSON = StringEscapeUtils.UNESCAPE_JAVA;
    public static final CharSequenceTranslator UNESCAPE_HTML3 = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator(EntityArrays.BASIC_UNESCAPE()), new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE()), new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0])});
    public static final CharSequenceTranslator UNESCAPE_HTML4 = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator(EntityArrays.BASIC_UNESCAPE()), new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE()), new LookupTranslator(EntityArrays.HTML40_EXTENDED_UNESCAPE()), new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0])});
    public static final CharSequenceTranslator UNESCAPE_XML = new AggregateTranslator(new CharSequenceTranslator[] { new LookupTranslator(EntityArrays.BASIC_UNESCAPE()), new LookupTranslator(EntityArrays.APOS_UNESCAPE()), new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0])});
    public static final CharSequenceTranslator UNESCAPE_CSV = new StringEscapeUtils.CsvUnescaper();

    public static final String escapeJava(String s) {
        return StringEscapeUtils.ESCAPE_JAVA.translate(s);
    }

    public static final String escapeEcmaScript(String s) {
        return StringEscapeUtils.ESCAPE_ECMASCRIPT.translate(s);
    }

    public static final String escapeJson(String s) {
        return StringEscapeUtils.ESCAPE_JSON.translate(s);
    }

    public static final String unescapeJava(String s) {
        return StringEscapeUtils.UNESCAPE_JAVA.translate(s);
    }

    public static final String unescapeEcmaScript(String s) {
        return StringEscapeUtils.UNESCAPE_ECMASCRIPT.translate(s);
    }

    public static final String unescapeJson(String s) {
        return StringEscapeUtils.UNESCAPE_JSON.translate(s);
    }

    public static final String escapeHtml4(String s) {
        return StringEscapeUtils.ESCAPE_HTML4.translate(s);
    }

    public static final String escapeHtml3(String s) {
        return StringEscapeUtils.ESCAPE_HTML3.translate(s);
    }

    public static final String unescapeHtml4(String s) {
        return StringEscapeUtils.UNESCAPE_HTML4.translate(s);
    }

    public static final String unescapeHtml3(String s) {
        return StringEscapeUtils.UNESCAPE_HTML3.translate(s);
    }

    /** @deprecated */
    @Deprecated
    public static final String escapeXml(String s) {
        return StringEscapeUtils.ESCAPE_XML.translate(s);
    }

    public static String escapeXml10(String s) {
        return StringEscapeUtils.ESCAPE_XML10.translate(s);
    }

    public static String escapeXml11(String s) {
        return StringEscapeUtils.ESCAPE_XML11.translate(s);
    }

    public static final String unescapeXml(String s) {
        return StringEscapeUtils.UNESCAPE_XML.translate(s);
    }

    public static final String escapeCsv(String s) {
        return StringEscapeUtils.ESCAPE_CSV.translate(s);
    }

    public static final String unescapeCsv(String s) {
        return StringEscapeUtils.UNESCAPE_CSV.translate(s);
    }

    static class CsvUnescaper extends CharSequenceTranslator {

        private static final char CSV_DELIMITER = ',';
        private static final char CSV_QUOTE = '\"';
        private static final String CSV_QUOTE_STR = String.valueOf('\"');
        private static final char[] CSV_SEARCH_CHARS = new char[] { ',', '\"', '\r', '\n'};

        public int translate(CharSequence charsequence, int i, Writer writer) throws IOException {
            if (i != 0) {
                throw new IllegalStateException("CsvUnescaper should never reach the [1] index");
            } else if (charsequence.charAt(0) == 34 && charsequence.charAt(charsequence.length() - 1) == 34) {
                String s = charsequence.subSequence(1, charsequence.length() - 1).toString();

                if (StringUtils.containsAny(s, StringEscapeUtils.CsvUnescaper.CSV_SEARCH_CHARS)) {
                    writer.write(StringUtils.replace(s, StringEscapeUtils.CsvUnescaper.CSV_QUOTE_STR + StringEscapeUtils.CsvUnescaper.CSV_QUOTE_STR, StringEscapeUtils.CsvUnescaper.CSV_QUOTE_STR));
                } else {
                    writer.write(charsequence.toString());
                }

                return Character.codePointCount(charsequence, 0, charsequence.length());
            } else {
                writer.write(charsequence.toString());
                return Character.codePointCount(charsequence, 0, charsequence.length());
            }
        }
    }

    static class CsvEscaper extends CharSequenceTranslator {

        private static final char CSV_DELIMITER = ',';
        private static final char CSV_QUOTE = '\"';
        private static final String CSV_QUOTE_STR = String.valueOf('\"');
        private static final char[] CSV_SEARCH_CHARS = new char[] { ',', '\"', '\r', '\n'};

        public int translate(CharSequence charsequence, int i, Writer writer) throws IOException {
            if (i != 0) {
                throw new IllegalStateException("CsvEscaper should never reach the [1] index");
            } else {
                if (StringUtils.containsNone(charsequence.toString(), StringEscapeUtils.CsvEscaper.CSV_SEARCH_CHARS)) {
                    writer.write(charsequence.toString());
                } else {
                    writer.write(34);
                    writer.write(StringUtils.replace(charsequence.toString(), StringEscapeUtils.CsvEscaper.CSV_QUOTE_STR, StringEscapeUtils.CsvEscaper.CSV_QUOTE_STR + StringEscapeUtils.CsvEscaper.CSV_QUOTE_STR));
                    writer.write(34);
                }

                return Character.codePointCount(charsequence, 0, charsequence.length());
            }
        }
    }
}
