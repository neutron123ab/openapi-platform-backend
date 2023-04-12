package com.neutron.openapiclientsdk;

import com.neutron.openapiclientsdk.client.OpenApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author zzs
 * @date 2023/4/10 15:40
 */
@Configuration
@ConfigurationProperties("openapi.client")
@Data
@ComponentScan
public class OpenApiClientConfig {

    /**
     * 用户标识
     */
    private String accessKey;

    /**
     * 用户秘钥
     */
    private String secretKey;

    @Bean
    public OpenApiClient openApiClient() {
        return new OpenApiClient(accessKey, secretKey);
    }

}
