package com.licon.liconserver.customer;

import com.licon.liconserver.util.ApplicationContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.*;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/19 14:37
 */
@Configuration
@ConditionalOnClass(WebSecurityConfigurerAdapter.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Slf4j
public class CustomerSpringBootWebSecurityConfiguration {

    @Bean
    public LoginProcessor loginProcessor(){
        return new JsonLoginPostProcessor();
    }

    @Bean
    public PreLoginFilter preLoginFilter(Collection<LoginProcessor> loginProcessors){
        return new PreLoginFilter("/process",loginProcessors);
    }


    @Configuration(proxyBeanMethods = false)
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    static class DefaultConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Autowired(required = false)
        private PreLoginFilter preLoginFilter;

        @Autowired(required = false)
        private AuthenticationSuccessHandler authenticationSuccessHandler;

        @Autowired(required = false)
        private AuthenticationFailureHandler authenticationFailureHandler;

        @Override
        public void configure(WebSecurity web) throws Exception {
            super.configure(web);
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            super.configure(auth);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .cors()
                    .and()
                    .authorizeRequests().anyRequest().authenticated()
                    .and()
                    //.addFilterBefore(preLoginFilter,UsernamePasswordAuthenticationFilter.class)
                    .formLogin()
                    .loginProcessingUrl("/process")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                    //.successForwardUrl("/success")
                    //.failureForwardUrl("/failure")
                    .and()
                    .logout()
                    .addLogoutHandler(new CustomerLogoutHandler())
                    .logoutSuccessHandler(new CustomerLogoutSuccessHandler())
                    .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(new Http403ForbiddenEntryPoint())//认证失败异常统一处理
                    .accessDeniedHandler(new AccessDeniedHandlerImpl());//拒绝访问异常统一处理
        }
    }

}

