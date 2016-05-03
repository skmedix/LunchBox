package org.apache.commons.io;

import java.io.Serializable;

public final class IOCase implements Serializable {

    public static final IOCase SENSITIVE = new IOCase("Sensitive", true);
    public static final IOCase INSENSITIVE = new IOCase("Insensitive", false);
    public static final IOCase SYSTEM = new IOCase("System", !FilenameUtils.isSystemWindows());
    private static final long serialVersionUID = -6343169151696340687L;
    private final String name;
    private final transient boolean sensitive;

    public static IOCase forName(String s) {
        if (IOCase.SENSITIVE.name.equals(s)) {
            return IOCase.SENSITIVE;
        } else if (IOCase.INSENSITIVE.name.equals(s)) {
            return IOCase.INSENSITIVE;
        } else if (IOCase.SYSTEM.name.equals(s)) {
            return IOCase.SYSTEM;
        } else {
            throw new IllegalArgumentException("Invalid IOCase name: " + s);
        }
    }

    private IOCase(String s, boolean flag) {
        this.name = s;
        this.sensitive = flag;
    }

    private Object readResolve() {
        return forName(this.name);
    }

    public String getName() {
        return this.name;
    }

    public boolean isCaseSensitive() {
        return this.sensitive;
    }

    public int checkCompareTo(String s, String s1) {
        if (s != null && s1 != null) {
            return this.sensitive ? s.compareTo(s1) : s.compareToIgnoreCase(s1);
        } else {
            throw new NullPointerException("The strings must not be null");
        }
    }

    public boolean checkEquals(String s, String s1) {
        if (s != null && s1 != null) {
            return this.sensitive ? s.equals(s1) : s.equalsIgnoreCase(s1);
        } else {
            throw new NullPointerException("The strings must not be null");
        }
    }

    public boolean checkStartsWith(String s, String s1) {
        return s.regionMatches(!this.sensitive, 0, s1, 0, s1.length());
    }

    public boolean checkEndsWith(String s, String s1) {
        int i = s1.length();

        return s.regionMatches(!this.sensitive, s.length() - i, s1, 0, i);
    }

    public int checkIndexOf(String s, int i, String s1) {
        int j = s.length() - s1.length();

        if (j >= i) {
            for (int k = i; k <= j; ++k) {
                if (this.checkRegionMatches(s, k, s1)) {
                    return k;
                }
            }
        }

        return -1;
    }

    public boolean checkRegionMatches(String s, int i, String s1) {
        return s.regionMatches(!this.sensitive, i, s1, 0, s1.length());
    }

    public String toString() {
        return this.name;
    }
}
