package com.licon.cilent.rpc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/30 13:24
 */
@Getter
@Setter
public class AccessTokenWrapper implements Serializable {

    private static final long serialVersionUID = -1063526340474233115L;

    private String accessToken;

    /**
     * 过期时间 单位s
     */
    private int expiresIn;

    private String refreshToken;

    private UserInfo userInfo;

    public AccessTokenWrapper(String accessToken, int expiresIn, String refreshToken, UserInfo userInfo) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.userInfo = userInfo;
    }
}
