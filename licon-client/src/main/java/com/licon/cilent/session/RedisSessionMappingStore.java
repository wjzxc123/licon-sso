package com.licon.cilent.session;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.data.redis.RedisSessionRepository;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/30 15:32
 */
@Component
public class RedisSessionMappingStore implements SessionMappingStore{
    private final static String SESSION_TOKEN_KEY = "session:token:key:";
    private final static String TOKEN_SESSION_KEY = "token:session:key:";

    @Autowired
    private RedissonClient redissonClient;

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private RedisSessionRepository redisSessionRepository;

    @Override
    public HttpSession removeSessionByAccessToken(String accessToken) {
        RLock lock = redissonClient.getLock(accessToken);
        try {
            lock.lock(10, TimeUnit.SECONDS);
            final String sessionId = redisTemplate.opsForValue().get(TOKEN_SESSION_KEY + accessToken);
            if (sessionId != null){
                removeSessionById(sessionId);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public void removeSessionById(String sessionId) {
        RLock lock = redissonClient.getLock(sessionId);
        try {
            lock.lock(10, TimeUnit.SECONDS);
            String accessToken = redisTemplate.opsForValue().get(SESSION_TOKEN_KEY + sessionId);
            if (accessToken!= null){
                redisTemplate.delete(SESSION_TOKEN_KEY+sessionId);
                redisTemplate.delete(TOKEN_SESSION_KEY+accessToken);

                redisSessionRepository.deleteById(sessionId);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    @Override
    public void addSessionByAccessToken(String accessToken, HttpSession session) {
        RLock lock = redissonClient.getLock(accessToken+"_"+session);
        try {
            lock.lock(10, TimeUnit.SECONDS);
            redisTemplate.opsForValue().set(SESSION_TOKEN_KEY+session.getId(),accessToken);
            redisTemplate.opsForValue().set(TOKEN_SESSION_KEY+accessToken,session.getId());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
