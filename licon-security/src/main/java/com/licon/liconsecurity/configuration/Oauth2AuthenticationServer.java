package com.licon.liconsecurity.configuration;


import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

/**
 * Describe:Oauth2认证配置
 *
 * @author Licon
 * @date 2020/11/18 15:54
 */
//@Configuration
//@EnableAuthorizationServer
public class Oauth2AuthenticationServer extends AuthorizationServerConfigurerAdapter {
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client")
                .secret("client")
                .authorizedGrantTypes("authorization_code")
                .scopes("all")
                .autoApprove(true);
    }

}
