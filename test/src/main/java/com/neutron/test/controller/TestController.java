package com.neutron.test.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zzs
 * @date 2023/4/14 15:02
 */
@RestController
public class TestController {

    @PostMapping("/demo")
    public String test1() {
        return "测试成功";
    }

}
