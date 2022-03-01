package com.montivero.poc.heroesmd.config;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.cache.CacheBuilder;

@Configuration
public class CacheConfig {

   @Bean
   @Scope(value = WebApplicationContext.SCOPE_APPLICATION, proxyMode = ScopedProxyMode.TARGET_CLASS)
   public CacheManager cacheManagerBySeconds() {
      ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager() {
         @Override
         protected Cache createConcurrentMapCache(String name) {
            return new ConcurrentMapCache(name,
                  CacheBuilder.newBuilder()
                              .expireAfterWrite(30, TimeUnit.SECONDS)
                              .maximumSize(100)
                              .build()
                              .asMap(),
                  false);
         }
      };

      cacheManager.setCacheNames(Collections.singletonList("cache-by-seconds"));
      return cacheManager;
   }

}
