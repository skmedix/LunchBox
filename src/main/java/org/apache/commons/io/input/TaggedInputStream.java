package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.UUID;
import org.apache.commons.io.TaggedIOException;

public class TaggedInputStream extends ProxyInputStream {

    private final Serializable tag = UUID.randomUUID();

    public TaggedInputStream(InputStream inputstream) {
        super(inputstream);
    }

    public boolean isCauseOf(Throwable throwable) {
        return TaggedIOException.isTaggedWith(throwable, this.tag);
    }

    public void throwIfCauseOf(Throwable throwable) throws IOException {
        TaggedIOException.throwCauseIfTaggedWith(throwable, this.tag);
    }

    protected void handleIOException(IOException ioexception) throws IOException {
        throw new TaggedIOException(ioexception, this.tag);
    }
}
