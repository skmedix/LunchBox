package org.apache.logging.log4j.core.lookup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
    name = "date",
    category = "Lookup"
)
public class DateLookup implements StrLookup {

    private static final Logger LOGGER = StatusLogger.getLogger();

    public String lookup(String s) {
        return this.formatDate(System.currentTimeMillis(), s);
    }

    public String lookup(LogEvent logevent, String s) {
        return this.formatDate(logevent.getMillis(), s);
    }

    private String formatDate(long i, String s) {
        Object object = null;

        if (s != null) {
            try {
                object = new SimpleDateFormat(s);
            } catch (Exception exception) {
                DateLookup.LOGGER.error("Invalid date format: \"" + s + "\", using default", (Throwable) exception);
            }
        }

        if (object == null) {
            object = DateFormat.getInstance();
        }

        return ((DateFormat) object).format(new Date(i));
    }
}
