package org.apache.logging.log4j.core.net;

import org.apache.logging.log4j.Level;

public enum Severity {

    EMERG(0), ALERT(1), CRITICAL(2), ERROR(3), WARNING(4), NOTICE(5), INFO(6), DEBUG(7);

    private final int code;

    private Severity(int i) {
        this.code = i;
    }

    public int getCode() {
        return this.code;
    }

    public boolean isEqual(String s) {
        return this.name().equalsIgnoreCase(s);
    }

    public static Severity getSeverity(Level level) {
        switch (Severity.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$Level[level.ordinal()]) {
        case 1:
            return Severity.DEBUG;

        case 2:
            return Severity.DEBUG;

        case 3:
            return Severity.DEBUG;

        case 4:
            return Severity.INFO;

        case 5:
            return Severity.WARNING;

        case 6:
            return Severity.ERROR;

        case 7:
            return Severity.ALERT;

        case 8:
            return Severity.EMERG;

        default:
            return Severity.DEBUG;
        }
    }

    static class SyntheticClass_1 {

        static final int[] $SwitchMap$org$apache$logging$log4j$Level = new int[Level.values().length];

        static {
            try {
                Severity.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$Level[Level.ALL.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                Severity.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$Level[Level.TRACE.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                Severity.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$Level[Level.DEBUG.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                Severity.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$Level[Level.INFO.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            try {
                Severity.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$Level[Level.WARN.ordinal()] = 5;
            } catch (NoSuchFieldError nosuchfielderror4) {
                ;
            }

            try {
                Severity.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$Level[Level.ERROR.ordinal()] = 6;
            } catch (NoSuchFieldError nosuchfielderror5) {
                ;
            }

            try {
                Severity.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$Level[Level.FATAL.ordinal()] = 7;
            } catch (NoSuchFieldError nosuchfielderror6) {
                ;
            }

            try {
                Severity.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$Level[Level.OFF.ordinal()] = 8;
            } catch (NoSuchFieldError nosuchfielderror7) {
                ;
            }

        }
    }
}
