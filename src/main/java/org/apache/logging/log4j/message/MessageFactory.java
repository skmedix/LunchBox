package org.apache.logging.log4j.message;

public interface MessageFactory {

    Message newMessage(Object object);

    Message newMessage(String s);

    Message newMessage(String s, Object... aobject);
}
