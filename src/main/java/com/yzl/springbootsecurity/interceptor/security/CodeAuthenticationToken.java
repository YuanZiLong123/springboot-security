package com.yzl.springbootsecurity.interceptor.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


/**
 * @author admin
 * @date 2020-06-28 17:53
 */

public class CodeAuthenticationToken extends AbstractAuthenticationToken {

    public CodeAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }


    /**
     * 获取资格证书
     *
     * @return
     */


    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * @return
     */

    @Override
    public Object getPrincipal() {
        return null;
    }
}
