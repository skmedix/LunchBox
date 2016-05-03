package org.apache.logging.log4j.core.net;

import java.io.Serializable;
import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.NamingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;

public class JMSTopicManager extends AbstractJMSManager {

    private static final JMSTopicManager.JMSTopicManagerFactory FACTORY = new JMSTopicManager.JMSTopicManagerFactory((JMSTopicManager.SyntheticClass_1) null);
    private JMSTopicManager.TopicInfo info;
    private final String factoryBindingName;
    private final String topicBindingName;
    private final String userName;
    private final String password;
    private final Context context;

    protected JMSTopicManager(String s, Context context, String s1, String s2, String s3, String s4, JMSTopicManager.TopicInfo jmstopicmanager_topicinfo) {
        super(s);
        this.context = context;
        this.factoryBindingName = s1;
        this.topicBindingName = s2;
        this.userName = s3;
        this.password = s4;
        this.info = jmstopicmanager_topicinfo;
    }

    public static JMSTopicManager getJMSTopicManager(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8) {
        if (s5 == null) {
            JMSTopicManager.LOGGER.error("No factory name provided for JMSTopicManager");
            return null;
        } else if (s6 == null) {
            JMSTopicManager.LOGGER.error("No topic name provided for JMSTopicManager");
            return null;
        } else {
            String s9 = "JMSTopic:" + s5 + '.' + s6;

            return (JMSTopicManager) getManager(s9, JMSTopicManager.FACTORY, new JMSTopicManager.FactoryData(s, s1, s2, s3, s4, s5, s6, s7, s8));
        }
    }

    public void send(Serializable serializable) throws Exception {
        if (this.info == null) {
            this.info = connect(this.context, this.factoryBindingName, this.topicBindingName, this.userName, this.password, false);
        }

        try {
            super.send(serializable, this.info.session, this.info.publisher);
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
                JMSTopicManager.LOGGER.error("Error closing session for " + this.getName(), (Throwable) exception);
            }
        }

        try {
            this.info.conn.close();
        } catch (Exception exception1) {
            if (!flag) {
                JMSTopicManager.LOGGER.error("Error closing connection for " + this.getName(), (Throwable) exception1);
            }
        }

        this.info = null;
    }

    private static JMSTopicManager.TopicInfo connect(Context context, String s, String s1, String s2, String s3, boolean flag) throws Exception {
        try {
            TopicConnectionFactory topicconnectionfactory = (TopicConnectionFactory) lookup(context, s);
            TopicConnection topicconnection;

            if (s2 != null) {
                topicconnection = topicconnectionfactory.createTopicConnection(s2, s3);
            } else {
                topicconnection = topicconnectionfactory.createTopicConnection();
            }

            TopicSession topicsession = topicconnection.createTopicSession(false, 1);
            Topic topic = (Topic) lookup(context, s1);
            TopicPublisher topicpublisher = topicsession.createPublisher(topic);

            topicconnection.start();
            return new JMSTopicManager.TopicInfo(topicconnection, topicsession, topicpublisher);
        } catch (NamingException namingexception) {
            JMSTopicManager.LOGGER.warn("Unable to locate connection factory " + s, (Throwable) namingexception);
            if (!flag) {
                throw namingexception;
            }
        } catch (JMSException jmsexception) {
            JMSTopicManager.LOGGER.warn("Unable to create connection to queue " + s1, (Throwable) jmsexception);
            if (!flag) {
                throw jmsexception;
            }
        }

        return null;
    }

    static class SyntheticClass_1 {    }

    private static class JMSTopicManagerFactory implements ManagerFactory {

        private JMSTopicManagerFactory() {}

        public JMSTopicManager createManager(String s, JMSTopicManager.FactoryData jmstopicmanager_factorydata) {
            try {
                Context context = AbstractJMSManager.createContext(jmstopicmanager_factorydata.factoryName, jmstopicmanager_factorydata.providerURL, jmstopicmanager_factorydata.urlPkgPrefixes, jmstopicmanager_factorydata.securityPrincipalName, jmstopicmanager_factorydata.securityCredentials);
                JMSTopicManager.TopicInfo jmstopicmanager_topicinfo = JMSTopicManager.connect(context, jmstopicmanager_factorydata.factoryBindingName, jmstopicmanager_factorydata.topicBindingName, jmstopicmanager_factorydata.userName, jmstopicmanager_factorydata.password, true);

                return new JMSTopicManager(s, context, jmstopicmanager_factorydata.factoryBindingName, jmstopicmanager_factorydata.topicBindingName, jmstopicmanager_factorydata.userName, jmstopicmanager_factorydata.password, jmstopicmanager_topicinfo);
            } catch (NamingException namingexception) {
                JMSTopicManager.LOGGER.error("Unable to locate resource", (Throwable) namingexception);
            } catch (Exception exception) {
                JMSTopicManager.LOGGER.error("Unable to connect", (Throwable) exception);
            }

            return null;
        }

        JMSTopicManagerFactory(JMSTopicManager.SyntheticClass_1 jmstopicmanager_syntheticclass_1) {
            this();
        }
    }

    private static class TopicInfo {

        private final TopicConnection conn;
        private final TopicSession session;
        private final TopicPublisher publisher;

        public TopicInfo(TopicConnection topicconnection, TopicSession topicsession, TopicPublisher topicpublisher) {
            this.conn = topicconnection;
            this.session = topicsession;
            this.publisher = topicpublisher;
        }
    }

    private static class FactoryData {

        private final String factoryName;
        private final String providerURL;
        private final String urlPkgPrefixes;
        private final String securityPrincipalName;
        private final String securityCredentials;
        private final String factoryBindingName;
        private final String topicBindingName;
        private final String userName;
        private final String password;

        public FactoryData(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8) {
            this.factoryName = s;
            this.providerURL = s1;
            this.urlPkgPrefixes = s2;
            this.securityPrincipalName = s3;
            this.securityCredentials = s4;
            this.factoryBindingName = s5;
            this.topicBindingName = s6;
            this.userName = s7;
            this.password = s8;
        }
    }
}
