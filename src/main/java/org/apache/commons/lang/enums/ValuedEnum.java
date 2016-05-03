package org.apache.commons.lang.enums;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.ClassUtils;

public abstract class ValuedEnum extends Enum {

    private static final long serialVersionUID = -7129650521543789085L;
    private final int iValue;

    protected ValuedEnum(String name, int value) {
        super(name);
        this.iValue = value;
    }

    protected static Enum getEnum(Class enumClass, int value) {
        if (enumClass == null) {
            throw new IllegalArgumentException("The Enum Class must not be null");
        } else {
            List list = Enum.getEnumList(enumClass);
            Iterator it = list.iterator();

            ValuedEnum enumeration;

            do {
                if (!it.hasNext()) {
                    return null;
                }

                enumeration = (ValuedEnum) it.next();
            } while (enumeration.getValue() != value);

            return enumeration;
        }
    }

    public final int getValue() {
        return this.iValue;
    }

    public int compareTo(Object other) {
        if (other == this) {
            return 0;
        } else if (other.getClass() != this.getClass()) {
            if (other.getClass().getName().equals(this.getClass().getName())) {
                return this.iValue - this.getValueInOtherClassLoader(other);
            } else {
                throw new ClassCastException("Different enum class \'" + ClassUtils.getShortClassName(other.getClass()) + "\'");
            }
        } else {
            return this.iValue - ((ValuedEnum) other).iValue;
        }
    }

    private int getValueInOtherClassLoader(Object other) {
        try {
            Method e = other.getClass().getMethod("getValue", (Class[]) null);
            Integer value = (Integer) e.invoke(other, (Object[]) null);

            return value.intValue();
        } catch (NoSuchMethodException nosuchmethodexception) {
            ;
        } catch (IllegalAccessException illegalaccessexception) {
            ;
        } catch (InvocationTargetException invocationtargetexception) {
            ;
        }

        throw new IllegalStateException("This should not happen");
    }

    public String toString() {
        if (this.iToString == null) {
            String shortName = ClassUtils.getShortClassName(this.getEnumClass());

            this.iToString = shortName + "[" + this.getName() + "=" + this.getValue() + "]";
        }

        return this.iToString;
    }
}
