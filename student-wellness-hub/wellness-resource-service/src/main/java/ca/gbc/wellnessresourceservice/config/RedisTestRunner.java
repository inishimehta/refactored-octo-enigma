package ca.gbc.wellnessresourceservice.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisTestRunner implements CommandLineRunner {

    private final StringRedisTemplate redisTemplate;

    public RedisTestRunner(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        // Write a test key
        redisTemplate.opsForValue().set("test-key", "Hello Redis!");
        // Read it back
        String value = redisTemplate.opsForValue().get("test-key");
        System.out.println("Redis test-key value: " + value);
    }
}
