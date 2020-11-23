package com.licon.liconserver.jwt;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/23 9:53
 */
@Component
public class JwtTokenGenerator {

    private static final String JWT_EXP_KEY = "exp";

    private final JwtPayloadBuilder jwtPayloadBuilder = new JwtPayloadBuilder();
    private final JwtProperties jwtProperties;
    private JwtTokenStorage jwtTokenStorage;

    private KeyPair keyPair;

    public JwtTokenGenerator(JwtTokenStorage jwtTokenStorage, JwtProperties jwtProperties) {
        this.jwtTokenStorage = jwtTokenStorage;
        this.jwtProperties = jwtProperties;
        KeyPairFactory keyPairFactory = new KeyPairFactory();
        this.keyPair = keyPairFactory.create(jwtProperties.getKeyLocation(), jwtProperties.getKeyAlias(), jwtProperties.getKeyPass());
    }

    private String jwtToken(String aud, int exp, Set<String> roles, Map<String, String> additional) {
        String payload = jwtPayloadBuilder
                .iss(jwtProperties.getIss())
                .sub(jwtProperties.getSub())
                .aud(aud)
                .additional(additional)
                .roles(roles)
                .expDays(exp)
                .builder();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RsaSigner signer = new RsaSigner(privateKey);
        return JwtHelper.encode(payload, signer).getEncoded();
    }


    public JwtTokenPair productJwt(String aud, Set<String> roles, Map<String, String> additional){
        String access_token = jwtToken(aud, jwtProperties.getAccessExpDays(), roles, additional);
        String refresh_token = jwtToken(aud, jwtProperties.getRefreshExpDays(), roles, additional);

        JwtTokenPair jwtTokenPair = new JwtTokenPair();
        jwtTokenPair.setAccess_token(access_token);
        jwtTokenPair.setRefresh_token(refresh_token);

        jwtTokenStorage.put(jwtTokenPair,aud);
        return jwtTokenPair;
    }


    public JSONObject decodeAndVerify(String jwtToken) {
        Assert.hasText(jwtToken, "jwt token must not be bank");
        RSAPublicKey rsaPublicKey = (RSAPublicKey) this.keyPair.getPublic();
        SignatureVerifier rsaVerifier = new RsaVerifier(rsaPublicKey);
        Jwt jwt = JwtHelper.decodeAndVerify(jwtToken, rsaVerifier);
        String claims = jwt.getClaims();
        JSONObject jsonObject = JSONUtil.parseObj(claims);
        String exp = jsonObject.getStr(JWT_EXP_KEY);

        if (isExpired(exp)) {
            throw new IllegalStateException("jwt token is expired");
        }
        return jsonObject;
    }

    private boolean isExpired(String exp) {
        return LocalDateTime.now().isAfter(LocalDateTime.parse(exp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
