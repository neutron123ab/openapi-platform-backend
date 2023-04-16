package com.neutron.servicebackend;

import com.google.gson.Gson;
import com.neutron.common.model.entity.Params;
import com.neutron.servicebackend.service.InterfaceInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ServiceBackendApplicationTests {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Test
    void contextLoads() {
        List<Params> list = new ArrayList<>();
        Params params = new Params();
        params.setName("1");
        params.setIsRequired(false);
        params.setType("1");
        params.setDescription("1");
        list.add(params);
        Gson gson = new Gson();
        String json = gson.toJson(list);
        System.out.println(json);


    }

}
