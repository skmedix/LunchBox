package org.apache.logging.log4j;

import java.io.Serializable;

public interface Marker extends Serializable {

    String getName();

    Marker getParent();

    boolean isInstanceOf(Marker marker);

    boolean isInstanceOf(String s);
}
