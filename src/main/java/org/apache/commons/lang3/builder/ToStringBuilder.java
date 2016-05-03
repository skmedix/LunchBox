package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ObjectUtils;

public class ToStringBuilder implements Builder {

    private static volatile ToStringStyle defaultStyle = ToStringStyle.DEFAULT_STYLE;
    private final StringBuffer buffer;
    private final Object object;
    private final ToStringStyle style;

    public static ToStringStyle getDefaultStyle() {
        return ToStringBuilder.defaultStyle;
    }

    public static void setDefaultStyle(ToStringStyle tostringstyle) {
        if (tostringstyle == null) {
            throw new IllegalArgumentException("The style must not be null");
        } else {
            ToStringBuilder.defaultStyle = tostringstyle;
        }
    }

    public static String reflectionToString(Object object) {
        return ReflectionToStringBuilder.toString(object);
    }

    public static String reflectionToString(Object object, ToStringStyle tostringstyle) {
        return ReflectionToStringBuilder.toString(object, tostringstyle);
    }

    public static String reflectionToString(Object object, ToStringStyle tostringstyle, boolean flag) {
        return ReflectionToStringBuilder.toString(object, tostringstyle, flag, false, (Class) null);
    }

    public static String reflectionToString(Object object, ToStringStyle tostringstyle, boolean flag, Class oclass) {
        return ReflectionToStringBuilder.toString(object, tostringstyle, flag, false, oclass);
    }

    public ToStringBuilder(Object object) {
        this(object, (ToStringStyle) null, (StringBuffer) null);
    }

    public ToStringBuilder(Object object, ToStringStyle tostringstyle) {
        this(object, tostringstyle, (StringBuffer) null);
    }

    public ToStringBuilder(Object object, ToStringStyle tostringstyle, StringBuffer stringbuffer) {
        if (tostringstyle == null) {
            tostringstyle = getDefaultStyle();
        }

        if (stringbuffer == null) {
            stringbuffer = new StringBuffer(512);
        }

        this.buffer = stringbuffer;
        this.style = tostringstyle;
        this.object = object;
        tostringstyle.appendStart(stringbuffer, object);
    }

    public ToStringBuilder append(boolean flag) {
        this.style.append(this.buffer, (String) null, flag);
        return this;
    }

    public ToStringBuilder append(boolean[] aboolean) {
        this.style.append(this.buffer, (String) null, aboolean, (Boolean) null);
        return this;
    }

    public ToStringBuilder append(byte b0) {
        this.style.append(this.buffer, (String) null, b0);
        return this;
    }

    public ToStringBuilder append(byte[] abyte) {
        this.style.append(this.buffer, (String) null, abyte, (Boolean) null);
        return this;
    }

    public ToStringBuilder append(char c0) {
        this.style.append(this.buffer, (String) null, c0);
        return this;
    }

    public ToStringBuilder append(char[] achar) {
        this.style.append(this.buffer, (String) null, achar, (Boolean) null);
        return this;
    }

    public ToStringBuilder append(double d0) {
        this.style.append(this.buffer, (String) null, d0);
        return this;
    }

    public ToStringBuilder append(double[] adouble) {
        this.style.append(this.buffer, (String) null, adouble, (Boolean) null);
        return this;
    }

    public ToStringBuilder append(float f) {
        this.style.append(this.buffer, (String) null, f);
        return this;
    }

    public ToStringBuilder append(float[] afloat) {
        this.style.append(this.buffer, (String) null, afloat, (Boolean) null);
        return this;
    }

    public ToStringBuilder append(int i) {
        this.style.append(this.buffer, (String) null, i);
        return this;
    }

    public ToStringBuilder append(int[] aint) {
        this.style.append(this.buffer, (String) null, aint, (Boolean) null);
        return this;
    }

    public ToStringBuilder append(long i) {
        this.style.append(this.buffer, (String) null, i);
        return this;
    }

    public ToStringBuilder append(long[] along) {
        this.style.append(this.buffer, (String) null, along, (Boolean) null);
        return this;
    }

    public ToStringBuilder append(Object object) {
        this.style.append(this.buffer, (String) null, object, (Boolean) null);
        return this;
    }

    public ToStringBuilder append(Object[] aobject) {
        this.style.append(this.buffer, (String) null, aobject, (Boolean) null);
        return this;
    }

    public ToStringBuilder append(short short0) {
        this.style.append(this.buffer, (String) null, short0);
        return this;
    }

    public ToStringBuilder append(short[] ashort) {
        this.style.append(this.buffer, (String) null, ashort, (Boolean) null);
        return this;
    }

    public ToStringBuilder append(String s, boolean flag) {
        this.style.append(this.buffer, s, flag);
        return this;
    }

    public ToStringBuilder append(String s, boolean[] aboolean) {
        this.style.append(this.buffer, s, aboolean, (Boolean) null);
        return this;
    }

