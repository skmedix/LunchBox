package org.apache.logging.log4j.core.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSTopicReceiver extends AbstractJMSReceiver {

    public JMSTopicReceiver(String s, String s1, String s2, String s3) {
        try {
            InitialContext initialcontext = new InitialContext();
            TopicConnectionFactory topicconnectionfactory = (TopicConnectionFactory) this.lookup(initialcontext, s);
            TopicConnection topicconnection = topicconnectionfactory.createTopicConnection(s2, s3);

            topicconnection.start();
            TopicSession topicsession = topicconnection.createTopicSession(false, 1);
            Topic topic = (Topic) initialcontext.lookup(s1);
            TopicSubscriber topicsubscriber = topicsession.createSubscriber(topic);

            topicsubscriber.setMessageListener(this);
        } catch (JMSException jmsexception) {
            this.logger.error("Could not read JMS message.", (Throwable) jmsexception);
        } catch (NamingException namingexception) {
            this.logger.error("Could not read JMS message.", (Throwable) namingexception);
        } catch (RuntimeException runtimeexception) {
            this.logger.error("Could not read JMS message.", (Throwable) runtimeexception);
        }

    }

    public static void main(String[] astring) throws Exception {
        if (astring.length != 4) {
            usage("Wrong number of arguments.");
        }

        String s = astring[0];
        String s1 = astring[1];
        String s2 = astring[2];
        String s3 = astring[3];

        new JMSTopicReceiver(s, s1, s2, s3);
        Charset charset = Charset.defaultCharset();
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(System.in, charset));

        System.out.println("Type \"exit\" to quit JMSTopicReceiver.");

        String s4;

        do {
            s4 = bufferedreader.readLine();
        } while (s4 != null && !s4.equalsIgnoreCase("exit"));

        System.out.println("Exiting. Kill the application if it does not exit due to daemon threads.");
    }

    private static void usage(String s) {
        System.err.println(s);
        System.err.println("Usage: java " + JMSTopicReceiver.class.getName() + " TopicConnectionFactoryBindingName TopicBindingName username password");
        System.exit(1);
    }
}
