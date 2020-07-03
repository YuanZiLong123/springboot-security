package com.yzl.springbootsecurity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yzl.springbootsecurity.entity.Customer;
import com.yzl.springbootsecurity.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author admin
 * @date 2020-06-28 14:51
 */
@Service
@Transactional
public class MySecurityService implements UserDetailsService
 {

     @Autowired
     private ICustomerService customerService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Customer::getCustomerAccount,s );
        Customer customer = customerService.getOne(queryWrapper);
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
        return new User(customer.getCustomerAccount(), new BCryptPasswordEncoder().encode(customer.getCustomerPassword()), authorities);

    }
}
