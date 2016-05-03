package org.apache.logging.log4j.core.net;

import java.io.Serializable;
import java.util.Properties;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import org.apache.logging.log4j.core.appender.AbstractManager;

public abstract class AbstractJMSManager extends AbstractManager {

    public AbstractJMSManager(String s) {
        super(s);
    }

    protected static Context createContext(String s, String s1, String s2, String s3, String s4) throws NamingException {
        Properties properties = getEnvironment(s, s1, s2, s3, s4);

        return new InitialContext(properties);
    }

    protected static Object lookup(Context context, String s) throws NamingException {
        try {
            return context.lookup(s);
        } catch (NameNotFoundException namenotfoundexception) {
            AbstractJMSManager.LOGGER.warn("Could not find name [" + s + "].");
            throw namenotfoundexception;
        }
    }

    protected static Properties getEnvironment(String s, String s1, String s2, String s3, String s4) {
        Properties properties = new Properties();

        if (s != null) {
            properties.put("java.naming.factory.initial", s);
            if (s1 != null) {
                properties.put("java.naming.provider.url", s1);
            } else {
                AbstractJMSManager.LOGGER.warn("The InitialContext factory name has been provided without a ProviderURL. This is likely to cause problems");
            }

            if (s2 != null) {
                properties.put("java.naming.factory.url.pkgs", s2);
            }

            if (s3 != null) {
                properties.put("java.naming.security.principal", s3);
                if (s4 != null) {
                    properties.put("java.naming.security.credentials", s4);
                } else {
                    AbstractJMSManager.LOGGER.warn("SecurityPrincipalName has been set without SecurityCredentials. This is likely to cause problems.");
                }
            }

            return properties;
        } else {
            return null;
        }
    }

    public abstract void send(Serializable serializable) throws Exception;

    public synchronized void send(Serializable serializable, Session session, MessageProducer messageproducer) throws Exception {
        try {
            Object object;

            if (serializable instanceof String) {
                object = session.createTextMessage();
                ((TextMessage) object).setText((String) serializable);
            } else {
                object = session.createObjectMessage();
                ((ObjectMessage) object).setObject(serializable);
            }

            messageproducer.send((Message) object);
        } catch (JMSException jmsexception) {
            AbstractJMSManager.LOGGER.error("Could not publish message via JMS " + this.getName());
            throw jmsexception;
        }
    }
}
