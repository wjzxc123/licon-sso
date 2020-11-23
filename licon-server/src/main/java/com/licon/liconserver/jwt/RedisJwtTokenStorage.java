package com.licon.liconserver.jwt;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/23 11:14
 */
@Component
public class RedisJwtTokenStorage implements JwtTokenStorage {
    public static final String PUB_KEY="jwt:key:";

    @Value("${jwt.config.access-exp-days}")
    public long accessTokenExp;


    @Resource
    private RedisTemplate<String,JwtTokenPair> redisTemplate;

    @Override
    public JwtTokenPair put(JwtTokenPair jwtTokenPair, String userId) {
        redisTemplate.opsForValue().set(PUB_KEY+userId,jwtTokenPair,accessTokenExp, TimeUnit.DAYS);
        return jwtTokenPair;
    }

    @Override
    public void expire(String userId) {

    }

    @Override
    public JwtTokenPair get(String userId) {
        return redisTemplate.opsForValue().get(PUB_KEY+userId);
    }
}
