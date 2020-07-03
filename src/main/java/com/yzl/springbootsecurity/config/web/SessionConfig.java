package com.yzl.springbootsecurity.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;

/**
 * @author admin
 * @date 2020-07-02 16:17
 */
@Configuration
public class SessionConfig {


    @Bean("sessionStrategy")
    public SessionStrategy registBean(){
        SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
        return  sessionStrategy;
    }

}
