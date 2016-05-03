package org.apache.commons.lang3.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

public class ExceptionUtils {

    static final String WRAPPED_MARKER = " [wrapped] ";
    private static final String[] CAUSE_METHOD_NAMES = new String[] { "getCause", "getNextException", "getTargetException", "getException", "getSourceException", "getRootCause", "getCausedByException", "getNested", "getLinkedException", "getNestedException", "getLinkedCause", "getThrowable"};

    /** @deprecated */
    @Deprecated
    public static String[] getDefaultCauseMethodNames() {
        return (String[]) ArrayUtils.clone((Object[]) ExceptionUtils.CAUSE_METHOD_NAMES);
    }

    /** @deprecated */
    @Deprecated
    public static Throwable getCause(Throwable throwable) {
        return getCause(throwable, ExceptionUtils.CAUSE_METHOD_NAMES);
    }

    /** @deprecated */
    @Deprecated
    public static Throwable getCause(Throwable throwable, String[] astring) {
        if (throwable == null) {
            return null;
        } else {
            if (astring == null) {
                astring = ExceptionUtils.CAUSE_METHOD_NAMES;
            }

            String[] astring1 = astring;
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String s = astring1[j];

                if (s != null) {
                    Throwable throwable1 = getCauseUsingMethodName(throwable, s);

                    if (throwable1 != null) {
                        return throwable1;
                    }
                }
            }

            return null;
        }
    }

    public static Throwable getRootCause(Throwable throwable) {
        List list = getThrowableList(throwable);

        return list.size() < 2 ? null : (Throwable) list.get(list.size() - 1);
    }

    private static Throwable getCauseUsingMethodName(Throwable throwable, String s) {
        Method method = null;

        try {
            method = throwable.getClass().getMethod(s, new Class[0]);
        } catch (NoSuchMethodException nosuchmethodexception) {
            ;
        } catch (SecurityException securityexception) {
            ;
        }

        if (method != null && Throwable.class.isAssignableFrom(method.getReturnType())) {
            try {
                return (Throwable) method.invoke(throwable, new Object[0]);
            } catch (IllegalAccessException illegalaccessexception) {
                ;
            } catch (IllegalArgumentException illegalargumentexception) {
                ;
            } catch (InvocationTargetException invocationtargetexception) {
                ;
            }
        }

        return null;
    }

    public static int getThrowableCount(Throwable throwable) {
        return getThrowableList(throwable).size();
    }

    public static Throwable[] getThrowables(Throwable throwable) {
        List list = getThrowableList(throwable);

        return (Throwable[]) list.toArray(new Throwable[list.size()]);
    }

    public static List getThrowableList(Throwable throwable) {
        ArrayList arraylist;

        for (arraylist = new ArrayList(); throwable != null && !arraylist.contains(throwable); throwable = getCause(throwable)) {
            arraylist.add(throwable);
        }

        return arraylist;
    }

    public static int indexOfThrowable(Throwable throwable, Class oclass) {
        return indexOf(throwable, oclass, 0, false);
    }

    public static int indexOfThrowable(Throwable throwable, Class oclass, int i) {
        return indexOf(throwable, oclass, i, false);
    }

    public static int indexOfType(Throwable throwable, Class oclass) {
        return indexOf(throwable, oclass, 0, true);
    }

    public static int indexOfType(Throwable throwable, Class oclass, int i) {
        return indexOf(throwable, oclass, i, true);
    }

    private static int indexOf(Throwable throwable, Class oclass, int i, boolean flag) {
        if (throwable != null && oclass != null) {
            if (i < 0) {
                i = 0;
            }

            Throwable[] athrowable = getThrowables(throwable);

            if (i >= athrowable.length) {
                return -1;
            } else {
                int j;

                if (flag) {
                    for (j = i; j < athrowable.length; ++j) {
                        if (oclass.isAssignableFrom(athrowable[j].getClass())) {
                            return j;
                        }
                    }
                } else {
                    for (j = i; j < athrowable.length; ++j) {
                        if (oclass.equals(athrowable[j].getClass())) {
                            return j;
                        }
                    }
                }

                return -1;
            }
        } else {
            return -1;
        }
    }

    public static void printRootCauseStackTrace(Throwable throwable) {
        printRootCauseStackTrace(throwable, System.err);
    }

    public static void printRootCauseStackTrace(Throwable throwable, PrintStream printstream) {
        if (throwable != null) {
            if (printstream == null) {
                throw new IllegalArgumentException("The PrintStream must not be null");
            } else {
                String[] astring = getRootCauseStackTrace(throwable);
                String[] astring1 = astring;
                int i = astring.length;

                for (int j = 0; j < i; ++j) {
                    String s = astring1[j];

                    printstream.println(s);
                }

                printstream.flush();
            }
        }
    }

    public static void printRootCauseStackTrace(Throwable throwable, PrintWriter printwriter) {
        if (throwable != null) {
            if (printwriter == null) {
                throw new IllegalArgumentException("The PrintWriter must not be null");
            } else {
                String[] astring = getRootCauseStackTrace(throwable);
                String[] astring1 = astring;
                int i = astring.length;

                for (int j = 0; j < i; ++j) {
                    String s = astring1[j];

                    printwriter.println(s);
                }

                printwriter.flush();
            }
        }
    }

    public static String[] getRootCauseStackTrace(Throwable throwable) {
        if (throwable == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        } else {
            Throwable[] athrowable = getThrowables(throwable);
            int i = athrowable.length;
            ArrayList arraylist = new ArrayList();
            List list = getStackFrameList(athrowable[i - 1]);
            int j = i;

            while (true) {
                --j;
                if (j < 0) {
                    return (String[]) arraylist.toArray(new String[arraylist.size()]);
                }

                List list1 = list;

                if (j != 0) {
                    list = getStackFrameList(athrowable[j - 1]);
                    removeCommonFrames(list1, list);
                }

                if (j == i - 1) {
                    arraylist.add(athrowable[j].toString());
                } else {
                    arraylist.add(" [wrapped] " + athrowable[j].toString());
                }

                for (int k = 0; k < list1.size(); ++k) {
                    arraylist.add(list1.get(k));
                }
            }
        }
    }

    public static void removeCommonFrames(List list, List list1) {
        if (list != null && list1 != null) {
            int i = list.size() - 1;

            for (int j = list1.size() - 1; i >= 0 && j >= 0; --j) {
                String s = (String) list.get(i);
                String s1 = (String) list1.get(j);

                if (s.equals(s1)) {
                    list.remove(i);
                }

                --i;
            }

        } else {
            throw new IllegalArgumentException("The List must not be null");
        }
    }

    public static String getStackTrace(Throwable throwable) {
        StringWriter stringwriter = new StringWriter();
        PrintWriter printwriter = new PrintWriter(stringwriter, true);

        throwable.printStackTrace(printwriter);
        return stringwriter.getBuffer().toString();
    }

    public static String[] getStackFrames(Throwable throwable) {
        return throwable == null ? ArrayUtils.EMPTY_STRING_ARRAY : getStackFrames(getStackTrace(throwable));
    }

    static String[] getStackFrames(String s) {
        String s1 = SystemUtils.LINE_SEPARATOR;
        StringTokenizer stringtokenizer = new StringTokenizer(s, s1);
        ArrayList arraylist = new ArrayList();

        while (stringtokenizer.hasMoreTokens()) {
            arraylist.add(stringtokenizer.nextToken());
        }

        return (String[]) arraylist.toArray(new String[arraylist.size()]);
    }

    static List getStackFrameList(Throwable throwable) {
        String s = getStackTrace(throwable);
        String s1 = SystemUtils.LINE_SEPARATOR;
        StringTokenizer stringtokenizer = new StringTokenizer(s, s1);
        ArrayList arraylist = new ArrayList();
        boolean flag = false;

        while (stringtokenizer.hasMoreTokens()) {
            String s2 = stringtokenizer.nextToken();
            int i = s2.indexOf("at");

            if (i != -1 && s2.substring(0, i).trim().isEmpty()) {
                flag = true;
                arraylist.add(s2);
            } else if (flag) {
                break;
            }
        }

        return arraylist;
    }

    public static String getMessage(Throwable throwable) {
        if (throwable == null) {
            return "";
        } else {
            String s = ClassUtils.getShortClassName(throwable, (String) null);
            String s1 = throwable.getMessage();

            return s + ": " + StringUtils.defaultString(s1);
        }
    }

    public static String getRootCauseMessage(Throwable throwable) {
        Throwable throwable1 = getRootCause(throwable);

        throwable1 = throwable1 == null ? throwable : throwable1;
        return getMessage(throwable1);
    }
}
