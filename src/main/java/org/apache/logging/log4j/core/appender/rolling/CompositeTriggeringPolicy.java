package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(
    name = "Policies",
    category = "Core",
    printObject = true
)
public final class CompositeTriggeringPolicy implements TriggeringPolicy {

    private final TriggeringPolicy[] policies;

    private CompositeTriggeringPolicy(TriggeringPolicy... atriggeringpolicy) {
        this.policies = atriggeringpolicy;
    }

    public void initialize(RollingFileManager rollingfilemanager) {
        TriggeringPolicy[] atriggeringpolicy = this.policies;
        int i = atriggeringpolicy.length;

        for (int j = 0; j < i; ++j) {
            TriggeringPolicy triggeringpolicy = atriggeringpolicy[j];

            triggeringpolicy.initialize(rollingfilemanager);
        }

    }

    public boolean isTriggeringEvent(LogEvent logevent) {
        TriggeringPolicy[] atriggeringpolicy = this.policies;
        int i = atriggeringpolicy.length;

        for (int j = 0; j < i; ++j) {
            TriggeringPolicy triggeringpolicy = atriggeringpolicy[j];

            if (triggeringpolicy.isTriggeringEvent(logevent)) {
                return true;
            }
        }

        return false;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("CompositeTriggeringPolicy{");
        boolean flag = true;
        TriggeringPolicy[] atriggeringpolicy = this.policies;
        int i = atriggeringpolicy.length;

        for (int j = 0; j < i; ++j) {
            TriggeringPolicy triggeringpolicy = atriggeringpolicy[j];

            if (!flag) {
                stringbuilder.append(", ");
            }

            stringbuilder.append(triggeringpolicy.toString());
            flag = false;
        }

        stringbuilder.append("}");
        return stringbuilder.toString();
    }

    @PluginFactory
    public static CompositeTriggeringPolicy createPolicy(@PluginElement("Policies") TriggeringPolicy... atriggeringpolicy) {
        return new CompositeTriggeringPolicy(atriggeringpolicy);
    }
}
