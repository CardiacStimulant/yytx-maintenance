package com.maintenance.context;

import com.maintenance.context.interceptor.CORSInterceptor;
import com.maintenance.context.response.ResponseResultInterceptor;
import com.maintenance.context.xss.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @date 2020/08/11
 */
@Configuration
public class WebAppConfig implements WebMvcConfigurer {
    /**
     * CORSInterceptor：添加拦截器规则
     * ResponseResultInterceptor：controller返回对象包装拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new CORSInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new ResponseResultInterceptor()).addPathPatterns("/**");
    }


    /**
     * xssFilter
     * @return
     */
    @Bean
    public FilterRegistrationBean<XssFilter> xssFilter() {
        FilterRegistrationBean<XssFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        XssFilter xssFilter = new XssFilter();
        filterRegistrationBean.setFilter(xssFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}
