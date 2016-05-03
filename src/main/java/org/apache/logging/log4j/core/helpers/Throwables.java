package org.apache.logging.log4j.core.helpers;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Throwables {

    public static List toStringList(Throwable throwable) {
        StringWriter stringwriter = new StringWriter();
        PrintWriter printwriter = new PrintWriter(stringwriter);

        try {
            throwable.printStackTrace(printwriter);
        } catch (RuntimeException runtimeexception) {
            ;
        }

        printwriter.flush();
        LineNumberReader linenumberreader = new LineNumberReader(new StringReader(stringwriter.toString()));
        ArrayList arraylist = new ArrayList();

        try {
            for (String s = linenumberreader.readLine(); s != null; s = linenumberreader.readLine()) {
                arraylist.add(s);
            }
        } catch (IOException ioexception) {
            if (ioexception instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }

            arraylist.add(ioexception.toString());
        }

        return arraylist;
    }
}
