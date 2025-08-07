package net.engineeringdigest.journalApp.Service;

import net.engineeringdigest.journalApp.api.Response.WeatherEntity;
import net.engineeringdigest.journalApp.service.WeatherService;
import net.engineeringdigest.journalApp.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class WeatherServiceTest {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private RedisService redisService;

    @Test
    public void testWeatherCachingInRedis() {
        String city = "Pune";
        String redisKey = "weather_of_" + city;

        // Clean Redis (ensure cache miss)
        redisService.setDataInRedisDB(redisKey, null, 1L);  // TTL = 1 sec
        try { Thread.sleep(1500); } catch (InterruptedException e) {}

        // First call – should hit API and populate Redis
        WeatherEntity fromAPI = weatherService.getWeather(city);
        assertNotNull(fromAPI);
        assertNotNull(fromAPI.getCurrent());
        System.out.println("API call complete. Feels like: " + fromAPI.getCurrent().getFeelslike());

        // Second call – should come from Redis
        WeatherEntity fromRedis = weatherService.getWeather(city);
        assertNotNull(fromRedis);
        assertEquals(fromAPI.getCurrent().getFeelslike(), fromRedis.getCurrent().getFeelslike());
        System.out.println("Redis call complete. Feels like: " + fromRedis.getCurrent().getFeelslike());
    }
}
