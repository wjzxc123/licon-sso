package com.licon.liconsecurity.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static com.licon.liconsecurity.jwt.JwtProperties.JWT_PREFIX;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/23 10:01
 */
@Data
@Component
@ConfigurationProperties(prefix= JWT_PREFIX )
public class JwtProperties {
    final static  String JWT_PREFIX= "jwt.config";
    /**
     * 是否可用
     */
    private boolean enabled;
    /**
     * jks 路径
     */
    private String keyLocation;
    /**
     * key alias
     */
    private String keyAlias;
    /**
     * key store pass
     */
    private String keyPass;
    /**
     * jwt签发者
     **/
    private String iss;
    /**
     * jwt所面向的用户
     **/
    private String sub;
    /**
     * access jwt token 有效天数
     */
    private int accessExpDays;
    /**
     * refresh jwt token 有效天数
     */
    private int refreshExpDays;
}
