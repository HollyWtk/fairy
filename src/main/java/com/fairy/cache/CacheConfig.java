
package com.fairy.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author supei
 * 
 * @desc caffeine配置bean 用于单机token管理策略
 *
 */

@Configuration
@Slf4j
public class CacheConfig {

    /**
     * initialCapacity maximumSize maximumWeight expireAfterAccess
     * expireAfterWrite refreshAfterWrite weakKeys weakValues softValues
     * recordStats
     */
    @Bean
    public CacheManager cacheManager() {
        // String specAsString =
        // "initialCapacity=100,maximumSize=500,expireAfterAccess=5m,recordStats";
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("token", "session");
        cacheManager.setAllowNullValues(false); // can happen if you get a value
                                                // from a @Cachable that returns
                                                // null
        cacheManager.setCaffeine(caffeineCacheBuilder());
        return cacheManager;
    }

    CaffeineSpec caffeineSpec() {
        return CaffeineSpec.parse("initialCapacity=100,maximumSize=500,expireAfterAccess=45m,recordStats");
    }

    Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder().initialCapacity(100).maximumSize(150).expireAfterAccess(30, TimeUnit.MINUTES)
                // .weakKeys()
                .removalListener(new CustomRemovalListener()).recordStats();
    }

    class CustomRemovalListener implements RemovalListener<Object, Object> {

        @Override
        public void onRemoval(Object key, Object value, RemovalCause cause) {
            log.info("removal listerner called with key {}, cause {}, evicted {}", key,
                    cause.toString(), cause.wasEvicted());
        }
    }

}