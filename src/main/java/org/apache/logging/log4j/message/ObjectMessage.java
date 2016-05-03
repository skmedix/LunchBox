package org.apache.logging.log4j.message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectMessage implements Message {

    private static final long serialVersionUID = -5903272448334166185L;
    private transient Object obj;

    public ObjectMessage(Object object) {
        if (object == null) {
            object = "null";
        }

        this.obj = object;
    }

    public String getFormattedMessage() {
        return this.obj.toString();
    }

    public String getFormat() {
        return this.obj.toString();
    }

    public Object[] getParameters() {
        return new Object[] { this.obj};
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            boolean flag;
            label35: {
                ObjectMessage objectmessage = (ObjectMessage) object;

                if (this.obj != null) {
                    if (this.obj.equals(objectmessage.obj)) {
                        break label35;
                    }
                } else if (objectmessage.obj == null) {
                    break label35;
                }

                flag = false;
                return flag;
            }

            flag = true;
            return flag;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.obj != null ? this.obj.hashCode() : 0;
    }

    public String toString() {
        return "ObjectMessage[obj=" + this.obj.toString() + "]";
    }

    private void writeObject(ObjectOutputStream objectoutputstream) throws IOException {
        objectoutputstream.defaultWriteObject();
        if (this.obj instanceof Serializable) {
            objectoutputstream.writeObject(this.obj);
        } else {
            objectoutputstream.writeObject(this.obj.toString());
        }

    }

    private void readObject(ObjectInputStream objectinputstream) throws IOException, ClassNotFoundException {
        objectinputstream.defaultReadObject();
        this.obj = objectinputstream.readObject();
    }

    public Throwable getThrowable() {
        return this.obj instanceof Throwable ? (Throwable) this.obj : null;
    }
}
