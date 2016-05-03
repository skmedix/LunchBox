package org.apache.commons.lang3.builder;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.SystemUtils;

public abstract class ToStringStyle implements Serializable {

    private static final long serialVersionUID = -2587890625525655916L;
    public static final ToStringStyle DEFAULT_STYLE = new ToStringStyle.DefaultToStringStyle();
    public static final ToStringStyle MULTI_LINE_STYLE = new ToStringStyle.MultiLineToStringStyle();
    public static final ToStringStyle NO_FIELD_NAMES_STYLE = new ToStringStyle.NoFieldNameToStringStyle();
    public static final ToStringStyle SHORT_PREFIX_STYLE = new ToStringStyle.ShortPrefixToStringStyle();
    public static final ToStringStyle SIMPLE_STYLE = new ToStringStyle.SimpleToStringStyle();
    private static final ThreadLocal REGISTRY = new ThreadLocal();
    private boolean useFieldNames = true;
    private boolean useClassName = true;
    private boolean useShortClassName = false;
    private boolean useIdentityHashCode = true;
    private String contentStart = "[";
    private String contentEnd = "]";
    private String fieldNameValueSeparator = "=";
    private boolean fieldSeparatorAtStart = false;
    private boolean fieldSeparatorAtEnd = false;
    private String fieldSeparator = ",";
    private String arrayStart = "{";
    private String arraySeparator = ",";
    private boolean arrayContentDetail = true;
    private String arrayEnd = "}";
    private boolean defaultFullDetail = true;
    private String nullText = "<null>";
    private String sizeStartText = "<size=";
    private String sizeEndText = ">";
    private String summaryObjectStartText = "<";
    private String summaryObjectEndText = ">";

    static Map getRegistry() {
        return (Map) ToStringStyle.REGISTRY.get();
    }

    static boolean isRegistered(Object object) {
        Map map = getRegistry();

        return map != null && map.containsKey(object);
    }

    static void register(Object object) {
        if (object != null) {
            Map map = getRegistry();

            if (map == null) {
                ToStringStyle.REGISTRY.set(new WeakHashMap());
            }

            getRegistry().put(object, (Object) null);
        }

    }

    static void unregister(Object object) {
        if (object != null) {
            Map map = getRegistry();

            if (map != null) {
                map.remove(object);
                if (map.isEmpty()) {
                    ToStringStyle.REGISTRY.remove();
                }
            }
        }

    }

    public void appendSuper(StringBuffer stringbuffer, String s) {
        this.appendToString(stringbuffer, s);
    }

    public void appendToString(StringBuffer stringbuffer, String s) {
        if (s != null) {
            int i = s.indexOf(this.contentStart) + this.contentStart.length();
            int j = s.lastIndexOf(this.contentEnd);

            if (i != j && i >= 0 && j >= 0) {
                String s1 = s.substring(i, j);

                if (this.fieldSeparatorAtStart) {
                    this.removeLastFieldSeparator(stringbuffer);
                }

                stringbuffer.append(s1);
                this.appendFieldSeparator(stringbuffer);
            }
        }

    }

    public void appendStart(StringBuffer stringbuffer, Object object) {
        if (object != null) {
            this.appendClassName(stringbuffer, object);
            this.appendIdentityHashCode(stringbuffer, object);
            this.appendContentStart(stringbuffer);
            if (this.fieldSeparatorAtStart) {
                this.appendFieldSeparator(stringbuffer);
            }
        }

    }

    public void appendEnd(StringBuffer stringbuffer, Object object) {
        if (!this.fieldSeparatorAtEnd) {
            this.removeLastFieldSeparator(stringbuffer);
        }

        this.appendContentEnd(stringbuffer);
        unregister(object);
    }

    protected void removeLastFieldSeparator(StringBuffer stringbuffer) {
        int i = stringbuffer.length();
        int j = this.fieldSeparator.length();

        if (i > 0 && j > 0 && i >= j) {
            boolean flag = true;

            for (int k = 0; k < j; ++k) {
                if (stringbuffer.charAt(i - 1 - k) != this.fieldSeparator.charAt(j - 1 - k)) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                stringbuffer.setLength(i - j);
            }
        }

    }

