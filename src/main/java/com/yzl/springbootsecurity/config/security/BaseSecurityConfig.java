package com.yzl.springbootsecurity.config.security;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author admin
 * @date 2020-07-08 10:43
 */
//@EnableWebSecurity //开启security
//@EnableAutoConfiguration
/**
 * 开启Controller方法上的注解
 * @EnableGlobalMethodSecurity(securedEnabled=true) 开启@Secured 注解过滤权限
 *
 * @EnableGlobalMethodSecurity(jsr250Enabled=true)开启@RolesAllowed 注解过滤权限 
 *
 * @EnableGlobalMethodSecurity(prePostEnabled=true) 使用表达式时间方法级别的安全性         
 * 以下4个注解可用
 *
 *  @PreAuthorize 在方法调用之前, 基于表达式的计算结果来限制对方法的访问 @PreAuthorize("hasRole('admin')")
 *
 *  @PostAuthorize 允许方法调用, 但是如果表达式计算结果为false, 将抛出一个安全性异常
 *
 *  @PostFilter 允许方法调用, 但必须按照表达式来过滤方法的结果
 *
 *  @PreFilter 允许方法调用, 但必须在进入方法之前过滤输入值
 *
 */
//@EnableGlobalMethodSecurity(prePostEnabled =true)
public class BaseSecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * 认证配置  内存中配置
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        super.configure(auth);
    }


    /**
     *web 忽略某些请求配置
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    /**
     * security配置
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
    }
}
