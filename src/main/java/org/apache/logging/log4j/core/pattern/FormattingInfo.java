package org.apache.logging.log4j.core.pattern;

public final class FormattingInfo {

    private static final char[] SPACES = new char[] { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
    private static final FormattingInfo DEFAULT = new FormattingInfo(false, 0, Integer.MAX_VALUE);
    private final int minLength;
    private final int maxLength;
    private final boolean leftAlign;

    public FormattingInfo(boolean flag, int i, int j) {
        this.leftAlign = flag;
        this.minLength = i;
        this.maxLength = j;
    }

    public static FormattingInfo getDefault() {
        return FormattingInfo.DEFAULT;
    }

    public boolean isLeftAligned() {
        return this.leftAlign;
    }

    public int getMinLength() {
        return this.minLength;
    }

    public int getMaxLength() {
        return this.maxLength;
    }

    public void format(int i, StringBuilder stringbuilder) {
        int j = stringbuilder.length() - i;

        if (j > this.maxLength) {
            stringbuilder.delete(i, stringbuilder.length() - this.maxLength);
        } else if (j < this.minLength) {
            int k;

            if (this.leftAlign) {
                k = stringbuilder.length();
                stringbuilder.setLength(i + this.minLength);

                for (int l = k; l < stringbuilder.length(); ++l) {
                    stringbuilder.setCharAt(l, ' ');
                }
            } else {
                for (k = this.minLength - j; k > FormattingInfo.SPACES.length; k -= FormattingInfo.SPACES.length) {
                    stringbuilder.insert(i, FormattingInfo.SPACES);
                }

                stringbuilder.insert(i, FormattingInfo.SPACES, 0, k);
            }
        }

    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append(super.toString());
        stringbuilder.append("[leftAlign=");
        stringbuilder.append(this.leftAlign);
        stringbuilder.append(", maxLength=");
        stringbuilder.append(this.maxLength);
        stringbuilder.append(", minLength=");
        stringbuilder.append(this.minLength);
        stringbuilder.append("]");
        return stringbuilder.toString();
    }
}
