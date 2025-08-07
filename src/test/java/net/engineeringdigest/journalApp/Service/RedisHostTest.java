package net.engineeringdigest.journalApp.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
public class RedisHostTest {

    @Autowired
    private LettuceConnectionFactory lettuceConnectionFactory;

    @Test
    public void testRedisConnectionHostAndPort() {
        assertNotNull(lettuceConnectionFactory, "LettuceConnectionFactory is not initialized");

        String expectedHost = "redis-11836.c212.ap-south-1-1.ec2.redns.redis-cloud.com";
        int expectedPort = 11836;

        String actualHost = lettuceConnectionFactory.getHostName();
        int actualPort = lettuceConnectionFactory.getPort();

        System.out.println("🔍 Redis Connected Host: " + actualHost + ", Port: " + actualPort);

        assertEquals(expectedHost, actualHost, "Unexpected Redis host");
        assertEquals(expectedPort, actualPort, "Unexpected Redis port");
    }
}
