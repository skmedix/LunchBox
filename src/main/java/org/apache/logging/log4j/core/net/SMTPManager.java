package org.apache.logging.log4j.core.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.helpers.CyclicBuffer;
import org.apache.logging.log4j.core.helpers.NameUtil;
import org.apache.logging.log4j.core.helpers.NetUtils;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.util.PropertiesUtil;

public class SMTPManager extends AbstractManager {

    private static final SMTPManager.SMTPManagerFactory FACTORY = new SMTPManager.SMTPManagerFactory((SMTPManager.SyntheticClass_1) null);
    private final Session session;
    private final CyclicBuffer buffer;
    private volatile MimeMessage message;
    private final SMTPManager.FactoryData data;

    protected SMTPManager(String s, Session session, MimeMessage mimemessage, SMTPManager.FactoryData smtpmanager_factorydata) {
        super(s);
        this.session = session;
        this.message = mimemessage;
        this.data = smtpmanager_factorydata;
        this.buffer = new CyclicBuffer(LogEvent.class, smtpmanager_factorydata.numElements);
    }

    public void add(LogEvent logevent) {
        this.buffer.add(logevent);
    }

    public static SMTPManager getSMTPManager(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, int i, String s8, String s9, boolean flag, String s10, int j) {
        if (Strings.isEmpty(s6)) {
            s6 = "smtp";
        }

        StringBuilder stringbuilder = new StringBuilder();

        if (s != null) {
            stringbuilder.append(s);
        }

        stringbuilder.append(":");
        if (s1 != null) {
            stringbuilder.append(s1);
        }

        stringbuilder.append(":");
        if (s2 != null) {
            stringbuilder.append(s2);
        }

        stringbuilder.append(":");
        if (s3 != null) {
            stringbuilder.append(s3);
        }

        stringbuilder.append(":");
        if (s4 != null) {
            stringbuilder.append(s4);
        }

        stringbuilder.append(":");
        if (s5 != null) {
            stringbuilder.append(s5);
        }

        stringbuilder.append(":");
        stringbuilder.append(s6).append(":").append(s7).append(":").append("port").append(":");
        if (s8 != null) {
            stringbuilder.append(s8);
        }

        stringbuilder.append(":");
        if (s9 != null) {
            stringbuilder.append(s9);
        }

        stringbuilder.append(flag ? ":debug:" : "::");
        stringbuilder.append(s10);
        String s11 = "SMTP:" + NameUtil.md5(stringbuilder.toString());

        return (SMTPManager) getManager(s11, SMTPManager.FACTORY, new SMTPManager.FactoryData(s, s1, s2, s3, s4, s5, s6, s7, i, s8, s9, flag, j));
    }

    public void sendEvents(Layout layout, LogEvent logevent) {
        if (this.message == null) {
            this.connect();
        }

        try {
            LogEvent[] alogevent = (LogEvent[]) this.buffer.removeAll();
            byte[] abyte = this.formatContentToBytes(alogevent, logevent, layout);
            String s = layout.getContentType();
            String s1 = this.getEncoding(abyte, s);
            byte[] abyte1 = this.encodeContentToBytes(abyte, s1);
            InternetHeaders internetheaders = this.getHeaders(s, s1);
            MimeMultipart mimemultipart = this.getMimeMultipart(abyte1, internetheaders);

            this.sendMultipartMessage(this.message, mimemultipart);
        } catch (MessagingException messagingexception) {
            SMTPManager.LOGGER.error("Error occurred while sending e-mail notification.", (Throwable) messagingexception);
            throw new LoggingException("Error occurred while sending email", messagingexception);
        } catch (IOException ioexception) {
            SMTPManager.LOGGER.error("Error occurred while sending e-mail notification.", (Throwable) ioexception);
            throw new LoggingException("Error occurred while sending email", ioexception);
        } catch (RuntimeException runtimeexception) {
            SMTPManager.LOGGER.error("Error occurred while sending e-mail notification.", (Throwable) runtimeexception);
            throw new LoggingException("Error occurred while sending email", runtimeexception);
        }
    }

    protected byte[] formatContentToBytes(LogEvent[] alogevent, LogEvent logevent, Layout layout) throws IOException {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();

        this.writeContent(alogevent, logevent, layout, bytearrayoutputstream);
        return bytearrayoutputstream.toByteArray();
    }

    private void writeContent(LogEvent[] alogevent, LogEvent logevent, Layout layout, ByteArrayOutputStream bytearrayoutputstream) throws IOException {
        this.writeHeader(layout, bytearrayoutputstream);
        this.writeBuffer(alogevent, logevent, layout, bytearrayoutputstream);
        this.writeFooter(layout, bytearrayoutputstream);
    }

    protected void writeHeader(Layout layout, OutputStream outputstream) throws IOException {
        byte[] abyte = layout.getHeader();

        if (abyte != null) {
            outputstream.write(abyte);
        }

    }

    protected void writeBuffer(LogEvent[] alogevent, LogEvent logevent, Layout layout, OutputStream outputstream) throws IOException {
        LogEvent[] alogevent1 = alogevent;
        int i = alogevent.length;

        for (int j = 0; j < i; ++j) {
            LogEvent logevent1 = alogevent1[j];
            byte[] abyte = layout.toByteArray(logevent1);

            outputstream.write(abyte);
        }

        byte[] abyte1 = layout.toByteArray(logevent);

        outputstream.write(abyte1);
    }

    protected void writeFooter(Layout layout, OutputStream outputstream) throws IOException {
        byte[] abyte = layout.getFooter();

        if (abyte != null) {
            outputstream.write(abyte);
        }

    }

