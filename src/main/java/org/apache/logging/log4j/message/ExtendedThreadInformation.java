package org.apache.logging.log4j.message;

import java.lang.Thread.State;
import java.lang.management.LockInfo;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;

class ExtendedThreadInformation implements ThreadInformation {

    private final ThreadInfo info;

    public ExtendedThreadInformation(ThreadInfo threadinfo) {
        this.info = threadinfo;
    }

    public void printThreadInfo(StringBuilder stringbuilder) {
        stringbuilder.append("\"").append(this.info.getThreadName()).append("\"");
        stringbuilder.append(" Id=").append(this.info.getThreadId()).append(" ");
        this.formatState(stringbuilder, this.info);
        if (this.info.isSuspended()) {
            stringbuilder.append(" (suspended)");
        }

        if (this.info.isInNative()) {
            stringbuilder.append(" (in native)");
        }

        stringbuilder.append('\n');
    }

    public void printStack(StringBuilder stringbuilder, StackTraceElement[] astacktraceelement) {
        int i = 0;
        StackTraceElement[] astacktraceelement1 = astacktraceelement;
        int j = astacktraceelement.length;

        int k;

        for (k = 0; k < j; ++k) {
            StackTraceElement stacktraceelement = astacktraceelement1[k];

            stringbuilder.append("\tat ").append(stacktraceelement.toString());
            stringbuilder.append('\n');
            if (i == 0 && this.info.getLockInfo() != null) {
                State state = this.info.getThreadState();

                switch (ExtendedThreadInformation.SyntheticClass_1.$SwitchMap$java$lang$Thread$State[state.ordinal()]) {
                case 1:
                    stringbuilder.append("\t-  blocked on ");
                    this.formatLock(stringbuilder, this.info.getLockInfo());
                    stringbuilder.append('\n');
                    break;

                case 2:
                    stringbuilder.append("\t-  waiting on ");
                    this.formatLock(stringbuilder, this.info.getLockInfo());
                    stringbuilder.append('\n');
                    break;

                case 3:
                    stringbuilder.append("\t-  waiting on ");
                    this.formatLock(stringbuilder, this.info.getLockInfo());
                    stringbuilder.append('\n');
                }
            }

            MonitorInfo[] amonitorinfo = this.info.getLockedMonitors();
            int l = amonitorinfo.length;

            for (int i1 = 0; i1 < l; ++i1) {
                MonitorInfo monitorinfo = amonitorinfo[i1];

                if (monitorinfo.getLockedStackDepth() == i) {
                    stringbuilder.append("\t-  locked ");
                    this.formatLock(stringbuilder, monitorinfo);
                    stringbuilder.append('\n');
                }
            }

            ++i;
        }

        LockInfo[] alockinfo = this.info.getLockedSynchronizers();

        if (alockinfo.length > 0) {
            stringbuilder.append("\n\tNumber of locked synchronizers = ").append(alockinfo.length).append('\n');
            LockInfo[] alockinfo1 = alockinfo;

            k = alockinfo.length;

            for (int j1 = 0; j1 < k; ++j1) {
                LockInfo lockinfo = alockinfo1[j1];

                stringbuilder.append("\t- ");
                this.formatLock(stringbuilder, lockinfo);
                stringbuilder.append('\n');
            }
        }

    }

    private void formatLock(StringBuilder stringbuilder, LockInfo lockinfo) {
        stringbuilder.append("<").append(lockinfo.getIdentityHashCode()).append("> (a ");
        stringbuilder.append(lockinfo.getClassName()).append(")");
    }

    private void formatState(StringBuilder stringbuilder, ThreadInfo threadinfo) {
        State state = threadinfo.getThreadState();

        stringbuilder.append(state);
        StackTraceElement stacktraceelement;
        String s;
        String s1;

        switch (ExtendedThreadInformation.SyntheticClass_1.$SwitchMap$java$lang$Thread$State[state.ordinal()]) {
        case 1:
            stringbuilder.append(" (on object monitor owned by \"");
            stringbuilder.append(threadinfo.getLockOwnerName()).append("\" Id=").append(threadinfo.getLockOwnerId()).append(")");
            break;

        case 2:
            stacktraceelement = threadinfo.getStackTrace()[0];
            s = stacktraceelement.getClassName();
            s1 = stacktraceelement.getMethodName();
            if (s.equals("java.lang.Object") && s1.equals("wait")) {
                stringbuilder.append(" (on object monitor");
                if (threadinfo.getLockOwnerName() != null) {
                    stringbuilder.append(" owned by \"");
                    stringbuilder.append(threadinfo.getLockOwnerName()).append("\" Id=").append(threadinfo.getLockOwnerId());
                }

                stringbuilder.append(")");
            } else if (s.equals("java.lang.Thread") && s1.equals("join")) {
                stringbuilder.append(" (on completion of thread ").append(threadinfo.getLockOwnerId()).append(")");
            } else {
                stringbuilder.append(" (parking for lock");
                if (threadinfo.getLockOwnerName() != null) {
                    stringbuilder.append(" owned by \"");
                    stringbuilder.append(threadinfo.getLockOwnerName()).append("\" Id=").append(threadinfo.getLockOwnerId());
                }

                stringbuilder.append(")");
            }
            break;

        case 3:
            stacktraceelement = threadinfo.getStackTrace()[0];
            s = stacktraceelement.getClassName();
            s1 = stacktraceelement.getMethodName();
            if (s.equals("java.lang.Object") && s1.equals("wait")) {
                stringbuilder.append(" (on object monitor");
                if (threadinfo.getLockOwnerName() != null) {
                    stringbuilder.append(" owned by \"");
                    stringbuilder.append(threadinfo.getLockOwnerName()).append("\" Id=").append(threadinfo.getLockOwnerId());
                }

                stringbuilder.append(")");
            } else if (s.equals("java.lang.Thread") && s1.equals("sleep")) {
                stringbuilder.append(" (sleeping)");
            } else if (s.equals("java.lang.Thread") && s1.equals("join")) {
                stringbuilder.append(" (on completion of thread ").append(threadinfo.getLockOwnerId()).append(")");
            } else {
                stringbuilder.append(" (parking for lock");
                if (threadinfo.getLockOwnerName() != null) {
                    stringbuilder.append(" owned by \"");
                    stringbuilder.append(threadinfo.getLockOwnerName()).append("\" Id=").append(threadinfo.getLockOwnerId());
                }

                stringbuilder.append(")");
            }
        }

    }

    static class SyntheticClass_1 {

        static final int[] $SwitchMap$java$lang$Thread$State = new int[State.values().length];

        static {
            try {
                ExtendedThreadInformation.SyntheticClass_1.$SwitchMap$java$lang$Thread$State[State.BLOCKED.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                ExtendedThreadInformation.SyntheticClass_1.$SwitchMap$java$lang$Thread$State[State.WAITING.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                ExtendedThreadInformation.SyntheticClass_1.$SwitchMap$java$lang$Thread$State[State.TIMED_WAITING.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

        }
    }
}
