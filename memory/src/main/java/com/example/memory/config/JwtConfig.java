package com.example.memory.config;

import com.example.memory.Interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class JwtConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器
        registry.addInterceptor(new JwtInterceptor())
                .excludePathPatterns("/register","/login") // 排除登录接口不拦截
                .addPathPatterns("/**"); // 拦截所有/api/**路径的请求
    }
}