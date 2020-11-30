package com.licon.cilent.rpc;

import lombok.Data;

import java.io.Serializable;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/30 11:00
 */
@Data
public class UserInfo implements Serializable {
    private static final long serialVersionUID = -304815489748054058L;

    /**
     * 用户名
     */
    private String userName;
}
