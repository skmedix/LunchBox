package org.apache.logging.log4j.core.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSQueueReceiver extends AbstractJMSReceiver {

    public JMSQueueReceiver(String s, String s1, String s2, String s3) {
        try {
            InitialContext initialcontext = new InitialContext();
            QueueConnectionFactory queueconnectionfactory = (QueueConnectionFactory) this.lookup(initialcontext, s);
            QueueConnection queueconnection = queueconnectionfactory.createQueueConnection(s2, s3);

            queueconnection.start();
            QueueSession queuesession = queueconnection.createQueueSession(false, 1);
            Queue queue = (Queue) initialcontext.lookup(s1);
            QueueReceiver queuereceiver = queuesession.createReceiver(queue);

            queuereceiver.setMessageListener(this);
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

        new JMSQueueReceiver(s, s1, s2, s3);
        Charset charset = Charset.defaultCharset();
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(System.in, charset));

        System.out.println("Type \"exit\" to quit JMSQueueReceiver.");

        String s4;

        do {
            s4 = bufferedreader.readLine();
        } while (s4 != null && !s4.equalsIgnoreCase("exit"));

        System.out.println("Exiting. Kill the application if it does not exit due to daemon threads.");
    }

    private static void usage(String s) {
        System.err.println(s);
        System.err.println("Usage: java " + JMSQueueReceiver.class.getName() + " QueueConnectionFactoryBindingName QueueBindingName username password");
        System.exit(1);
    }
}
