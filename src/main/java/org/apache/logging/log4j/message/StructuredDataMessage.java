package org.apache.logging.log4j.message;

import java.util.Map;
import org.apache.logging.log4j.util.EnglishEnums;

public class StructuredDataMessage extends MapMessage {

    private static final long serialVersionUID = 1703221292892071920L;
    private static final int MAX_LENGTH = 32;
    private static final int HASHVAL = 31;
    private StructuredDataId id;
    private String message;
    private String type;

    public StructuredDataMessage(String s, String s1, String s2) {
        this.id = new StructuredDataId(s, (String[]) null, (String[]) null);
        this.message = s1;
        this.type = s2;
    }

    public StructuredDataMessage(String s, String s1, String s2, Map map) {
        super(map);
        this.id = new StructuredDataId(s, (String[]) null, (String[]) null);
        this.message = s1;
        this.type = s2;
    }

    public StructuredDataMessage(StructuredDataId structureddataid, String s, String s1) {
        this.id = structureddataid;
        this.message = s;
        this.type = s1;
    }

    public StructuredDataMessage(StructuredDataId structureddataid, String s, String s1, Map map) {
        super(map);
        this.id = structureddataid;
        this.message = s;
        this.type = s1;
    }

    private StructuredDataMessage(StructuredDataMessage structureddatamessage, Map map) {
        super(map);
        this.id = structureddatamessage.id;
        this.message = structureddatamessage.message;
        this.type = structureddatamessage.type;
    }

    protected StructuredDataMessage() {}

    public String[] getFormats() {
        String[] astring = new String[StructuredDataMessage.Format.values().length];
        int i = 0;
        StructuredDataMessage.Format[] astructureddatamessage_format = StructuredDataMessage.Format.values();
        int j = astructureddatamessage_format.length;

        for (int k = 0; k < j; ++k) {
            StructuredDataMessage.Format structureddatamessage_format = astructureddatamessage_format[k];

            astring[i++] = structureddatamessage_format.name();
        }

        return astring;
    }

    public StructuredDataId getId() {
        return this.id;
    }

    protected void setId(String s) {
        this.id = new StructuredDataId(s, (String[]) null, (String[]) null);
    }

    protected void setId(StructuredDataId structureddataid) {
        this.id = structureddataid;
    }

    public String getType() {
        return this.type;
    }

    protected void setType(String s) {
        if (s.length() > 32) {
            throw new IllegalArgumentException("structured data type exceeds maximum length of 32 characters: " + s);
        } else {
            this.type = s;
        }
    }

    public String getFormat() {
        return this.message;
    }

    protected void setMessageFormat(String s) {
        this.message = s;
    }

    protected void validate(String s, String s1) {
        this.validateKey(s);
    }

    private void validateKey(String s) {
        if (s.length() > 32) {
            throw new IllegalArgumentException("Structured data keys are limited to 32 characters. key: " + s);
        } else {
            char[] achar = s.toCharArray();
            char[] achar1 = achar;
            int i = achar.length;

            for (int j = 0; j < i; ++j) {
                char c0 = achar1[j];

                if (c0 < 33 || c0 > 126 || c0 == 61 || c0 == 93 || c0 == 34) {
                    throw new IllegalArgumentException("Structured data keys must contain printable US ASCII charactersand may not contain a space, =, ], or \"");
                }
            }

        }
    }

    public String asString() {
        return this.asString(StructuredDataMessage.Format.FULL, (StructuredDataId) null);
    }

    public String asString(String s) {
        try {
            return this.asString((StructuredDataMessage.Format) EnglishEnums.valueOf(StructuredDataMessage.Format.class, s), (StructuredDataId) null);
        } catch (IllegalArgumentException illegalargumentexception) {
            return this.asString();
        }
    }

    public final String asString(StructuredDataMessage.Format structureddatamessage_format, StructuredDataId structureddataid) {
        StringBuilder stringbuilder = new StringBuilder();
        boolean flag = StructuredDataMessage.Format.FULL.equals(structureddatamessage_format);

        if (flag) {
            String s = this.getType();

            if (s == null) {
                return stringbuilder.toString();
            }

            stringbuilder.append(this.getType()).append(" ");
        }

        StructuredDataId structureddataid1 = this.getId();

        if (structureddataid1 != null) {
            structureddataid1 = structureddataid1.makeId(structureddataid);
        } else {
            structureddataid1 = structureddataid;
        }

        if (structureddataid1 != null && structureddataid1.getName() != null) {
            stringbuilder.append("[");
            stringbuilder.append(structureddataid1);
            stringbuilder.append(" ");
            this.appendMap(stringbuilder);
            stringbuilder.append("]");
            if (flag) {
                String s1 = this.getFormat();

                if (s1 != null) {
                    stringbuilder.append(" ").append(s1);
                }
            }

            return stringbuilder.toString();
        } else {
            return stringbuilder.toString();
        }
    }

    public String getFormattedMessage() {
        return this.asString(StructuredDataMessage.Format.FULL, (StructuredDataId) null);
    }

    public String getFormattedMessage(String[] astring) {
        if (astring != null && astring.length > 0) {
            String[] astring1 = astring;
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String s = astring1[j];

                if (StructuredDataMessage.Format.XML.name().equalsIgnoreCase(s)) {
                    return this.asXML();
                }

                if (StructuredDataMessage.Format.FULL.name().equalsIgnoreCase(s)) {
                    return this.asString(StructuredDataMessage.Format.FULL, (StructuredDataId) null);
                }
            }

            return this.asString((StructuredDataMessage.Format) null, (StructuredDataId) null);
        } else {
            return this.asString(StructuredDataMessage.Format.FULL, (StructuredDataId) null);
        }
    }

    private String asXML() {
        StringBuilder stringbuilder = new StringBuilder();
        StructuredDataId structureddataid = this.getId();

        if (structureddataid != null && structureddataid.getName() != null && this.type != null) {
            stringbuilder.append("<StructuredData>\n");
            stringbuilder.append("<type>").append(this.type).append("</type>\n");
            stringbuilder.append("<id>").append(structureddataid).append("</id>\n");
            super.asXML(stringbuilder);
            stringbuilder.append("</StructuredData>\n");
            return stringbuilder.toString();
        } else {
            return stringbuilder.toString();
        }
    }

    public String toString() {
        return this.asString((StructuredDataMessage.Format) null, (StructuredDataId) null);
    }

    public MapMessage newInstance(Map map) {
        return new StructuredDataMessage(this, map);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            StructuredDataMessage structureddatamessage = (StructuredDataMessage) object;

            if (!super.equals(object)) {
                return false;
            } else {
                label48: {
                    if (this.type != null) {
                        if (this.type.equals(structureddatamessage.type)) {
                            break label48;
                        }
                    } else if (structureddatamessage.type == null) {
                        break label48;
                    }

                    return false;
                }

                if (this.id != null) {
                    if (!this.id.equals(structureddatamessage.id)) {
                        return false;
                    }
                } else if (structureddatamessage.id != null) {
                    return false;
                }

                if (this.message != null) {
                    if (!this.message.equals(structureddatamessage.message)) {
                        return false;
                    }
                } else if (structureddatamessage.message != null) {
                    return false;
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int i = super.hashCode();

        i = 31 * i + (this.type != null ? this.type.hashCode() : 0);
        i = 31 * i + (this.id != null ? this.id.hashCode() : 0);
        i = 31 * i + (this.message != null ? this.message.hashCode() : 0);
        return i;
    }

    public static enum Format {

        XML, FULL;
    }
}
