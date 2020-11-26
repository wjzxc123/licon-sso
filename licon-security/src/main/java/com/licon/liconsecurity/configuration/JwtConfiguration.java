package com.licon.liconsecurity.configuration;

import com.licon.liconsecurity.entity.RestBody;
import com.licon.liconsecurity.jwt.JwtProperties;
import com.licon.liconsecurity.jwt.JwtTokenPair;
import com.licon.liconsecurity.util.JwtUtils;
import com.licon.liconsecurity.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/23 10:15
 */
@EnableConfigurationProperties(JwtProperties.class)
@ConditionalOnProperty(prefix = "jwt.config",name = "enabled")
@Configuration
@Slf4j
public class JwtConfiguration {
    @Autowired
    JwtUtils jwtUtils;

    public JwtConfiguration() {
        System.out.println("start……");
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return (request, response, authentication) -> {
            if (response.isCommitted()) {
                log.debug("Response has already been committed");
                return;
            }

            Map<String,String> map = new HashMap<>();

            map.put("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            map.put("flag", "success_login");

            User principal = (User) authentication.getPrincipal();
            String username = principal.getUsername();
            Collection<GrantedAuthority> authorities =
                    principal.getAuthorities();
            Set<String> roles = new HashSet<>();
            if (!CollectionUtils.isEmpty(authorities)){
                authorities.forEach(x->{
                    String authority = x.getAuthority();
                    roles.add(authority);
                });
            }

            JwtTokenPair token = jwtUtils.getToken(username, roles, null);
            map.put("access_token",token.getAccess_token());
            map.put("refresh_token",token.getRefresh_token());

            ResponseUtil.responseJsonWriter(response, RestBody.okData(map));
        };
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return (request, response, exception) -> {
            if (response.isCommitted()) {
                log.debug("Response has already been committed");
                return;
            }
            Map<String, Object> map = new HashMap<>(2);

            map.put("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            map.put("flag", "failure_login");
            ResponseUtil.responseJsonWriter(response, RestBody.build(HttpStatus.UNAUTHORIZED.value(), map, "认证失败","-9999"));
        };
    }
}