    public void append(StringBuffer stringbuffer, String s, Object object, Boolean obool) {
        this.appendFieldStart(stringbuffer, s);
        if (object == null) {
            this.appendNullText(stringbuffer, s);
        } else {
            this.appendInternal(stringbuffer, s, object, this.isFullDetail(obool));
        }

        this.appendFieldEnd(stringbuffer, s);
    }

    protected void appendInternal(StringBuffer stringbuffer, String s, Object object, boolean flag) {
        if (isRegistered(object) && !(object instanceof Number) && !(object instanceof Boolean) && !(object instanceof Character)) {
            this.appendCyclicObject(stringbuffer, s, object);
        } else {
            register(object);

            try {
                if (object instanceof Collection) {
                    if (flag) {
                        this.appendDetail(stringbuffer, s, (Collection) object);
                    } else {
                        this.appendSummarySize(stringbuffer, s, ((Collection) object).size());
                    }
                } else if (object instanceof Map) {
                    if (flag) {
                        this.appendDetail(stringbuffer, s, (Map) object);
                    } else {
                        this.appendSummarySize(stringbuffer, s, ((Map) object).size());
                    }
                } else if (object instanceof long[]) {
                    if (flag) {
                        this.appendDetail(stringbuffer, s, (long[]) ((long[]) object));
                    } else {
                        this.appendSummary(stringbuffer, s, (long[]) ((long[]) object));
                    }
                } else if (object instanceof int[]) {
                    if (flag) {
                        this.appendDetail(stringbuffer, s, (int[]) ((int[]) object));
                    } else {
                        this.appendSummary(stringbuffer, s, (int[]) ((int[]) object));
                    }
                } else if (object instanceof short[]) {
                    if (flag) {
                        this.appendDetail(stringbuffer, s, (short[]) ((short[]) object));
                    } else {
                        this.appendSummary(stringbuffer, s, (short[]) ((short[]) object));
                    }
                } else if (object instanceof byte[]) {
                    if (flag) {
                        this.appendDetail(stringbuffer, s, (byte[]) ((byte[]) object));
                    } else {
                        this.appendSummary(stringbuffer, s, (byte[]) ((byte[]) object));
                    }
                } else if (object instanceof char[]) {
                    if (flag) {
                        this.appendDetail(stringbuffer, s, (char[]) ((char[]) object));
                    } else {
                        this.appendSummary(stringbuffer, s, (char[]) ((char[]) object));
                    }
                } else if (object instanceof double[]) {
                    if (flag) {
                        this.appendDetail(stringbuffer, s, (double[]) ((double[]) object));
                    } else {
                        this.appendSummary(stringbuffer, s, (double[]) ((double[]) object));
                    }
                } else if (object instanceof float[]) {
                    if (flag) {
                        this.appendDetail(stringbuffer, s, (float[]) ((float[]) object));
                    } else {
                        this.appendSummary(stringbuffer, s, (float[]) ((float[]) object));
                    }
                } else if (object instanceof boolean[]) {
                    if (flag) {
                        this.appendDetail(stringbuffer, s, (boolean[]) ((boolean[]) object));
                    } else {
                        this.appendSummary(stringbuffer, s, (boolean[]) ((boolean[]) object));
                    }
                } else if (object.getClass().isArray()) {
                    if (flag) {
                        this.appendDetail(stringbuffer, s, (Object[]) ((Object[]) object));
                    } else {
                        this.appendSummary(stringbuffer, s, (Object[]) ((Object[]) object));
                    }
                } else if (flag) {
                    this.appendDetail(stringbuffer, s, object);
                } else {
                    this.appendSummary(stringbuffer, s, object);
                }
            } finally {
                unregister(object);
            }

        }
    }

    protected void appendCyclicObject(StringBuffer stringbuffer, String s, Object object) {
        ObjectUtils.identityToString(stringbuffer, object);
    }

    protected void appendDetail(StringBuffer stringbuffer, String s, Object object) {
        stringbuffer.append(object);
    }

    protected void appendDetail(StringBuffer stringbuffer, String s, Collection collection) {
        stringbuffer.append(collection);
    }

    protected void appendDetail(StringBuffer stringbuffer, String s, Map map) {
        stringbuffer.append(map);
    }

