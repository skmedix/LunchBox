package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class BeiderMorseEncoder implements StringEncoder {

    private PhoneticEngine engine;

    public BeiderMorseEncoder() {
        this.engine = new PhoneticEngine(NameType.GENERIC, RuleType.APPROX, true);
    }

    public Object encode(Object object) throws EncoderException {
        if (!(object instanceof String)) {
            throw new EncoderException("BeiderMorseEncoder encode parameter is not of type String");
        } else {
            return this.encode((String) object);
        }
    }

    public String encode(String s) throws EncoderException {
        return s == null ? null : this.engine.encode(s);
    }

    public NameType getNameType() {
        return this.engine.getNameType();
    }

    public RuleType getRuleType() {
        return this.engine.getRuleType();
    }

    public boolean isConcat() {
        return this.engine.isConcat();
    }

    public void setConcat(boolean flag) {
        this.engine = new PhoneticEngine(this.engine.getNameType(), this.engine.getRuleType(), flag, this.engine.getMaxPhonemes());
    }

    public void setNameType(NameType nametype) {
        this.engine = new PhoneticEngine(nametype, this.engine.getRuleType(), this.engine.isConcat(), this.engine.getMaxPhonemes());
    }

    public void setRuleType(RuleType ruletype) {
        this.engine = new PhoneticEngine(this.engine.getNameType(), ruletype, this.engine.isConcat(), this.engine.getMaxPhonemes());
    }

    public void setMaxPhonemes(int i) {
        this.engine = new PhoneticEngine(this.engine.getNameType(), this.engine.getRuleType(), this.engine.isConcat(), i);
    }
}
