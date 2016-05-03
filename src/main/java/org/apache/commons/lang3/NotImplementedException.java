package org.apache.commons.lang3;

public class NotImplementedException extends UnsupportedOperationException {

    private static final long serialVersionUID = 20131021L;
    private final String code;

    public NotImplementedException(String s) {
        this(s, (String) null);
    }

    public NotImplementedException(Throwable throwable) {
        this(throwable, (String) null);
    }

    public NotImplementedException(String s, Throwable throwable) {
        this(s, throwable, (String) null);
    }

    public NotImplementedException(String s, String s1) {
        super(s);
        this.code = s1;
    }

    public NotImplementedException(Throwable throwable, String s) {
        super(throwable);
        this.code = s;
    }

    public NotImplementedException(String s, Throwable throwable, String s1) {
        super(s, throwable);
        this.code = s1;
    }

    public String getCode() {
        return this.code;
    }
}
