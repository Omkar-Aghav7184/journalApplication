package net.engineeringdigest.journalApp.Scheduler;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.entity.JournalAppEntryV2;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.enums.Sentiment;
import net.engineeringdigest.journalApp.model.SentimentData;
import net.engineeringdigest.journalApp.repository.UserRepositoryImpl;
import net.engineeringdigest.journalApp.service.EmailSenderService;
import net.engineeringdigest.journalApp.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserScheduler {

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private UserRepositoryImpl userRepository;

    /*@Autowired
    private SentimentAnalysisService sentimentAnalysisService;
     */

    @Autowired
    private AppCache appCache;

    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate;

    @Scheduled(cron = "* * 9 * * SUN") // every minute during 9 AM every Sunday
    public void fetchAllUsersandSendSAEmail()
    {
        List<User> userSa = userRepository.getAllUsersEmailSA();
        for(User user: userSa)
        {
            List<JournalAppEntryV2> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream().filter(x -> x.getLocalDateTime()
                            .isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                    .map(x -> x.getSentiment()).collect(Collectors.toList());

            Map<Sentiment,Integer> sentimentCounts=new HashMap<>();

            for(Sentiment sentiment: sentiments)
            {
                if(sentiment!=null)
                {
                    sentimentCounts.put(sentiment,sentimentCounts.getOrDefault(sentiment,0)+1);
                }
            }
            Sentiment mostFrequentSentiments=null;
            int maxCount=0;

            for(Map.Entry<Sentiment,Integer> entry: sentimentCounts.entrySet())
            {
                if(entry.getValue()>maxCount)
                {
                    maxCount= entry.getValue();
                    mostFrequentSentiments=entry.getKey();
                }
            }
            if(mostFrequentSentiments!=null)
            {
                SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment("Sentiment for last 7 days " + mostFrequentSentiments).build();
                log.info("Fetched Users: {}", userSa.size());
                log.info("Processing user: {}", user.getEmail());
                log.info("Sentiment summary: {}", sentimentCounts);
                log.info("Most frequent sentiment: {}", mostFrequentSentiments);

                try{
                    kafkaTemplate.send("weekly_sentiments",sentimentData.getEmail(),sentimentData);
                }
                catch (Exception e)
                {
                    log.error("An error occurred during kafka to mail!",e);
                    //if kafka expires.we use kafka fallback
                    //emailSenderService.sendEmail(sentimentData.getEmail(), "Sentiment for previous week", sentimentData.getSentiment());
                }
                //emailSenderService.sendEmail(user.getEmail(), "Sentiment for last 7 Days",mostFrequentSentiments.toString());
            }
        }
    }

    /*
    // @Scheduled(cron="0 0 9 * * SUN")
    @Scheduled(cron="0 * * ? * *")
    public void fetchAllUsersandSendSAEmail()
    {
        List<User> userSA = userRepository.getAllUsersEmailSA();
        for(User user:userSA)
        {
            List<JournalAppEntryV2> journalEntries = user.getJournalEntries();
            List<String> filteredEntries = journalEntries.stream().filter(x -> x.getLocalDateTime()
                            .isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                            .map(x -> x.getContent()).collect(Collectors.toList());

            String entry=String.join(" ",filteredEntries);
            String sentiment = sentimentAnalysisService.getSentiment();
            emailSenderService.sendEmail(user.getEmail(), "What is sentiment Champion? for last 7 days!",sentiment);
        }
    }
     */

    @Scheduled(cron="0 0/10 * ? * *")
    public void clearCache()
    {
        appCache.init();
    }

}
