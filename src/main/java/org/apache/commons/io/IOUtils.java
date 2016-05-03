package org.apache.commons.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.output.StringBuilderWriter;

public class IOUtils {

    private static final int EOF = -1;
    public static final char DIR_SEPARATOR_UNIX = '/';
    public static final char DIR_SEPARATOR_WINDOWS = '\\';
    public static final char DIR_SEPARATOR = File.separatorChar;
    public static final String LINE_SEPARATOR_UNIX = "\n";
    public static final String LINE_SEPARATOR_WINDOWS = "\r\n";
    public static final String LINE_SEPARATOR;
    private static final int DEFAULT_BUFFER_SIZE = 4096;
    private static final int SKIP_BUFFER_SIZE = 2048;
    private static char[] SKIP_CHAR_BUFFER;
    private static byte[] SKIP_BYTE_BUFFER;

    public static void close(URLConnection urlconnection) {
        if (urlconnection instanceof HttpURLConnection) {
            ((HttpURLConnection) urlconnection).disconnect();
        }

    }

    public static void closeQuietly(Reader reader) {
        closeQuietly((Closeable) reader);
    }

    public static void closeQuietly(Writer writer) {
        closeQuietly((Closeable) writer);
    }

    public static void closeQuietly(InputStream inputstream) {
        closeQuietly((Closeable) inputstream);
    }

