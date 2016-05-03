package org.apache.logging.log4j.core.appender;

public interface ManagerFactory {

    Object createManager(String s, Object object);
}
