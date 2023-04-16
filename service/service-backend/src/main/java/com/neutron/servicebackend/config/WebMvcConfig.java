package com.neutron.servicebackend.config;

import com.neutron.servicebackend.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 注册拦截器
 * @author zzs
 * @date 2023/4/12 16:21
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> ignorePaths = new ArrayList<>();
        ignorePaths.add("/backend/user/login");
        ignorePaths.add("/backend/user/register");
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(ignorePaths);
    }
}
