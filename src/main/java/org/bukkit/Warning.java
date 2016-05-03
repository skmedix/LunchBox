package org.bukkit;

import com.google.common.collect.ImmutableMap;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

@Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Warning {

    boolean value() default false;

    String reason() default "";

    public static enum WarningState {

        ON, OFF, DEFAULT;

        private static final Map values = ImmutableMap.builder().put("off", Warning.WarningState.OFF).put("false", Warning.WarningState.OFF).put("f", Warning.WarningState.OFF).put("no", Warning.WarningState.OFF).put("n", Warning.WarningState.OFF).put("on", Warning.WarningState.ON).put("true", Warning.WarningState.ON).put("t", Warning.WarningState.ON).put("yes", Warning.WarningState.ON).put("y", Warning.WarningState.ON).put("", Warning.WarningState.DEFAULT).put("d", Warning.WarningState.DEFAULT).put("default", Warning.WarningState.DEFAULT).build();

        public boolean printFor(Warning warning) {
            return this == Warning.WarningState.DEFAULT ? warning == null || warning.value() : this == Warning.WarningState.ON;
        }

        public static Warning.WarningState value(String value) {
            if (value == null) {
                return Warning.WarningState.DEFAULT;
            } else {
                Warning.WarningState state = (Warning.WarningState) Warning.WarningState.values.get(value.toLowerCase());

                return state == null ? Warning.WarningState.DEFAULT : state;
            }
        }
    }
}
