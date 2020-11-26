package com.licon.liconsecurity.dynamic;

import com.licon.liconsecurity.service.IMetaResourceService;
import com.licon.liconsecurity.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/24 15:40
 */
@Slf4j
@Component
public class DynamicFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    IMetaResourceService iMetaResourceService;

    @Autowired
    IRoleService iRoleService;

    @Autowired
    RequestMatchCreator requestMatchCreator;


    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        final HttpServletRequest request = ((FilterInvocation) object).getRequest();
        Set<RequestMatcher> requestMatchers = requestMatchCreator.resourceConvertMatcher(iMetaResourceService.getPatternAndMethod());
        RequestMatcher requestMatcher = requestMatchers.stream().filter(x -> x.matches(request)).findAny().orElseThrow(() -> new AccessDeniedException("非法访问"));
        if (requestMatcher instanceof AntPathRequestMatcher){
            AntPathRequestMatcher antPathRequestMatcher = (AntPathRequestMatcher) requestMatcher;
            //根据pattern获取角色
            Set<String> roles = iRoleService.queryRoleByPattern(antPathRequestMatcher.getPattern());
            return CollectionUtils.isEmpty(roles)?null:SecurityConfig.createList(roles.toArray(new String[0]));
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return SecurityConfig.createList(Optional.ofNullable(iRoleService.queryAllAvailable()).orElse(new HashSet<>()).toArray(new String[0]));
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
