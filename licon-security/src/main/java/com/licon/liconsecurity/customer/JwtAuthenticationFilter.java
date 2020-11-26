package com.licon.liconsecurity.customer;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.licon.liconsecurity.jwt.JwtTokenPair;
import com.licon.liconsecurity.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/24 9:03
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHENTICATION_PREFIX = "Bearer ";

    @Autowired
    JwtUtils jwtUtils;

    private AuthenticationEntryPoint authenticationEntryPoint = new Http403ForbiddenEntryPoint();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //如果已经认证通过则不需要再次解析jwt
        if (SecurityContextHolder.getContext().getAuthentication() != null){
            filterChain.doFilter(request,response);
            return;
        }
        //否则获取jwt然后解析,无token 直接进入下一个过滤器  因为  SecurityContext 的缘故 如果无权限并不会放行
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith(AUTHENTICATION_PREFIX)){
            String jwtToken= header.replace(AUTHENTICATION_PREFIX, "");
            if (StringUtils.hasText(jwtToken)){
                try {
                    authenticationTokenHandle(jwtToken,request);
                }catch (AuthenticationException e){
                    authenticationEntryPoint.commence(request,response,e);
                }
            }else {
                authenticationEntryPoint.commence(request,response,new AuthenticationCredentialsNotFoundException("token is not found"));
            }
        }
        filterChain.doFilter(request,response);
    }

    private void authenticationTokenHandle(String jwtToken,HttpServletRequest request) throws AuthenticationException {
        JSONObject jsonObject = jwtUtils.parseToken(jwtToken);
        //如果不存在则抛出异常
        if (Objects.isNull(jsonObject)){
            if (log.isDebugEnabled()){
                log.debug("token:{} is invalid",jwtToken);
            }
            throw new BadCredentialsException("token is invalid");
        }else {
            String username = jsonObject.getStr("aud");

            //从存储空间中获取jwttoken,如果不存在则失效了
            JwtTokenPair jwtTokenPair = jwtUtils.getTokenFromStore(username);
            if (Objects.isNull(jwtTokenPair)){
                if (log.isDebugEnabled()) {
                    log.debug("token : {}  is  not in cache", jwtToken);
                }
                // 缓存中不存在就算 失败了
                throw new CredentialsExpiredException("token is not in cache");
            }else {
                String access_token = jwtTokenPair.getAccess_token();

                if (StringUtils.endsWithIgnoreCase(access_token,jwtToken)){
                    JSONArray rolesArray = jsonObject.getJSONArray("roles");
                    String[] roles = rolesArray.toArray(new String[]{});
                    List<GrantedAuthority> grantedAuthorities = AuthorityUtils.createAuthorityList(roles);
                    User user = new User("username","[PROTECTED]",grantedAuthorities);
                    //构建用户认证token
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(user,null,grantedAuthorities);
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //加入上下文
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }else {
                    // token 不匹配
                    if (log.isDebugEnabled()){
                        log.debug("token : {}  is  not in matched", jwtToken);
                    }

                    throw new BadCredentialsException("token is not matched");
                }
            }
        }
    }
}
