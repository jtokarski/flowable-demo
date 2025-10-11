package org.defendev.email.demo;

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

import java.util.Properties;
import java.util.Set;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.defendev.email.demo.Credentials.GMAIL_FROM;
import static org.defendev.email.demo.Credentials.GMAIL_PASSWORD;
import static org.defendev.email.demo.Credentials.GMAIL_TO;
import static org.defendev.email.demo.Credentials.GMAIL_USERNAME;



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
    public void sendPlainPlusRichtextEmail() throws MessagingException {
        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setFrom(GMAIL_FROM);
        helper.setTo(GMAIL_TO);
        helper.setSubject("This was sent programmatically form JUnit @Test using Thymeleaf");

        final ITemplateEngine templateEngine = templateEngine();

        final Context thymeleafContext = new Context();
        thymeleafContext.setVariable("recipientName", "Giuseppe");

        final String plainText = templateEngine.process("itWorks.th.txt", thymeleafContext);
        final String htmlText = templateEngine.process("itWorks.th.html", thymeleafContext);

        helper.setText(plainText, htmlText);

         mailSender.send(mimeMessage);
    }

}
