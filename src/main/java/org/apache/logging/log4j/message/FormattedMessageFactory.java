package org.apache.logging.log4j.message;

public class FormattedMessageFactory extends AbstractMessageFactory {

    public Message newMessage(String s, Object... aobject) {
        return new FormattedMessage(s, aobject);
    }
}
