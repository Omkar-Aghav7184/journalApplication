package net.engineeringdigest.journalApp.Service;

import net.engineeringdigest.journalApp.api.Response.WeatherEntity;
import net.engineeringdigest.journalApp.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisServiceTest {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisService redisService;

    @Test
    public void testSendEmail()
    {
        redisTemplate.opsForValue().set("email","omky.aghav@gmail.com");
        Object email = redisTemplate.opsForValue().get("email");
        Object name=redisTemplate.opsForValue().get("name");
        Object lastName = redisTemplate.opsForValue().get("lastName");
        System.out.println("Redis connection factory: " + redisTemplate.getConnectionFactory().getConnection().ping());
        //int a=1;

    }
}
