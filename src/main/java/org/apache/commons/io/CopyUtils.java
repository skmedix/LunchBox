package org.apache.commons.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

/** @deprecated */
@Deprecated
public class CopyUtils {

    private static final int DEFAULT_BUFFER_SIZE = 4096;

    public static void copy(byte[] abyte, OutputStream outputstream) throws IOException {
        outputstream.write(abyte);
    }

    public static void copy(byte[] abyte, Writer writer) throws IOException {
        ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte);

        copy((InputStream) bytearrayinputstream, writer);
    }

    public static void copy(byte[] abyte, Writer writer, String s) throws IOException {
        ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte);

        copy((InputStream) bytearrayinputstream, writer, s);
    }

    public static int copy(InputStream inputstream, OutputStream outputstream) throws IOException {
        byte[] abyte = new byte[4096];
        int i = 0;

        int j;

        for (boolean flag = false; -1 != (j = inputstream.read(abyte)); i += j) {
            outputstream.write(abyte, 0, j);
        }

        return i;
    }

    public static int copy(Reader reader, Writer writer) throws IOException {
        char[] achar = new char[4096];
        int i = 0;

        int j;

        for (boolean flag = false; -1 != (j = reader.read(achar)); i += j) {
            writer.write(achar, 0, j);
        }

        return i;
    }

    public static void copy(InputStream inputstream, Writer writer) throws IOException {
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);

        copy((Reader) inputstreamreader, writer);
    }

    public static void copy(InputStream inputstream, Writer writer, String s) throws IOException {
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream, s);

        copy((Reader) inputstreamreader, writer);
    }

    public static void copy(Reader reader, OutputStream outputstream) throws IOException {
        OutputStreamWriter outputstreamwriter = new OutputStreamWriter(outputstream);

        copy(reader, (Writer) outputstreamwriter);
        outputstreamwriter.flush();
    }

    public static void copy(String s, OutputStream outputstream) throws IOException {
        StringReader stringreader = new StringReader(s);
        OutputStreamWriter outputstreamwriter = new OutputStreamWriter(outputstream);

        copy((Reader) stringreader, (Writer) outputstreamwriter);
        outputstreamwriter.flush();
    }

    public static void copy(String s, Writer writer) throws IOException {
        writer.write(s);
    }
}
