package org.apache.logging.log4j.status;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.message.Message;

public class StatusData implements Serializable {

    private static final long serialVersionUID = -4341916115118014017L;
    private final long timestamp = System.currentTimeMillis();
    private final StackTraceElement caller;
    private final Level level;
    private final Message msg;
    private final Throwable throwable;

    public StatusData(StackTraceElement stacktraceelement, Level level, Message message, Throwable throwable) {
        this.caller = stacktraceelement;
        this.level = level;
        this.msg = message;
        this.throwable = throwable;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public StackTraceElement getStackTraceElement() {
        return this.caller;
    }

    public Level getLevel() {
        return this.level;
    }

    public Message getMessage() {
        return this.msg;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public String getFormattedStatus() {
        StringBuilder stringbuilder = new StringBuilder();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

        stringbuilder.append(simpledateformat.format(new Date(this.timestamp)));
        stringbuilder.append(" ");
        stringbuilder.append(this.level.toString());
        stringbuilder.append(" ");
        stringbuilder.append(this.msg.getFormattedMessage());
        Object[] aobject = this.msg.getParameters();
        Throwable throwable;

        if (this.throwable == null && aobject != null && aobject[aobject.length - 1] instanceof Throwable) {
            throwable = (Throwable) aobject[aobject.length - 1];
        } else {
            throwable = this.throwable;
        }

        if (throwable != null) {
            stringbuilder.append(" ");
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();

            throwable.printStackTrace(new PrintStream(bytearrayoutputstream));
            stringbuilder.append(bytearrayoutputstream.toString());
        }

        return stringbuilder.toString();
    }
}
