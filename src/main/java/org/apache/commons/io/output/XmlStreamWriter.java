package org.apache.commons.io.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.input.XmlStreamReader;

public class XmlStreamWriter extends Writer {

    private static final int BUFFER_SIZE = 4096;
    private final OutputStream out;
    private final String defaultEncoding;
    private StringWriter xmlPrologWriter;
    private Writer writer;
    private String encoding;
    static final Pattern ENCODING_PATTERN = XmlStreamReader.ENCODING_PATTERN;

    public XmlStreamWriter(OutputStream outputstream) {
        this(outputstream, (String) null);
    }

    public XmlStreamWriter(OutputStream outputstream, String s) {
        this.xmlPrologWriter = new StringWriter(4096);
        this.out = outputstream;
        this.defaultEncoding = s != null ? s : "UTF-8";
    }

    public XmlStreamWriter(File file) throws FileNotFoundException {
        this(file, (String) null);
    }

    public XmlStreamWriter(File file, String s) throws FileNotFoundException {
        this((OutputStream) (new FileOutputStream(file)), s);
    }

    public String getEncoding() {
        return this.encoding;
    }

    public String getDefaultEncoding() {
        return this.defaultEncoding;
    }

    public void close() throws IOException {
        if (this.writer == null) {
            this.encoding = this.defaultEncoding;
            this.writer = new OutputStreamWriter(this.out, this.encoding);
            this.writer.write(this.xmlPrologWriter.toString());
        }

        this.writer.close();
    }

    public void flush() throws IOException {
        if (this.writer != null) {
            this.writer.flush();
        }

    }

    private void detectEncoding(char[] achar, int i, int j) throws IOException {
        int k = j;
        StringBuffer stringbuffer = this.xmlPrologWriter.getBuffer();

        if (stringbuffer.length() + j > 4096) {
            k = 4096 - stringbuffer.length();
        }

        this.xmlPrologWriter.write(achar, i, k);
        if (stringbuffer.length() >= 5) {
            if (stringbuffer.substring(0, 5).equals("<?xml")) {
                int l = stringbuffer.indexOf("?>");

                if (l > 0) {
                    Matcher matcher = XmlStreamWriter.ENCODING_PATTERN.matcher(stringbuffer.substring(0, l));

                    if (matcher.find()) {
                        this.encoding = matcher.group(1).toUpperCase();
                        this.encoding = this.encoding.substring(1, this.encoding.length() - 1);
                    } else {
                        this.encoding = this.defaultEncoding;
                    }
                } else if (stringbuffer.length() >= 4096) {
                    this.encoding = this.defaultEncoding;
                }
            } else {
                this.encoding = this.defaultEncoding;
            }

            if (this.encoding != null) {
                this.xmlPrologWriter = null;
                this.writer = new OutputStreamWriter(this.out, this.encoding);
                this.writer.write(stringbuffer.toString());
                if (j > k) {
                    this.writer.write(achar, i + k, j - k);
                }
            }
        }

    }

    public void write(char[] achar, int i, int j) throws IOException {
        if (this.xmlPrologWriter != null) {
            this.detectEncoding(achar, i, j);
        } else {
            this.writer.write(achar, i, j);
        }

    }
}
