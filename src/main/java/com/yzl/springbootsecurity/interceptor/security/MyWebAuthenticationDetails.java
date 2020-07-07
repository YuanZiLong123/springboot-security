package com.yzl.springbootsecurity.interceptor.security;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义
 * @author admin
 * @date 2020-07-07 17:29
 */
public class MyWebAuthenticationDetails extends WebAuthenticationDetails {


    /**
     * Records the remote address and will also set the session Id if a session already
     * exists (it won't create one).
     *
     * @param request that the authentication request was received from
     */
    public MyWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
    }
}
