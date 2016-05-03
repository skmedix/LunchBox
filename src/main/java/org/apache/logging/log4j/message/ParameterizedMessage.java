package org.apache.logging.log4j.message;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class ParameterizedMessage implements Message {

    public static final String RECURSION_PREFIX = "[...";
    public static final String RECURSION_SUFFIX = "...]";
    public static final String ERROR_PREFIX = "[!!!";
    public static final String ERROR_SEPARATOR = "=>";
    public static final String ERROR_MSG_SEPARATOR = ":";
    public static final String ERROR_SUFFIX = "!!!]";
    private static final long serialVersionUID = -665975803997290697L;
    private static final int HASHVAL = 31;
    private static final char DELIM_START = '{';
    private static final char DELIM_STOP = '}';
    private static final char ESCAPE_CHAR = '\\';
    private final String messagePattern;
    private final String[] stringArgs;
    private transient Object[] argArray;
    private transient String formattedMessage;
    private transient Throwable throwable;

    public ParameterizedMessage(String s, String[] astring, Throwable throwable) {
        this.messagePattern = s;
        this.stringArgs = astring;
        this.throwable = throwable;
    }

    public ParameterizedMessage(String s, Object[] aobject, Throwable throwable) {
        this.messagePattern = s;
        this.throwable = throwable;
        this.stringArgs = this.parseArguments(aobject);
    }

    public ParameterizedMessage(String s, Object[] aobject) {
        this.messagePattern = s;
        this.stringArgs = this.parseArguments(aobject);
    }

    public ParameterizedMessage(String s, Object object) {
        this(s, new Object[] { object});
    }

    public ParameterizedMessage(String s, Object object, Object object1) {
        this(s, new Object[] { object, object1});
    }

    private String[] parseArguments(Object[] aobject) {
        if (aobject == null) {
            return null;
        } else {
            int i = countArgumentPlaceholders(this.messagePattern);
            int j = aobject.length;

            if (i < aobject.length && this.throwable == null && aobject[aobject.length - 1] instanceof Throwable) {
                this.throwable = (Throwable) aobject[aobject.length - 1];
                --j;
            }

            this.argArray = new Object[j];

            for (int k = 0; k < j; ++k) {
                this.argArray[k] = aobject[k];
            }

            String[] astring;

            if (i == 1 && this.throwable == null && aobject.length > 1) {
                astring = new String[] { deepToString(aobject)};
            } else {
                astring = new String[j];

                for (int l = 0; l < astring.length; ++l) {
                    astring[l] = deepToString(aobject[l]);
                }
            }

            return astring;
        }
    }

    public String getFormattedMessage() {
        if (this.formattedMessage == null) {
            this.formattedMessage = this.formatMessage(this.messagePattern, this.stringArgs);
        }

        return this.formattedMessage;
    }

    public String getFormat() {
        return this.messagePattern;
    }

    public Object[] getParameters() {
        return (Object[]) (this.argArray != null ? this.argArray : this.stringArgs);
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    protected String formatMessage(String s, String[] astring) {
        return format(s, astring);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            ParameterizedMessage parameterizedmessage = (ParameterizedMessage) object;

            if (this.messagePattern != null) {
                if (this.messagePattern.equals(parameterizedmessage.messagePattern)) {
                    return Arrays.equals(this.stringArgs, parameterizedmessage.stringArgs);
                }
            } else if (parameterizedmessage.messagePattern == null) {
                return Arrays.equals(this.stringArgs, parameterizedmessage.stringArgs);
            }

            return false;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int i = this.messagePattern != null ? this.messagePattern.hashCode() : 0;

        i = 31 * i + (this.stringArgs != null ? Arrays.hashCode(this.stringArgs) : 0);
        return i;
    }

    public static String format(String s, Object[] aobject) {
        if (s != null && aobject != null && aobject.length != 0) {
            StringBuilder stringbuilder = new StringBuilder();
            int i = 0;
            int j = 0;

            for (int k = 0; k < s.length(); ++k) {
                char c0 = s.charAt(k);

                if (c0 == 92) {
                    ++i;
                } else {
                    int l;

                    if (c0 == 123 && k < s.length() - 1 && s.charAt(k + 1) == 125) {
                        l = i / 2;

                        for (int i1 = 0; i1 < l; ++i1) {
                            stringbuilder.append('\\');
                        }

                        if (i % 2 == 1) {
                            stringbuilder.append('{');
                            stringbuilder.append('}');
                        } else {
                            if (j < aobject.length) {
                                stringbuilder.append(aobject[j]);
                            } else {
                                stringbuilder.append('{').append('}');
                            }

                            ++j;
                        }

                        ++k;
                        i = 0;
                    } else {
                        if (i > 0) {
                            for (l = 0; l < i; ++l) {
                                stringbuilder.append('\\');
                            }

                            i = 0;
                        }

                        stringbuilder.append(c0);
                    }
                }
            }

            return stringbuilder.toString();
        } else {
            return s;
        }
    }

    public static int countArgumentPlaceholders(String s) {
        if (s == null) {
            return 0;
        } else {
            int i = s.indexOf(123);

            if (i == -1) {
                return 0;
            } else {
                int j = 0;
                boolean flag = false;

                for (int k = 0; k < s.length(); ++k) {
                    char c0 = s.charAt(k);

                    if (c0 == 92) {
                        flag = !flag;
                    } else if (c0 == 123) {
                        if (!flag && k < s.length() - 1 && s.charAt(k + 1) == 125) {
                            ++j;
                            ++k;
                        }

                        flag = false;
                    } else {
                        flag = false;
                    }
                }

                return j;
            }
        }
    }

    public static String deepToString(Object object) {
        if (object == null) {
            return null;
        } else if (object instanceof String) {
            return (String) object;
        } else {
            StringBuilder stringbuilder = new StringBuilder();
            HashSet hashset = new HashSet();

            recursiveDeepToString(object, stringbuilder, hashset);
            return stringbuilder.toString();
        }
    }

    private static void recursiveDeepToString(Object object, StringBuilder stringbuilder, Set set) {
        if (object == null) {
            stringbuilder.append("null");
        } else if (object instanceof String) {
            stringbuilder.append(object);
        } else {
            Class oclass = object.getClass();
            String s;
            boolean flag;
            Object object1;

            if (oclass.isArray()) {
                if (oclass == byte[].class) {
                    stringbuilder.append(Arrays.toString((byte[]) ((byte[]) object)));
                } else if (oclass == short[].class) {
                    stringbuilder.append(Arrays.toString((short[]) ((short[]) object)));
                } else if (oclass == int[].class) {
                    stringbuilder.append(Arrays.toString((int[]) ((int[]) object)));
                } else if (oclass == long[].class) {
                    stringbuilder.append(Arrays.toString((long[]) ((long[]) object)));
                } else if (oclass == float[].class) {
                    stringbuilder.append(Arrays.toString((float[]) ((float[]) object)));
                } else if (oclass == double[].class) {
                    stringbuilder.append(Arrays.toString((double[]) ((double[]) object)));
                } else if (oclass == boolean[].class) {
                    stringbuilder.append(Arrays.toString((boolean[]) ((boolean[]) object)));
                } else if (oclass == char[].class) {
                    stringbuilder.append(Arrays.toString((char[]) ((char[]) object)));
                } else {
                    s = identityToString(object);
                    if (set.contains(s)) {
                        stringbuilder.append("[...").append(s).append("...]");
                    } else {
                        set.add(s);
                        Object[] aobject = (Object[]) ((Object[]) object);

                        stringbuilder.append("[");
                        flag = true;
                        Object[] aobject1 = aobject;
                        int i = aobject.length;

                        for (int j = 0; j < i; ++j) {
                            object1 = aobject1[j];
                            if (flag) {
                                flag = false;
                            } else {
                                stringbuilder.append(", ");
                            }

                            recursiveDeepToString(object1, stringbuilder, new HashSet(set));
                        }

                        stringbuilder.append("]");
                    }
                }
            } else {
                Iterator iterator;

                if (object instanceof Map) {
                    s = identityToString(object);
                    if (set.contains(s)) {
                        stringbuilder.append("[...").append(s).append("...]");
                    } else {
                        set.add(s);
                        Map map = (Map) object;

                        stringbuilder.append("{");
                        flag = true;
                        iterator = map.entrySet().iterator();

                        while (iterator.hasNext()) {
                            Entry entry = (Entry) iterator.next();
                            Entry entry1 = (Entry) entry;

                            if (flag) {
                                flag = false;
                            } else {
                                stringbuilder.append(", ");
                            }

                            object1 = entry1.getKey();
                            Object object2 = entry1.getValue();

                            recursiveDeepToString(object1, stringbuilder, new HashSet(set));
                            stringbuilder.append("=");
                            recursiveDeepToString(object2, stringbuilder, new HashSet(set));
                        }

                        stringbuilder.append("}");
                    }
                } else if (object instanceof Collection) {
                    s = identityToString(object);
                    if (set.contains(s)) {
                        stringbuilder.append("[...").append(s).append("...]");
                    } else {
                        set.add(s);
                        Collection collection = (Collection) object;

                        stringbuilder.append("[");
                        flag = true;

                        Object object3;

                        for (iterator = collection.iterator(); iterator.hasNext(); recursiveDeepToString(object3, stringbuilder, new HashSet(set))) {
                            object3 = iterator.next();
                            if (flag) {
                                flag = false;
                            } else {
                                stringbuilder.append(", ");
                            }
                        }

                        stringbuilder.append("]");
                    }
                } else if (object instanceof Date) {
                    Date date = (Date) object;
                    SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss.SSSZ");

                    stringbuilder.append(simpledateformat.format(date));
                } else {
                    try {
                        stringbuilder.append(object.toString());
                    } catch (Throwable throwable) {
                        stringbuilder.append("[!!!");
                        stringbuilder.append(identityToString(object));
                        stringbuilder.append("=>");
                        String s1 = throwable.getMessage();
                        String s2 = throwable.getClass().getName();

                        stringbuilder.append(s2);
                        if (!s2.equals(s1)) {
                            stringbuilder.append(":");
                            stringbuilder.append(s1);
                        }

                        stringbuilder.append("!!!]");
                    }
                }
            }

        }
    }

    public static String identityToString(Object object) {
        return object == null ? null : object.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(object));
    }

    public String toString() {
        return "ParameterizedMessage[messagePattern=" + this.messagePattern + ", stringArgs=" + Arrays.toString(this.stringArgs) + ", throwable=" + this.throwable + "]";
    }
}