    protected void appendSummary(StringBuffer stringbuffer, String s, Object object) {
        stringbuffer.append(this.summaryObjectStartText);
        stringbuffer.append(this.getShortClassName(object.getClass()));
        stringbuffer.append(this.summaryObjectEndText);
    }

    public void append(StringBuffer stringbuffer, String s, long i) {
        this.appendFieldStart(stringbuffer, s);
        this.appendDetail(stringbuffer, s, i);
        this.appendFieldEnd(stringbuffer, s);
    }

    protected void appendDetail(StringBuffer stringbuffer, String s, long i) {
        stringbuffer.append(i);
    }

    public void append(StringBuffer stringbuffer, String s, int i) {
        this.appendFieldStart(stringbuffer, s);
        this.appendDetail(stringbuffer, s, i);
        this.appendFieldEnd(stringbuffer, s);
    }

    protected void appendDetail(StringBuffer stringbuffer, String s, int i) {
        stringbuffer.append(i);
    }

    public void append(StringBuffer stringbuffer, String s, short short0) {
        this.appendFieldStart(stringbuffer, s);
        this.appendDetail(stringbuffer, s, short0);
        this.appendFieldEnd(stringbuffer, s);
    }

    protected void appendDetail(StringBuffer stringbuffer, String s, short short0) {
        stringbuffer.append(short0);
    }

    public void append(StringBuffer stringbuffer, String s, byte b0) {
        this.appendFieldStart(stringbuffer, s);
        this.appendDetail(stringbuffer, s, b0);
        this.appendFieldEnd(stringbuffer, s);
    }

    protected void appendDetail(StringBuffer stringbuffer, String s, byte b0) {
        stringbuffer.append(b0);
    }

    public void append(StringBuffer stringbuffer, String s, char c0) {
        this.appendFieldStart(stringbuffer, s);
        this.appendDetail(stringbuffer, s, c0);
        this.appendFieldEnd(stringbuffer, s);
    }

    protected void appendDetail(StringBuffer stringbuffer, String s, char c0) {
        stringbuffer.append(c0);
    }

    public void append(StringBuffer stringbuffer, String s, double d0) {
        this.appendFieldStart(stringbuffer, s);
        this.appendDetail(stringbuffer, s, d0);
        this.appendFieldEnd(stringbuffer, s);
    }

    protected void appendDetail(StringBuffer stringbuffer, String s, double d0) {
        stringbuffer.append(d0);
    }

    public void append(StringBuffer stringbuffer, String s, float f) {
        this.appendFieldStart(stringbuffer, s);
        this.appendDetail(stringbuffer, s, f);
        this.appendFieldEnd(stringbuffer, s);
    }

    protected void appendDetail(StringBuffer stringbuffer, String s, float f) {
        stringbuffer.append(f);
    }

    public void append(StringBuffer stringbuffer, String s, boolean flag) {
        this.appendFieldStart(stringbuffer, s);
        this.appendDetail(stringbuffer, s, flag);
        this.appendFieldEnd(stringbuffer, s);
    }

    protected void appendDetail(StringBuffer stringbuffer, String s, boolean flag) {
        stringbuffer.append(flag);
    }

    public void append(StringBuffer stringbuffer, String s, Object[] aobject, Boolean obool) {
        this.appendFieldStart(stringbuffer, s);
        if (aobject == null) {
            this.appendNullText(stringbuffer, s);
        } else if (this.isFullDetail(obool)) {
            this.appendDetail(stringbuffer, s, aobject);
        } else {
            this.appendSummary(stringbuffer, s, aobject);
        }

        this.appendFieldEnd(stringbuffer, s);
    }

    protected void appendDetail(StringBuffer stringbuffer, String s, Object[] aobject) {
        stringbuffer.append(this.arrayStart);

        for (int i = 0; i < aobject.length; ++i) {
            Object object = aobject[i];

            if (i > 0) {
                stringbuffer.append(this.arraySeparator);
            }

            if (object == null) {
                this.appendNullText(stringbuffer, s);
            } else {
                this.appendInternal(stringbuffer, s, object, this.arrayContentDetail);
            }
        }

        stringbuffer.append(this.arrayEnd);
    }

