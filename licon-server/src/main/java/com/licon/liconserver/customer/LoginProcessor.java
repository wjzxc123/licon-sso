package com.licon.liconserver.customer;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/19 15:21
 */
public interface LoginProcessor{
    LoginEnum getLoginType();

    String getUserName(ServletRequest request);

    String getPassword(ServletRequest request);
}
