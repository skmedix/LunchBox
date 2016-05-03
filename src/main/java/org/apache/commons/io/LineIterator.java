package org.apache.commons.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LineIterator implements Iterator {

    private final BufferedReader bufferedReader;
    private String cachedLine;
    private boolean finished = false;

    public LineIterator(Reader reader) throws IllegalArgumentException {
        if (reader == null) {
            throw new IllegalArgumentException("Reader must not be null");
        } else {
            if (reader instanceof BufferedReader) {
                this.bufferedReader = (BufferedReader) reader;
            } else {
                this.bufferedReader = new BufferedReader(reader);
            }

        }
    }

    public boolean hasNext() {
        if (this.cachedLine != null) {
            return true;
        } else if (this.finished) {
            return false;
        } else {
            try {
                String s;

                do {
                    s = this.bufferedReader.readLine();
                    if (s == null) {
                        this.finished = true;
                        return false;
                    }
                } while (!this.isValidLine(s));

                this.cachedLine = s;
                return true;
            } catch (IOException ioexception) {
                this.close();
                throw new IllegalStateException(ioexception);
            }
        }
    }

    protected boolean isValidLine(String s) {
        return true;
    }

    public String next() {
        return this.nextLine();
    }

    public String nextLine() {
        if (!this.hasNext()) {
            throw new NoSuchElementException("No more lines");
        } else {
            String s = this.cachedLine;

            this.cachedLine = null;
            return s;
        }
    }

    public void close() {
        this.finished = true;
        IOUtils.closeQuietly((Reader) this.bufferedReader);
        this.cachedLine = null;
    }

    public void remove() {
        throw new UnsupportedOperationException("Remove unsupported on LineIterator");
    }

    public static void closeQuietly(LineIterator lineiterator) {
        if (lineiterator != null) {
            lineiterator.close();
        }

    }
}
