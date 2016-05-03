package org.apache.logging.log4j.message;

public final class ParameterizedMessageFactory extends AbstractMessageFactory {

    public static final ParameterizedMessageFactory INSTANCE = new ParameterizedMessageFactory();

    public Message newMessage(String s, Object... aobject) {
        return new ParameterizedMessage(s, aobject);
    }
}
