package org.yaml.snakeyaml.reader;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.scanner.Constant;

public class StreamReader {

    public static final Pattern NON_PRINTABLE = Pattern.compile("[^\t\n\r -~\u0085 -\ud7ff\ue000-�]");
    private String name;
    private final Reader stream;
    private int pointer = 0;
    private boolean eof = true;
    private String buffer;
    private int index = 0;
    private int line = 0;
    private int column = 0;
    private char[] data;

    public StreamReader(String stream) {
        this.name = "\'string\'";
        this.buffer = "";
        this.checkPrintable(stream);
        this.buffer = stream + "\u0000";
        this.stream = null;
        this.eof = true;
        this.data = null;
    }

    public StreamReader(Reader reader) {
        this.name = "\'reader\'";
        this.buffer = "";
        this.stream = reader;
        this.eof = false;
        this.data = new char[1024];
        this.update();
    }

    void checkPrintable(CharSequence data) {
        Matcher em = StreamReader.NON_PRINTABLE.matcher(data);

        if (em.find()) {
            int position = this.index + this.buffer.length() - this.pointer + em.start();

            throw new ReaderException(this.name, position, em.group().charAt(0), "special characters are not allowed");
        }
    }

    void checkPrintable(char[] chars, int begin, int end) {
        for (int i = begin; i < end; ++i) {
            char c = chars[i];

            if (!isPrintable(c)) {
                int position = this.index + this.buffer.length() - this.pointer + i;

                throw new ReaderException(this.name, position, c, "special characters are not allowed");
            }
        }

    }

    public static boolean isPrintable(char c) {
        return c >= 32 && c <= 126 || c == 10 || c == 13 || c == 9 || c == 133 || c >= 160 && c <= '\ud7ff' || c >= '\ue000' && c <= '�';
    }

    public Mark getMark() {
        return new Mark(this.name, this.index, this.line, this.column, this.buffer, this.pointer);
    }

    public void forward() {
        this.forward(1);
    }

    public void forward(int length) {
        if (this.pointer + length + 1 >= this.buffer.length()) {
            this.update();
        }

        boolean ch = false;

        for (int i = 0; i < length; ++i) {
            char c0 = this.buffer.charAt(this.pointer);

            ++this.pointer;
            ++this.index;
            if (!Constant.LINEBR.has(c0) && (c0 != 13 || this.buffer.charAt(this.pointer) == 10)) {
                if (c0 != '\ufeff') {
                    ++this.column;
                }
            } else {
                ++this.line;
                this.column = 0;
            }
        }

    }

    public char peek() {
        return this.buffer.charAt(this.pointer);
    }

    public char peek(int index) {
        if (this.pointer + index + 1 > this.buffer.length()) {
            this.update();
        }

        return this.buffer.charAt(this.pointer + index);
    }

    public String prefix(int length) {
        if (this.pointer + length >= this.buffer.length()) {
            this.update();
        }

        return this.pointer + length > this.buffer.length() ? this.buffer.substring(this.pointer) : this.buffer.substring(this.pointer, this.pointer + length);
    }

    public String prefixForward(int length) {
        String prefix = this.prefix(length);

        this.pointer += length;
        this.index += length;
        this.column += length;
        return prefix;
    }

    private void update() {
        if (!this.eof) {
            this.buffer = this.buffer.substring(this.pointer);
            this.pointer = 0;

            try {
                int ioe = this.stream.read(this.data);

                if (ioe > 0) {
                    this.checkPrintable(this.data, 0, ioe);
                    this.buffer = (new StringBuilder(this.buffer.length() + ioe)).append(this.buffer).append(this.data, 0, ioe).toString();
                } else {
                    this.eof = true;
                    this.buffer = this.buffer + "\u0000";
                }
            } catch (IOException ioexception) {
                throw new YAMLException(ioexception);
            }
        }

    }

    public int getColumn() {
        return this.column;
    }

    public Charset getEncoding() {
        return Charset.forName(((UnicodeReader) this.stream).getEncoding());
    }

    public int getIndex() {
        return this.index;
    }

    public int getLine() {
        return this.line;
    }
}
