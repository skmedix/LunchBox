package org.apache.commons.lang;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.lang.text.StrBuilder;

public class StringEscapeUtils {

    private static final char CSV_DELIMITER = ',';
    private static final char CSV_QUOTE = '\"';
    private static final String CSV_QUOTE_STR = String.valueOf('\"');
    private static final char[] CSV_SEARCH_CHARS = new char[] { ',', '\"', '\r', '\n'};

    public static String escapeJava(String str) {
        return escapeJavaStyleString(str, false, false);
    }

    public static void escapeJava(Writer out, String str) throws IOException {
        escapeJavaStyleString(out, str, false, false);
    }

    public static String escapeJavaScript(String str) {
        return escapeJavaStyleString(str, true, true);
    }

    public static void escapeJavaScript(Writer out, String str) throws IOException {
        escapeJavaStyleString(out, str, true, true);
    }

    private static String escapeJavaStyleString(String str, boolean escapeSingleQuotes, boolean escapeForwardSlash) {
        if (str == null) {
            return null;
        } else {
            try {
                StringWriter ioe = new StringWriter(str.length() * 2);

                escapeJavaStyleString(ioe, str, escapeSingleQuotes, escapeForwardSlash);
                return ioe.toString();
            } catch (IOException ioexception) {
                throw new UnhandledException(ioexception);
            }
        }
    }

