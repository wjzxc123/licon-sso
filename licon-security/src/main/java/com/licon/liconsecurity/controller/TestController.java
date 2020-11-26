package com.licon.liconsecurity.controller;

import com.licon.liconsecurity.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/18 13:13
 */
@RestController
@Slf4j
//@RequestMapping("/foo")
public class TestController {

    @Resource
    RedisTemplate<String,Object> redisTemplate;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/success")
    public User testSuccess(HttpServletRequest request, HttpServletResponse response){
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info(jwtUtils.getToken(principal.getUsername()).toString());
        log.info(jwtUtils.parseToken(jwtUtils.getToken(principal.getUsername()).getAccess_token()).toString());
        return principal;
    }

    @PostMapping("/failure")
    public String testFailure(HttpServletRequest request, HttpServletResponse response){
        redisTemplate.opsForValue().set("lsw",new ArrayList<>());
        Object wy = redisTemplate.opsForValue().get("lsw");
        return Optional.ofNullable(wy).orElse(new Object()).toString();
    }

    @GetMapping("/test")
    public String test() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("current authentication: 【 {} 】", authentication);
        return "success";
    }

    @PostMapping("/foo")
    public String foo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("current authentication: 【 {} 】", authentication);
        return "foo";
    }
}
