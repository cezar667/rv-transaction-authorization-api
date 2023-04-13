package com.vr.transactionauthorization.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

  @Bean
  @Primary
  public LettuceConnectionFactory redisConnectionFactory(){
    return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
  }

  @Bean
  @Primary
  public RedisTemplate<String, Object> redisTemplate(){

    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory());

    return redisTemplate;
  }


}
