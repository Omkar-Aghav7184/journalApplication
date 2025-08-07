package net.engineeringdigest.journalApp.Service;


import net.engineeringdigest.journalApp.service.EmailSenderService;
import net.engineeringdigest.journalApp.service.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class EmailSenderTest {

    @Autowired
    private EmailSenderService emailSenderService;
    @Test
    public void testSendEmail()
    {
        emailSenderService.sendEmail("udsqlp@gmail.com",
                "JavaMailSender Testing with Journal App",
                "Hello Champion,have a Nice Day!");

    }
}
