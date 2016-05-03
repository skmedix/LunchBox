package org.apache.logging.log4j.message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.logging.log4j.status.StatusLogger;

public class LocalizedMessage implements Message, LoggerNameAwareMessage {

    private static final long serialVersionUID = 3893703791567290742L;
    private String bundleId;
    private transient ResourceBundle bundle;
    private Locale locale;
    private transient StatusLogger logger;
    private String loggerName;
    private String messagePattern;
    private String[] stringArgs;
    private transient Object[] argArray;
    private String formattedMessage;
    private transient Throwable throwable;

    public LocalizedMessage(String s, Object[] aobject) {
        this((ResourceBundle) null, (Locale) null, s, aobject);
    }

    public LocalizedMessage(String s, String s1, Object[] aobject) {
        this(s, (Locale) null, s1, aobject);
    }

    public LocalizedMessage(ResourceBundle resourcebundle, String s, Object[] aobject) {
        this(resourcebundle, (Locale) null, s, aobject);
    }

    public LocalizedMessage(String s, Locale locale, String s1, Object[] aobject) {
        this.logger = StatusLogger.getLogger();
        this.messagePattern = s1;
        this.argArray = aobject;
        this.throwable = null;
        this.setup(s, (ResourceBundle) null, locale);
    }

    public LocalizedMessage(ResourceBundle resourcebundle, Locale locale, String s, Object[] aobject) {
        this.logger = StatusLogger.getLogger();
        this.messagePattern = s;
        this.argArray = aobject;
        this.throwable = null;
        this.setup((String) null, resourcebundle, locale);
    }

    public LocalizedMessage(Locale locale, String s, Object[] aobject) {
        this((ResourceBundle) null, locale, s, aobject);
    }

    public LocalizedMessage(String s, Object object) {
        this((ResourceBundle) null, (Locale) null, s, new Object[] { object});
    }

    public LocalizedMessage(String s, String s1, Object object) {
        this(s, (Locale) null, s1, new Object[] { object});
    }

    public LocalizedMessage(ResourceBundle resourcebundle, String s, Object object) {
        this(resourcebundle, (Locale) null, s, new Object[] { object});
    }

    public LocalizedMessage(String s, Locale locale, String s1, Object object) {
        this(s, locale, s1, new Object[] { object});
    }

    public LocalizedMessage(ResourceBundle resourcebundle, Locale locale, String s, Object object) {
        this(resourcebundle, locale, s, new Object[] { object});
    }

    public LocalizedMessage(Locale locale, String s, Object object) {
        this((ResourceBundle) null, locale, s, new Object[] { object});
    }

    public LocalizedMessage(String s, Object object, Object object1) {
        this((ResourceBundle) null, (Locale) null, s, new Object[] { object, object1});
    }

    public LocalizedMessage(String s, String s1, Object object, Object object1) {
        this(s, (Locale) null, s1, new Object[] { object, object1});
    }

    public LocalizedMessage(ResourceBundle resourcebundle, String s, Object object, Object object1) {
        this(resourcebundle, (Locale) null, s, new Object[] { object, object1});
    }

    public LocalizedMessage(String s, Locale locale, String s1, Object object, Object object1) {
        this(s, locale, s1, new Object[] { object, object1});
    }

    public LocalizedMessage(ResourceBundle resourcebundle, Locale locale, String s, Object object, Object object1) {
        this(resourcebundle, locale, s, new Object[] { object, object1});
    }

    public LocalizedMessage(Locale locale, String s, Object object, Object object1) {
        this((ResourceBundle) null, locale, s, new Object[] { object, object1});
    }

    public void setLoggerName(String s) {
        this.loggerName = s;
    }

    public String getLoggerName() {
        return this.loggerName;
    }

    private void setup(String s, ResourceBundle resourcebundle, Locale locale) {
        this.bundleId = s;
        this.bundle = resourcebundle;
        this.locale = locale;
    }

    public String getFormattedMessage() {
        if (this.formattedMessage != null) {
            return this.formattedMessage;
        } else {
            ResourceBundle resourcebundle = this.bundle;

            if (resourcebundle == null) {
                if (this.bundleId != null) {
                    resourcebundle = this.getBundle(this.bundleId, this.locale, false);
                } else {
                    resourcebundle = this.getBundle(this.loggerName, this.locale, true);
                }
            }

            String s = this.getFormat();
            String s1 = resourcebundle != null && resourcebundle.containsKey(s) ? resourcebundle.getString(s) : s;
            Object object = this.argArray == null ? this.stringArgs : this.argArray;
            FormattedMessage formattedmessage = new FormattedMessage(s1, (Object[]) object);

            this.formattedMessage = formattedmessage.getFormattedMessage();
            this.throwable = formattedmessage.getThrowable();
            return this.formattedMessage;
        }
    }

    public String getFormat() {
        return this.messagePattern;
    }

    public Object[] getParameters() {
        return (Object[]) (this.argArray != null ? this.argArray : this.stringArgs);
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    protected ResourceBundle getBundle(String s, Locale locale, boolean flag) {
        ResourceBundle resourcebundle = null;

        if (s == null) {
            return null;
        } else {
            try {
                if (locale != null) {
                    resourcebundle = ResourceBundle.getBundle(s, locale);
                } else {
                    resourcebundle = ResourceBundle.getBundle(s);
                }
            } catch (MissingResourceException missingresourceexception) {
                if (!flag) {
                    this.logger.debug("Unable to locate ResourceBundle " + s);
                    return null;
                }
            }

            String s1 = s;

            int i;

            while (resourcebundle == null && (i = s1.lastIndexOf(46)) > 0) {
                s1 = s1.substring(0, i);

                try {
                    if (locale != null) {
                        resourcebundle = ResourceBundle.getBundle(s1, locale);
                    } else {
                        resourcebundle = ResourceBundle.getBundle(s1);
                    }
                } catch (MissingResourceException missingresourceexception1) {
                    this.logger.debug("Unable to locate ResourceBundle " + s1);
                }
            }

            return resourcebundle;
        }
    }

    private void writeObject(ObjectOutputStream objectoutputstream) throws IOException {
        objectoutputstream.defaultWriteObject();
        this.getFormattedMessage();
        objectoutputstream.writeUTF(this.formattedMessage);
        objectoutputstream.writeUTF(this.messagePattern);
        objectoutputstream.writeUTF(this.bundleId);
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
        this.bundleId = objectinputstream.readUTF();
        int i = objectinputstream.readInt();

        this.stringArgs = new String[i];

        for (int j = 0; j < i; ++j) {
            this.stringArgs[j] = objectinputstream.readUTF();
        }

        this.logger = StatusLogger.getLogger();
        this.bundle = null;
        this.argArray = null;
    }
}
