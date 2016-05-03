package org.apache.logging.log4j.message;

public class MessageFormatMessageFactory extends AbstractMessageFactory {

    public Message newMessage(String s, Object... aobject) {
        return new MessageFormatMessage(s, aobject);
    }
}
