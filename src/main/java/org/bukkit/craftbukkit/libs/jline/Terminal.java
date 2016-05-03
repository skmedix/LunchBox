package org.bukkit.craftbukkit.libs.jline;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Terminal {

    void init() throws Exception;

    void restore() throws Exception;

    void reset() throws Exception;

    boolean isSupported();

    int getWidth();

    int getHeight();

    boolean isAnsiSupported();

    OutputStream wrapOutIfNeeded(OutputStream outputstream);

    InputStream wrapInIfNeeded(InputStream inputstream) throws IOException;

    boolean hasWeirdWrap();

    boolean isEchoEnabled();

    void setEchoEnabled(boolean flag);

    String getOutputEncoding();
}
