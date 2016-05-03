package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.MessageFactory;

public interface LoggerContext {

    Object getExternalContext();

    Logger getLogger(String s);

    Logger getLogger(String s, MessageFactory messagefactory);

    boolean hasLogger(String s);
}
