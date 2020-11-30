package com.licon.cilent.session;

import com.licon.cilent.rpc.AccessTokenWrapper;
import com.licon.cilent.rpc.UserInfo;
import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.*;

import java.util.Calendar;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/30 10:48
 */
@Setter
@Getter
public class SessionAccessToken extends AccessTokenWrapper {
    private static final long serialVersionUID = -6826253175612833275L;

    /**
     * 过期时间
     */
    private long expirationTime;

    public SessionAccessToken(String accessToken, int expiresIn, String refreshToken, UserInfo userInfo, long expirationTime) {
        super(accessToken, expiresIn, refreshToken, userInfo);
        this.expirationTime = expirationTime;
    }

    /***
     *  判断是否失效
     * @return {@link boolean}
     * @author Licon
     * @date 2020/11/30 13:57
     */
    public boolean isExpired(){
        return Calendar.getInstance().getTimeInMillis() > expirationTime;
    }
}
