package com.licon.liconsecurity.customer;

import javax.servlet.ServletRequest;

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
