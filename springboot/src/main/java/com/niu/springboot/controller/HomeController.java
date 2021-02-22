package com.niu.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 控制器
 *
 * @author [nza]
 * @version 1.0 [2021/02/22 13:49]
 * @createTime [2021/02/22 13:49]
 */
@RestController
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "hello world";
    }
}
