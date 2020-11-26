package com.licon.liconserver.service.impl;

import com.licon.liconserver.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/24 15:44
 */
@Service
@Slf4j
public class RoleServiceImpl implements IRoleService {
    @Override
    public Set<String> queryRoleByPattern(String pattern) {
        Set<String> set = new HashSet<>();
        set.add("ROLE_wy");
        set.add("ROLE_lsw");
        return set;
    }

    @Override
    public Set<String> queryAllAvailable() {
        Set<String> set = new HashSet<>();
        set.add("wy");
        set.add("lsw");
        set.add("admin");
        set.add("user");
        set.add("custom");
        return set;
    }
}
