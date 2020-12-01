package com.maintenance.context.interceptor;

import com.maintenance.response.ResponseResultInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author wangzh
 * @date 2020/08/11
 */
@Configuration
public class WebAppConfig implements WebMvcConfigurer {
    /**
     * 添加拦截器规则
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new CORSInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new ResponseResultInterceptor()).addPathPatterns("/**");
    }

}
