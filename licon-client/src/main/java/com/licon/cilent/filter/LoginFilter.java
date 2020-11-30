package com.licon.cilent.filter;

import com.licon.cilent.constant.Oauth2Constant;
import com.licon.cilent.constant.SsoConstant;
import com.licon.cilent.rpc.AccessTokenWrapper;
import com.licon.cilent.session.SessionAccessToken;
import com.licon.cilent.util.SessionUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.MessageFormat;

/**
 * Describe:
 *
 * @author Licon
 * @date 2020/11/30 10:41
 */
public class LoginFilter extends ClientFilter{
    @Override
    protected boolean isAllowAccess(HttpServletRequest requet, HttpServletResponse response) throws IOException {

        //如果accessToken存在于session则直接通过校验
        SessionAccessToken accessToken = SessionUtils.getAccessToken(requet);
        if (isValid(accessToken,requet)){
            return true;
        }

        //用于授权码获取accessToken
        String code = requet.getParameter(Oauth2Constant.AUTH_CODE);
        if (code != null){
            getAccessTokenAndAddSession(code,requet);

            redirectLocalRemoveCode(requet,response);
        }else {
            redirectLogin(requet,response);
        }
        return false;
    }

    /**
     * 判断sessionAccessToken是否有效，或者可刷新
     * @param sessionAccessToken  session中的access_token
     * @param request 请求
     * @return {@link boolean}
     * @author Licon
     * @date 2020/11/30 14:57
     */
    private boolean isValid(SessionAccessToken sessionAccessToken,HttpServletRequest request){
        return sessionAccessToken != null && (!sessionAccessToken.isExpired() ||
                refreshToken(sessionAccessToken.getRefreshToken(),request));
    }

    /**
     * 重定向登陆地址
     * @param request request
     * @param response response
     * @author Licon
     * @date 2020/11/30 21:33
     */
    private void redirectLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isAjaxRequest(request)) {
            responseJson(response, SsoConstant.NO_LOGIN, "未登录或已超时");
        }
        else {
            String loginUrl = MessageFormat.format(SsoConstant.LOGIN_URL, getServerUrl(), getAppId(),
                    URLEncoder.encode(getCurrentUrl(request), "utf-8"));
            response.sendRedirect(loginUrl);
        }
    }

    /**
     * 移除code重定向当前地址
     * @param request request
     * @param response response
     * @author Licon
     * @date 2020/11/30 21:23
     */
    private void redirectLocalRemoveCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String currentUrl = getCurrentUrl(request);
        currentUrl = currentUrl.substring(0, currentUrl.indexOf(Oauth2Constant.AUTH_CODE) - 1);
        response.sendRedirect(currentUrl);
    }

    /**
     * 获取当前请求路径
     * @param request request
     * @return {@link String}
     * @author Licon
     * @date 2020/11/30 21:22
     */
    private String getCurrentUrl(HttpServletRequest request) {
        return new StringBuilder(request.getRequestURL())
                .append(request.getQueryString() == null ? "" : "?" + request.getQueryString()).toString();
    }

    /***
     * 获取accessToken并加入session
     * @param code Oauth2授权码
     * @param request request
     * @author Licon
     * @date 2020/11/30 21:17
     */
    private void getAccessTokenAndAddSession(String code,HttpServletRequest request){
        //Oauth2获取accessToken
        AccessTokenWrapper accessTokenWrapper = new AccessTokenWrapper("",0,"",null);
        //加入session
        setAccessTokenInSession(accessTokenWrapper,request);
    }
    /**
     * 通过refreshToken参数调用http请求延长服务端session，并返回新的accessToken
     * @param refreshToken refreshToken
     * @param request 请求
     * @return {@link boolean}
     * @author Licon
     * @date 2020/11/30 15:05
     */
    public boolean refreshToken(String refreshToken,HttpServletRequest request){
        AccessTokenWrapper accessTokenWrapper = new AccessTokenWrapper("",0,refreshToken,null);
        return setAccessTokenInSession(accessTokenWrapper,request);
    }

    /**
     * 设置 accessTokenWrapper到session中去，并做映射关系
     * @param accessTokenWrapper accessTokenWrapper
     * @param request 请求
     * @return {@link boolean}
     * @author Licon
     * @date 2020/11/30 15:06
     */
    private boolean setAccessTokenInSession(AccessTokenWrapper accessTokenWrapper,HttpServletRequest request){
        if (accessTokenWrapper == null){
            return false;
        }
        //将accessTokenWrapper存入本地session
        SessionUtils.setSessionAccessToken(request,accessTokenWrapper);

        recordSession(request,accessTokenWrapper.getAccessToken());
        return true;
    }

    /**
     * 将access_token与session做映射
     * @param request 请求
     * @param accessToken accessToken
     * @author Licon
     * @date 2020/11/30 21:02
     */
    private void recordSession(final HttpServletRequest request,String accessToken){
        final HttpSession session = request.getSession();
        getSessionMappingStore().removeSessionById(session.getId());
        getSessionMappingStore().addSessionByAccessToken(accessToken,session);
    }

    protected boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestedWith);
    }

    protected void responseJson(HttpServletResponse response, int code, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(200);
        PrintWriter writer = response.getWriter();
       // writer.write(JSON.toJSONString(Result.create(code, message)));
        writer.flush();
        writer.close();
    }
}
