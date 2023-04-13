package com.neutron.servicemanager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author zzs
 */
@MapperScan("com.neutron.common.model.mapper")
@EnableDiscoveryClient
@SpringBootApplication
public class ServiceManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceManagerApplication.class, args);
    }

}