    protected String getEncoding(byte[] abyte, String s) {
        ByteArrayDataSource bytearraydatasource = new ByteArrayDataSource(abyte, s);

        return MimeUtility.getEncoding(bytearraydatasource);
    }

    protected byte[] encodeContentToBytes(byte[] abyte, String s) throws MessagingException, IOException {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();

        this.encodeContent(abyte, s, bytearrayoutputstream);
        return bytearrayoutputstream.toByteArray();
    }

    protected void encodeContent(byte[] abyte, String s, ByteArrayOutputStream bytearrayoutputstream) throws MessagingException, IOException {
        OutputStream outputstream = MimeUtility.encode(bytearrayoutputstream, s);

        outputstream.write(abyte);
        outputstream.close();
    }

    protected InternetHeaders getHeaders(String s, String s1) {
        InternetHeaders internetheaders = new InternetHeaders();

        internetheaders.setHeader("Content-Type", s + "; charset=UTF-8");
        internetheaders.setHeader("Content-Transfer-Encoding", s1);
        return internetheaders;
    }

    protected MimeMultipart getMimeMultipart(byte[] abyte, InternetHeaders internetheaders) throws MessagingException {
        MimeMultipart mimemultipart = new MimeMultipart();
        MimeBodyPart mimebodypart = new MimeBodyPart(internetheaders, abyte);

        mimemultipart.addBodyPart(mimebodypart);
        return mimemultipart;
    }

    protected void sendMultipartMessage(MimeMessage mimemessage, MimeMultipart mimemultipart) throws MessagingException {
        synchronized (mimemessage) {
            mimemessage.setContent(mimemultipart);
            mimemessage.setSentDate(new Date());
            Transport.send(mimemessage);
        }
    }

    private synchronized void connect() {
        if (this.message == null) {
            try {
                this.message = (new MimeMessageBuilder(this.session)).setFrom(this.data.from).setReplyTo(this.data.replyto).setRecipients(RecipientType.TO, this.data.to).setRecipients(RecipientType.CC, this.data.cc).setRecipients(RecipientType.BCC, this.data.bcc).setSubject(this.data.subject).getMimeMessage();
            } catch (MessagingException messagingexception) {
                SMTPManager.LOGGER.error("Could not set SMTPAppender message options.", (Throwable) messagingexception);
                this.message = null;
            }

        }
    }

    static class SyntheticClass_1 {    }

    private static class SMTPManagerFactory implements ManagerFactory {

        private SMTPManagerFactory() {}

        public SMTPManager createManager(String s, SMTPManager.FactoryData smtpmanager_factorydata) {
            String s1 = "mail." + smtpmanager_factorydata.protocol;
            Properties properties = PropertiesUtil.getSystemProperties();

            properties.put("mail.transport.protocol", smtpmanager_factorydata.protocol);
            if (properties.getProperty("mail.host") == null) {
                properties.put("mail.host", NetUtils.getLocalHostname());
            }

            if (null != smtpmanager_factorydata.host) {
                properties.put(s1 + ".host", smtpmanager_factorydata.host);
            }

            if (smtpmanager_factorydata.port > 0) {
                properties.put(s1 + ".port", String.valueOf(smtpmanager_factorydata.port));
            }

            Authenticator authenticator = this.buildAuthenticator(smtpmanager_factorydata.username, smtpmanager_factorydata.password);

            if (null != authenticator) {
                properties.put(s1 + ".auth", "true");
            }

            Session session = Session.getInstance(properties, authenticator);

            session.setProtocolForAddress("rfc822", smtpmanager_factorydata.protocol);
            session.setDebug(smtpmanager_factorydata.isDebug);

            MimeMessage mimemessage;

            try {
                mimemessage = (new MimeMessageBuilder(session)).setFrom(smtpmanager_factorydata.from).setReplyTo(smtpmanager_factorydata.replyto).setRecipients(RecipientType.TO, smtpmanager_factorydata.to).setRecipients(RecipientType.CC, smtpmanager_factorydata.cc).setRecipients(RecipientType.BCC, smtpmanager_factorydata.bcc).setSubject(smtpmanager_factorydata.subject).getMimeMessage();
            } catch (MessagingException messagingexception) {
                SMTPManager.LOGGER.error("Could not set SMTPAppender message options.", (Throwable) messagingexception);
                mimemessage = null;
            }

            return new SMTPManager(s, session, mimemessage, smtpmanager_factorydata);
        }

        private Authenticator buildAuthenticator(final String s, final String s1) {
            return null != s1 && null != s ? new Authenticator() {
                private final PasswordAuthentication passwordAuthentication = new PasswordAuthentication(s, s1);

                protected PasswordAuthentication getPasswordAuthentication() {
                    return this.passwordAuthentication;
                }
            } : null;
        }

        SMTPManagerFactory(SMTPManager.SyntheticClass_1 smtpmanager_syntheticclass_1) {
            this();
        }
    }

    private static class FactoryData {

        private final String to;
        private final String cc;
        private final String bcc;
        private final String from;
        private final String replyto;
        private final String subject;
        private final String protocol;
        private final String host;
        private final int port;
        private final String username;
        private final String password;
        private final boolean isDebug;
        private final int numElements;

        public FactoryData(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, int i, String s8, String s9, boolean flag, int j) {
            this.to = s;
            this.cc = s1;
            this.bcc = s2;
            this.from = s3;
            this.replyto = s4;
            this.subject = s5;
            this.protocol = s6;
            this.host = s7;
            this.port = i;
            this.username = s8;
            this.password = s9;
            this.isDebug = flag;
            this.numElements = j;
        }
    }
}
