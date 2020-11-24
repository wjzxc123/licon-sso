package com.licon.liconserver.service;

import java.util.Set;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/24 15:42
 */
public interface IRoleService {
    /**
     * 根据路径pattern 来获取 对应的角色.
     *
     * @param pattern the pattern
     * @return the set
     */
    Set<String> queryRoleByPattern(String pattern);

    /**
     * 获取所有可用的
     *
     * @return
     */
    Set<String>  queryAllAvailable();
}
