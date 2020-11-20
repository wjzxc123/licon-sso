package com.licon.liconserver.customer;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


public class JsonLoginPostProcessor implements LoginProcessor {

    private static final ThreadLocal<String> passwordThreadLocal = new ThreadLocal<>();


    @Override
    public LoginEnum getLoginType() {
        return LoginEnum.JSON;
    }

    @Override
    public String getUserName(ServletRequest request) {
        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper((HttpServletRequest) request);
        passwordThreadLocal.set(requestWrapper.getParameter("password"));
        return requestWrapper.getParameter("username");
    }

    @Override
    public String getPassword(ServletRequest request) {
        String s = passwordThreadLocal.get();
        passwordThreadLocal.remove();
        return s;
    }
}
