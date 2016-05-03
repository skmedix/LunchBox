package org.apache.logging.log4j.message;

import java.util.ResourceBundle;

public class LocalizedMessageFactory extends AbstractMessageFactory {

    private final ResourceBundle bundle;
    private final String bundleId;

    public LocalizedMessageFactory(ResourceBundle resourcebundle) {
        this.bundle = resourcebundle;
        this.bundleId = null;
    }

    public LocalizedMessageFactory(String s) {
        this.bundle = null;
        this.bundleId = s;
    }

    public Message newMessage(String s, Object... aobject) {
        return this.bundle == null ? new LocalizedMessage(this.bundleId, s, aobject) : new LocalizedMessage(this.bundle, s, aobject);
    }
}
