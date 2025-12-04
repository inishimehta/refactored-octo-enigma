package ca.gbc.wellnessresourceservice.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class RedisConfig {

    public static final String CACHE_RESOURCES = "resources";
    public static final String CACHE_RESOURCES_BY_CATEGORY = "resourcesByCategory";
    public static final String CACHE_RESOURCE_BY_ID = "resourceById";

    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory connectionFactory) {
        RedisCacheConfiguration defaults = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofMinutes(10));

        Map<String, RedisCacheConfiguration> configs = new HashMap<>();
        configs.put(CACHE_RESOURCES, defaults.entryTtl(Duration.ofMinutes(5)));
        configs.put(CACHE_RESOURCES_BY_CATEGORY, defaults.entryTtl(Duration.ofMinutes(10)));
        configs.put(CACHE_RESOURCE_BY_ID, defaults.entryTtl(Duration.ofMinutes(30)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaults)
                .withInitialCacheConfigurations(configs)
                .build();
    }
}

