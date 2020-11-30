package com.licon.cilent.util;


import com.licon.cilent.constant.SsoConstant;
import com.licon.cilent.rpc.AccessTokenWrapper;
import com.licon.cilent.rpc.UserInfo;
import com.licon.cilent.session.SessionAccessToken;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Optional;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/30 10:43
 */
public class SessionUtils {

    /**
     * 获取request中存储的SessionAccessToken
     * @param request 客户端请求
     * @return SessionAccessToken
     */
    public static SessionAccessToken getAccessToken(HttpServletRequest request){
        return (SessionAccessToken)request.getSession().getAttribute(SsoConstant.ACCESS_TOKEN_CODE);
    }

    /**
     *
     * @param request 请求
     * @return {@link UserInfo}
     * @author Licon
     * @date 2020/11/30 13:05
     */
    public static UserInfo getUser(HttpServletRequest request){
        return Optional.ofNullable(getAccessToken(request)).map(SessionAccessToken::getUserInfo).orElse(null);
    }

    /**
     *创建SessionAccessToken
     * @param accessTokenWrapper accessToken包装器
     * @return {@link SessionAccessToken}
     * @author Licon
     * @date 2020/11/30 13:45
     */
    private static SessionAccessToken createSessionAccessToken(AccessTokenWrapper accessTokenWrapper){
        long expireTime = Calendar.getInstance().getTimeInMillis()+accessTokenWrapper.getExpiresIn()*1000;
        return new SessionAccessToken(accessTokenWrapper.getAccessToken(),accessTokenWrapper.getExpiresIn(),
                accessTokenWrapper.getRefreshToken(),accessTokenWrapper.getUserInfo(),expireTime);
    }

    /**
     * 在session中设置accessToken
     * @param request 请求
     * @param accessToken access_token
     * @author Licon
     * @date 2020/11/30 13:46
     */
    public static void setSessionAccessToken(HttpServletRequest request, AccessTokenWrapper accessToken){
        SessionAccessToken sessionAccessToken = null;
        if (accessToken != null){
            sessionAccessToken = createSessionAccessToken(accessToken);
        }
        request.getSession().setAttribute(SsoConstant.ACCESS_TOKEN_CODE,sessionAccessToken);
    }

    /**
     * 使accessToken为空,置session为失效
     * @param request 请求
     * @author Licon
     * @date 2020/11/30 13:48
     */
    public static void invalidate(HttpServletRequest request){
        setSessionAccessToken(request,null);
        request.getSession().invalidate();
    }
}
