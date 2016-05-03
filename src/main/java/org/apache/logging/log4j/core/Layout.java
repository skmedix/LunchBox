package org.apache.logging.log4j.core;

import java.io.Serializable;
import java.util.Map;

public interface Layout {

    byte[] getFooter();

    byte[] getHeader();

    byte[] toByteArray(LogEvent logevent);

    Serializable toSerializable(LogEvent logevent);

    String getContentType();

    Map getContentFormat();
}
