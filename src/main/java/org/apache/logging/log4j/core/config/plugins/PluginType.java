package org.apache.logging.log4j.core.config.plugins;

import java.io.Serializable;

public class PluginType implements Serializable {

    private static final long serialVersionUID = 4743255148794846612L;
    private final Class pluginClass;
    private final String elementName;
    private final boolean printObject;
    private final boolean deferChildren;

    public PluginType(Class oclass, String s, boolean flag, boolean flag1) {
        this.pluginClass = oclass;
        this.elementName = s;
        this.printObject = flag;
        this.deferChildren = flag1;
    }

    public Class getPluginClass() {
        return this.pluginClass;
    }

    public String getElementName() {
        return this.elementName;
    }

    public boolean isObjectPrintable() {
        return this.printObject;
    }

    public boolean isDeferChildren() {
        return this.deferChildren;
    }
}
