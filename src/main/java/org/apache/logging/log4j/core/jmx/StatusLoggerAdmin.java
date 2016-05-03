package org.apache.logging.log4j.core.jmx;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.status.StatusData;
import org.apache.logging.log4j.status.StatusListener;
import org.apache.logging.log4j.status.StatusLogger;

public class StatusLoggerAdmin extends NotificationBroadcasterSupport implements StatusListener, StatusLoggerAdminMBean {

    private final AtomicLong sequenceNo = new AtomicLong();
    private final ObjectName objectName;
    private Level level;

    public StatusLoggerAdmin(Executor executor) {
        super(executor, new MBeanNotificationInfo[] { createNotificationInfo()});
        this.level = Level.WARN;

        try {
            this.objectName = new ObjectName("org.apache.logging.log4j2:type=StatusLogger");
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }

        StatusLogger.getLogger().registerListener(this);
    }

    private static MBeanNotificationInfo createNotificationInfo() {
        String[] astring = new String[] { "com.apache.logging.log4j.core.jmx.statuslogger.data", "com.apache.logging.log4j.core.jmx.statuslogger.message"};
        String s = Notification.class.getName();
        String s1 = "StatusLogger has logged an event";

        return new MBeanNotificationInfo(astring, s, "StatusLogger has logged an event");
    }

    public String[] getStatusDataHistory() {
        List list = this.getStatusData();
        String[] astring = new String[list.size()];

        for (int i = 0; i < astring.length; ++i) {
            astring[i] = ((StatusData) list.get(i)).getFormattedStatus();
        }

        return astring;
    }

    public List getStatusData() {
        return StatusLogger.getLogger().getStatusData();
    }

    public String getLevel() {
        return this.level.name();
    }

    public Level getStatusLevel() {
        return this.level;
    }

    public void setLevel(String s) {
        this.level = Level.toLevel(s, Level.ERROR);
    }

    public void log(StatusData statusdata) {
        Notification notification = new Notification("com.apache.logging.log4j.core.jmx.statuslogger.message", this.getObjectName(), this.nextSeqNo(), this.now(), statusdata.getFormattedStatus());

        this.sendNotification(notification);
        Notification notification1 = new Notification("com.apache.logging.log4j.core.jmx.statuslogger.data", this.getObjectName(), this.nextSeqNo(), this.now());

        notification1.setUserData(statusdata);
        this.sendNotification(notification1);
    }

    public ObjectName getObjectName() {
        return this.objectName;
    }

    private long nextSeqNo() {
        return this.sequenceNo.getAndIncrement();
    }

    private long now() {
        return System.currentTimeMillis();
    }
}
