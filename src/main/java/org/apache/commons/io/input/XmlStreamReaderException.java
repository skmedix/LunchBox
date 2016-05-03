package org.apache.commons.io.input;

import java.io.IOException;

public class XmlStreamReaderException extends IOException {

    private static final long serialVersionUID = 1L;
    private final String bomEncoding;
    private final String xmlGuessEncoding;
    private final String xmlEncoding;
    private final String contentTypeMime;
    private final String contentTypeEncoding;

    public XmlStreamReaderException(String s, String s1, String s2, String s3) {
        this(s, (String) null, (String) null, s1, s2, s3);
    }

    public XmlStreamReaderException(String s, String s1, String s2, String s3, String s4, String s5) {
        super(s);
        this.contentTypeMime = s1;
        this.contentTypeEncoding = s2;
        this.bomEncoding = s3;
        this.xmlGuessEncoding = s4;
        this.xmlEncoding = s5;
    }

    public String getBomEncoding() {
        return this.bomEncoding;
    }

    public String getXmlGuessEncoding() {
        return this.xmlGuessEncoding;
    }

    public String getXmlEncoding() {
        return this.xmlEncoding;
    }

    public String getContentTypeMime() {
        return this.contentTypeMime;
    }

    public String getContentTypeEncoding() {
        return this.contentTypeEncoding;
    }
}
