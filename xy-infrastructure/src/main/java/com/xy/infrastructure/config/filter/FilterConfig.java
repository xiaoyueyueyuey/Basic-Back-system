package com.xy.infrastructure.config.filter;

import com.xy.infrastructure.exception.GlobalExceptionFilter;
import com.xy.infrastructure.filter.TestFilter;
import com.xy.infrastructure.filter.TraceIdFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Filter配置
 * Filter Configuration Class
 * @author valarchie
 */
@Configuration
public class FilterConfig {

    // TODO 后续统一到一个properties 类中比较好
    // TODO It's better to unify this into a properties class in the future
    @Value("${xyboot.traceRequestIdKey}")
    private String requestIdKey;

    @Bean
    // 注册TestFilter
    // Register TestFilter
    public FilterRegistrationBean<TestFilter> testFilterRegistrationBean() {
        FilterRegistrationBean<TestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new TestFilter());
        registration.addUrlPatterns("/*");
        registration.setName("testFilter");
        registration.setOrder(2);
        return registration;
    }

    @Bean
    // 注册TraceIdFilter
    // Register TraceIdFilter
    public FilterRegistrationBean<TraceIdFilter> traceIdFilterRegistrationBean() {
        FilterRegistrationBean<TraceIdFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new TraceIdFilter(requestIdKey));
        registration.addUrlPatterns("/*");
        registration.setName("traceIdFilter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    @Bean
    // 注册GlobalExceptionFilter
    // Register GlobalExceptionFilter
    public FilterRegistrationBean<GlobalExceptionFilter> exceptionFilterRegistrationBean() {
        FilterRegistrationBean<GlobalExceptionFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new GlobalExceptionFilter());
        registration.addUrlPatterns("/*");
        registration.setName("exceptionFilter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    /**
     * 跨域配置
     * Cross-Origin Resource Sharing Configuration
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置访问源地址
        // Set allowed origins
        config.addAllowedOriginPattern("*");
        // 设置访问源请求头
        // Set allowed request headers
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        // Set allowed request methods
        config.addAllowedMethod("*");
        // 有效期 1800秒
        // Max age 1800 seconds
        config.setMaxAge(1800L);
        // 添加映射路径，拦截一切请求
        // Add mapping paths to intercept all requests
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // 返回新的CorsFilter
        // Return a new CorsFilter
        return new CorsFilter(source);
    }


}