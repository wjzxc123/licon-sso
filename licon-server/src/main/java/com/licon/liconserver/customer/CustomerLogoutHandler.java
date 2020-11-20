package com.licon.liconserver.customer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/20 13:26
 */
@Slf4j
public class CustomerLogoutHandler implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();
        log.info("username: {}  is offline now", username);
    }
}
