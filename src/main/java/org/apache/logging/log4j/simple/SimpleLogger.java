package org.apache.logging.log4j.simple;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public class SimpleLogger extends AbstractLogger {

    private static final char SPACE = ' ';
    private DateFormat dateFormatter;
    private Level level;
    private final boolean showDateTime;
    private final boolean showContextMap;
    private PrintStream stream;
    private final String logName;

    public SimpleLogger(String s, Level level, boolean flag, boolean flag1, boolean flag2, boolean flag3, String s1, MessageFactory messagefactory, PropertiesUtil propertiesutil, PrintStream printstream) {
        super(s, messagefactory);
        String s2 = propertiesutil.getStringProperty("org.apache.logging.log4j.simplelog." + s + ".level");

        this.level = Level.toLevel(s2, level);
        if (flag1) {
            int i = s.lastIndexOf(".");

            if (i > 0 && i < s.length()) {
                this.logName = s.substring(i + 1);
            } else {
                this.logName = s;
            }
        } else if (flag) {
            this.logName = s;
        } else {
            this.logName = null;
        }

        this.showDateTime = flag2;
        this.showContextMap = flag3;
        this.stream = printstream;
        if (flag2) {
            try {
                this.dateFormatter = new SimpleDateFormat(s1);
            } catch (IllegalArgumentException illegalargumentexception) {
                this.dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS zzz");
            }
        }

    }

    public void setStream(PrintStream printstream) {
        this.stream = printstream;
    }

    public Level getLevel() {
        return this.level;
    }

    public void setLevel(Level level) {
        if (level != null) {
            this.level = level;
        }

    }

    public void log(Marker marker, String s, Level level, Message message, Throwable throwable) {
        StringBuilder stringbuilder = new StringBuilder();

        if (this.showDateTime) {
            Date date = new Date();
            DateFormat dateformat = this.dateFormatter;
            String s1;

            synchronized (this.dateFormatter) {
                s1 = this.dateFormatter.format(date);
            }

            stringbuilder.append(s1);
            stringbuilder.append(' ');
        }

        stringbuilder.append(level.toString());
        stringbuilder.append(' ');
        if (this.logName != null && this.logName.length() > 0) {
            stringbuilder.append(this.logName);
            stringbuilder.append(' ');
        }

        stringbuilder.append(message.getFormattedMessage());
        if (this.showContextMap) {
            Map map = ThreadContext.getContext();

            if (map.size() > 0) {
                stringbuilder.append(' ');
                stringbuilder.append(map.toString());
                stringbuilder.append(' ');
            }
        }

        Object[] aobject = message.getParameters();
        Throwable throwable1;

        if (throwable == null && aobject != null && aobject[aobject.length - 1] instanceof Throwable) {
            throwable1 = (Throwable) aobject[aobject.length - 1];
        } else {
            throwable1 = throwable;
        }

        if (throwable1 != null) {
            stringbuilder.append(' ');
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();

            throwable1.printStackTrace(new PrintStream(bytearrayoutputstream));
            stringbuilder.append(bytearrayoutputstream.toString());
        }

        this.stream.println(stringbuilder.toString());
    }

    protected boolean isEnabled(Level level, Marker marker, String s) {
        return this.level.intLevel() >= level.intLevel();
    }

    protected boolean isEnabled(Level level, Marker marker, String s, Throwable throwable) {
        return this.level.intLevel() >= level.intLevel();
    }

    protected boolean isEnabled(Level level, Marker marker, String s, Object... aobject) {
        return this.level.intLevel() >= level.intLevel();
    }

    protected boolean isEnabled(Level level, Marker marker, Object object, Throwable throwable) {
        return this.level.intLevel() >= level.intLevel();
    }

    protected boolean isEnabled(Level level, Marker marker, Message message, Throwable throwable) {
        return this.level.intLevel() >= level.intLevel();
    }
}
