package com.licon.liconserver.jwt;

import lombok.Data;

import java.io.Serializable;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/23 9:56
 */
@Data
public class JwtTokenPair implements Serializable {

    private static final long serialVersionUID = 7166515727001063889L;

    private String access_token;

    private String refresh_token;
}
