package org.apache.logging.log4j.core.pattern;

public interface PatternConverter {

    void format(Object object, StringBuilder stringbuilder);

    String getName();

    String getStyleClass(Object object);
}
