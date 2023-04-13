package com.neutron.servicebackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author zzs
 * @date 2023/4/13 11:04
 */
@Configuration
@EnableRedisHttpSession
public class SessionConfig {
}
