package com.neutron.servicebackend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试模块
 * @author zzs
 * @date 2023/4/10 19:33
 */
@RestController
public class TestController {

    @PostMapping("/demo")
    public Map<String, String> demo() {
        HashMap<String, String> map = new HashMap<>(1);
        map.put("demo","测试");
        return map;
    }

}
