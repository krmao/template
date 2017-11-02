package com.jianyi.base.spring.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
public class LogAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("execution(public * com.jianyi.controller..*.*(..))")
    public void printLog() {
    }

    //使用@Before在切入点开始处切入内容
    @Before("printLog()")
    public void doBefore(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());
        logger.warn("[请求开始]: " + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        logger.info("[请求地址]: " + request.getRequestURL().toString());
        logger.info("[请求类型]: " + request.getMethod());
        logger.info("[请求来源]: " + request.getRemoteAddr());
        StringBuilder requestBuilder = new StringBuilder();
        Arrays.asList(joinPoint.getArgs()).forEach(value -> requestBuilder.append(value.toString()));
        logger.info("[请求参数]: " + requestBuilder);
        logger.info("[映射地址]: " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
    }
    //使用@AfterReturning在切入点return内容之后切入内容（可以用来对处理返回值做一些加工处理）
    @AfterReturning(returning = "response", pointcut = "printLog()")
    public void doAfterReturning(Object response) {
        logger.info("[请求耗时]: " + (System.currentTimeMillis() - startTime.get()) + " ms");
        logger.info("[返回结果]: " + response);
        logger.warn("[请求结束]: " + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n");
    }
}