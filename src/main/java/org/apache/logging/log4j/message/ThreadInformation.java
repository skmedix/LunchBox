package org.apache.logging.log4j.message;

interface ThreadInformation {

    void printThreadInfo(StringBuilder stringbuilder);

    void printStack(StringBuilder stringbuilder, StackTraceElement[] astacktraceelement);
}
