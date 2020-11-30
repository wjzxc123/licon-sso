package com.licon.liconsession.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/27 16:44
 */
@RestController
public class TestController {

    @GetMapping("/get-session")
    public HttpSession getSession(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute("wy","ni zhi bu zhi dao duo jiu mei you shuo ai wo");
        return session;
    }
}
