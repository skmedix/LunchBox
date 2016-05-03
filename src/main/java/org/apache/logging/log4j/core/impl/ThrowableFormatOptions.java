package org.apache.logging.log4j.core.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import org.apache.logging.log4j.core.helpers.Constants;

public final class ThrowableFormatOptions {

    private static final int DEFAULT_LINES = Integer.MAX_VALUE;
    protected static final ThrowableFormatOptions DEFAULT = new ThrowableFormatOptions();
    private static final String FULL = "full";
    private static final String NONE = "none";
    private static final String SHORT = "short";
    private final int lines;
    private final String separator;
    private final List packages;
    public static final String CLASS_NAME = "short.className";
    public static final String METHOD_NAME = "short.methodName";
    public static final String LINE_NUMBER = "short.lineNumber";
    public static final String FILE_NAME = "short.fileName";
    public static final String MESSAGE = "short.message";
    public static final String LOCALIZED_MESSAGE = "short.localizedMessage";

    protected ThrowableFormatOptions(int i, String s, List list) {
        this.lines = i;
        this.separator = s == null ? Constants.LINE_SEP : s;
        this.packages = list;
    }

    protected ThrowableFormatOptions(List list) {
        this(Integer.MAX_VALUE, (String) null, list);
    }

    protected ThrowableFormatOptions() {
        this(Integer.MAX_VALUE, (String) null, (List) null);
    }

    public int getLines() {
        return this.lines;
    }

    public String getSeparator() {
        return this.separator;
    }

    public List getPackages() {
        return this.packages;
    }

    public boolean allLines() {
        return this.lines == Integer.MAX_VALUE;
    }

    public boolean anyLines() {
        return this.lines > 0;
    }

    public int minLines(int i) {
        return this.lines > i ? i : this.lines;
    }

    public boolean hasPackages() {
        return this.packages != null && !this.packages.isEmpty();
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append("{").append(this.allLines() ? "full" : (this.lines == 2 ? "short" : (this.anyLines() ? String.valueOf(this.lines) : "none"))).append("}");
        stringbuilder.append("{separator(").append(this.separator).append(")}");
        if (this.hasPackages()) {
            stringbuilder.append("{filters(");
            Iterator iterator = this.packages.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();

                stringbuilder.append(s).append(",");
            }

            stringbuilder.deleteCharAt(stringbuilder.length() - 1);
            stringbuilder.append(")}");
        }

        return stringbuilder.toString();
    }

    public static ThrowableFormatOptions newInstance(String[] astring) {
        if (astring != null && astring.length != 0) {
            String s;

            if (astring.length == 1 && astring[0] != null && astring[0].length() > 0) {
                String[] astring1 = astring[0].split(",", 2);

                s = astring1[0].trim();
                Scanner scanner = new Scanner(s);

                if (astring1.length > 1 && (s.equalsIgnoreCase("full") || s.equalsIgnoreCase("short") || s.equalsIgnoreCase("none") || scanner.hasNextInt())) {
                    astring = new String[] { s, astring1[1].trim()};
                }

                scanner.close();
            }

            int i = ThrowableFormatOptions.DEFAULT.lines;

            s = ThrowableFormatOptions.DEFAULT.separator;
            Object object = ThrowableFormatOptions.DEFAULT.packages;
            String[] astring2 = astring;
            int j = astring.length;

            for (int k = 0; k < j; ++k) {
                String s1 = astring2[k];

                if (s1 != null) {
                    String s2 = s1.trim();

                    if (!s2.isEmpty()) {
                        if (s2.startsWith("separator(") && s2.endsWith(")")) {
                            s = s2.substring("separator(".length(), s2.length() - 1);
                        } else if (s2.startsWith("filters(") && s2.endsWith(")")) {
                            String s3 = s2.substring("filters(".length(), s2.length() - 1);

                            if (s3.length() > 0) {
                                String[] astring3 = s3.split(",");

                                if (astring3.length > 0) {
                                    object = new ArrayList(astring3.length);
                                    String[] astring4 = astring3;
                                    int l = astring3.length;

                                    for (int i1 = 0; i1 < l; ++i1) {
                                        String s4 = astring4[i1];

                                        s4 = s4.trim();
                                        if (s4.length() > 0) {
                                            ((List) object).add(s4);
                                        }
                                    }
                                }
                            }
                        } else if (s2.equalsIgnoreCase("none")) {
                            i = 0;
                        } else if (!s2.equalsIgnoreCase("short") && !s2.equalsIgnoreCase("short.className") && !s2.equalsIgnoreCase("short.methodName") && !s2.equalsIgnoreCase("short.lineNumber") && !s2.equalsIgnoreCase("short.fileName") && !s2.equalsIgnoreCase("short.message") && !s2.equalsIgnoreCase("short.localizedMessage")) {
                            if (!s2.equalsIgnoreCase("full")) {
                                i = Integer.parseInt(s2);
                            }
                        } else {
                            i = 2;
                        }
                    }
                }
            }

            return new ThrowableFormatOptions(i, s, (List) object);
        } else {
            return ThrowableFormatOptions.DEFAULT;
        }
    }
}
