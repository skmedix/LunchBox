package org.apache.commons.io;

import java.io.IOException;
import java.io.Serializable;

public class TaggedIOException extends IOExceptionWithCause {

    private static final long serialVersionUID = -6994123481142850163L;
    private final Serializable tag;

    public static boolean isTaggedWith(Throwable throwable, Object object) {
        return object != null && throwable instanceof TaggedIOException && object.equals(((TaggedIOException) throwable).tag);
    }

    public static void throwCauseIfTaggedWith(Throwable throwable, Object object) throws IOException {
        if (isTaggedWith(throwable, object)) {
            throw ((TaggedIOException) throwable).getCause();
        }
    }

    public TaggedIOException(IOException ioexception, Serializable serializable) {
        super(ioexception.getMessage(), ioexception);
        this.tag = serializable;
    }

    public Serializable getTag() {
        return this.tag;
    }

    public IOException getCause() {
        return (IOException) super.getCause();
    }
}
