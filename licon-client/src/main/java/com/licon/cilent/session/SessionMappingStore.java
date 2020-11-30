package com.licon.cilent.session;

import javax.servlet.http.HttpSession;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/30 15:28
 */
public interface SessionMappingStore {
    /**
     *通过accessToken移除session
     * @param accessToken accessToken
     * @return {@link HttpSession}
     * @author Licon
     * @date 2020/11/30 15:30
     */
    HttpSession removeSessionByAccessToken(String accessToken);

    /**
     *通过sessionId移除session
     * @param sessionId sessionId
     * @author Licon
     * @date 2020/11/30 15:31
     */
    void removeSessionById(String sessionId);

    /**
     *通过accessToken添加session
     * @param accessToken accessToken
     * @param session session
     * @author Licon
     * @date 2020/11/30 15:32
     */
    void addSessionByAccessToken(String accessToken,HttpSession session);
}
