package com.yzl.springbootsecurity.config.security;

import com.yzl.springbootsecurity.interceptor.security.*;
import com.yzl.springbootsecurity.service.impl.MySecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * @author admin
 * @date 2020-06-23 16:12
 */

@Configuration
//@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private MySecurityService mySecurityService;

    @Autowired
    private MyAccessDecisionManager myAccessDecisionManager;

    @Autowired
    private MyFilterInvocationSecurityMetadataSource myFilterInvocationSecurityMetadataSource;
/**
    *@Description: 配置放行的资源
    *@Param: 
    *@return: 
    *@Author: yzl
    *@date: 2020/6/28*/


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                // 给 swagger 放行；不需要权限能访问的资源
                .antMatchers("/doc.html", "/swagger-resources/**", "/images/**", "/webjars/**", "/v2/api-docs", "/configuration/ui", "/configuration/security");
    }



/**
    *@Description:  配置userDetails的数据源，密码加密格式
    *@Param: 
    *@return: 
    *@Author: yzl
    *@date: 2020/6/28*/


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       auth.userDetailsService(mySecurityService);
    }



/**
    *@Description:
        *      HttpSecurity包含了原数据（主要是url）
        *       通过withObjectPostProcessor将MyFilterInvocationSecurityMetadataSource和MyAccessDecisionManager注入进来
        *      此url先被MyFilterInvocationSecurityMetadataSource处理，然后 丢给 MyAccessDecisionManager处理
        *       如果不匹配，返回 MyAccessDeniedHandler
    *@Param:
    *@return:
    *@Author: yzl
    *@date: 2020/6/28*/


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(myFilterInvocationSecurityMetadataSource);
                        o.setAccessDecisionManager(myAccessDecisionManager);
                        return o;
                    }
                })
                .and()
                .formLogin().loginProcessingUrl("/login")
                .usernameParameter("account").passwordParameter("password")

                .failureHandler(new MyAuthenticationFailureHandler())
                .successHandler(new MyAuthenticationSuccessHandler())
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/loginOut")
                .logoutSuccessHandler(new MyLogoutSuccessHandler())
                .permitAll()
                .and().csrf().disable()
                .exceptionHandling().accessDeniedHandler(new MyAccessDeniedHandler());
    }
}