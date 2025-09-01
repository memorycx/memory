package com.example.memory.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 全局配置类
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 所有接口
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST")  // GET/POST等
                .allowedHeaders("*")  // 请求头
                .allowCredentials(true)  // 允许带cookie
                .maxAge(3600);  // 预检请求缓存时间
    }
}

