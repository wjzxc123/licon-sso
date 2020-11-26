package com.licon.liconsecurity.entity;

import lombok.Data;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/24 15:32
 */
@Data
public class MetaResource {
    /**
     * 匹配路径
     */
    String pattern;

    /**
     * 请求类型
     */
    String method;
}
