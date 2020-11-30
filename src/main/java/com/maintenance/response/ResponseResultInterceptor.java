package com.maintenance.response;

import com.maintenance.pojo.BaseControllerAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j
@Component
public class ResponseResultInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod) {
            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            final Class<?> clazz = handlerMethod.getBeanType();
            final Method method = handlerMethod.getMethod();
            if(clazz.isAnnotationPresent(ResponseResult.class)) {
                request.setAttribute(Constant.RESPONSE_RESULT_ANNOTATION, clazz.getAnnotation(ResponseResult.class));
            } else if(method.isAnnotationPresent(ResponseResult.class)) {
                request.setAttribute(Constant.RESPONSE_RESULT_ANNOTATION, method.getAnnotation(ResponseResult.class));
            } else if(clazz.isAnnotationPresent(BaseControllerAnnotation.class)) {
                request.setAttribute(Constant.RESPONSE_RESULT_ANNOTATION, clazz.getAnnotation(BaseControllerAnnotation.class));
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
