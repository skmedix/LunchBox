package org.apache.logging.log4j.message;

import java.lang.Thread.State;

class BasicThreadInformation implements ThreadInformation {

    private static final int HASH_SHIFT = 32;
    private static final int HASH_MULTIPLIER = 31;
    private final long id;
    private final String name;
    private final String longName;
    private final State state;
    private final int priority;
    private final boolean isAlive;
    private final boolean isDaemon;
    private final String threadGroupName;

    public BasicThreadInformation(Thread thread) {
        this.id = thread.getId();
        this.name = thread.getName();
        this.longName = thread.toString();
        this.state = thread.getState();
        this.priority = thread.getPriority();
        this.isAlive = thread.isAlive();
        this.isDaemon = thread.isDaemon();
        ThreadGroup threadgroup = thread.getThreadGroup();

        this.threadGroupName = threadgroup == null ? null : threadgroup.getName();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            BasicThreadInformation basicthreadinformation = (BasicThreadInformation) object;

            if (this.id != basicthreadinformation.id) {
                return false;
            } else {
                if (this.name != null) {
                    if (!this.name.equals(basicthreadinformation.name)) {
                        return false;
                    }
                } else if (basicthreadinformation.name != null) {
                    return false;
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int i = (int) (this.id ^ this.id >>> 32);

        i = 31 * i + (this.name != null ? this.name.hashCode() : 0);
        return i;
    }

    public void printThreadInfo(StringBuilder stringbuilder) {
        stringbuilder.append("\"").append(this.name).append("\" ");
        if (this.isDaemon) {
            stringbuilder.append("daemon ");
        }

        stringbuilder.append("prio=").append(this.priority).append(" tid=").append(this.id).append(" ");
        if (this.threadGroupName != null) {
            stringbuilder.append("group=\"").append(this.threadGroupName).append("\"");
        }

        stringbuilder.append("\n");
        stringbuilder.append("\tThread state: ").append(this.state.name()).append("\n");
    }

    public void printStack(StringBuilder stringbuilder, StackTraceElement[] astacktraceelement) {
        StackTraceElement[] astacktraceelement1 = astacktraceelement;
        int i = astacktraceelement.length;

        for (int j = 0; j < i; ++j) {
            StackTraceElement stacktraceelement = astacktraceelement1[j];

            stringbuilder.append("\tat ").append(stacktraceelement).append("\n");
        }

    }
}
