package org.defendev.email.greenmail;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class GreenmailConfig {

    @Bean
    public GreenMail greenMail() {

        /*
         * ServerSetupTest.ALL starts all default mail services using default ports including offset of 3000
         * (to mitigate the limitation of binding on lower ports as low-privilege user):
         *   SMTP : 3025
         *   SMTPS : 3465
         *   IMAP : 3143
         *   IMAPS : 3993
         *   POP3 : 3110
         *   POP3S : 3995
         *   API : 8080
         *
         */
        final GreenMail greenMail = new GreenMail(ServerSetupTest.ALL);

        final GreenMailConfiguration greenMailConfiguration = GreenMailConfiguration.aConfig();

        /*
         * Todo:
         *   - user accounts
         *   - controlling access without authentication - explicit allow/disallow
         *
         *
         *  greenMail.withConfiguration(GreenMailConfiguration.aConfig())
         *
         */


        greenMail.start();

        return greenMail;
    }

}
