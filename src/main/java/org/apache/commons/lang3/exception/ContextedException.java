package org.apache.commons.lang3.exception;

import java.util.List;
import java.util.Set;

public class ContextedException extends Exception implements ExceptionContext {

    private static final long serialVersionUID = 20110706L;
    private final ExceptionContext exceptionContext;

    public ContextedException() {
        this.exceptionContext = new DefaultExceptionContext();
    }

    public ContextedException(String s) {
        super(s);
        this.exceptionContext = new DefaultExceptionContext();
    }

    public ContextedException(Throwable throwable) {
        super(throwable);
        this.exceptionContext = new DefaultExceptionContext();
    }

    public ContextedException(String s, Throwable throwable) {
        super(s, throwable);
        this.exceptionContext = new DefaultExceptionContext();
    }

    public ContextedException(String s, Throwable throwable, ExceptionContext exceptioncontext) {
        super(s, throwable);
        if (exceptioncontext == null) {
            exceptioncontext = new DefaultExceptionContext();
        }

        this.exceptionContext = (ExceptionContext) exceptioncontext;
    }

    public ContextedException addContextValue(String s, Object object) {
        this.exceptionContext.addContextValue(s, object);
        return this;
    }

    public ContextedException setContextValue(String s, Object object) {
        this.exceptionContext.setContextValue(s, object);
        return this;
    }

    public List getContextValues(String s) {
        return this.exceptionContext.getContextValues(s);
    }

    public Object getFirstContextValue(String s) {
        return this.exceptionContext.getFirstContextValue(s);
    }

    public List getContextEntries() {
        return this.exceptionContext.getContextEntries();
    }

    public Set getContextLabels() {
        return this.exceptionContext.getContextLabels();
    }

    public String getMessage() {
        return this.getFormattedExceptionMessage(super.getMessage());
    }

    public String getRawMessage() {
        return super.getMessage();
    }

    public String getFormattedExceptionMessage(String s) {
        return this.exceptionContext.getFormattedExceptionMessage(s);
    }
}
