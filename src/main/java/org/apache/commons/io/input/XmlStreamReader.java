package org.apache.commons.io.input;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.ByteOrderMark;

public class XmlStreamReader extends Reader {

    private static final int BUFFER_SIZE = 4096;
    private static final String UTF_8 = "UTF-8";
    private static final String US_ASCII = "US-ASCII";
    private static final String UTF_16BE = "UTF-16BE";
    private static final String UTF_16LE = "UTF-16LE";
    private static final String UTF_32BE = "UTF-32BE";
    private static final String UTF_32LE = "UTF-32LE";
    private static final String UTF_16 = "UTF-16";
    private static final String UTF_32 = "UTF-32";
    private static final String EBCDIC = "CP1047";
    private static final ByteOrderMark[] BOMS = new ByteOrderMark[] { ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32LE};
    private static final ByteOrderMark[] XML_GUESS_BYTES = new ByteOrderMark[] { new ByteOrderMark("UTF-8", new int[] { 60, 63, 120, 109}), new ByteOrderMark("UTF-16BE", new int[] { 0, 60, 0, 63}), new ByteOrderMark("UTF-16LE", new int[] { 60, 0, 63, 0}), new ByteOrderMark("UTF-32BE", new int[] { 0, 0, 0, 60, 0, 0, 0, 63, 0, 0, 0, 120, 0, 0, 0, 109}), new ByteOrderMark("UTF-32LE", new int[] { 60, 0, 0, 0, 63, 0, 0, 0, 120, 0, 0, 0, 109, 0, 0, 0}), new ByteOrderMark("CP1047", new int[] { 76, 111, 167, 148})};
    private final Reader reader;
    private final String encoding;
    private final String defaultEncoding;
    private static final Pattern CHARSET_PATTERN = Pattern.compile("charset=[\"\']?([.[^; \"\']]*)[\"\']?");
    public static final Pattern ENCODING_PATTERN = Pattern.compile("<\\?xml.*encoding[\\s]*=[\\s]*((?:\".[^\"]*\")|(?:\'.[^\']*\'))", 8);
    private static final String RAW_EX_1 = "Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch";
    private static final String RAW_EX_2 = "Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] unknown BOM";
    private static final String HTTP_EX_1 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL";
    private static final String HTTP_EX_2 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch";
    private static final String HTTP_EX_3 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], Invalid MIME";

    public String getDefaultEncoding() {
        return this.defaultEncoding;
    }

    public XmlStreamReader(File file) throws IOException {
        this((InputStream) (new FileInputStream(file)));
    }

    public XmlStreamReader(InputStream inputstream) throws IOException {
        this(inputstream, true);
    }

    public XmlStreamReader(InputStream inputstream, boolean flag) throws IOException {
        this(inputstream, flag, (String) null);
    }

    public XmlStreamReader(InputStream inputstream, boolean flag, String s) throws IOException {
        this.defaultEncoding = s;
        BOMInputStream bominputstream = new BOMInputStream(new BufferedInputStream(inputstream, 4096), false, XmlStreamReader.BOMS);
        BOMInputStream bominputstream1 = new BOMInputStream(bominputstream, true, XmlStreamReader.XML_GUESS_BYTES);

        this.encoding = this.doRawStream(bominputstream, bominputstream1, flag);
        this.reader = new InputStreamReader(bominputstream1, this.encoding);
    }

    public XmlStreamReader(URL url) throws IOException {
        this(url.openConnection(), (String) null);
    }

    public XmlStreamReader(URLConnection urlconnection, String s) throws IOException {
        this.defaultEncoding = s;
        boolean flag = true;
        String s1 = urlconnection.getContentType();
        InputStream inputstream = urlconnection.getInputStream();
        BOMInputStream bominputstream = new BOMInputStream(new BufferedInputStream(inputstream, 4096), false, XmlStreamReader.BOMS);
        BOMInputStream bominputstream1 = new BOMInputStream(bominputstream, true, XmlStreamReader.XML_GUESS_BYTES);

        if (!(urlconnection instanceof HttpURLConnection) && s1 == null) {
            this.encoding = this.doRawStream(bominputstream, bominputstream1, flag);
        } else {
            this.encoding = this.doHttpStream(bominputstream, bominputstream1, s1, flag);
        }

        this.reader = new InputStreamReader(bominputstream1, this.encoding);
    }

    public XmlStreamReader(InputStream inputstream, String s) throws IOException {
        this(inputstream, s, true);
    }

    public XmlStreamReader(InputStream inputstream, String s, boolean flag, String s1) throws IOException {
        this.defaultEncoding = s1;
        BOMInputStream bominputstream = new BOMInputStream(new BufferedInputStream(inputstream, 4096), false, XmlStreamReader.BOMS);
        BOMInputStream bominputstream1 = new BOMInputStream(bominputstream, true, XmlStreamReader.XML_GUESS_BYTES);

        this.encoding = this.doHttpStream(bominputstream, bominputstream1, s, flag);
        this.reader = new InputStreamReader(bominputstream1, this.encoding);
    }

    public XmlStreamReader(InputStream inputstream, String s, boolean flag) throws IOException {
        this(inputstream, s, flag, (String) null);
    }

    public String getEncoding() {
        return this.encoding;
    }

    public int read(char[] achar, int i, int j) throws IOException {
        return this.reader.read(achar, i, j);
    }

    public void close() throws IOException {
        this.reader.close();
    }

    private String doRawStream(BOMInputStream bominputstream, BOMInputStream bominputstream1, boolean flag) throws IOException {
        String s = bominputstream.getBOMCharsetName();
        String s1 = bominputstream1.getBOMCharsetName();
        String s2 = getXmlProlog(bominputstream1, s1);

        try {
            return this.calculateRawEncoding(s, s1, s2);
        } catch (XmlStreamReaderException xmlstreamreaderexception) {
            if (flag) {
                return this.doLenientDetection((String) null, xmlstreamreaderexception);
            } else {
                throw xmlstreamreaderexception;
            }
        }
    }

    private String doHttpStream(BOMInputStream bominputstream, BOMInputStream bominputstream1, String s, boolean flag) throws IOException {
        String s1 = bominputstream.getBOMCharsetName();
        String s2 = bominputstream1.getBOMCharsetName();
        String s3 = getXmlProlog(bominputstream1, s2);

        try {
            return this.calculateHttpEncoding(s, s1, s2, s3, flag);
        } catch (XmlStreamReaderException xmlstreamreaderexception) {
            if (flag) {
                return this.doLenientDetection(s, xmlstreamreaderexception);
            } else {
                throw xmlstreamreaderexception;
            }
        }
    }

    private String doLenientDetection(String s, XmlStreamReaderException xmlstreamreaderexception) throws IOException {
        if (s != null && s.startsWith("text/html")) {
            s = s.substring("text/html".length());
            s = "text/xml" + s;

            try {
                return this.calculateHttpEncoding(s, xmlstreamreaderexception.getBomEncoding(), xmlstreamreaderexception.getXmlGuessEncoding(), xmlstreamreaderexception.getXmlEncoding(), true);
            } catch (XmlStreamReaderException xmlstreamreaderexception1) {
                xmlstreamreaderexception = xmlstreamreaderexception1;
            }
        }

        String s1 = xmlstreamreaderexception.getXmlEncoding();

        if (s1 == null) {
            s1 = xmlstreamreaderexception.getContentTypeEncoding();
        }

        if (s1 == null) {
            s1 = this.defaultEncoding == null ? "UTF-8" : this.defaultEncoding;
        }

        return s1;
    }

    String calculateRawEncoding(String s, String s1, String s2) throws IOException {
        if (s != null) {
            String s3;

            if (s.equals("UTF-8")) {
                if (s1 != null && !s1.equals("UTF-8")) {
                    s3 = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { s, s1, s2});
                    throw new XmlStreamReaderException(s3, s, s1, s2);
                } else if (s2 != null && !s2.equals("UTF-8")) {
                    s3 = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { s, s1, s2});
                    throw new XmlStreamReaderException(s3, s, s1, s2);
                } else {
                    return s;
                }
            } else if (!s.equals("UTF-16BE") && !s.equals("UTF-16LE")) {
                if (!s.equals("UTF-32BE") && !s.equals("UTF-32LE")) {
                    s3 = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] unknown BOM", new Object[] { s, s1, s2});
                    throw new XmlStreamReaderException(s3, s, s1, s2);
                } else if (s1 != null && !s1.equals(s)) {
                    s3 = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { s, s1, s2});
                    throw new XmlStreamReaderException(s3, s, s1, s2);
                } else if (s2 != null && !s2.equals("UTF-32") && !s2.equals(s)) {
                    s3 = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { s, s1, s2});
                    throw new XmlStreamReaderException(s3, s, s1, s2);
                } else {
                    return s;
                }
            } else if (s1 != null && !s1.equals(s)) {
                s3 = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { s, s1, s2});
                throw new XmlStreamReaderException(s3, s, s1, s2);
            } else if (s2 != null && !s2.equals("UTF-16") && !s2.equals(s)) {
                s3 = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { s, s1, s2});
                throw new XmlStreamReaderException(s3, s, s1, s2);
            } else {
                return s;
            }
        } else {
            return s1 != null && s2 != null ? (s2.equals("UTF-16") && (s1.equals("UTF-16BE") || s1.equals("UTF-16LE")) ? s1 : s2) : (this.defaultEncoding == null ? "UTF-8" : this.defaultEncoding);
        }
    }

    String calculateHttpEncoding(String s, String s1, String s2, String s3, boolean flag) throws IOException {
        if (flag && s3 != null) {
            return s3;
        } else {
            String s4 = getContentTypeMime(s);
            String s5 = getContentTypeEncoding(s);
            boolean flag1 = isAppXml(s4);
            boolean flag2 = isTextXml(s4);
            String s6;

            if (!flag1 && !flag2) {
                s6 = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], Invalid MIME", new Object[] { s4, s5, s1, s2, s3});
                throw new XmlStreamReaderException(s6, s4, s5, s1, s2, s3);
            } else if (s5 == null) {
                return flag1 ? this.calculateRawEncoding(s1, s2, s3) : (this.defaultEncoding == null ? "US-ASCII" : this.defaultEncoding);
            } else if (!s5.equals("UTF-16BE") && !s5.equals("UTF-16LE")) {
                if (s5.equals("UTF-16")) {
                    if (s1 != null && s1.startsWith("UTF-16")) {
                        return s1;
                    } else {
                        s6 = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch", new Object[] { s4, s5, s1, s2, s3});
                        throw new XmlStreamReaderException(s6, s4, s5, s1, s2, s3);
                    }
                } else if (!s5.equals("UTF-32BE") && !s5.equals("UTF-32LE")) {
                    if (s5.equals("UTF-32")) {
                        if (s1 != null && s1.startsWith("UTF-32")) {
                            return s1;
                        } else {
                            s6 = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch", new Object[] { s4, s5, s1, s2, s3});
                            throw new XmlStreamReaderException(s6, s4, s5, s1, s2, s3);
                        }
                    } else {
                        return s5;
                    }
                } else if (s1 != null) {
                    s6 = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL", new Object[] { s4, s5, s1, s2, s3});
                    throw new XmlStreamReaderException(s6, s4, s5, s1, s2, s3);
                } else {
                    return s5;
                }
            } else if (s1 != null) {
                s6 = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL", new Object[] { s4, s5, s1, s2, s3});
                throw new XmlStreamReaderException(s6, s4, s5, s1, s2, s3);
            } else {
                return s5;
            }
        }
    }

    static String getContentTypeMime(String s) {
        String s1 = null;

        if (s != null) {
            int i = s.indexOf(";");

            if (i >= 0) {
                s1 = s.substring(0, i);
            } else {
                s1 = s;
            }

            s1 = s1.trim();
        }

        return s1;
    }

    static String getContentTypeEncoding(String s) {
        String s1 = null;

        if (s != null) {
            int i = s.indexOf(";");

            if (i > -1) {
                String s2 = s.substring(i + 1);
                Matcher matcher = XmlStreamReader.CHARSET_PATTERN.matcher(s2);

                s1 = matcher.find() ? matcher.group(1) : null;
                s1 = s1 != null ? s1.toUpperCase(Locale.US) : null;
            }
        }

        return s1;
    }

    private static String getXmlProlog(InputStream inputstream, String s) throws IOException {
        String s1 = null;

        if (s != null) {
            byte[] abyte = new byte[4096];

            inputstream.mark(4096);
            int i = 0;
            int j = 4096;
            int k = inputstream.read(abyte, i, j);
            int l = -1;

            String s2;

            for (s2 = null; k != -1 && l == -1 && i < 4096; l = s2.indexOf(62)) {
                i += k;
                j -= k;
                k = inputstream.read(abyte, i, j);
                s2 = new String(abyte, 0, i, s);
            }

            if (l == -1) {
                if (k == -1) {
                    throw new IOException("Unexpected end of XML stream");
                }

                throw new IOException("XML prolog or ROOT element not found on first " + i + " bytes");
            }

            if (i > 0) {
                inputstream.reset();
                BufferedReader bufferedreader = new BufferedReader(new StringReader(s2.substring(0, l + 1)));
                StringBuffer stringbuffer = new StringBuffer();

                for (String s3 = bufferedreader.readLine(); s3 != null; s3 = bufferedreader.readLine()) {
                    stringbuffer.append(s3);
                }

                Matcher matcher = XmlStreamReader.ENCODING_PATTERN.matcher(stringbuffer);

                if (matcher.find()) {
                    s1 = matcher.group(1).toUpperCase();
                    s1 = s1.substring(1, s1.length() - 1);
                }
            }
        }

        return s1;
    }

    static boolean isAppXml(String s) {
        return s != null && (s.equals("application/xml") || s.equals("application/xml-dtd") || s.equals("application/xml-external-parsed-entity") || s.startsWith("application/") && s.endsWith("+xml"));
    }

    static boolean isTextXml(String s) {
        return s != null && (s.equals("text/xml") || s.equals("text/xml-external-parsed-entity") || s.startsWith("text/") && s.endsWith("+xml"));
    }
}
