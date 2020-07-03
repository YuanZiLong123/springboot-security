package com.yzl.springbootsecurity.controller;

import com.yzl.springbootsecurity.service.ICustomerService;
import com.yzl.springbootsecurity.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author admin
 * @date 2020-06-30 16:25
 */
@RestController
@Api(tags = "登录控制器")
public class LoginController  {


    @Autowired
    private ICustomerService customerService;

    @PostMapping("/login")
    @ApiOperation("登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account",value = "账号"),
            @ApiImplicitParam(name = "password",value = "密码")
    }
    )
    public void login(String account ,String password){
    }

    @GetMapping("/loginOut")
    @ApiOperation("登录")
    public void loginOut(){
    }

}
