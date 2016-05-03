package org.apache.commons.lang;

import java.util.Arrays;

public class IncompleteArgumentException extends IllegalArgumentException {

    private static final long serialVersionUID = 4954193403612068178L;

    public IncompleteArgumentException(String argName) {
        super(argName + " is incomplete.");
    }

    public IncompleteArgumentException(String argName, String[] items) {
        super(argName + " is missing the following items: " + safeArrayToString(items));
    }

    private static final String safeArrayToString(Object[] array) {
        return array == null ? null : Arrays.asList(array).toString();
    }
}