    public ToStringBuilder append(String s, boolean[] aboolean, boolean flag) {
        this.style.append(this.buffer, s, aboolean, Boolean.valueOf(flag));
        return this;
    }

    public ToStringBuilder append(String s, byte b0) {
        this.style.append(this.buffer, s, b0);
        return this;
    }

    public ToStringBuilder append(String s, byte[] abyte) {
        this.style.append(this.buffer, s, abyte, (Boolean) null);
        return this;
    }

    public ToStringBuilder append(String s, byte[] abyte, boolean flag) {
        this.style.append(this.buffer, s, abyte, Boolean.valueOf(flag));
        return this;
    }

    public ToStringBuilder append(String s, char c0) {
        this.style.append(this.buffer, s, c0);
        return this;
    }

    public ToStringBuilder append(String s, char[] achar) {
        this.style.append(this.buffer, s, achar, (Boolean) null);
        return this;
    }

    public ToStringBuilder append(String s, char[] achar, boolean flag) {
        this.style.append(this.buffer, s, achar, Boolean.valueOf(flag));
        return this;
    }

    public ToStringBuilder append(String s, double d0) {
        this.style.append(this.buffer, s, d0);
        return this;
    }

    public ToStringBuilder append(String s, double[] adouble) {
        this.style.append(this.buffer, s, adouble, (Boolean) null);
        return this;
    }

    public ToStringBuilder append(String s, double[] adouble, boolean flag) {
        this.style.append(this.buffer, s, adouble, Boolean.valueOf(flag));
        return this;
    }

    public ToStringBuilder append(String s, float f) {
        this.style.append(this.buffer, s, f);
        return this;
    }

    public ToStringBuilder append(String s, float[] afloat) {
        this.style.append(this.buffer, s, afloat, (Boolean) null);
        return this;
    }

    public ToStringBuilder append(String s, float[] afloat, boolean flag) {
        this.style.append(this.buffer, s, afloat, Boolean.valueOf(flag));
        return this;
    }

    public ToStringBuilder append(String s, int i) {
        this.style.append(this.buffer, s, i);
        return this;
    }

    public ToStringBuilder append(String s, int[] aint) {
        this.style.append(this.buffer, s, aint, (Boolean) null);
        return this;
    }

    public ToStringBuilder append(String s, int[] aint, boolean flag) {
        this.style.append(this.buffer, s, aint, Boolean.valueOf(flag));
        return this;
    }

    public ToStringBuilder append(String s, long i) {
        this.style.append(this.buffer, s, i);
        return this;
    }

    public ToStringBuilder append(String s, long[] along) {
        this.style.append(this.buffer, s, along, (Boolean) null);
        return this;
    }

    public ToStringBuilder append(String s, long[] along, boolean flag) {
        this.style.append(this.buffer, s, along, Boolean.valueOf(flag));
        return this;
    }

    public ToStringBuilder append(String s, Object object) {
        this.style.append(this.buffer, s, object, (Boolean) null);
        return this;
    }

    public ToStringBuilder append(String s, Object object, boolean flag) {
        this.style.append(this.buffer, s, object, Boolean.valueOf(flag));
        return this;
    }

    public ToStringBuilder append(String s, Object[] aobject) {
        this.style.append(this.buffer, s, aobject, (Boolean) null);
        return this;
    }

    public ToStringBuilder append(String s, Object[] aobject, boolean flag) {
        this.style.append(this.buffer, s, aobject, Boolean.valueOf(flag));
        return this;
    }

    public ToStringBuilder append(String s, short short0) {
        this.style.append(this.buffer, s, short0);
        return this;
    }

    public ToStringBuilder append(String s, short[] ashort) {
        this.style.append(this.buffer, s, ashort, (Boolean) null);
        return this;
    }

    public ToStringBuilder append(String s, short[] ashort, boolean flag) {
        this.style.append(this.buffer, s, ashort, Boolean.valueOf(flag));
        return this;
    }

    public ToStringBuilder appendAsObjectToString(Object object) {
        ObjectUtils.identityToString(this.getStringBuffer(), object);
        return this;
    }

    public ToStringBuilder appendSuper(String s) {
        if (s != null) {
            this.style.appendSuper(this.buffer, s);
        }

        return this;
    }

    public ToStringBuilder appendToString(String s) {
        if (s != null) {
            this.style.appendToString(this.buffer, s);
        }

        return this;
    }

    public Object getObject() {
        return this.object;
    }

    public StringBuffer getStringBuffer() {
        return this.buffer;
    }

    public ToStringStyle getStyle() {
        return this.style;
    }

    public String toString() {
        if (this.getObject() == null) {
            this.getStringBuffer().append(this.getStyle().getNullText());
        } else {
            this.style.appendEnd(this.getStringBuffer(), this.getObject());
        }

        return this.getStringBuffer().toString();
    }

    public String build() {
        return this.toString();
    }
}