    protected void reflectionAppendArrayDetail(StringBuffer stringbuffer, String s, Object object) {
        stringbuffer.append(this.arrayStart);
        int i = Array.getLength(object);

        for (int j = 0; j < i; ++j) {
            Object object1 = Array.get(object, j);

            if (j > 0) {
                stringbuffer.append(this.arraySeparator);
            }

            if (object1 == null) {
                this.appendNullText(stringbuffer, s);
            } else {
                this.appendInternal(stringbuffer, s, object1, this.arrayContentDetail);
            }
        }

        stringbuffer.append(this.arrayEnd);
    }

    protected void appendSummary(StringBuffer stringbuffer, String s, Object[] aobject) {
        this.appendSummarySize(stringbuffer, s, aobject.length);
    }

    public void append(StringBuffer stringbuffer, String s, long[] along, Boolean obool) {
        this.appendFieldStart(stringbuffer, s);
        if (along == null) {
            this.appendNullText(stringbuffer, s);
        } else if (this.isFullDetail(obool)) {
            this.appendDetail(stringbuffer, s, along);
        } else {
            this.appendSummary(stringbuffer, s, along);
        }

        this.appendFieldEnd(stringbuffer, s);
    }

    protected void appendDetail(StringBuffer stringbuffer, String s, long[] along) {
        stringbuffer.append(this.arrayStart);

        for (int i = 0; i < along.length; ++i) {
            if (i > 0) {
                stringbuffer.append(this.arraySeparator);
            }

            this.appendDetail(stringbuffer, s, along[i]);
        }

        stringbuffer.append(this.arrayEnd);
    }

    protected void appendSummary(StringBuffer stringbuffer, String s, long[] along) {
        this.appendSummarySize(stringbuffer, s, along.length);
    }

    public void append(StringBuffer stringbuffer, String s, int[] aint, Boolean obool) {
        this.appendFieldStart(stringbuffer, s);
        if (aint == null) {
            this.appendNullText(stringbuffer, s);
        } else if (this.isFullDetail(obool)) {
            this.appendDetail(stringbuffer, s, aint);
        } else {
            this.appendSummary(stringbuffer, s, aint);
        }

        this.appendFieldEnd(stringbuffer, s);
    }

    protected void appendDetail(StringBuffer stringbuffer, String s, int[] aint) {
        stringbuffer.append(this.arrayStart);

        for (int i = 0; i < aint.length; ++i) {
            if (i > 0) {
                stringbuffer.append(this.arraySeparator);
            }

            this.appendDetail(stringbuffer, s, aint[i]);
        }

        stringbuffer.append(this.arrayEnd);
    }

    protected void appendSummary(StringBuffer stringbuffer, String s, int[] aint) {
        this.appendSummarySize(stringbuffer, s, aint.length);
    }

    public void append(StringBuffer stringbuffer, String s, short[] ashort, Boolean obool) {
        this.appendFieldStart(stringbuffer, s);
        if (ashort == null) {
            this.appendNullText(stringbuffer, s);
        } else if (this.isFullDetail(obool)) {
            this.appendDetail(stringbuffer, s, ashort);
        } else {
            this.appendSummary(stringbuffer, s, ashort);
        }

        this.appendFieldEnd(stringbuffer, s);
    }

    protected void appendDetail(StringBuffer stringbuffer, String s, short[] ashort) {
        stringbuffer.append(this.arrayStart);

        for (int i = 0; i < ashort.length; ++i) {
            if (i > 0) {
                stringbuffer.append(this.arraySeparator);
            }

            this.appendDetail(stringbuffer, s, ashort[i]);
        }

        stringbuffer.append(this.arrayEnd);
    }

    protected void appendSummary(StringBuffer stringbuffer, String s, short[] ashort) {
        this.appendSummarySize(stringbuffer, s, ashort.length);
    }

    public void append(StringBuffer stringbuffer, String s, byte[] abyte, Boolean obool) {
        this.appendFieldStart(stringbuffer, s);
        if (abyte == null) {
            this.appendNullText(stringbuffer, s);
        } else if (this.isFullDetail(obool)) {
            this.appendDetail(stringbuffer, s, abyte);
        } else {
            this.appendSummary(stringbuffer, s, abyte);
        }

        this.appendFieldEnd(stringbuffer, s);
    }

