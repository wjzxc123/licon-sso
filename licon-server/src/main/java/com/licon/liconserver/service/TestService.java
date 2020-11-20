package com.licon.liconserver.service;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/19 9:50
 */
public interface TestService<O> {
    O build() throws Exception;
}
