package com.licon.liconserver.util;

import cn.hutool.json.JSONObject;
import com.licon.liconserver.jwt.JwtTokenGenerator;
import com.licon.liconserver.jwt.JwtTokenPair;
import com.licon.liconserver.jwt.RedisJwtTokenStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/23 13:31
 */
@Component
public class JwtUtils {
    public static final String PUB_KEY="jwt:key:";

    private static JwtUtils jwtUtils;

    @Autowired
    RedisJwtTokenStorage jwtTokenStorage;

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    @PostConstruct
    public void init(){
        jwtUtils = this;
        jwtUtils.jwtTokenStorage = this.jwtTokenStorage;
        jwtUtils.jwtTokenGenerator = this.jwtTokenGenerator;
    }

    public  JwtTokenPair getToken(String aud){
        JwtTokenPair jwtTokenPair = jwtTokenStorage.get(PUB_KEY + aud);
        if (jwtTokenPair == null){
            jwtTokenPair = jwtTokenGenerator.productJwt(aud, new HashSet<>(), new HashMap<>());
        }
        return jwtTokenStorage.put(jwtTokenPair,PUB_KEY + aud);
    }

    public  JwtTokenPair getToken(String aud, Set<String> roles, Map<String,String> additional){
        JwtTokenPair jwtTokenPair = jwtTokenStorage.get(PUB_KEY + aud);
        if (jwtTokenPair == null){
            jwtTokenPair = jwtTokenGenerator.productJwt(aud, roles, additional);
        }
        return jwtTokenStorage.put(jwtTokenPair,PUB_KEY + aud);
    }

    public JwtTokenPair getTokenFromStore(String aud){
        return jwtTokenStorage.get(PUB_KEY + aud);
    }

    public JSONObject parseToken(String token){
        return jwtTokenGenerator.decodeAndVerify(token);
    }
}
