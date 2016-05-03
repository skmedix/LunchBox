package org.apache.logging.log4j.message;

import java.io.Serializable;

public class StructuredDataId implements Serializable {

    public static final StructuredDataId TIME_QUALITY = new StructuredDataId("timeQuality", (String[]) null, new String[] { "tzKnown", "isSynced", "syncAccuracy"});
    public static final StructuredDataId ORIGIN = new StructuredDataId("origin", (String[]) null, new String[] { "ip", "enterpriseId", "software", "swVersion"});
    public static final StructuredDataId META = new StructuredDataId("meta", (String[]) null, new String[] { "sequenceId", "sysUpTime", "language"});
    public static final int RESERVED = -1;
    private static final long serialVersionUID = 9031746276396249990L;
    private static final int MAX_LENGTH = 32;
    private final String name;
    private final int enterpriseNumber;
    private final String[] required;
    private final String[] optional;

    protected StructuredDataId(String s, String[] astring, String[] astring1) {
        int i = -1;

        if (s != null) {
            if (s.length() > 32) {
                throw new IllegalArgumentException(String.format("Length of id %s exceeds maximum of %d characters", new Object[] { s, Integer.valueOf(32)}));
            }

            i = s.indexOf("@");
        }

        if (i > 0) {
            this.name = s.substring(0, i);
            this.enterpriseNumber = Integer.parseInt(s.substring(i + 1));
        } else {
            this.name = s;
            this.enterpriseNumber = -1;
        }

        this.required = astring;
        this.optional = astring1;
    }

    public StructuredDataId(String s, int i, String[] astring, String[] astring1) {
        if (s == null) {
            throw new IllegalArgumentException("No structured id name was supplied");
        } else if (s.contains("@")) {
            throw new IllegalArgumentException("Structured id name cannot contain an \'@");
        } else if (i <= 0) {
            throw new IllegalArgumentException("No enterprise number was supplied");
        } else {
            this.name = s;
            this.enterpriseNumber = i;
            String s1 = i < 0 ? s : s + "@" + i;

            if (s1.length() > 32) {
                throw new IllegalArgumentException("Length of id exceeds maximum of 32 characters: " + s1);
            } else {
                this.required = astring;
                this.optional = astring1;
            }
        }
    }

    public StructuredDataId makeId(StructuredDataId structureddataid) {
        return structureddataid == null ? this : this.makeId(structureddataid.getName(), structureddataid.getEnterpriseNumber());
    }

    public StructuredDataId makeId(String s, int i) {
        if (i <= 0) {
            return this;
        } else {
            String s1;
            String[] astring;
            String[] astring1;

            if (this.name != null) {
                s1 = this.name;
                astring = this.required;
                astring1 = this.optional;
            } else {
                s1 = s;
                astring = null;
                astring1 = null;
            }

            return new StructuredDataId(s1, i, astring, astring1);
        }
    }

    public String[] getRequired() {
        return this.required;
    }

    public String[] getOptional() {
        return this.optional;
    }

    public String getName() {
        return this.name;
    }

    public int getEnterpriseNumber() {
        return this.enterpriseNumber;
    }

    public boolean isReserved() {
        return this.enterpriseNumber <= 0;
    }

    public String toString() {
        return this.isReserved() ? this.name : this.name + "@" + this.enterpriseNumber;
    }
}