    protected void appendDetail(StringBuffer stringbuffer, String s, byte[] abyte) {
        stringbuffer.append(this.arrayStart);

        for (int i = 0; i < abyte.length; ++i) {
            if (i > 0) {
                stringbuffer.append(this.arraySeparator);
            }

            this.appendDetail(stringbuffer, s, abyte[i]);
        }

        stringbuffer.append(this.arrayEnd);
    }

    protected void appendSummary(StringBuffer stringbuffer, String s, byte[] abyte) {
        this.appendSummarySize(stringbuffer, s, abyte.length);
    }

    public void append(StringBuffer stringbuffer, String s, char[] achar, Boolean obool) {
        this.appendFieldStart(stringbuffer, s);
        if (achar == null) {
            this.appendNullText(stringbuffer, s);
        } else if (this.isFullDetail(obool)) {
            this.appendDetail(stringbuffer, s, achar);
        } else {
            this.appendSummary(stringbuffer, s, achar);
        }

        this.appendFieldEnd(stringbuffer, s);
    }

    protected void appendDetail(StringBuffer stringbuffer, String s, char[] achar) {
        stringbuffer.append(this.arrayStart);

        for (int i = 0; i < achar.length; ++i) {
            if (i > 0) {
                stringbuffer.append(this.arraySeparator);
            }

            this.appendDetail(stringbuffer, s, achar[i]);
        }

        stringbuffer.append(this.arrayEnd);
    }

    protected void appendSummary(StringBuffer stringbuffer, String s, char[] achar) {
        this.appendSummarySize(stringbuffer, s, achar.length);
    }

    public void append(StringBuffer stringbuffer, String s, double[] adouble, Boolean obool) {
        this.appendFieldStart(stringbuffer, s);
        if (adouble == null) {
            this.appendNullText(stringbuffer, s);
        } else if (this.isFullDetail(obool)) {
            this.appendDetail(stringbuffer, s, adouble);
        } else {
            this.appendSummary(stringbuffer, s, adouble);
        }

        this.appendFieldEnd(stringbuffer, s);
    }

    protected void appendDetail(StringBuffer stringbuffer, String s, double[] adouble) {
        stringbuffer.append(this.arrayStart);

        for (int i = 0; i < adouble.length; ++i) {
            if (i > 0) {
                stringbuffer.append(this.arraySeparator);
            }

            this.appendDetail(stringbuffer, s, adouble[i]);
        }

        stringbuffer.append(this.arrayEnd);
    }

    protected void appendSummary(StringBuffer stringbuffer, String s, double[] adouble) {
        this.appendSummarySize(stringbuffer, s, adouble.length);
    }

    public void append(StringBuffer stringbuffer, String s, float[] afloat, Boolean obool) {
        this.appendFieldStart(stringbuffer, s);
        if (afloat == null) {
            this.appendNullText(stringbuffer, s);
        } else if (this.isFullDetail(obool)) {
            this.appendDetail(stringbuffer, s, afloat);
        } else {
            this.appendSummary(stringbuffer, s, afloat);
        }

        this.appendFieldEnd(stringbuffer, s);
    }

    protected void appendDetail(StringBuffer stringbuffer, String s, float[] afloat) {
        stringbuffer.append(this.arrayStart);

        for (int i = 0; i < afloat.length; ++i) {
            if (i > 0) {
                stringbuffer.append(this.arraySeparator);
            }

            this.appendDetail(stringbuffer, s, afloat[i]);
        }

        stringbuffer.append(this.arrayEnd);
    }

    protected void appendSummary(StringBuffer stringbuffer, String s, float[] afloat) {
        this.appendSummarySize(stringbuffer, s, afloat.length);
    }

    public void append(StringBuffer stringbuffer, String s, boolean[] aboolean, Boolean obool) {
        this.appendFieldStart(stringbuffer, s);
        if (aboolean == null) {
            this.appendNullText(stringbuffer, s);
        } else if (this.isFullDetail(obool)) {
            this.appendDetail(stringbuffer, s, aboolean);
        } else {
            this.appendSummary(stringbuffer, s, aboolean);
        }

        this.appendFieldEnd(stringbuffer, s);
    }

