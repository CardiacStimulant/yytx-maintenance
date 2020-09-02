package com.maintenance.context.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 跨域拦截器
 * @author wangzh
 * @date 2020/08/11
 */
public class CORSInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.addHeader("Access-Control-Allow-Origin", "*");
        //response.addHeader("Access-Control-Allow-Methods", "POST");
        //response.addHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
        HttpServletResponse response,
        Object handler,
        ModelAndView modelAndView) throws Exception {
    }
    @Override
    public void afterCompletion(HttpServletRequest request,
        HttpServletResponse response,
        Object handler,
        Exception ex) throws Exception {
    }

}

