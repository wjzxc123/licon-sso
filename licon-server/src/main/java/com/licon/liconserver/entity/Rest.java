package com.licon.liconserver.entity;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/23 14:54
 */
public interface Rest<T> {
    /**
     * 状态码 .
     *
     * @param httpStatus the http status
     */
    void setHttpStatus(int httpStatus);

    /**
     * 数据载体.
     *
     * @param data the data
     */
    void setData(T data);

    /**
     * 提示信息.
     *
     * @param msg the msg
     */
    void setMsg(String msg);

    /**
     * Sets identifier.
     *
     * @param identifier 标识
     */
    void setIdentifier(String identifier);
}

