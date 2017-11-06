package com.smart.housekeeper.base.spring.aspect;

import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class HKRequestFilter extends ResourceUrlEncodingFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        super.doFilterInternal(request, response, filterChain);
        System.out.println("request:" + request.getRequestURI());
    }
}
