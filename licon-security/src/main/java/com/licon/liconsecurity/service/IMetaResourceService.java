package com.licon.liconsecurity.service;

import com.licon.liconsecurity.entity.MetaResource;

import java.util.Set;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/24 15:32
 */
public interface IMetaResourceService {
    /**
     * 获取uri ant风格的 pattern 和方法
     * @param
     * @return {@link Set< MetaResource>}
     * @throws
     * @author Licon
     * @date 2020/11/24 15:38
     */
    Set<MetaResource> getPatternAndMethod();
}
