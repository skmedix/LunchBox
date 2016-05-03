package org.apache.logging.log4j.message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.IllegalFormatException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public class MessageFormatMessage implements Message {

    private static final Logger LOGGER = StatusLogger.getLogger();
    private static final long serialVersionUID = -665975803997290697L;
    private static final int HASHVAL = 31;
    private String messagePattern;
    private transient Object[] argArray;
    private String[] stringArgs;
    private transient String formattedMessage;
    private transient Throwable throwable;

    public MessageFormatMessage(String s, Object... aobject) {
        this.messagePattern = s;
        this.argArray = aobject;
        if (aobject != null && aobject.length > 0 && aobject[aobject.length - 1] instanceof Throwable) {
            this.throwable = (Throwable) aobject[aobject.length - 1];
        }

    }

    public String getFormattedMessage() {
        if (this.formattedMessage == null) {
            this.formattedMessage = this.formatMessage(this.messagePattern, this.argArray);
        }

        return this.formattedMessage;
    }

    public String getFormat() {
        return this.messagePattern;
    }

    public Object[] getParameters() {
        return (Object[]) (this.argArray != null ? this.argArray : this.stringArgs);
    }

    protected String formatMessage(String s, Object... aobject) {
        try {
            return MessageFormat.format(s, aobject);
        } catch (IllegalFormatException illegalformatexception) {
            MessageFormatMessage.LOGGER.error("Unable to format msg: " + s, (Throwable) illegalformatexception);
            return s;
        }
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            MessageFormatMessage messageformatmessage = (MessageFormatMessage) object;

            if (this.messagePattern != null) {
                if (this.messagePattern.equals(messageformatmessage.messagePattern)) {
                    return Arrays.equals(this.stringArgs, messageformatmessage.stringArgs);
                }
            } else if (messageformatmessage.messagePattern == null) {
                return Arrays.equals(this.stringArgs, messageformatmessage.stringArgs);
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
        return "StringFormatMessage[messagePattern=" + this.messagePattern + ", args=" + Arrays.toString(this.argArray) + "]";
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
        return this.throwable;
    }
}
