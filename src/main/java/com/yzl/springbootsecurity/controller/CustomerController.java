package com.yzl.springbootsecurity.controller;


import com.yzl.springbootsecurity.entity.Customer;
import com.yzl.springbootsecurity.service.ICustomerService;
import com.yzl.springbootsecurity.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.web.servlet.SecurityMarker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yzl
 * @since 2020-06-30
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @GetMapping("/getCustomer.auth")
    public Result<List<Customer>> getCustomer(){
        Result result = new Result();
        result.setData(customerService.list());
        result.setCode(Result.RESULT_SUCCESS);
        result.setMessage("获取数据成功");
        return result;
    }

}

