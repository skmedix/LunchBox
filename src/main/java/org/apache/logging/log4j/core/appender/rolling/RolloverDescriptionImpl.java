package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.appender.rolling.helper.Action;

public final class RolloverDescriptionImpl implements RolloverDescription {

    private final String activeFileName;
    private final boolean append;
    private final Action synchronous;
    private final Action asynchronous;

    public RolloverDescriptionImpl(String s, boolean flag, Action action, Action action1) {
        if (s == null) {
            throw new NullPointerException("activeFileName");
        } else {
            this.append = flag;
            this.activeFileName = s;
            this.synchronous = action;
            this.asynchronous = action1;
        }
    }

    public String getActiveFileName() {
        return this.activeFileName;
    }

    public boolean getAppend() {
        return this.append;
    }

    public Action getSynchronous() {
        return this.synchronous;
    }

    public Action getAsynchronous() {
        return this.asynchronous;
    }
}
