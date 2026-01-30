package com.rainer.cloudmall.product.config;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableCaching
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class CacheConfig {
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
        configuration = configuration
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        CacheProperties.Redis cachePropertiesRedis = cacheProperties.getRedis();
        if (!cachePropertiesRedis.isCacheNullValues()) {
            configuration = configuration.disableCachingNullValues();
        }
        if (!cachePropertiesRedis.isUseKeyPrefix()) {
            configuration = configuration.disableKeyPrefix();
        }
        if (cachePropertiesRedis.getTimeToLive() != null) {
            configuration = configuration.entryTtl(cachePropertiesRedis.getTimeToLive());
        }
        if (cachePropertiesRedis.getKeyPrefix() != null) {
            configuration = configuration.computePrefixWith(name -> cachePropertiesRedis.getKeyPrefix() + ":" + name);
        }
        return configuration;
    }
}
