package org.apache.logging.log4j.status;

import java.io.PrintStream;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.util.PropertiesUtil;

public class StatusConsoleListener implements StatusListener {

    private static final String STATUS_LEVEL = "org.apache.logging.log4j.StatusLevel";
    private Level level;
    private String[] filters;
    private final PrintStream stream;

    public StatusConsoleListener() {
        this.level = Level.FATAL;
        this.filters = null;
        String s = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.StatusLevel");

        if (s != null) {
            this.level = Level.toLevel(s, Level.FATAL);
        }

        this.stream = System.out;
    }

    public StatusConsoleListener(Level level) {
        this.level = Level.FATAL;
        this.filters = null;
        this.level = level;
        this.stream = System.out;
    }

    public StatusConsoleListener(Level level, PrintStream printstream) {
        this.level = Level.FATAL;
        this.filters = null;
        this.level = level;
        this.stream = printstream;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Level getStatusLevel() {
        return this.level;
    }

    public void log(StatusData statusdata) {
        if (!this.filtered(statusdata)) {
            this.stream.println(statusdata.getFormattedStatus());
        }

    }

    public void setFilters(String... astring) {
        this.filters = astring;
    }

    private boolean filtered(StatusData statusdata) {
        if (this.filters == null) {
            return false;
        } else {
            String s = statusdata.getStackTraceElement().getClassName();
            String[] astring = this.filters;
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String s1 = astring[j];

                if (s.startsWith(s1)) {
                    return true;
                }
            }

            return false;
        }
    }
}