    public static void closeQuietly(OutputStream outputstream) {
        closeQuietly((Closeable) outputstream);
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioexception) {
            ;
        }

    }

    public static void closeQuietly(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ioexception) {
                ;
            }
        }

    }

    public static void closeQuietly(Selector selector) {
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException ioexception) {
                ;
            }
        }

    }

    public static void closeQuietly(ServerSocket serversocket) {
        if (serversocket != null) {
            try {
                serversocket.close();
            } catch (IOException ioexception) {
                ;
            }
        }

    }

    public static InputStream toBufferedInputStream(InputStream inputstream) throws IOException {
        return ByteArrayOutputStream.toBufferedInputStream(inputstream);
    }

    public static BufferedReader toBufferedReader(Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    public static byte[] toByteArray(InputStream inputstream) throws IOException {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();

        copy(inputstream, (OutputStream) bytearrayoutputstream);
        return bytearrayoutputstream.toByteArray();
    }

    public static byte[] toByteArray(InputStream inputstream, long i) throws IOException {
        if (i > 2147483647L) {
            throw new IllegalArgumentException("Size cannot be greater than Integer max value: " + i);
        } else {
            return toByteArray(inputstream, (int) i);
        }
    }

    public static byte[] toByteArray(InputStream inputstream, int i) throws IOException {
        if (i < 0) {
            throw new IllegalArgumentException("Size must be equal or greater than zero: " + i);
        } else if (i == 0) {
            return new byte[0];
        } else {
            byte[] abyte = new byte[i];

            int j;
            int k;

            for (j = 0; j < i && (k = inputstream.read(abyte, j, i - j)) != -1; j += k) {
                ;
            }

            if (j != i) {
                throw new IOException("Unexpected readed size. current: " + j + ", excepted: " + i);
            } else {
                return abyte;
            }
        }
    }

    public static byte[] toByteArray(Reader reader) throws IOException {
        return toByteArray(reader, Charset.defaultCharset());
    }

    public static byte[] toByteArray(Reader reader, Charset charset) throws IOException {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();

        copy(reader, (OutputStream) bytearrayoutputstream, charset);
        return bytearrayoutputstream.toByteArray();
    }

    public static byte[] toByteArray(Reader reader, String s) throws IOException {
        return toByteArray(reader, Charsets.toCharset(s));
    }

    /** @deprecated */
    @Deprecated
    public static byte[] toByteArray(String s) throws IOException {
        return s.getBytes();
    }

    public static byte[] toByteArray(URI uri) throws IOException {
        return toByteArray(uri.toURL());
    }

    public static byte[] toByteArray(URL url) throws IOException {
        URLConnection urlconnection = url.openConnection();

        byte[] abyte;

        try {
            abyte = toByteArray(urlconnection);
        } finally {
            close(urlconnection);
        }

        return abyte;
    }

    public static byte[] toByteArray(URLConnection urlconnection) throws IOException {
        InputStream inputstream = urlconnection.getInputStream();

        byte[] abyte;

        try {
            abyte = toByteArray(inputstream);
        } finally {
            inputstream.close();
        }

        return abyte;
    }

    public static char[] toCharArray(InputStream inputstream) throws IOException {
        return toCharArray(inputstream, Charset.defaultCharset());
    }

    public static char[] toCharArray(InputStream inputstream, Charset charset) throws IOException {
        CharArrayWriter chararraywriter = new CharArrayWriter();

        copy(inputstream, (Writer) chararraywriter, charset);
        return chararraywriter.toCharArray();
    }

    public static char[] toCharArray(InputStream inputstream, String s) throws IOException {
        return toCharArray(inputstream, Charsets.toCharset(s));
    }

    public static char[] toCharArray(Reader reader) throws IOException {
        CharArrayWriter chararraywriter = new CharArrayWriter();

        copy(reader, (Writer) chararraywriter);
        return chararraywriter.toCharArray();
    }

    public static String toString(InputStream inputstream) throws IOException {
        return toString(inputstream, Charset.defaultCharset());
    }

    public static String toString(InputStream inputstream, Charset charset) throws IOException {
        StringBuilderWriter stringbuilderwriter = new StringBuilderWriter();

        copy(inputstream, (Writer) stringbuilderwriter, charset);
        return stringbuilderwriter.toString();
    }

    public static String toString(InputStream inputstream, String s) throws IOException {
        return toString(inputstream, Charsets.toCharset(s));
    }

    public static String toString(Reader reader) throws IOException {
        StringBuilderWriter stringbuilderwriter = new StringBuilderWriter();

        copy(reader, (Writer) stringbuilderwriter);
        return stringbuilderwriter.toString();
    }

    public static String toString(URI uri) throws IOException {
        return toString(uri, Charset.defaultCharset());
    }

    public static String toString(URI uri, Charset charset) throws IOException {
        return toString(uri.toURL(), Charsets.toCharset(charset));
    }

    public static String toString(URI uri, String s) throws IOException {
        return toString(uri, Charsets.toCharset(s));
    }

    public static String toString(URL url) throws IOException {
        return toString(url, Charset.defaultCharset());
    }

    public static String toString(URL url, Charset charset) throws IOException {
        InputStream inputstream = url.openStream();

        String s;

        try {
            s = toString(inputstream, charset);
        } finally {
            inputstream.close();
        }

        return s;
    }

    public static String toString(URL url, String s) throws IOException {
        return toString(url, Charsets.toCharset(s));
    }

    /** @deprecated */
    @Deprecated
    public static String toString(byte[] abyte) throws IOException {
        return new String(abyte);
    }

    public static String toString(byte[] abyte, String s) throws IOException {
        return new String(abyte, Charsets.toCharset(s));
    }

    public static List readLines(InputStream inputstream) throws IOException {
        return readLines(inputstream, Charset.defaultCharset());
    }

    public static List readLines(InputStream inputstream, Charset charset) throws IOException {
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream, Charsets.toCharset(charset));

        return readLines((Reader) inputstreamreader);
    }

    public static List readLines(InputStream inputstream, String s) throws IOException {
        return readLines(inputstream, Charsets.toCharset(s));
    }

    public static List readLines(Reader reader) throws IOException {
        BufferedReader bufferedreader = toBufferedReader(reader);
        ArrayList arraylist = new ArrayList();

        for (String s = bufferedreader.readLine(); s != null; s = bufferedreader.readLine()) {
            arraylist.add(s);
        }

        return arraylist;
    }

    public static LineIterator lineIterator(Reader reader) {
        return new LineIterator(reader);
    }

    public static LineIterator lineIterator(InputStream inputstream, Charset charset) throws IOException {
        return new LineIterator(new InputStreamReader(inputstream, Charsets.toCharset(charset)));
    }

    public static LineIterator lineIterator(InputStream inputstream, String s) throws IOException {
        return lineIterator(inputstream, Charsets.toCharset(s));
    }

    public static InputStream toInputStream(CharSequence charsequence) {
        return toInputStream(charsequence, Charset.defaultCharset());
    }

    public static InputStream toInputStream(CharSequence charsequence, Charset charset) {
        return toInputStream(charsequence.toString(), charset);
    }

    public static InputStream toInputStream(CharSequence charsequence, String s) throws IOException {
        return toInputStream(charsequence, Charsets.toCharset(s));
    }

    public static InputStream toInputStream(String s) {
        return toInputStream(s, Charset.defaultCharset());
    }

    public static InputStream toInputStream(String s, Charset charset) {
        return new ByteArrayInputStream(s.getBytes(Charsets.toCharset(charset)));
    }

    public static InputStream toInputStream(String s, String s1) throws IOException {
        byte[] abyte = s.getBytes(Charsets.toCharset(s1));

        return new ByteArrayInputStream(abyte);
    }

    public static void write(byte[] abyte, OutputStream outputstream) throws IOException {
        if (abyte != null) {
            outputstream.write(abyte);
        }

    }

    public static void write(byte[] abyte, Writer writer) throws IOException {
        write(abyte, writer, Charset.defaultCharset());
    }

    public static void write(byte[] abyte, Writer writer, Charset charset) throws IOException {
        if (abyte != null) {
            writer.write(new String(abyte, Charsets.toCharset(charset)));
        }

    }

    public static void write(byte[] abyte, Writer writer, String s) throws IOException {
        write(abyte, writer, Charsets.toCharset(s));
    }

    public static void write(char[] achar, Writer writer) throws IOException {
        if (achar != null) {
            writer.write(achar);
        }

    }

    public static void write(char[] achar, OutputStream outputstream) throws IOException {
        write(achar, outputstream, Charset.defaultCharset());
    }

    public static void write(char[] achar, OutputStream outputstream, Charset charset) throws IOException {
        if (achar != null) {
            outputstream.write((new String(achar)).getBytes(Charsets.toCharset(charset)));
        }

    }

    public static void write(char[] achar, OutputStream outputstream, String s) throws IOException {
        write(achar, outputstream, Charsets.toCharset(s));
    }

    public static void write(CharSequence charsequence, Writer writer) throws IOException {
        if (charsequence != null) {
            write(charsequence.toString(), writer);
        }

    }

    public static void write(CharSequence charsequence, OutputStream outputstream) throws IOException {
        write(charsequence, outputstream, Charset.defaultCharset());
    }

    public static void write(CharSequence charsequence, OutputStream outputstream, Charset charset) throws IOException {
        if (charsequence != null) {
            write(charsequence.toString(), outputstream, charset);
        }

    }

    public static void write(CharSequence charsequence, OutputStream outputstream, String s) throws IOException {
        write(charsequence, outputstream, Charsets.toCharset(s));
    }

    public static void write(String s, Writer writer) throws IOException {
        if (s != null) {
            writer.write(s);
        }

    }

    public static void write(String s, OutputStream outputstream) throws IOException {
        write(s, outputstream, Charset.defaultCharset());
    }

    public static void write(String s, OutputStream outputstream, Charset charset) throws IOException {
        if (s != null) {
            outputstream.write(s.getBytes(Charsets.toCharset(charset)));
        }

    }

    public static void write(String s, OutputStream outputstream, String s1) throws IOException {
        write(s, outputstream, Charsets.toCharset(s1));
    }

    /** @deprecated */
    @Deprecated
    public static void write(StringBuffer stringbuffer, Writer writer) throws IOException {
        if (stringbuffer != null) {
            writer.write(stringbuffer.toString());
        }

    }

    /** @deprecated */
    @Deprecated
    public static void write(StringBuffer stringbuffer, OutputStream outputstream) throws IOException {
        write(stringbuffer, outputstream, (String) null);
    }

    /** @deprecated */
    @Deprecated
    public static void write(StringBuffer stringbuffer, OutputStream outputstream, String s) throws IOException {
        if (stringbuffer != null) {
            outputstream.write(stringbuffer.toString().getBytes(Charsets.toCharset(s)));
        }

    }

    public static void writeLines(Collection collection, String s, OutputStream outputstream) throws IOException {
        writeLines(collection, s, outputstream, Charset.defaultCharset());
    }

    public static void writeLines(Collection collection, String s, OutputStream outputstream, Charset charset) throws IOException {
        if (collection != null) {
            if (s == null) {
                s = IOUtils.LINE_SEPARATOR;
            }

            Charset charset1 = Charsets.toCharset(charset);

            for (Iterator iterator = collection.iterator(); iterator.hasNext(); outputstream.write(s.getBytes(charset1))) {
                Object object = iterator.next();

                if (object != null) {
                    outputstream.write(object.toString().getBytes(charset1));
                }
            }

        }
    }

    public static void writeLines(Collection collection, String s, OutputStream outputstream, String s1) throws IOException {
        writeLines(collection, s, outputstream, Charsets.toCharset(s1));
    }

    public static void writeLines(Collection collection, String s, Writer writer) throws IOException {
        if (collection != null) {
            if (s == null) {
                s = IOUtils.LINE_SEPARATOR;
            }

            for (Iterator iterator = collection.iterator(); iterator.hasNext(); writer.write(s)) {
                Object object = iterator.next();

                if (object != null) {
                    writer.write(object.toString());
                }
            }

        }
    }

    public static int copy(InputStream inputstream, OutputStream outputstream) throws IOException {
        long i = copyLarge(inputstream, outputstream);

        return i > 2147483647L ? -1 : (int) i;
    }

    public static long copyLarge(InputStream inputstream, OutputStream outputstream) throws IOException {
        return copyLarge(inputstream, outputstream, new byte[4096]);
    }

    public static long copyLarge(InputStream inputstream, OutputStream outputstream, byte[] abyte) throws IOException {
        long i = 0L;

        int j;

        for (boolean flag = false; -1 != (j = inputstream.read(abyte)); i += (long) j) {
            outputstream.write(abyte, 0, j);
        }

        return i;
    }

    public static long copyLarge(InputStream inputstream, OutputStream outputstream, long i, long j) throws IOException {
        return copyLarge(inputstream, outputstream, i, j, new byte[4096]);
    }

    public static long copyLarge(InputStream inputstream, OutputStream outputstream, long i, long j, byte[] abyte) throws IOException {
        if (i > 0L) {
            skipFully(inputstream, i);
        }

        if (j == 0L) {
            return 0L;
        } else {
            int k = abyte.length;
            int l = k;

            if (j > 0L && j < (long) k) {
                l = (int) j;
            }

            long i1 = 0L;

            int j1;

            while (l > 0 && -1 != (j1 = inputstream.read(abyte, 0, l))) {
                outputstream.write(abyte, 0, j1);
                i1 += (long) j1;
                if (j > 0L) {
                    l = (int) Math.min(j - i1, (long) k);
                }
            }

            return i1;
        }
    }

    public static void copy(InputStream inputstream, Writer writer) throws IOException {
        copy(inputstream, writer, Charset.defaultCharset());
    }

    public static void copy(InputStream inputstream, Writer writer, Charset charset) throws IOException {
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream, Charsets.toCharset(charset));

        copy((Reader) inputstreamreader, writer);
    }

    public static void copy(InputStream inputstream, Writer writer, String s) throws IOException {
        copy(inputstream, writer, Charsets.toCharset(s));
    }

    public static int copy(Reader reader, Writer writer) throws IOException {
        long i = copyLarge(reader, writer);

        return i > 2147483647L ? -1 : (int) i;
    }

    public static long copyLarge(Reader reader, Writer writer) throws IOException {
        return copyLarge(reader, writer, new char[4096]);
    }

    public static long copyLarge(Reader reader, Writer writer, char[] achar) throws IOException {
        long i = 0L;

        int j;

        for (boolean flag = false; -1 != (j = reader.read(achar)); i += (long) j) {
            writer.write(achar, 0, j);
        }

        return i;
    }

    public static long copyLarge(Reader reader, Writer writer, long i, long j) throws IOException {
        return copyLarge(reader, writer, i, j, new char[4096]);
    }

    public static long copyLarge(Reader reader, Writer writer, long i, long j, char[] achar) throws IOException {
        if (i > 0L) {
            skipFully(reader, i);
        }

        if (j == 0L) {
            return 0L;
        } else {
            int k = achar.length;

            if (j > 0L && j < (long) achar.length) {
                k = (int) j;
            }

            long l = 0L;

            int i1;

            while (k > 0 && -1 != (i1 = reader.read(achar, 0, k))) {
                writer.write(achar, 0, i1);
                l += (long) i1;
                if (j > 0L) {
                    k = (int) Math.min(j - l, (long) achar.length);
                }
            }

            return l;
        }
    }

    public static void copy(Reader reader, OutputStream outputstream) throws IOException {
        copy(reader, outputstream, Charset.defaultCharset());
    }

    public static void copy(Reader reader, OutputStream outputstream, Charset charset) throws IOException {
        OutputStreamWriter outputstreamwriter = new OutputStreamWriter(outputstream, Charsets.toCharset(charset));

        copy(reader, (Writer) outputstreamwriter);
        outputstreamwriter.flush();
    }

    public static void copy(Reader reader, OutputStream outputstream, String s) throws IOException {
        copy(reader, outputstream, Charsets.toCharset(s));
    }

    public static boolean contentEquals(InputStream inputstream, InputStream inputstream1) throws IOException {
        if (!(inputstream instanceof BufferedInputStream)) {
            inputstream = new BufferedInputStream((InputStream) inputstream);
        }

        if (!(inputstream1 instanceof BufferedInputStream)) {
            inputstream1 = new BufferedInputStream((InputStream) inputstream1);
        }

        int i;

        for (int j = ((InputStream) inputstream).read(); -1 != j; j = ((InputStream) inputstream).read()) {
            i = ((InputStream) inputstream1).read();
            if (j != i) {
                return false;
            }
        }

        i = ((InputStream) inputstream1).read();
        return i == -1;
    }

    public static boolean contentEquals(Reader reader, Reader reader1) throws IOException {
        BufferedReader bufferedreader = toBufferedReader(reader);
        BufferedReader bufferedreader1 = toBufferedReader(reader1);

        int i;

        for (int j = bufferedreader.read(); -1 != j; j = bufferedreader.read()) {
            i = bufferedreader1.read();
            if (j != i) {
                return false;
            }
        }

        i = bufferedreader1.read();
        return i == -1;
    }

    public static boolean contentEqualsIgnoreEOL(Reader reader, Reader reader1) throws IOException {
        BufferedReader bufferedreader = toBufferedReader(reader);
        BufferedReader bufferedreader1 = toBufferedReader(reader1);
        String s = bufferedreader.readLine();

        String s1;

        for (s1 = bufferedreader1.readLine(); s != null && s1 != null && s.equals(s1); s1 = bufferedreader1.readLine()) {
            s = bufferedreader.readLine();
        }

        return s == null ? s1 == null : s.equals(s1);
    }

    public static long skip(InputStream inputstream, long i) throws IOException {
        if (i < 0L) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + i);
        } else {
            if (IOUtils.SKIP_BYTE_BUFFER == null) {
                IOUtils.SKIP_BYTE_BUFFER = new byte[2048];
            }

            long j;
            long k;

            for (j = i; j > 0L; j -= k) {
                k = (long) inputstream.read(IOUtils.SKIP_BYTE_BUFFER, 0, (int) Math.min(j, 2048L));
                if (k < 0L) {
                    break;
                }
            }

            return i - j;
        }
    }

    public static long skip(Reader reader, long i) throws IOException {
        if (i < 0L) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + i);
        } else {
            if (IOUtils.SKIP_CHAR_BUFFER == null) {
                IOUtils.SKIP_CHAR_BUFFER = new char[2048];
            }

            long j;
            long k;

            for (j = i; j > 0L; j -= k) {
                k = (long) reader.read(IOUtils.SKIP_CHAR_BUFFER, 0, (int) Math.min(j, 2048L));
                if (k < 0L) {
                    break;
                }
            }

            return i - j;
        }
    }

    public static void skipFully(InputStream inputstream, long i) throws IOException {
        if (i < 0L) {
            throw new IllegalArgumentException("Bytes to skip must not be negative: " + i);
        } else {
            long j = skip(inputstream, i);

            if (j != i) {
                throw new EOFException("Bytes to skip: " + i + " actual: " + j);
            }
        }
    }

    public static void skipFully(Reader reader, long i) throws IOException {
        long j = skip(reader, i);

        if (j != i) {
            throw new EOFException("Chars to skip: " + i + " actual: " + j);
        }
    }

    public static int read(Reader reader, char[] achar, int i, int j) throws IOException {
        if (j < 0) {
            throw new IllegalArgumentException("Length must not be negative: " + j);
        } else {
            int k;
            int l;

            for (k = j; k > 0; k -= l) {
                int i1 = j - k;

                l = reader.read(achar, i + i1, k);
                if (-1 == l) {
                    break;
                }
            }

            return j - k;
        }
    }

    public static int read(Reader reader, char[] achar) throws IOException {
        return read(reader, achar, 0, achar.length);
    }

    public static int read(InputStream inputstream, byte[] abyte, int i, int j) throws IOException {
        if (j < 0) {
            throw new IllegalArgumentException("Length must not be negative: " + j);
        } else {
            int k;
            int l;

            for (k = j; k > 0; k -= l) {
                int i1 = j - k;

                l = inputstream.read(abyte, i + i1, k);
                if (-1 == l) {
                    break;
                }
            }

            return j - k;
        }
    }

    public static int read(InputStream inputstream, byte[] abyte) throws IOException {
        return read(inputstream, abyte, 0, abyte.length);
    }

    public static void readFully(Reader reader, char[] achar, int i, int j) throws IOException {
        int k = read(reader, achar, i, j);

        if (k != j) {
            throw new EOFException("Length to read: " + j + " actual: " + k);
        }
    }

    public static void readFully(Reader reader, char[] achar) throws IOException {
        readFully(reader, achar, 0, achar.length);
    }

    public static void readFully(InputStream inputstream, byte[] abyte, int i, int j) throws IOException {
        int k = read(inputstream, abyte, i, j);

        if (k != j) {
            throw new EOFException("Length to read: " + j + " actual: " + k);
        }
    }

    public static void readFully(InputStream inputstream, byte[] abyte) throws IOException {
        readFully(inputstream, abyte, 0, abyte.length);
    }

    static {
        StringBuilderWriter stringbuilderwriter = new StringBuilderWriter(4);
        PrintWriter printwriter = new PrintWriter(stringbuilderwriter);

        printwriter.println();
        LINE_SEPARATOR = stringbuilderwriter.toString();
        printwriter.close();
    }
}
