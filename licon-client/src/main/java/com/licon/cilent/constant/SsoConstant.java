package com.licon.cilent.constant;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/30 10:50
 */
public class SsoConstant {
    public final static String ACCESS_TOKEN_CODE = "access_code";

    /**
     * 服务端登录地址
     */
    public static final String LOGIN_URL = "{0}/login?appId={1}&redirectUri={2}";

    /**
     * 未登录或已过期
     */
    public static final int NO_LOGIN = 2100;
}
