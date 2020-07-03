package com.yzl.springbootsecurity.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author admin
 * @date 2020-07-02 15:57
 */
@RestController
@RequestMapping("/session")
public class SessionController {

    @GetMapping("/me1")
    @ApiOperation("用户信息")
    public Object getMeDetail() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/me2")
    @ApiOperation("权限信息")
    public Object getMeDetail(Authentication authentication){
        return authentication;
    }

    @GetMapping("/me3")
    @ApiOperation("用户信息")
    public Object getMeDetail(@AuthenticationPrincipal UserDetails userDetails){
        return userDetails;
    }

}
