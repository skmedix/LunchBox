package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

/** @deprecated */
@Deprecated
public class Caverphone implements StringEncoder {

    private final Caverphone2 encoder = new Caverphone2();

    public String caverphone(String s) {
        return this.encoder.encode(s);
    }

    public Object encode(Object object) throws EncoderException {
        if (!(object instanceof String)) {
            throw new EncoderException("Parameter supplied to Caverphone encode is not of type java.lang.String");
        } else {
            return this.caverphone((String) object);
        }
    }

    public String encode(String s) {
        return this.caverphone(s);
    }

    public boolean isCaverphoneEqual(String s, String s1) {
        return this.caverphone(s).equals(this.caverphone(s1));
    }
}