    protected void appendDetail(StringBuffer stringbuffer, String s, boolean[] aboolean) {
        stringbuffer.append(this.arrayStart);

        for (int i = 0; i < aboolean.length; ++i) {
            if (i > 0) {
                stringbuffer.append(this.arraySeparator);
            }

            this.appendDetail(stringbuffer, s, aboolean[i]);
        }

        stringbuffer.append(this.arrayEnd);
    }

    protected void appendSummary(StringBuffer stringbuffer, String s, boolean[] aboolean) {
        this.appendSummarySize(stringbuffer, s, aboolean.length);
    }

    protected void appendClassName(StringBuffer stringbuffer, Object object) {
        if (this.useClassName && object != null) {
            register(object);
            if (this.useShortClassName) {
                stringbuffer.append(this.getShortClassName(object.getClass()));
            } else {
                stringbuffer.append(object.getClass().getName());
            }
        }

    }

    protected void appendIdentityHashCode(StringBuffer stringbuffer, Object object) {
        if (this.isUseIdentityHashCode() && object != null) {
            register(object);
            stringbuffer.append('@');
            stringbuffer.append(Integer.toHexString(System.identityHashCode(object)));
        }

    }

    protected void appendContentStart(StringBuffer stringbuffer) {
        stringbuffer.append(this.contentStart);
    }

    protected void appendContentEnd(StringBuffer stringbuffer) {
        stringbuffer.append(this.contentEnd);
    }

    protected void appendNullText(StringBuffer stringbuffer, String s) {
        stringbuffer.append(this.nullText);
    }

    protected void appendFieldSeparator(StringBuffer stringbuffer) {
        stringbuffer.append(this.fieldSeparator);
    }

    protected void appendFieldStart(StringBuffer stringbuffer, String s) {
        if (this.useFieldNames && s != null) {
            stringbuffer.append(s);
            stringbuffer.append(this.fieldNameValueSeparator);
        }

    }

    protected void appendFieldEnd(StringBuffer stringbuffer, String s) {
        this.appendFieldSeparator(stringbuffer);
    }

    protected void appendSummarySize(StringBuffer stringbuffer, String s, int i) {
        stringbuffer.append(this.sizeStartText);
        stringbuffer.append(i);
        stringbuffer.append(this.sizeEndText);
    }

    protected boolean isFullDetail(Boolean obool) {
        return obool == null ? this.defaultFullDetail : obool.booleanValue();
    }

    protected String getShortClassName(Class oclass) {
        return ClassUtils.getShortClassName(oclass);
    }

    protected boolean isUseClassName() {
        return this.useClassName;
    }

    protected void setUseClassName(boolean flag) {
        this.useClassName = flag;
    }

    protected boolean isUseShortClassName() {
        return this.useShortClassName;
    }

    protected void setUseShortClassName(boolean flag) {
        this.useShortClassName = flag;
    }

    protected boolean isUseIdentityHashCode() {
        return this.useIdentityHashCode;
    }

    protected void setUseIdentityHashCode(boolean flag) {
        this.useIdentityHashCode = flag;
    }

    protected boolean isUseFieldNames() {
        return this.useFieldNames;
    }

    protected void setUseFieldNames(boolean flag) {
        this.useFieldNames = flag;
    }

    protected boolean isDefaultFullDetail() {
        return this.defaultFullDetail;
    }

    protected void setDefaultFullDetail(boolean flag) {
        this.defaultFullDetail = flag;
    }

    protected boolean isArrayContentDetail() {
        return this.arrayContentDetail;
    }

    protected void setArrayContentDetail(boolean flag) {
        this.arrayContentDetail = flag;
    }

    protected String getArrayStart() {
        return this.arrayStart;
    }

    protected void setArrayStart(String s) {
        if (s == null) {
            s = "";
        }

        this.arrayStart = s;
    }

    protected String getArrayEnd() {
        return this.arrayEnd;
    }

    protected void setArrayEnd(String s) {
        if (s == null) {
            s = "";
        }

        this.arrayEnd = s;
    }

    protected String getArraySeparator() {
        return this.arraySeparator;
    }

    protected void setArraySeparator(String s) {
        if (s == null) {
            s = "";
        }

        this.arraySeparator = s;
    }

