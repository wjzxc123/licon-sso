package com.licon.liconserver.dynamic;

import com.licon.liconserver.entity.MetaResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/24 16:05
 */
@Component
@Slf4j
public class AntPathRequestMatcherCreator implements RequestMatchCreator{
    @Override
    public Set<RequestMatcher> resourceConvertMatcher(Set<MetaResource> metaResources) {
        Set<RequestMatcher> requestMatchers = new HashSet<>();
        metaResources.forEach(x->{
            AntPathRequestMatcher antPathRequestMatcher = new AntPathRequestMatcher(x.getPattern(),x.getMethod());
            requestMatchers.add(antPathRequestMatcher);
        });
        return requestMatchers;
    }
}
