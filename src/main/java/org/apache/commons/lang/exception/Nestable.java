package org.apache.commons.lang.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public interface Nestable {

    Throwable getCause();

    String getMessage();

    String getMessage(int i);

    String[] getMessages();

    Throwable getThrowable(int i);

    int getThrowableCount();

    Throwable[] getThrowables();

    int indexOfThrowable(Class oclass);

    int indexOfThrowable(Class oclass, int i);

    void printStackTrace(PrintWriter printwriter);

    void printStackTrace(PrintStream printstream);

    void printPartialStackTrace(PrintWriter printwriter);
}
