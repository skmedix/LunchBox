package org.apache.logging.log4j.core.selector;

import java.net.URI;
import org.apache.logging.log4j.core.LoggerContext;

public interface NamedContextSelector extends ContextSelector {

    LoggerContext locateContext(String s, Object object, URI uri);

    LoggerContext removeContext(String s);
}
