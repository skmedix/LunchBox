package org.apache.commons.lang3.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class FieldUtils {

    public static Field getField(Class oclass, String s) {
        Field field = getField(oclass, s, false);

        MemberUtils.setAccessibleWorkaround(field);
        return field;
    }

    public static Field getField(Class oclass, String s, boolean flag) {
        Validate.isTrue(oclass != null, "The class must not be null", new Object[0]);
        Validate.isTrue(StringUtils.isNotBlank(s), "The field name must not be blank/empty", new Object[0]);

        for (Class oclass1 = oclass; oclass1 != null; oclass1 = oclass1.getSuperclass()) {
            try {
                Field field = oclass1.getDeclaredField(s);

                if (!Modifier.isPublic(field.getModifiers())) {
                    if (!flag) {
                        continue;
                    }

                    field.setAccessible(true);
                }

                return field;
            } catch (NoSuchFieldException nosuchfieldexception) {
                ;
            }
        }

        Field field1 = null;
        Iterator iterator = ClassUtils.getAllInterfaces(oclass).iterator();

        while (iterator.hasNext()) {
            Class oclass2 = (Class) iterator.next();

            try {
                Field field2 = oclass2.getField(s);

                Validate.isTrue(field1 == null, "Reference to field %s is ambiguous relative to %s; a matching field exists on two or more implemented interfaces.", new Object[] { s, oclass});
                field1 = field2;
            } catch (NoSuchFieldException nosuchfieldexception1) {
                ;
            }
        }

        return field1;
    }

    public static Field getDeclaredField(Class oclass, String s) {
        return getDeclaredField(oclass, s, false);
    }

    public static Field getDeclaredField(Class oclass, String s, boolean flag) {
        Validate.isTrue(oclass != null, "The class must not be null", new Object[0]);
        Validate.isTrue(StringUtils.isNotBlank(s), "The field name must not be blank/empty", new Object[0]);

        try {
            Field field = oclass.getDeclaredField(s);

            if (!MemberUtils.isAccessible(field)) {
                if (!flag) {
                    return null;
                }

                field.setAccessible(true);
            }

            return field;
        } catch (NoSuchFieldException nosuchfieldexception) {
            return null;
        }
    }

    public static Field[] getAllFields(Class oclass) {
        List list = getAllFieldsList(oclass);

        return (Field[]) list.toArray(new Field[list.size()]);
    }

    public static List getAllFieldsList(Class oclass) {
        Validate.isTrue(oclass != null, "The class must not be null", new Object[0]);
        ArrayList arraylist = new ArrayList();

        for (Class oclass1 = oclass; oclass1 != null; oclass1 = oclass1.getSuperclass()) {
            Field[] afield = oclass1.getDeclaredFields();
            Field[] afield1 = afield;
            int i = afield.length;

            for (int j = 0; j < i; ++j) {
                Field field = afield1[j];

                arraylist.add(field);
            }
        }

        return arraylist;
    }

    public static Object readStaticField(Field field) throws IllegalAccessException {
        return readStaticField(field, false);
    }

    public static Object readStaticField(Field field, boolean flag) throws IllegalAccessException {
        Validate.isTrue(field != null, "The field must not be null", new Object[0]);
        Validate.isTrue(Modifier.isStatic(field.getModifiers()), "The field \'%s\' is not static", new Object[] { field.getName()});
        return readField(field, (Object) null, flag);
    }

    public static Object readStaticField(Class oclass, String s) throws IllegalAccessException {
        return readStaticField(oclass, s, false);
    }

    public static Object readStaticField(Class oclass, String s, boolean flag) throws IllegalAccessException {
        Field field = getField(oclass, s, flag);

        Validate.isTrue(field != null, "Cannot locate field \'%s\' on %s", new Object[] { s, oclass});
        return readStaticField(field, false);
    }

    public static Object readDeclaredStaticField(Class oclass, String s) throws IllegalAccessException {
        return readDeclaredStaticField(oclass, s, false);
    }

    public static Object readDeclaredStaticField(Class oclass, String s, boolean flag) throws IllegalAccessException {
        Field field = getDeclaredField(oclass, s, flag);

        Validate.isTrue(field != null, "Cannot locate declared field %s.%s", new Object[] { oclass.getName(), s});
        return readStaticField(field, false);
    }

    public static Object readField(Field field, Object object) throws IllegalAccessException {
        return readField(field, object, false);
    }

    public static Object readField(Field field, Object object, boolean flag) throws IllegalAccessException {
        Validate.isTrue(field != null, "The field must not be null", new Object[0]);
        if (flag && !field.isAccessible()) {
            field.setAccessible(true);
        } else {
            MemberUtils.setAccessibleWorkaround(field);
        }

        return field.get(object);
    }

    public static Object readField(Object object, String s) throws IllegalAccessException {
        return readField(object, s, false);
    }

    public static Object readField(Object object, String s, boolean flag) throws IllegalAccessException {
        Validate.isTrue(object != null, "target object must not be null", new Object[0]);
        Class oclass = object.getClass();
        Field field = getField(oclass, s, flag);

        Validate.isTrue(field != null, "Cannot locate field %s on %s", new Object[] { s, oclass});
        return readField(field, object, false);
    }

    public static Object readDeclaredField(Object object, String s) throws IllegalAccessException {
        return readDeclaredField(object, s, false);
    }

    public static Object readDeclaredField(Object object, String s, boolean flag) throws IllegalAccessException {
        Validate.isTrue(object != null, "target object must not be null", new Object[0]);
        Class oclass = object.getClass();
        Field field = getDeclaredField(oclass, s, flag);

        Validate.isTrue(field != null, "Cannot locate declared field %s.%s", new Object[] { oclass, s});
        return readField(field, object, false);
    }

    public static void writeStaticField(Field field, Object object) throws IllegalAccessException {
        writeStaticField(field, object, false);
    }

    public static void writeStaticField(Field field, Object object, boolean flag) throws IllegalAccessException {
        Validate.isTrue(field != null, "The field must not be null", new Object[0]);
        Validate.isTrue(Modifier.isStatic(field.getModifiers()), "The field %s.%s is not static", new Object[] { field.getDeclaringClass().getName(), field.getName()});
        writeField(field, (Object) null, object, flag);
    }

    public static void writeStaticField(Class oclass, String s, Object object) throws IllegalAccessException {
        writeStaticField(oclass, s, object, false);
    }

    public static void writeStaticField(Class oclass, String s, Object object, boolean flag) throws IllegalAccessException {
        Field field = getField(oclass, s, flag);

        Validate.isTrue(field != null, "Cannot locate field %s on %s", new Object[] { s, oclass});
        writeStaticField(field, object, false);
    }

    public static void writeDeclaredStaticField(Class oclass, String s, Object object) throws IllegalAccessException {
        writeDeclaredStaticField(oclass, s, object, false);
    }

    public static void writeDeclaredStaticField(Class oclass, String s, Object object, boolean flag) throws IllegalAccessException {
        Field field = getDeclaredField(oclass, s, flag);

        Validate.isTrue(field != null, "Cannot locate declared field %s.%s", new Object[] { oclass.getName(), s});
        writeField(field, (Object) null, object, false);
    }

    public static void writeField(Field field, Object object, Object object1) throws IllegalAccessException {
        writeField(field, object, object1, false);
    }

    public static void writeField(Field field, Object object, Object object1, boolean flag) throws IllegalAccessException {
        Validate.isTrue(field != null, "The field must not be null", new Object[0]);
        if (flag && !field.isAccessible()) {
            field.setAccessible(true);
        } else {
            MemberUtils.setAccessibleWorkaround(field);
        }

        field.set(object, object1);
    }

    public static void removeFinalModifier(Field field) {
        removeFinalModifier(field, true);
    }

    public static void removeFinalModifier(Field field, boolean flag) {
        Validate.isTrue(field != null, "The field must not be null", new Object[0]);

        try {
            if (Modifier.isFinal(field.getModifiers())) {
                Field field1 = Field.class.getDeclaredField("modifiers");
                boolean flag1 = flag && !field1.isAccessible();

                if (flag1) {
                    field1.setAccessible(true);
                }

                try {
                    field1.setInt(field, field.getModifiers() & -17);
                } finally {
                    if (flag1) {
                        field1.setAccessible(false);
                    }

                }
            }
        } catch (NoSuchFieldException nosuchfieldexception) {
            ;
        } catch (IllegalAccessException illegalaccessexception) {
            ;
        }

    }

    public static void writeField(Object object, String s, Object object1) throws IllegalAccessException {
        writeField(object, s, object1, false);
    }

    public static void writeField(Object object, String s, Object object1, boolean flag) throws IllegalAccessException {
        Validate.isTrue(object != null, "target object must not be null", new Object[0]);
        Class oclass = object.getClass();
        Field field = getField(oclass, s, flag);

        Validate.isTrue(field != null, "Cannot locate declared field %s.%s", new Object[] { oclass.getName(), s});
        writeField(field, object, object1, false);
    }

    public static void writeDeclaredField(Object object, String s, Object object1) throws IllegalAccessException {
        writeDeclaredField(object, s, object1, false);
    }

    public static void writeDeclaredField(Object object, String s, Object object1, boolean flag) throws IllegalAccessException {
        Validate.isTrue(object != null, "target object must not be null", new Object[0]);
        Class oclass = object.getClass();
        Field field = getDeclaredField(oclass, s, flag);

        Validate.isTrue(field != null, "Cannot locate declared field %s.%s", new Object[] { oclass.getName(), s});
        writeField(field, object, object1, false);
    }
}
