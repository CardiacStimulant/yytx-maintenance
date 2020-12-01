package com.maintenance.response;

import com.maintenance.excepion.RoleManagerException;
import com.maintenance.pojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {
    private Logger logger = LoggerFactory.getLogger(ResponseResultHandler.class);

    /**
     * 判断组件支持的类型
     * @param returnType
     * @param converterType
     * @return
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        /* 判断是否需要统一包装，如果attribute中有，则表示需要处理，返回true */
        Object annotation = request.getAttribute(Constant.RESPONSE_RESULT_ANNOTATION);
        return !(annotation == null);
    }

    /**
     * 处理返回体
     * @param body
     * @param returnType
     * @param selectedContentType
     * @param selectedConverterType
     * @param request
     * @param response
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if(!(body instanceof Result)) {
            // 包装返回体
            return Result.success(body);
        }
        return body;
    }

    /**
     * 自定义异常
     * @param e
     * @return
     */
    @ExceptionHandler(RoleManagerException.class)
    @ResponseBody
    public Result<?> exceptionHandler(RoleManagerException e) {
        logger.error(e.getMessage(), e);
        return Result.failure(e.getMessage());
    }

    /**
     * 系统异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<?> exceptionHandler(Exception e) {
        logger.error(e.getMessage(), e);
        return Result.failure(HttpStatus.INTERNAL_SERVER_ERROR.value(), "系统异常，请联系管理员");
    }
}
