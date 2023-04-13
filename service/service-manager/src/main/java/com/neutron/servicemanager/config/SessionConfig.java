package com.neutron.servicemanager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author zzs
 */
@Configuration
@EnableRedisHttpSession
public class SessionConfig {
}