package org.apache.logging.log4j.message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.Format;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.regex.Pattern;

public class FormattedMessage implements Message {

    private static final long serialVersionUID = -665975803997290697L;
    private static final int HASHVAL = 31;
    private static final String FORMAT_SPECIFIER = "%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])";
    private static final Pattern MSG_PATTERN = Pattern.compile("%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])");
    private String messagePattern;
    private transient Object[] argArray;
    private String[] stringArgs;
    private transient String formattedMessage;
    private final Throwable throwable;
    private Message message;

    public FormattedMessage(String s, Object[] aobject, Throwable throwable) {
        this.messagePattern = s;
        this.argArray = aobject;
        this.throwable = throwable;
    }

    public FormattedMessage(String s, Object[] aobject) {
        this.messagePattern = s;
        this.argArray = aobject;
        this.throwable = null;
    }

    public FormattedMessage(String s, Object object) {
        this.messagePattern = s;
        this.argArray = new Object[] { object};
        this.throwable = null;
    }

    public FormattedMessage(String s, Object object, Object object1) {
        this(s, new Object[] { object, object1});
    }

    public String getFormattedMessage() {
        if (this.formattedMessage == null) {
            if (this.message == null) {
                this.message = this.getMessage(this.messagePattern, this.argArray, this.throwable);
            }

            this.formattedMessage = this.message.getFormattedMessage();
        }

        return this.formattedMessage;
    }

    public String getFormat() {
        return this.messagePattern;
    }

    public Object[] getParameters() {
        return (Object[]) (this.argArray != null ? this.argArray : this.stringArgs);
    }

    protected Message getMessage(String s, Object[] aobject, Throwable throwable) {
        try {
            MessageFormat messageformat = new MessageFormat(s);
            Format[] aformat = messageformat.getFormats();

            if (aformat != null && aformat.length > 0) {
                return new MessageFormatMessage(s, aobject);
            }
        } catch (Exception exception) {
            ;
        }

        try {
            if (FormattedMessage.MSG_PATTERN.matcher(s).find()) {
                return new StringFormattedMessage(s, aobject);
            }
        } catch (Exception exception1) {
            ;
        }

        return new ParameterizedMessage(s, aobject, throwable);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            FormattedMessage formattedmessage = (FormattedMessage) object;

            if (this.messagePattern != null) {
                if (this.messagePattern.equals(formattedmessage.messagePattern)) {
                    return Arrays.equals(this.stringArgs, formattedmessage.stringArgs);
                }
            } else if (formattedmessage.messagePattern == null) {
                return Arrays.equals(this.stringArgs, formattedmessage.stringArgs);
            }

            return false;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int i = this.messagePattern != null ? this.messagePattern.hashCode() : 0;

        i = 31 * i + (this.stringArgs != null ? Arrays.hashCode(this.stringArgs) : 0);
        return i;
    }

    public String toString() {
        return "FormattedMessage[messagePattern=" + this.messagePattern + ", args=" + Arrays.toString(this.argArray) + "]";
    }

    private void writeObject(ObjectOutputStream objectoutputstream) throws IOException {
        objectoutputstream.defaultWriteObject();
        this.getFormattedMessage();
        objectoutputstream.writeUTF(this.formattedMessage);
        objectoutputstream.writeUTF(this.messagePattern);
        objectoutputstream.writeInt(this.argArray.length);
        this.stringArgs = new String[this.argArray.length];
        int i = 0;
        Object[] aobject = this.argArray;
        int j = aobject.length;

        for (int k = 0; k < j; ++k) {
            Object object = aobject[k];

            this.stringArgs[i] = object.toString();
            ++i;
        }

    }

    private void readObject(ObjectInputStream objectinputstream) throws IOException, ClassNotFoundException {
        objectinputstream.defaultReadObject();
        this.formattedMessage = objectinputstream.readUTF();
        this.messagePattern = objectinputstream.readUTF();
        int i = objectinputstream.readInt();

        this.stringArgs = new String[i];

        for (int j = 0; j < i; ++j) {
            this.stringArgs[j] = objectinputstream.readUTF();
        }

    }

    public Throwable getThrowable() {
        if (this.throwable != null) {
            return this.throwable;
        } else {
            if (this.message == null) {
                this.message = this.getMessage(this.messagePattern, this.argArray, this.throwable);
            }

            return this.message.getThrowable();
        }
    }
}
