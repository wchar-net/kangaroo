package com.latmn.kangaroo.framework.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SayHelloController {
    @GetMapping("/sayHello")
    public String sayHello() {
        return "Are you ok?";
    }

    @GetMapping("/sayHello2")
    public String sayHello2() {
        int r = 1 / 0;
        return "Are you ok?";
    }
}
