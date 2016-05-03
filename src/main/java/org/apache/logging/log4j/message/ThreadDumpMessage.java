package org.apache.logging.log4j.message;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ThreadDumpMessage implements Message {

    private static final long serialVersionUID = -1103400781608841088L;
    private static final ThreadDumpMessage.ThreadInfoFactory FACTORY;
    private volatile Map threads;
    private final String title;
    private String formattedMessage;

    public ThreadDumpMessage(String s) {
        this.title = s == null ? "" : s;
        this.threads = ThreadDumpMessage.FACTORY.createThreadInfo();
    }

    private ThreadDumpMessage(String s, String s1) {
        this.formattedMessage = s;
        this.title = s1 == null ? "" : s1;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("ThreadDumpMessage[");

        if (this.title.length() > 0) {
            stringbuilder.append("Title=\"").append(this.title).append("\"");
        }

        stringbuilder.append("]");
        return stringbuilder.toString();
    }

    public String getFormattedMessage() {
        if (this.formattedMessage != null) {
            return this.formattedMessage;
        } else {
            StringBuilder stringbuilder = new StringBuilder(this.title);

            if (this.title.length() > 0) {
                stringbuilder.append("\n");
            }

            Iterator iterator = this.threads.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                ThreadInformation threadinformation = (ThreadInformation) entry.getKey();

                threadinformation.printThreadInfo(stringbuilder);
                threadinformation.printStack(stringbuilder, (StackTraceElement[]) entry.getValue());
                stringbuilder.append("\n");
            }

            return stringbuilder.toString();
        }
    }

    public String getFormat() {
        return this.title == null ? "" : this.title;
    }

    public Object[] getParameters() {
        return null;
    }

    protected Object writeReplace() {
        return new ThreadDumpMessage.ThreadDumpMessageProxy(this);
    }

    private void readObject(ObjectInputStream objectinputstream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }

    public Throwable getThrowable() {
        return null;
    }

    ThreadDumpMessage(String s, String s1, ThreadDumpMessage.SyntheticClass_1 threaddumpmessage_syntheticclass_1) {
        this(s, s1);
    }

    static {
        Method[] amethod = ThreadInfo.class.getMethods();
        boolean flag = true;
        Method[] amethod1 = amethod;
        int i = amethod.length;

        for (int j = 0; j < i; ++j) {
            Method method = amethod1[j];

            if (method.getName().equals("getLockInfo")) {
                flag = false;
                break;
            }
        }

        FACTORY = (ThreadDumpMessage.ThreadInfoFactory) (flag ? new ThreadDumpMessage.BasicThreadInfoFactory((ThreadDumpMessage.SyntheticClass_1) null) : new ThreadDumpMessage.ExtendedThreadInfoFactory((ThreadDumpMessage.SyntheticClass_1) null));
    }

    static class SyntheticClass_1 {    }

    private static class ExtendedThreadInfoFactory implements ThreadDumpMessage.ThreadInfoFactory {

        private ExtendedThreadInfoFactory() {}

        public Map createThreadInfo() {
            ThreadMXBean threadmxbean = ManagementFactory.getThreadMXBean();
            ThreadInfo[] athreadinfo = threadmxbean.dumpAllThreads(true, true);
            HashMap hashmap = new HashMap(athreadinfo.length);
            ThreadInfo[] athreadinfo1 = athreadinfo;
            int i = athreadinfo.length;

            for (int j = 0; j < i; ++j) {
                ThreadInfo threadinfo = athreadinfo1[j];

                hashmap.put(new ExtendedThreadInformation(threadinfo), threadinfo.getStackTrace());
            }

            return hashmap;
        }

        ExtendedThreadInfoFactory(ThreadDumpMessage.SyntheticClass_1 threaddumpmessage_syntheticclass_1) {
            this();
        }
    }

    private static class BasicThreadInfoFactory implements ThreadDumpMessage.ThreadInfoFactory {

        private BasicThreadInfoFactory() {}

        public Map createThreadInfo() {
            Map map = Thread.getAllStackTraces();
            HashMap hashmap = new HashMap(map.size());
            Iterator iterator = map.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();

                hashmap.put(new BasicThreadInformation((Thread) entry.getKey()), entry.getValue());
            }

            return hashmap;
        }

        BasicThreadInfoFactory(ThreadDumpMessage.SyntheticClass_1 threaddumpmessage_syntheticclass_1) {
            this();
        }
    }

    private interface ThreadInfoFactory {

        Map createThreadInfo();
    }

    private static class ThreadDumpMessageProxy implements Serializable {

        private static final long serialVersionUID = -3476620450287648269L;
        private final String formattedMsg;
        private final String title;

        public ThreadDumpMessageProxy(ThreadDumpMessage threaddumpmessage) {
            this.formattedMsg = threaddumpmessage.getFormattedMessage();
            this.title = threaddumpmessage.title;
        }

        protected Object readResolve() {
            return new ThreadDumpMessage(this.formattedMsg, this.title, (ThreadDumpMessage.SyntheticClass_1) null);
        }
    }
}
