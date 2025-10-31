package org.defendev.email.demo;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.defendev.email.demo.Credentials.GMAIL_FROM;
import static org.defendev.email.demo.Credentials.GMAIL_PASSWORD;
import static org.defendev.email.demo.Credentials.GMAIL_USERNAME;
import static org.defendev.email.demo.Credentials.RECIPIENT_1;
import static org.defendev.email.demo.Credentials.RECIPIENT_2;



public class ThymeleafMailTemplateTest {

    private static JavaMailSender mailSender;

    private ITemplateResolver textTemplateResolver() {
        final ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setOrder(1);
        resolver.setResolvablePatterns(Set.of("*.txt", "**/*.txt"));
        resolver.setPrefix("email-templates/");
        resolver.setSuffix("");
        resolver.setTemplateMode(TemplateMode.TEXT);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(true);
        return resolver;
    }

    private ITemplateResolver htmlTemplateResolver() {
        final ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setResolvablePatterns(Set.of("*.html", "**/*.html"));
        resolver.setOrder(2);
        resolver.setPrefix("email-templates/");
        resolver.setSuffix("");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(true);
        return resolver;
    }

    private ITemplateEngine templateEngine() {
        final TemplateEngine engine = new TemplateEngine();
        engine.addTemplateResolver(textTemplateResolver());
        engine.addTemplateResolver(htmlTemplateResolver());
        return engine;
    }

    @BeforeAll
    public static void setUp() {
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

        mailSender = impl;
    }

    @Test
    public void sendPlainPlusRichtextEmail() throws MessagingException, UnsupportedEncodingException {
        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setFrom(GMAIL_FROM, "Jan Kowalski");
        final List<String> toRecipients = List.of("serious.person1@corpoxyz.com", "serious.person2@corpoxyz.com");
        final List<String> ccRecipients = List.of(RECIPIENT_1);
        final List<String> bccRecipients = List.of();
        helper.setTo(toRecipients.toArray(new String[0]));
        helper.setCc(ccRecipients.toArray(new String[0]));
        helper.setBcc(bccRecipients.toArray(new String[0]));
        helper.setSubject("Programmatically form JUnit @Test using Thymeleaf");

        final ITemplateEngine templateEngine = templateEngine();

        final Context thymeleafContext = new Context();
        thymeleafContext.setVariable("recipientName", "Giuseppe");
        thymeleafContext.setVariable("processInstanceId", "2002002");
        thymeleafContext.setVariable("originalTo", toRecipients.toString());
        thymeleafContext.setVariable("originalCc", ccRecipients.toString());
        thymeleafContext.setVariable("originalBcc", bccRecipients.toString());

        final String plainText = templateEngine.process("itWorks.th.txt", thymeleafContext);
        final String htmlText = templateEngine.process("itWorks.th.html", thymeleafContext);

        helper.setText(plainText, htmlText);

        /*
         * If needed e.g. on local development environment, it's possible to override properties of MimeMessage
         * on late stage.
         */
        mimeMessage.setSubject("[non-prod] " + mimeMessage.getSubject());
        mimeMessage.setRecipients(Message.RecipientType.TO, RECIPIENT_2);
        mimeMessage.setRecipients(Message.RecipientType.CC, "");
        mimeMessage.setRecipients(Message.RecipientType.BCC, "");

        mailSender.send(mimeMessage);
    }

}
