package org.apache.commons.lang;

public class IllegalClassException extends IllegalArgumentException {

    private static final long serialVersionUID = 8063272569377254819L;

    public IllegalClassException(Class expected, Object actual) {
        super("Expected: " + safeGetClassName(expected) + ", actual: " + (actual == null ? "null" : actual.getClass().getName()));
    }

    public IllegalClassException(Class expected, Class actual) {
        super("Expected: " + safeGetClassName(expected) + ", actual: " + safeGetClassName(actual));
    }

    public IllegalClassException(String message) {
        super(message);
    }

    private static final String safeGetClassName(Class cls) {
        return cls == null ? null : cls.getName();
    }
}
