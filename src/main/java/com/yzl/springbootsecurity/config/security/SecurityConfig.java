package com.yzl.springbootsecurity.config.security;

import com.yzl.springbootsecurity.interceptor.ValidateCodeFilter;
import com.yzl.springbootsecurity.interceptor.security.*;
import com.yzl.springbootsecurity.service.impl.MySecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * @author admin
 * @date 2020-06-23 16:12
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private MySecurityService mySecurityService;

    @Autowired
    private MyAccessDecisionManager myAccessDecisionManager;

    @Autowired
    private MyFilterInvocationSecurityMetadataSource myFilterInvocationSecurityMetadataSource;

    @Autowired
    private ValidateCodeFilter validateCodeFilter;

    @Autowired
    private DataSource dataSource;


/**
 *@Description: 配置放行的资源
 *@Param:
 *@return:
 *@Author: yzl
 *@date: 2020/6/28*/


   /* @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                // 给 swagger 放行；不需要权限能访问的资源
                .antMatchers("/doc.html", "/swagger-resources/**", "/images/**", "/webjars/**", "/v2/api-docs", "/configuration/ui", "/configuration/security");
    }
*/


    /**
     * @Description: 配置userDetails的数据源，密码加密格式
     * @Param:
     * @return:
     * @Author: yzl
     * @date: 2020/6/28
     */

    @Bean
    MyAuthenticationProvider myAuthenticationProvider() {
        MyAuthenticationProvider myAuthenticationProvider = new MyAuthenticationProvider();
        myAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        myAuthenticationProvider.setUserDetailsService(mySecurityService);
        return myAuthenticationProvider;
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        ProviderManager manager = new ProviderManager(Arrays.asList(myAuthenticationProvider()));
        return manager;
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(mySecurityService)
                .passwordEncoder(passwordEncoder());
    }


    /**
     * @Description: HttpSecurity包含了原数据（主要是url）
     * 通过withObjectPostProcessor将MyFilterInvocationSecurityMetadataSource和MyAccessDecisionManager注入进来
     * 此url先被MyFilterInvocationSecurityMetadataSource处理，然后 丢给 MyAccessDecisionManager处理
     * 如果不匹配，返回 MyAccessDeniedHandler
     * @Param:
     * @return:
     * @Author: yzl
     * @date: 2020/6/28
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/", "/img-code/login", "/img-code/getCode")
                .permitAll()
//                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
//                    @Override
//                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
//                        o.setSecurityMetadataSource(myFilterInvocationSecurityMetadataSource);
//                        o.setAccessDecisionManager(myAccessDecisionManager);
//                        return o;
//                    }
//                })

                .and()
                .formLogin().loginProcessingUrl("/img-code/login")
                .usernameParameter("account").passwordParameter("password")
                .failureHandler(new MyAuthenticationFailureHandler())
                .successHandler(new MyAuthenticationSuccessHandler())
               // .defaultSuccessUrl("/session/me3")
                .and()
                .logout()
                .logoutUrl("/loginOut")
                .logoutSuccessHandler(new MyLogoutSuccessHandler())
                .permitAll()
                .and()
                .csrf().disable()
                .exceptionHandling()
                .accessDeniedHandler(new MyAccessDeniedHandler())
                .and()
                .rememberMe()                                   // 记住我配置
                .tokenRepository(persistentTokenRepository())  // 配置数据库源
                .tokenValiditySeconds(3600)
                ;

    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl persistentTokenRepository = new JdbcTokenRepositoryImpl();
        // 将 DataSource 设置到 PersistentTokenRepository
        persistentTokenRepository.setDataSource(dataSource);
        // 第一次启动的时候自动建表（可以不用这句话，自己手动建表，源码中有语句的）
         //persistentTokenRepository.setCreateTableOnStartup(true);
        return persistentTokenRepository;
    }



    /**
     * 放行所有路径
     * @param http
     * @throws Exception
     */
   /* @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/**").permitAll();
    }*/


    /**
     * 自定义用户认证
     * @param http
     * @throws Exception
     */
/*    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin().loginProcessingUrl("/login")//  定义当需要提交表单进行用户登录时候，转到的登录页面。
                .usernameParameter("account")
                .passwordParameter("password")
                .failureHandler(new MyAuthenticationFailureHandler())
                .successHandler(new MyAuthenticationSuccessHandler())
                .permitAll()
//                .and()
//                .logout()
//                .logoutUrl("/loginOut")
//                .logoutSuccessHandler(new MyLogoutSuccessHandler())
                .and()
                .authorizeRequests()   // 定义哪些URL需要被保护、哪些不需要被保护
                .anyRequest()          // 任何请求,登录后可以访问
                .authenticated();
    }*/

}
