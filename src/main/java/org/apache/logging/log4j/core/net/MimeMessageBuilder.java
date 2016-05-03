package org.apache.logging.log4j.core.net;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.logging.log4j.core.helpers.Charsets;

public class MimeMessageBuilder {

    private final MimeMessage message;

    public MimeMessageBuilder(Session session) {
        this.message = new MimeMessage(session);
    }

    public MimeMessageBuilder setFrom(String s) throws MessagingException {
        InternetAddress internetaddress = parseAddress(s);

        if (null != internetaddress) {
            this.message.setFrom(internetaddress);
        } else {
            try {
                this.message.setFrom();
            } catch (Exception exception) {
                this.message.setFrom((InternetAddress) null);
            }
        }

        return this;
    }

    public MimeMessageBuilder setReplyTo(String s) throws MessagingException {
        InternetAddress[] ainternetaddress = parseAddresses(s);

        if (null != ainternetaddress) {
            this.message.setReplyTo(ainternetaddress);
        }

        return this;
    }

    public MimeMessageBuilder setRecipients(RecipientType recipienttype, String s) throws MessagingException {
        InternetAddress[] ainternetaddress = parseAddresses(s);

        if (null != ainternetaddress) {
            this.message.setRecipients(recipienttype, ainternetaddress);
        }

        return this;
    }

    public MimeMessageBuilder setSubject(String s) throws MessagingException {
        if (s != null) {
            this.message.setSubject(s, Charsets.UTF_8.name());
        }

        return this;
    }

    public MimeMessage getMimeMessage() {
        return this.message;
    }

    private static InternetAddress parseAddress(String s) throws AddressException {
        return s == null ? null : new InternetAddress(s);
    }

    private static InternetAddress[] parseAddresses(String s) throws AddressException {
        return s == null ? null : InternetAddress.parse(s, true);
    }
}
