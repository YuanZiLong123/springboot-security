package com.yzl.springbootsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author admin
 * @date 2020-07-06 17:13
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/admin/hello")
    public String admin() {
        return "admin";
    }

    @GetMapping("/user/hello")
    public String user() {
        return "user";
    }



    @GetMapping("/remember-me")
    public String rememberMe(){
        return "remember-me";
    }


    @GetMapping("/need-login")
    public String needLogin(){
        return "need-login";
    }


}
