package com.neutron.servicemanager.config;

import com.neutron.servicemanager.interceptor.RoleInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author zzs
 * @date 2023/4/12 16:25
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private RoleInterceptor roleInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(roleInterceptor).addPathPatterns("/**");
    }
}
