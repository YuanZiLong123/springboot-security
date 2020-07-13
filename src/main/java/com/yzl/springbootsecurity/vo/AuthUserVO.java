package com.yzl.springbootsecurity.vo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;

import java.util.*;

/**
 * @author admin
 * @date 2020-07-02 15:44
 */
public class AuthUserVO implements UserDetails {

    private String password;
    private  String username;
    private  Set<GrantedAuthority> authorities;
    private  boolean accountNonExpired;
    private  boolean accountNonLocked;
    private  boolean credentialsNonExpired;
    private  boolean enabled;

    private String token;
    public AuthUserVO() {
    }

    public AuthUserVO(String password, String username, Set<GrantedAuthority> authorities,String token) {
        this.password = password;
        this.username = username;
        this.authorities = authorities;
        this.token = token;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return DigestUtils.md5DigestAsHex(this.password.getBytes()) ;
    }


    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
