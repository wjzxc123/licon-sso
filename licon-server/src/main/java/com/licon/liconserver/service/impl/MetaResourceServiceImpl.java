package com.licon.liconserver.service.impl;

import cn.hutool.core.map.MapWrapper;
import com.licon.liconserver.entity.MetaResource;
import com.licon.liconserver.service.IMetaResourceService;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/24 15:43
 */
@Service
public class MetaResourceServiceImpl implements IMetaResourceService {
    @Override
    public Set<MetaResource> getPatternAndMethod() {
        Set<MetaResource> metaResources = new HashSet<>();
        MetaResource metaResource1 = new MetaResource();
        metaResource1.setPattern("/test");
        metaResource1.setMethod("GET");
        metaResources.add(metaResource1);

        MetaResource metaResource2 = new MetaResource();
        metaResource2.setPattern("/foo");
        metaResource2.setMethod("POST");
        metaResources.add(metaResource2);
        return metaResources;
    }
}
