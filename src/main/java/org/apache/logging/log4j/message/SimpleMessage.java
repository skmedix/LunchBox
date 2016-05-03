package org.apache.logging.log4j.message;

public class SimpleMessage implements Message {

    private static final long serialVersionUID = -8398002534962715992L;
    private final String message;

    public SimpleMessage() {
        this((String) null);
    }

    public SimpleMessage(String s) {
        this.message = s;
    }

    public String getFormattedMessage() {
        return this.message;
    }

    public String getFormat() {
        return this.message;
    }

    public Object[] getParameters() {
        return null;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            boolean flag;
            label35: {
                SimpleMessage simplemessage = (SimpleMessage) object;

                if (this.message != null) {
                    if (this.message.equals(simplemessage.message)) {
                        break label35;
                    }
                } else if (simplemessage.message == null) {
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
        return this.message != null ? this.message.hashCode() : 0;
    }

    public String toString() {
        return "SimpleMessage[message=" + this.message + "]";
    }

    public Throwable getThrowable() {
        return null;
    }
}
