package com.neutron.servicebackend;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author zzs
 */
@MapperScan("com.neutron.common.model.mapper")
@EnableDiscoveryClient
@EnableDubbo
@SpringBootApplication
public class ServiceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceBackendApplication.class, args);
    }

}
