package com.licon.liconserver.customer;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.GenericFilterBean;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Describe:预登录器
 *
 * @author Licon
 * @date 2020/11/19 15:23
 */
public class PreLoginFilter extends GenericFilterBean {

    private static final String LOGIN_TYPE = "login_type";

    private RequestMatcher requiresAuthenticationRequestMatcher;

    private Map<LoginEnum,LoginProcessor> processors = new HashMap<>();


    public PreLoginFilter(String loginProcessUrl, Collection<LoginProcessor> loginProcessors ) {
        Assert.notNull(loginProcessUrl, "loginProcessingUrl must not be null");
        requiresAuthenticationRequestMatcher = new AntPathRequestMatcher(loginProcessUrl, "POST");
        LoginProcessor loginProcessor = defaultLoginProcessor();
        processors.put(loginProcessor.getLoginType(),loginProcessor);

        if (!CollectionUtils.isEmpty(loginProcessors)){
            loginProcessors.forEach(x->{
                processors.put(x.getLoginType(),x);
            });
        }
    }


    private LoginEnum getTypeFromReq(ServletRequest request) {
        String parameter = request.getParameter(LOGIN_TYPE);
        int i = Integer.parseInt(parameter);
        LoginEnum[] values = LoginEnum.values();
        return values[i];
    }


    private LoginProcessor defaultLoginProcessor(){
        return new LoginProcessor() {
            @Override
            public LoginEnum getLoginType() {
                return LoginEnum.FORM;
            }

            @Override
            public String getUserName(ServletRequest request) {
                return request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
            }

            @Override
            public String getPassword(ServletRequest request) {
                return request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY);
            }
        };
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        if (requiresAuthenticationRequestMatcher.matches((HttpServletRequest) request)) {

            LoginEnum typeFromReq = getTypeFromReq(request);

            LoginProcessor loginProcessor = processors.get(typeFromReq);


            String username = loginProcessor.getUserName(request);

            String password = loginProcessor.getPassword(request);


            httpServletRequest.setAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);
            httpServletRequest.setAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY, password);

        }

        chain.doFilter(httpServletRequest, response);
    }
}
