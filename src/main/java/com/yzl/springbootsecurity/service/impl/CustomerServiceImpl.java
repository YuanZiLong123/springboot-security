package com.yzl.springbootsecurity.service.impl;

import com.yzl.springbootsecurity.entity.Customer;
import com.yzl.springbootsecurity.mapper.CustomerMapper;
import com.yzl.springbootsecurity.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yzl
 * @since 2020-06-30
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

}
