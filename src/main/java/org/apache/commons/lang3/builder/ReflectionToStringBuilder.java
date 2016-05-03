package org.apache.commons.lang3.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.lang3.ArrayUtils;

public class ReflectionToStringBuilder extends ToStringBuilder {

    private boolean appendStatics = false;
    private boolean appendTransients = false;
    protected String[] excludeFieldNames;
    private Class upToClass = null;

    public static String toString(Object object) {
        return toString(object, (ToStringStyle) null, false, false, (Class) null);
    }

    public static String toString(Object object, ToStringStyle tostringstyle) {
        return toString(object, tostringstyle, false, false, (Class) null);
    }

    public static String toString(Object object, ToStringStyle tostringstyle, boolean flag) {
        return toString(object, tostringstyle, flag, false, (Class) null);
    }

    public static String toString(Object object, ToStringStyle tostringstyle, boolean flag, boolean flag1) {
        return toString(object, tostringstyle, flag, flag1, (Class) null);
    }

    public static String toString(Object object, ToStringStyle tostringstyle, boolean flag, boolean flag1, Class oclass) {
        return (new ReflectionToStringBuilder(object, tostringstyle, (StringBuffer) null, oclass, flag, flag1)).toString();
    }

    public static String toStringExclude(Object object, Collection collection) {
        return toStringExclude(object, toNoNullStringArray(collection));
    }

    static String[] toNoNullStringArray(Collection collection) {
        return collection == null ? ArrayUtils.EMPTY_STRING_ARRAY : toNoNullStringArray(collection.toArray());
    }

    static String[] toNoNullStringArray(Object[] aobject) {
        ArrayList arraylist = new ArrayList(aobject.length);
        Object[] aobject1 = aobject;
        int i = aobject.length;

        for (int j = 0; j < i; ++j) {
            Object object = aobject1[j];

            if (object != null) {
                arraylist.add(object.toString());
            }
        }

        return (String[]) arraylist.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public static String toStringExclude(Object object, String... astring) {
        return (new ReflectionToStringBuilder(object)).setExcludeFieldNames(astring).toString();
    }

    public ReflectionToStringBuilder(Object object) {
        super(object);
    }

    public ReflectionToStringBuilder(Object object, ToStringStyle tostringstyle) {
        super(object, tostringstyle);
    }

    public ReflectionToStringBuilder(Object object, ToStringStyle tostringstyle, StringBuffer stringbuffer) {
        super(object, tostringstyle, stringbuffer);
    }

    public ReflectionToStringBuilder(Object object, ToStringStyle tostringstyle, StringBuffer stringbuffer, Class oclass, boolean flag, boolean flag1) {
        super(object, tostringstyle, stringbuffer);
        this.setUpToClass(oclass);
        this.setAppendTransients(flag);
        this.setAppendStatics(flag1);
    }

    protected boolean accept(Field field) {
        return field.getName().indexOf(36) != -1 ? false : (Modifier.isTransient(field.getModifiers()) && !this.isAppendTransients() ? false : (Modifier.isStatic(field.getModifiers()) && !this.isAppendStatics() ? false : this.excludeFieldNames == null || Arrays.binarySearch(this.excludeFieldNames, field.getName()) < 0));
    }

    protected void appendFieldsIn(Class oclass) {
        if (oclass.isArray()) {
            this.reflectionAppendArray(this.getObject());
        } else {
            Field[] afield = oclass.getDeclaredFields();

            AccessibleObject.setAccessible(afield, true);
            Field[] afield1 = afield;
            int i = afield.length;

            for (int j = 0; j < i; ++j) {
                Field field = afield1[j];
                String s = field.getName();

                if (this.accept(field)) {
                    try {
                        Object object = this.getValue(field);

                        this.append(s, object);
                    } catch (IllegalAccessException illegalaccessexception) {
                        throw new InternalError("Unexpected IllegalAccessException: " + illegalaccessexception.getMessage());
                    }
                }
            }

        }
    }

    public String[] getExcludeFieldNames() {
        return (String[]) this.excludeFieldNames.clone();
    }

    public Class getUpToClass() {
        return this.upToClass;
    }

    protected Object getValue(Field field) throws IllegalArgumentException, IllegalAccessException {
        return field.get(this.getObject());
    }

    public boolean isAppendStatics() {
        return this.appendStatics;
    }

    public boolean isAppendTransients() {
        return this.appendTransients;
    }

    public ReflectionToStringBuilder reflectionAppendArray(Object object) {
        this.getStyle().reflectionAppendArrayDetail(this.getStringBuffer(), (String) null, object);
        return this;
    }

    public void setAppendStatics(boolean flag) {
        this.appendStatics = flag;
    }

    public void setAppendTransients(boolean flag) {
        this.appendTransients = flag;
    }

    public ReflectionToStringBuilder setExcludeFieldNames(String... astring) {
        if (astring == null) {
            this.excludeFieldNames = null;
        } else {
            this.excludeFieldNames = toNoNullStringArray((Object[]) astring);
            Arrays.sort(this.excludeFieldNames);
        }

        return this;
    }

    public void setUpToClass(Class oclass) {
        if (oclass != null) {
            Object object = this.getObject();

            if (object != null && !oclass.isInstance(object)) {
                throw new IllegalArgumentException("Specified class is not a superclass of the object");
            }
        }

        this.upToClass = oclass;
    }

    public String toString() {
        if (this.getObject() == null) {
            return this.getStyle().getNullText();
        } else {
            Class oclass = this.getObject().getClass();

            this.appendFieldsIn(oclass);

            while (oclass.getSuperclass() != null && oclass != this.getUpToClass()) {
                oclass = oclass.getSuperclass();
                this.appendFieldsIn(oclass);
            }

            return super.toString();
        }
    }
}
