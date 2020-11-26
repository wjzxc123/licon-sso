package com.licon.liconsecurity.service.impl;

import com.licon.liconsecurity.entity.MetaResource;
import com.licon.liconsecurity.service.IMetaResourceService;
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
