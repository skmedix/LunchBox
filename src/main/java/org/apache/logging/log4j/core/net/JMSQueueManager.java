package org.apache.logging.log4j.core.net;

import java.io.Serializable;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.Context;
import javax.naming.NamingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;

public class JMSQueueManager extends AbstractJMSManager {

    private static final JMSQueueManager.JMSQueueManagerFactory FACTORY = new JMSQueueManager.JMSQueueManagerFactory((JMSQueueManager.SyntheticClass_1) null);
    private JMSQueueManager.QueueInfo info;
    private final String factoryBindingName;
    private final String queueBindingName;
    private final String userName;
    private final String password;
    private final Context context;

    protected JMSQueueManager(String s, Context context, String s1, String s2, String s3, String s4, JMSQueueManager.QueueInfo jmsqueuemanager_queueinfo) {
        super(s);
        this.context = context;
        this.factoryBindingName = s1;
        this.queueBindingName = s2;
        this.userName = s3;
        this.password = s4;
        this.info = jmsqueuemanager_queueinfo;
    }

    public static JMSQueueManager getJMSQueueManager(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8) {
        if (s5 == null) {
            JMSQueueManager.LOGGER.error("No factory name provided for JMSQueueManager");
            return null;
        } else if (s6 == null) {
            JMSQueueManager.LOGGER.error("No topic name provided for JMSQueueManager");
            return null;
        } else {
            String s9 = "JMSQueue:" + s5 + '.' + s6;

            return (JMSQueueManager) getManager(s9, JMSQueueManager.FACTORY, new JMSQueueManager.FactoryData(s, s1, s2, s3, s4, s5, s6, s7, s8));
        }
    }

    public synchronized void send(Serializable serializable) throws Exception {
        if (this.info == null) {
            this.info = connect(this.context, this.factoryBindingName, this.queueBindingName, this.userName, this.password, false);
        }

        try {
            super.send(serializable, this.info.session, this.info.sender);
        } catch (Exception exception) {
            this.cleanup(true);
            throw exception;
        }
    }

    public void releaseSub() {
        if (this.info != null) {
            this.cleanup(false);
        }

    }

    private void cleanup(boolean flag) {
        try {
            this.info.session.close();
        } catch (Exception exception) {
            if (!flag) {
                JMSQueueManager.LOGGER.error("Error closing session for " + this.getName(), (Throwable) exception);
            }
        }

        try {
            this.info.conn.close();
        } catch (Exception exception1) {
            if (!flag) {
                JMSQueueManager.LOGGER.error("Error closing connection for " + this.getName(), (Throwable) exception1);
            }
        }

        this.info = null;
    }

    private static JMSQueueManager.QueueInfo connect(Context context, String s, String s1, String s2, String s3, boolean flag) throws Exception {
        try {
            QueueConnectionFactory queueconnectionfactory = (QueueConnectionFactory) lookup(context, s);
            QueueConnection queueconnection;

            if (s2 != null) {
                queueconnection = queueconnectionfactory.createQueueConnection(s2, s3);
            } else {
                queueconnection = queueconnectionfactory.createQueueConnection();
            }

            QueueSession queuesession = queueconnection.createQueueSession(false, 1);
            Queue queue = (Queue) lookup(context, s1);
            QueueSender queuesender = queuesession.createSender(queue);

            queueconnection.start();
            return new JMSQueueManager.QueueInfo(queueconnection, queuesession, queuesender);
        } catch (NamingException namingexception) {
            JMSQueueManager.LOGGER.warn("Unable to locate connection factory " + s, (Throwable) namingexception);
            if (!flag) {
                throw namingexception;
            }
        } catch (JMSException jmsexception) {
            JMSQueueManager.LOGGER.warn("Unable to create connection to queue " + s1, (Throwable) jmsexception);
            if (!flag) {
                throw jmsexception;
            }
        }

        return null;
    }

    static class SyntheticClass_1 {    }

    private static class JMSQueueManagerFactory implements ManagerFactory {

        private JMSQueueManagerFactory() {}

        public JMSQueueManager createManager(String s, JMSQueueManager.FactoryData jmsqueuemanager_factorydata) {
            try {
                Context context = AbstractJMSManager.createContext(jmsqueuemanager_factorydata.factoryName, jmsqueuemanager_factorydata.providerURL, jmsqueuemanager_factorydata.urlPkgPrefixes, jmsqueuemanager_factorydata.securityPrincipalName, jmsqueuemanager_factorydata.securityCredentials);
                JMSQueueManager.QueueInfo jmsqueuemanager_queueinfo = JMSQueueManager.connect(context, jmsqueuemanager_factorydata.factoryBindingName, jmsqueuemanager_factorydata.queueBindingName, jmsqueuemanager_factorydata.userName, jmsqueuemanager_factorydata.password, true);

                return new JMSQueueManager(s, context, jmsqueuemanager_factorydata.factoryBindingName, jmsqueuemanager_factorydata.queueBindingName, jmsqueuemanager_factorydata.userName, jmsqueuemanager_factorydata.password, jmsqueuemanager_queueinfo);
            } catch (NamingException namingexception) {
                JMSQueueManager.LOGGER.error("Unable to locate resource", (Throwable) namingexception);
            } catch (Exception exception) {
                JMSQueueManager.LOGGER.error("Unable to connect", (Throwable) exception);
            }

            return null;
        }

        JMSQueueManagerFactory(JMSQueueManager.SyntheticClass_1 jmsqueuemanager_syntheticclass_1) {
            this();
        }
    }

    private static class QueueInfo {

        private final QueueConnection conn;
        private final QueueSession session;
        private final QueueSender sender;

        public QueueInfo(QueueConnection queueconnection, QueueSession queuesession, QueueSender queuesender) {
            this.conn = queueconnection;
            this.session = queuesession;
            this.sender = queuesender;
        }
    }

    private static class FactoryData {

        private final String factoryName;
        private final String providerURL;
        private final String urlPkgPrefixes;
        private final String securityPrincipalName;
        private final String securityCredentials;
        private final String factoryBindingName;
        private final String queueBindingName;
        private final String userName;
        private final String password;

        public FactoryData(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8) {
            this.factoryName = s;
            this.providerURL = s1;
            this.urlPkgPrefixes = s2;
            this.securityPrincipalName = s3;
            this.securityCredentials = s4;
            this.factoryBindingName = s5;
            this.queueBindingName = s6;
            this.userName = s7;
            this.password = s8;
        }
    }
}
