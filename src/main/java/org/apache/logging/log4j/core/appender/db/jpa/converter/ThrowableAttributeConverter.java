package org.apache.logging.log4j.core.appender.db.jpa.converter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.apache.logging.log4j.core.helpers.Strings;

@Converter(
    autoApply = false
)
public class ThrowableAttributeConverter implements AttributeConverter {

    private static final int CAUSED_BY_STRING_LENGTH = 10;
    private static final Field THROWABLE_CAUSE;
    private static final Field THROWABLE_MESSAGE;

    public String convertToDatabaseColumn(Throwable throwable) {
        if (throwable == null) {
            return null;
        } else {
            StringBuilder stringbuilder = new StringBuilder();

            this.convertThrowable(stringbuilder, throwable);
            return stringbuilder.toString();
        }
    }

    private void convertThrowable(StringBuilder stringbuilder, Throwable throwable) {
        stringbuilder.append(throwable.toString()).append('\n');
        StackTraceElement[] astacktraceelement = throwable.getStackTrace();
        int i = astacktraceelement.length;

        for (int j = 0; j < i; ++j) {
            StackTraceElement stacktraceelement = astacktraceelement[j];

            stringbuilder.append("\tat ").append(stacktraceelement).append('\n');
        }

        if (throwable.getCause() != null) {
            stringbuilder.append("Caused by ");
            this.convertThrowable(stringbuilder, throwable.getCause());
        }

    }

    public Throwable convertToEntityAttribute(String s) {
        if (Strings.isEmpty(s)) {
            return null;
        } else {
            List list = Arrays.asList(s.split("(\n|\r\n)"));

            return this.convertString(list.listIterator(), false);
        }
    }

    private Throwable convertString(ListIterator listiterator, boolean flag) {
        String s = (String) listiterator.next();

        if (flag) {
            s = s.substring(10);
        }

        int i = s.indexOf(":");
        String s1 = null;
        String s2;

        if (i > 1) {
            s2 = s.substring(0, i);
            if (s.length() > i + 1) {
                s1 = s.substring(i + 1).trim();
            }
        } else {
            s2 = s;
        }

        ArrayList arraylist = new ArrayList();
        Throwable throwable = null;

        while (listiterator.hasNext()) {
            String s3 = (String) listiterator.next();

            if (s3.startsWith("Caused by ")) {
                listiterator.previous();
                throwable = this.convertString(listiterator, true);
                break;
            }

            arraylist.add(StackTraceElementAttributeConverter.convertString(s3.trim().substring(3).trim()));
        }

        return this.getThrowable(s2, s1, throwable, (StackTraceElement[]) arraylist.toArray(new StackTraceElement[arraylist.size()]));
    }

    private Throwable getThrowable(String s, String s1, Throwable throwable, StackTraceElement[] astacktraceelement) {
        try {
            Class oclass = Class.forName(s);

            if (!Throwable.class.isAssignableFrom(oclass)) {
                return null;
            } else {
                Throwable throwable1;

                if (s1 != null && throwable != null) {
                    throwable1 = this.getThrowable(oclass, s1, throwable);
                    if (throwable1 == null) {
                        throwable1 = this.getThrowable(oclass, throwable);
                        if (throwable1 == null) {
                            throwable1 = this.getThrowable(oclass, s1);
                            if (throwable1 == null) {
                                throwable1 = this.getThrowable(oclass);
                                if (throwable1 != null) {
                                    ThrowableAttributeConverter.THROWABLE_MESSAGE.set(throwable1, s1);
                                    ThrowableAttributeConverter.THROWABLE_CAUSE.set(throwable1, throwable);
                                }
                            } else {
                                ThrowableAttributeConverter.THROWABLE_CAUSE.set(throwable1, throwable);
                            }
                        } else {
                            ThrowableAttributeConverter.THROWABLE_MESSAGE.set(throwable1, s1);
                        }
                    }
                } else if (throwable != null) {
                    throwable1 = this.getThrowable(oclass, throwable);
                    if (throwable1 == null) {
                        throwable1 = this.getThrowable(oclass);
                        if (throwable1 != null) {
                            ThrowableAttributeConverter.THROWABLE_CAUSE.set(throwable1, throwable);
                        }
                    }
                } else if (s1 != null) {
                    throwable1 = this.getThrowable(oclass, s1);
                    if (throwable1 == null) {
                        throwable1 = this.getThrowable(oclass);
                        if (throwable1 != null) {
                            ThrowableAttributeConverter.THROWABLE_MESSAGE.set(throwable1, throwable);
                        }
                    }
                } else {
                    throwable1 = this.getThrowable(oclass);
                }

                if (throwable1 == null) {
                    return null;
                } else {
                    throwable1.setStackTrace(astacktraceelement);
                    return throwable1;
                }
            }
        } catch (Exception exception) {
            return null;
        }
    }

    private Throwable getThrowable(Class oclass, String s, Throwable throwable) {
        try {
            Constructor[] aconstructor = (Constructor[]) oclass.getConstructors();
            Constructor[] aconstructor1 = aconstructor;
            int i = aconstructor.length;

            for (int j = 0; j < i; ++j) {
                Constructor constructor = aconstructor1[j];
                Class[] aclass = constructor.getParameterTypes();

                if (aclass.length == 2) {
                    if (String.class == aclass[0] && Throwable.class.isAssignableFrom(aclass[1])) {
                        return (Throwable) constructor.newInstance(new Object[] { s, throwable});
                    }

                    if (String.class == aclass[1] && Throwable.class.isAssignableFrom(aclass[0])) {
                        return (Throwable) constructor.newInstance(new Object[] { throwable, s});
                    }
                }
            }

            return null;
        } catch (Exception exception) {
            return null;
        }
    }

    private Throwable getThrowable(Class oclass, Throwable throwable) {
        try {
            Constructor[] aconstructor = (Constructor[]) oclass.getConstructors();
            Constructor[] aconstructor1 = aconstructor;
            int i = aconstructor.length;

            for (int j = 0; j < i; ++j) {
                Constructor constructor = aconstructor1[j];
                Class[] aclass = constructor.getParameterTypes();

                if (aclass.length == 1 && Throwable.class.isAssignableFrom(aclass[0])) {
                    return (Throwable) constructor.newInstance(new Object[] { throwable});
                }
            }

            return null;
        } catch (Exception exception) {
            return null;
        }
    }

    private Throwable getThrowable(Class oclass, String s) {
        try {
            return (Throwable) oclass.getConstructor(new Class[] { String.class}).newInstance(new Object[] { s});
        } catch (Exception exception) {
            return null;
        }
    }

    private Throwable getThrowable(Class oclass) {
        try {
            return (Throwable) oclass.newInstance();
        } catch (Exception exception) {
            return null;
        }
    }

    static {
        try {
            THROWABLE_CAUSE = Throwable.class.getDeclaredField("cause");
            ThrowableAttributeConverter.THROWABLE_CAUSE.setAccessible(true);
            THROWABLE_MESSAGE = Throwable.class.getDeclaredField("detailMessage");
            ThrowableAttributeConverter.THROWABLE_MESSAGE.setAccessible(true);
        } catch (NoSuchFieldException nosuchfieldexception) {
            throw new IllegalStateException("Something is wrong with java.lang.Throwable.", nosuchfieldexception);
        }
    }
}
