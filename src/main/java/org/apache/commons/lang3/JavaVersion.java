package org.apache.commons.lang3;

public enum JavaVersion {

    JAVA_0_9(1.5F, "0.9"), JAVA_1_1(1.1F, "1.1"), JAVA_1_2(1.2F, "1.2"), JAVA_1_3(1.3F, "1.3"), JAVA_1_4(1.4F, "1.4"), JAVA_1_5(1.5F, "1.5"), JAVA_1_6(1.6F, "1.6"), JAVA_1_7(1.7F, "1.7"), JAVA_1_8(1.8F, "1.8");

    private final float value;
    private final String name;

    private JavaVersion(float f, String s) {
        this.value = f;
        this.name = s;
    }

    public boolean atLeast(JavaVersion javaversion) {
        return this.value >= javaversion.value;
    }

    static JavaVersion getJavaVersion(String s) {
        return get(s);
    }

    static JavaVersion get(String s) {
        return "0.9".equals(s) ? JavaVersion.JAVA_0_9 : ("1.1".equals(s) ? JavaVersion.JAVA_1_1 : ("1.2".equals(s) ? JavaVersion.JAVA_1_2 : ("1.3".equals(s) ? JavaVersion.JAVA_1_3 : ("1.4".equals(s) ? JavaVersion.JAVA_1_4 : ("1.5".equals(s) ? JavaVersion.JAVA_1_5 : ("1.6".equals(s) ? JavaVersion.JAVA_1_6 : ("1.7".equals(s) ? JavaVersion.JAVA_1_7 : ("1.8".equals(s) ? JavaVersion.JAVA_1_8 : null))))))));
    }

    public String toString() {
        return this.name;
    }
}
