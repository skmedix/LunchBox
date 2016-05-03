package org.apache.logging.log4j.core.appender;

public class TLSSyslogFrame {

    public static final char SPACE = ' ';
    private String message;
    private int messageLengthInBytes;

    public TLSSyslogFrame(String s) {
        this.setMessage(s);
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String s) {
        this.message = s;
        this.setLengthInBytes();
    }

    private void setLengthInBytes() {
        this.messageLengthInBytes = this.message.length();
    }

    public byte[] getBytes() {
        String s = this.toString();

        return s.getBytes();
    }

    public String toString() {
        String s = Integer.toString(this.messageLengthInBytes);

        return s + ' ' + this.message;
    }

    public boolean equals(Object object) {
        return super.equals(object);
    }

    public boolean equals(TLSSyslogFrame tlssyslogframe) {
        return this.isLengthEquals(tlssyslogframe) && this.isMessageEquals(tlssyslogframe);
    }

    private boolean isLengthEquals(TLSSyslogFrame tlssyslogframe) {
        return this.messageLengthInBytes == tlssyslogframe.messageLengthInBytes;
    }

    private boolean isMessageEquals(TLSSyslogFrame tlssyslogframe) {
        return this.message.equals(tlssyslogframe.message);
    }
}
