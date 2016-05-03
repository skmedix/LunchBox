package org.spigotmc;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.server.v1_8_R3.NBTReadLimiter;

public class LimitStream extends FilterInputStream {

    private final NBTReadLimiter limit;

    public LimitStream(InputStream is, NBTReadLimiter limit) {
        super(is);
        this.limit = limit;
    }

    public int read() throws IOException {
        this.limit.a(8L);
        return super.read();
    }

    public int read(byte[] b) throws IOException {
        this.limit.a((long) (b.length * 8));
        return super.read(b);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        this.limit.a((long) (len * 8));
        return super.read(b, off, len);
    }
}
