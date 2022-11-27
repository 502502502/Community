package com.ningct.community.config;

import com.ningct.community.controller.interceptor.DataInterceptor;
import com.ningct.community.controller.interceptor.LoginRequiredInterceptor;
import com.ningct.community.controller.interceptor.LoginTicketInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Resource
    private LoginTicketInterceptor loginTicketInterceptor;
//    @Resource
//    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Resource
    private DataInterceptor dataInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.js","/**/*.css","/**/*.html","/**/*.png","/**/*.jpg","/**/*.jpeg");
        registry.addInterceptor(dataInterceptor)
                .excludePathPatterns("/**/*.js","/**/*.css","/**/*.html","/**/*.png","/**/*.jpg","/**/*.jpeg");

//        registry.addInterceptor(loginRequiredInterceptor)
//                .excludePathPatterns("/**/*.js","/**/*.css","/**/*.html","/**/*.png","/**/*.jpg","/**/*.jpeg");


    }
}