    protected String getContentStart() {
        return this.contentStart;
    }

    protected void setContentStart(String s) {
        if (s == null) {
            s = "";
        }

        this.contentStart = s;
    }

    protected String getContentEnd() {
        return this.contentEnd;
    }

    protected void setContentEnd(String s) {
        if (s == null) {
            s = "";
        }

        this.contentEnd = s;
    }

    protected String getFieldNameValueSeparator() {
        return this.fieldNameValueSeparator;
    }

    protected void setFieldNameValueSeparator(String s) {
        if (s == null) {
            s = "";
        }

        this.fieldNameValueSeparator = s;
    }

    protected String getFieldSeparator() {
        return this.fieldSeparator;
    }

    protected void setFieldSeparator(String s) {
        if (s == null) {
            s = "";
        }

        this.fieldSeparator = s;
    }

    protected boolean isFieldSeparatorAtStart() {
        return this.fieldSeparatorAtStart;
    }

    protected void setFieldSeparatorAtStart(boolean flag) {
        this.fieldSeparatorAtStart = flag;
    }

    protected boolean isFieldSeparatorAtEnd() {
        return this.fieldSeparatorAtEnd;
    }

    protected void setFieldSeparatorAtEnd(boolean flag) {
        this.fieldSeparatorAtEnd = flag;
    }

    protected String getNullText() {
        return this.nullText;
    }

    protected void setNullText(String s) {
        if (s == null) {
            s = "";
        }

        this.nullText = s;
    }

    protected String getSizeStartText() {
        return this.sizeStartText;
    }

    protected void setSizeStartText(String s) {
        if (s == null) {
            s = "";
        }

        this.sizeStartText = s;
    }

    protected String getSizeEndText() {
        return this.sizeEndText;
    }

    protected void setSizeEndText(String s) {
        if (s == null) {
            s = "";
        }

        this.sizeEndText = s;
    }

    protected String getSummaryObjectStartText() {
        return this.summaryObjectStartText;
    }

    protected void setSummaryObjectStartText(String s) {
        if (s == null) {
            s = "";
        }

        this.summaryObjectStartText = s;
    }

    protected String getSummaryObjectEndText() {
        return this.summaryObjectEndText;
    }

    protected void setSummaryObjectEndText(String s) {
        if (s == null) {
            s = "";
        }

        this.summaryObjectEndText = s;
    }

    private static final class MultiLineToStringStyle extends ToStringStyle {

        private static final long serialVersionUID = 1L;

        MultiLineToStringStyle() {
            this.setContentStart("[");
            this.setFieldSeparator(SystemUtils.LINE_SEPARATOR + "  ");
            this.setFieldSeparatorAtStart(true);
            this.setContentEnd(SystemUtils.LINE_SEPARATOR + "]");
        }

        private Object readResolve() {
            return ToStringStyle.MULTI_LINE_STYLE;
        }
    }

    private static final class SimpleToStringStyle extends ToStringStyle {

        private static final long serialVersionUID = 1L;

        SimpleToStringStyle() {
            this.setUseClassName(false);
            this.setUseIdentityHashCode(false);
            this.setUseFieldNames(false);
            this.setContentStart("");
            this.setContentEnd("");
        }

        private Object readResolve() {
            return ToStringStyle.SIMPLE_STYLE;
        }
    }

    private static final class ShortPrefixToStringStyle extends ToStringStyle {

        private static final long serialVersionUID = 1L;

        ShortPrefixToStringStyle() {
            this.setUseShortClassName(true);
            this.setUseIdentityHashCode(false);
        }

        private Object readResolve() {
            return ToStringStyle.SHORT_PREFIX_STYLE;
        }
    }

    private static final class NoFieldNameToStringStyle extends ToStringStyle {

        private static final long serialVersionUID = 1L;

        NoFieldNameToStringStyle() {
            this.setUseFieldNames(false);
        }

        private Object readResolve() {
            return ToStringStyle.NO_FIELD_NAMES_STYLE;
        }
    }

    private static final class DefaultToStringStyle extends ToStringStyle {

        private static final long serialVersionUID = 1L;

        private Object readResolve() {
            return ToStringStyle.DEFAULT_STYLE;
        }
    }
}
