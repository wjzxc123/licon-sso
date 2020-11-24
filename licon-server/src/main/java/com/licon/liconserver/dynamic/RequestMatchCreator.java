package com.licon.liconserver.dynamic;

import com.licon.liconserver.entity.MetaResource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Set;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/24 15:58
 */
public interface RequestMatchCreator {
    /**
     *请求元数据转化为RequestMatcher
     * @param metaResources
     * @return {@link Set< RequestMatcher>}
     * @throws
     * @author Licon
     * @date 2020/11/24 16:06
     */
    Set<RequestMatcher> resourceConvertMatcher(Set<MetaResource> metaResources);
}
