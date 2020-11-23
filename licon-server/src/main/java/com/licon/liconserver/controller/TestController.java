package com.licon.liconserver.controller;

import com.licon.liconserver.jwt.JwtTokenGenerator;
import com.licon.liconserver.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class TestController {

    @Resource
    RedisTemplate<String,Object> redisTemplate;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/success")
    public User testSuccess(HttpServletRequest request, HttpServletResponse response){
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(jwtUtils.getToken(principal.getUsername()));
        System.out.println(jwtUtils.parseToken(jwtUtils.getToken(principal.getUsername()).getAccess_token()));
        return principal;
    }

    @PostMapping("/failure")
    public String testFailure(HttpServletRequest request, HttpServletResponse response){
        redisTemplate.opsForValue().set("lsw",new ArrayList<>());
        Object wy = redisTemplate.opsForValue().get("lsw");
        return Optional.ofNullable(wy).orElse(new Object()).toString();
    }


}
