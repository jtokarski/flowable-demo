package org.defendev.email.greenmail;

import com.icegreen.greenmail.util.GreenMail;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;



@Controller
public class ReceivedMessagesController {

    private static final Logger log = LogManager.getLogger();

    private final GreenMail greenMail;

    @Autowired
    public ReceivedMessagesController(GreenMail greenMail) {
        this.greenMail = greenMail;
    }

    @RequestMapping(method = GET, path = "received-messages")
    public ResponseEntity<String> getReceivedMessages() {

        final MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        log.info(receivedMessages);

        return ResponseEntity.ok("");
    }

}
