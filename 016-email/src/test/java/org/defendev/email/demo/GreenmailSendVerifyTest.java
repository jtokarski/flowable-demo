package org.defendev.email.demo;

import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;



public class GreenmailSendVerifyTest {

    @Test
    public void programmaticGreenMailStartAndSendEmail() {

        /*
         * Todo: demo in-memory run Greenmail for unit test
         *
         */

    }

    /*
     * Assumes that the 018-mock-email-server/GreenmailApp is running
     *
     *
     */
    @Test
    public void justSendToMockEmailServer() {
        createSenderAndSendEmail();
    }

    private void createSenderAndSendEmail() {
        final JavaMailSenderImpl impl = new JavaMailSenderImpl();
        impl.setHost("localhost");
        impl.setPort(ServerSetupTest.SMTP.getPort());

        final Properties props = impl.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", FALSE);
        props.put("mail.smtp.starttls.enable", FALSE);
        props.put("mail.smtp.ssl.enable", FALSE);
        props.put("mail.debug", TRUE);

        final JavaMailSender mailSender = (JavaMailSender) impl;

        final SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("oooddd@example.com");
        message.setTo("dddooo@example.com");
        message.setSubject(" --- ");
        message.setText("Hello GreenMail GGG");

        mailSender.send(message);
    }

}
