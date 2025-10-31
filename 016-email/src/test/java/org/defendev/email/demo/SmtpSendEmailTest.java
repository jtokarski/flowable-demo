package org.defendev.email.demo;

import org.defendev.common.net.ssl.SSLContextFactory;
import org.defendev.common.time.TimeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.net.ssl.SSLSocketFactory;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.defendev.email.demo.Credentials.GMAIL_FROM;
import static org.defendev.email.demo.Credentials.GMAIL_PASSWORD;
import static org.defendev.email.demo.Credentials.RECIPIENT_1;
import static org.defendev.email.demo.Credentials.GMAIL_USERNAME;



public class SmtpSendEmailTest {

    private static final DateTimeFormatter NOW_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm VV");

    private String now() {
        final ZonedDateTime nowZoned = ZonedDateTime.now(TimeUtil.EUROPE_WARSAW_ZONE_ID);
        return NOW_FORMATTER.format(nowZoned);
    }

    @Test
    public void sendFromGmailSsl() {
        /*
         * Gmail uses standard port fot SMTP SSL - 465
         */
        final JavaMailSenderImpl impl = new JavaMailSenderImpl();
        impl.setHost("smtp.gmail.com");
        impl.setPort(465);
        impl.setUsername(GMAIL_USERNAME);
        impl.setPassword(GMAIL_PASSWORD);

        final Properties props = impl.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", TRUE);
        props.put("mail.smtp.starttls.enable", FALSE);
        props.put("mail.smtp.ssl.enable", TRUE);
        props.put("mail.debug", TRUE);

        final JavaMailSender mailSender = (JavaMailSender) impl;

        final SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(GMAIL_FROM);
        message.setTo(RECIPIENT_1);
        message.setSubject("e-mail SSL-465 at " + now());
        message.setText("This was sent programmatically form JUnit @Test through SMPT with SSL 465");

        mailSender.send(message);
    }

    @Test
    public void sendFromGmailStarttls() {
        /*
         * Gmail uses standard port fot SMTP STARTTLS - 587
         */
        final JavaMailSenderImpl impl = new JavaMailSenderImpl();
        impl.setHost("smtp.gmail.com");
        impl.setPort(587);
        impl.setUsername(GMAIL_USERNAME);
        impl.setPassword(GMAIL_PASSWORD);

        final Properties props = impl.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", TRUE);
        props.put("mail.smtp.starttls.enable", TRUE);
        props.put("mail.smtp.ssl.enable", FALSE);
        props.put("mail.debug", TRUE);

        final JavaMailSender mailSender = impl;

        final SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(GMAIL_FROM);
        message.setTo(RECIPIENT_1);
        message.setSubject("e-mail STARTTLS-587 at " + now());
        message.setText("This was sent programmatically form JUnit @Test through SMPT with STARTTLS 587");

        mailSender.send(message);
    }

    @Test
    public void sendFromGmailSslCustomSocketFactory() {
        /*
         * Gmail uses standard port fot SMTP SSL - 465
         */
        final JavaMailSenderImpl impl = new JavaMailSenderImpl();
        impl.setHost("smtp.gmail.com");
        impl.setPort(465);
        impl.setUsername(GMAIL_USERNAME);
        impl.setPassword(GMAIL_PASSWORD);

        final Properties props = impl.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", TRUE);
        props.put("mail.smtp.starttls.enable", FALSE);
        props.put("mail.smtp.ssl.enable", TRUE);
        final SSLSocketFactory customSocketFactory = SSLContextFactory.lax().getSocketFactory();
        props.put("mail.smtp.ssl.socketFactory", customSocketFactory);
        props.put("mail.debug", TRUE);

        final JavaMailSender mailSender = (JavaMailSender) impl;

        final SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(GMAIL_FROM);
        message.setTo(RECIPIENT_1);
        message.setSubject("e-mail SSL-465 Custom-Socket-Factory at " + now());
        message.setText("This was sent programmatically form JUnit @Test through SMPT with SSL 465");

        mailSender.send(message);
    }

}
