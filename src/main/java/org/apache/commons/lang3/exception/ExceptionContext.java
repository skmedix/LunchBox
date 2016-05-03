package org.apache.commons.lang3.exception;

import java.util.List;
import java.util.Set;

public interface ExceptionContext {

    ExceptionContext addContextValue(String s, Object object);

    ExceptionContext setContextValue(String s, Object object);

    List getContextValues(String s);

    Object getFirstContextValue(String s);

    Set getContextLabels();

    List getContextEntries();

    String getFormattedExceptionMessage(String s);
}
