package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;

public interface StrLookup {

    String lookup(String s);

    String lookup(LogEvent logevent, String s);
}
