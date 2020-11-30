package com.licon.cilent.filter;

import com.licon.cilent.session.SessionMappingStore;
import lombok.Data;

/**
 * Describe:参数过滤器
 *
 * @author Licon
 * @date 2020/11/18 9:32
 */
@Data
public class ParamFilter {
    private String appId;
    private String appSecret;
    private String serverUrl;
    private SessionMappingStore sessionMappingStore;
}
