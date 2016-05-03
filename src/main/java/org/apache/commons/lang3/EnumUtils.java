package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EnumUtils {

    private static final String NULL_ELEMENTS_NOT_PERMITTED = "null elements not permitted";
    private static final String CANNOT_STORE_S_S_VALUES_IN_S_BITS = "Cannot store %s %s values in %s bits";
    private static final String S_DOES_NOT_SEEM_TO_BE_AN_ENUM_TYPE = "%s does not seem to be an Enum type";
    private static final String ENUM_CLASS_MUST_BE_DEFINED = "EnumClass must be defined.";

    public static Map getEnumMap(Class oclass) {
        LinkedHashMap linkedhashmap = new LinkedHashMap();
        Enum[] aenum = (Enum[]) oclass.getEnumConstants();
        int i = aenum.length;

        for (int j = 0; j < i; ++j) {
            Enum oenum = aenum[j];

            linkedhashmap.put(oenum.name(), oenum);
        }

        return linkedhashmap;
    }

    public static List getEnumList(Class oclass) {
        return new ArrayList(Arrays.asList(oclass.getEnumConstants()));
    }

    public static boolean isValidEnum(Class oclass, String s) {
        if (s == null) {
            return false;
        } else {
            try {
                Enum.valueOf(oclass, s);
                return true;
            } catch (IllegalArgumentException illegalargumentexception) {
                return false;
            }
        }
    }

    public static Enum getEnum(Class oclass, String s) {
        if (s == null) {
            return null;
        } else {
            try {
                return Enum.valueOf(oclass, s);
            } catch (IllegalArgumentException illegalargumentexception) {
                return null;
            }
        }
    }

    public static long generateBitVector(Class oclass, Iterable iterable) {
        checkBitVectorable(oclass);
        Validate.notNull(iterable);
        long i = 0L;

        Enum oenum;

        for (Iterator iterator = iterable.iterator(); iterator.hasNext(); i |= (long) (1 << oenum.ordinal())) {
            oenum = (Enum) iterator.next();
            Validate.isTrue(oenum != null, "null elements not permitted", new Object[0]);
        }

        return i;
    }

    public static long[] generateBitVectors(Class oclass, Iterable iterable) {
        asEnum(oclass);
        Validate.notNull(iterable);
        EnumSet enumset = EnumSet.noneOf(oclass);
        Iterator iterator = iterable.iterator();

        while (iterator.hasNext()) {
            Enum oenum = (Enum) iterator.next();

            Validate.isTrue(oenum != null, "null elements not permitted", new Object[0]);
            enumset.add(oenum);
        }

        long[] along = new long[(((Enum[]) oclass.getEnumConstants()).length - 1) / 64 + 1];

        Enum oenum1;
        int i;

        for (Iterator iterator1 = enumset.iterator(); iterator1.hasNext(); along[i] |= (long) (1 << oenum1.ordinal() % 64)) {
            oenum1 = (Enum) iterator1.next();
            i = oenum1.ordinal() / 64;
        }

        ArrayUtils.reverse(along);
        return along;
    }

    public static long generateBitVector(Class oclass, Enum... aenum) {
        Validate.noNullElements((Object[]) aenum);
        return generateBitVector(oclass, (Iterable) Arrays.asList(aenum));
    }

    public static long[] generateBitVectors(Class oclass, Enum... aenum) {
        asEnum(oclass);
        Validate.noNullElements((Object[]) aenum);
        EnumSet enumset = EnumSet.noneOf(oclass);

        Collections.addAll(enumset, aenum);
        long[] along = new long[(((Enum[]) oclass.getEnumConstants()).length - 1) / 64 + 1];

        Enum oenum;
        int i;

        for (Iterator iterator = enumset.iterator(); iterator.hasNext(); along[i] |= (long) (1 << oenum.ordinal() % 64)) {
            oenum = (Enum) iterator.next();
            i = oenum.ordinal() / 64;
        }

        ArrayUtils.reverse(along);
        return along;
    }

    public static EnumSet processBitVector(Class oclass, long i) {
        checkBitVectorable(oclass).getEnumConstants();
        return processBitVectors(oclass, new long[] { i});
    }

    public static EnumSet processBitVectors(Class oclass, long... along) {
        EnumSet enumset = EnumSet.noneOf(asEnum(oclass));
        long[] along1 = ArrayUtils.clone((long[]) Validate.notNull(along));

        ArrayUtils.reverse(along1);
        Enum[] aenum = (Enum[]) oclass.getEnumConstants();
        int i = aenum.length;

        for (int j = 0; j < i; ++j) {
            Enum oenum = aenum[j];
            int k = oenum.ordinal() / 64;

            if (k < along1.length && (along1[k] & (long) (1 << oenum.ordinal() % 64)) != 0L) {
                enumset.add(oenum);
            }
        }

        return enumset;
    }

    private static Class checkBitVectorable(Class oclass) {
        Enum[] aenum = (Enum[]) asEnum(oclass).getEnumConstants();

        Validate.isTrue(aenum.length <= 64, "Cannot store %s %s values in %s bits", new Object[] { Integer.valueOf(aenum.length), oclass.getSimpleName(), Integer.valueOf(64)});
        return oclass;
    }

    private static Class asEnum(Class oclass) {
        Validate.notNull(oclass, "EnumClass must be defined.", new Object[0]);
        Validate.isTrue(oclass.isEnum(), "%s does not seem to be an Enum type", new Object[] { oclass});
        return oclass;
    }
}
