package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public abstract class AbstractCaverphone implements StringEncoder {

    public Object encode(Object object) throws EncoderException {
        if (!(object instanceof String)) {
            throw new EncoderException("Parameter supplied to Caverphone encode is not of type java.lang.String");
        } else {
            return this.encode((String) object);
        }
    }

    public boolean isEncodeEqual(String s, String s1) throws EncoderException {
        return this.encode(s).equals(this.encode(s1));
    }
}
