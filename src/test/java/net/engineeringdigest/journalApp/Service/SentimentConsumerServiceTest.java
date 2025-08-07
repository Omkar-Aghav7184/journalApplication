package net.engineeringdigest.journalApp.Service;

import net.engineeringdigest.journalApp.model.SentimentData;
import net.engineeringdigest.journalApp.service.SentimentConsumerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SentimentConsumerServiceTest {

    @Autowired
    private SentimentConsumerService sentimentConsumerService;

    @Test
    public void testSendEmail()
    {
        SentimentData testData = SentimentData.builder()
                .email("test@example.com")
                .sentiment("Positive sentiment message for the week")
                .build();
        sentimentConsumerService.consume(testData);
    }
}
