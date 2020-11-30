package com.licon.cilent.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Describe:客户端过滤器，实现filter，添加访问控制方法
 *
 * @author Licon
 * @date 2020/11/18 9:46
 */
public abstract class ClientFilter extends ParamFilter implements Filter {
    /**
     * 是否允许访问
     * @param request request
     * @param response response
     * @return {@link boolean}
     * @throws IOException
     * @author Licon
     * @date 2020/11/30 21:25
     */
    protected abstract boolean isAllowAccess(HttpServletRequest request, HttpServletResponse response) throws IOException;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
