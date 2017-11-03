package com.smart.housekeeper.base.spring.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * spring mvc 拦截器,用于拦截用户的请求并进行相应的处理,例如权限,登录的判断
 *
 * @author hejinguo
 * @version $Id: AbstractHandlerPreparInterceptor.java, v 0.1 2015-8-8 下午11:45:05
 */
public abstract class AbstractHandlerInterceptor implements HandlerInterceptor {
    /**
     * 在请求处理前会被调用
     * 返回值如果是false,则整个请求结束
     */
    @Override
    public abstract boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler);

    /**
     * 请求处理后会被调用
     * 当preHandle方法返回值为true时会被调用
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    /**
     * 在整个请求结束后会被调用
     * 当preHandle方法返回值为true时会被调用
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
