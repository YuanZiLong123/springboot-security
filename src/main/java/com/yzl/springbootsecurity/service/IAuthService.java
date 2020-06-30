package com.yzl.springbootsecurity.service;


//import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author admin
 * @date 2020-06-23 16:54
 */
public interface IAuthService {

    boolean checkRole(String token, String role);

}
