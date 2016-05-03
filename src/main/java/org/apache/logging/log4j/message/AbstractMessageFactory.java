package org.apache.logging.log4j.message;

public abstract class AbstractMessageFactory implements MessageFactory {

    public Message newMessage(Object object) {
        return new ObjectMessage(object);
    }

    public Message newMessage(String s) {
        return new SimpleMessage(s);
    }

    public abstract Message newMessage(String s, Object... aobject);
}