    private static void escapeJavaStyleString(Writer out, String str, boolean escapeSingleQuote, boolean escapeForwardSlash) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        } else if (str != null) {
            int sz = str.length();

            for (int i = 0; i < sz; ++i) {
                char ch = str.charAt(i);

                if (ch > 4095) {
                    out.write("\\u" + hex(ch));
                } else if (ch > 255) {
                    out.write("\\u0" + hex(ch));
                } else if (ch > 127) {
                    out.write("\\u00" + hex(ch));
                } else if (ch < 32) {
                    switch (ch) {
                    case '\b':
                        out.write(92);
                        out.write(98);
                        break;

                    case '\t':
                        out.write(92);
                        out.write(116);
                        break;

                    case '\n':
                        out.write(92);
                        out.write(110);
                        break;

                    case '\u000b':
                    default:
                        if (ch > 15) {
                            out.write("\\u00" + hex(ch));
                        } else {
                            out.write("\\u000" + hex(ch));
                        }
                        break;

                    case '\f':
                        out.write(92);
                        out.write(102);
                        break;

                    case '\r':
                        out.write(92);
                        out.write(114);
                    }
                } else {
                    switch (ch) {
                    case '\"':
                        out.write(92);
                        out.write(34);
                        break;

                    case '\'':
                        if (escapeSingleQuote) {
                            out.write(92);
                        }

                        out.write(39);
                        break;

                    case '/':
                        if (escapeForwardSlash) {
                            out.write(92);
                        }

                        out.write(47);
                        break;

                    case '\\':
                        out.write(92);
                        out.write(92);
                        break;

                    default:
                        out.write(ch);
                    }
                }
            }

        }
    }

    private static String hex(char ch) {
        return Integer.toHexString(ch).toUpperCase(Locale.ENGLISH);
    }

    public static String unescapeJava(String str) {
        if (str == null) {
            return null;
        } else {
            try {
                StringWriter ioe = new StringWriter(str.length());

                unescapeJava(ioe, str);
                return ioe.toString();
            } catch (IOException ioexception) {
                throw new UnhandledException(ioexception);
            }
        }
    }

    public static void unescapeJava(Writer out, String str) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        } else if (str != null) {
            int sz = str.length();
            StrBuilder unicode = new StrBuilder(4);
            boolean hadSlash = false;
            boolean inUnicode = false;

            for (int i = 0; i < sz; ++i) {
                char ch = str.charAt(i);

                if (inUnicode) {
                    unicode.append(ch);
                    if (unicode.length() == 4) {
                        try {
                            int nfe = Integer.parseInt(unicode.toString(), 16);

                            out.write((char) nfe);
                            unicode.setLength(0);
                            inUnicode = false;
                            hadSlash = false;
                        } catch (NumberFormatException numberformatexception) {
                            throw new NestableRuntimeException("Unable to parse unicode value: " + unicode, numberformatexception);
                        }
                    }
                } else if (hadSlash) {
                    hadSlash = false;
                    switch (ch) {
                    case '\"':
                        out.write(34);
                        break;

                    case '\'':
                        out.write(39);
                        break;

                    case '\\':
                        out.write(92);
                        break;

                    case 'b':
                        out.write(8);
                        break;

                    case 'f':
                        out.write(12);
                        break;

                    case 'n':
                        out.write(10);
                        break;

                    case 'r':
                        out.write(13);
                        break;

                    case 't':
                        out.write(9);
                        break;

                    case 'u':
                        inUnicode = true;
                        break;

                    default:
                        out.write(ch);
                    }
                } else if (ch == 92) {
                    hadSlash = true;
                } else {
                    out.write(ch);
                }
            }

            if (hadSlash) {
                out.write(92);
            }

        }
    }

    public static String unescapeJavaScript(String str) {
        return unescapeJava(str);
    }

    public static void unescapeJavaScript(Writer out, String str) throws IOException {
        unescapeJava(out, str);
    }

    public static String escapeHtml(String str) {
        if (str == null) {
            return null;
        } else {
            try {
                StringWriter ioe = new StringWriter((int) ((double) str.length() * 1.5D));

                escapeHtml(ioe, str);
                return ioe.toString();
            } catch (IOException ioexception) {
                throw new UnhandledException(ioexception);
            }
        }
    }

    public static void escapeHtml(Writer writer, String string) throws IOException {
        if (writer == null) {
            throw new IllegalArgumentException("The Writer must not be null.");
        } else if (string != null) {
            Entities.HTML40.escape(writer, string);
        }
    }

    public static String unescapeHtml(String str) {
        if (str == null) {
            return null;
        } else {
            try {
                StringWriter ioe = new StringWriter((int) ((double) str.length() * 1.5D));

                unescapeHtml(ioe, str);
                return ioe.toString();
            } catch (IOException ioexception) {
                throw new UnhandledException(ioexception);
            }
        }
    }

    public static void unescapeHtml(Writer writer, String string) throws IOException {
        if (writer == null) {
            throw new IllegalArgumentException("The Writer must not be null.");
        } else if (string != null) {
            Entities.HTML40.unescape(writer, string);
        }
    }

    public static void escapeXml(Writer writer, String str) throws IOException {
        if (writer == null) {
            throw new IllegalArgumentException("The Writer must not be null.");
        } else if (str != null) {
            Entities.XML.escape(writer, str);
        }
    }

    public static String escapeXml(String str) {
        return str == null ? null : Entities.XML.escape(str);
    }

    public static void unescapeXml(Writer writer, String str) throws IOException {
        if (writer == null) {
            throw new IllegalArgumentException("The Writer must not be null.");
        } else if (str != null) {
            Entities.XML.unescape(writer, str);
        }
    }

    public static String unescapeXml(String str) {
        return str == null ? null : Entities.XML.unescape(str);
    }

    public static String escapeSql(String str) {
        return str == null ? null : StringUtils.replace(str, "\'", "\'\'");
    }

    public static String escapeCsv(String str) {
        if (StringUtils.containsNone(str, StringEscapeUtils.CSV_SEARCH_CHARS)) {
            return str;
        } else {
            try {
                StringWriter ioe = new StringWriter();

                escapeCsv(ioe, str);
                return ioe.toString();
            } catch (IOException ioexception) {
                throw new UnhandledException(ioexception);
            }
        }
    }

    public static void escapeCsv(Writer out, String str) throws IOException {
        if (StringUtils.containsNone(str, StringEscapeUtils.CSV_SEARCH_CHARS)) {
            if (str != null) {
                out.write(str);
            }

        } else {
            out.write(34);

            for (int i = 0; i < str.length(); ++i) {
                char c = str.charAt(i);

                if (c == 34) {
                    out.write(34);
                }

                out.write(c);
            }

            out.write(34);
        }
    }

    public static String unescapeCsv(String str) {
        if (str == null) {
            return null;
        } else {
            try {
                StringWriter ioe = new StringWriter();

                unescapeCsv(ioe, str);
                return ioe.toString();
            } catch (IOException ioexception) {
                throw new UnhandledException(ioexception);
            }
        }
    }

    public static void unescapeCsv(Writer out, String str) throws IOException {
        if (str != null) {
            if (str.length() < 2) {
                out.write(str);
            } else if (str.charAt(0) == 34 && str.charAt(str.length() - 1) == 34) {
                String quoteless = str.substring(1, str.length() - 1);

                if (StringUtils.containsAny(quoteless, StringEscapeUtils.CSV_SEARCH_CHARS)) {
                    str = StringUtils.replace(quoteless, StringEscapeUtils.CSV_QUOTE_STR + StringEscapeUtils.CSV_QUOTE_STR, StringEscapeUtils.CSV_QUOTE_STR);
                }

                out.write(str);
            } else {
                out.write(str);
            }
        }
    }
}
