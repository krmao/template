package com.smart.housekeeper.base.spring.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebFilter(asyncSupported = true, urlPatterns = { "/*" })

/**
 * http请求拦截器
 */
public class JianYiFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException {
        logger.info("执行请求开始");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        // CORS "pre-flight" request
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        res.addHeader("Access-Control-Allow-Headers", "Content-Type");
        res.addHeader("Access-Control-Max-Age", "1800");//30 min
        //捕获业务运行时异常
        try {
            chain.doFilter(req, res);
        } catch (Exception ignored) {
        }
        logger.info("执行请求结束");
    }

    @Override
    public void destroy() {

    }
}
