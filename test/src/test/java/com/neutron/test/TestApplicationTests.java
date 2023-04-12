package com.neutron.test;

import com.neutron.openapiclientsdk.client.OpenApiClient;
import com.neutron.openapiclientsdk.model.Request;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class TestApplicationTests {

    @Resource
    private OpenApiClient openApiClient;

    @Test
    void contextLoads() {
        Request request = new Request();
        request.setUserAccount("neutron");
        request.setUrl("localhost:8000/api/demo");
        request.setBody(null);
        String result = openApiClient.sendRequest(request);
        System.out.println("result = " + result);
    }

}
