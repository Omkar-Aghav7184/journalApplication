package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.model.SentimentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SentimentConsumerService {

    @Autowired
    private EmailSenderService emailSenderService;

    @KafkaListener(topics = "weekly_sentiments", groupId = "weekly-sentiment-group")
    public void consume(SentimentData sentimentData){
        log.info("📩 Received message from Kafka: {}", sentimentData);
        sendEmail(sentimentData);
    }

    private void sendEmail(SentimentData sentimentData)
    {
        try{
            emailSenderService.sendEmail(sentimentData.getEmail(), "Sentiment for previous week", sentimentData.getSentiment());
        }
        catch (Exception e)
        {
            log.error("An error occurred during send email!",e);
        }
    }
}
