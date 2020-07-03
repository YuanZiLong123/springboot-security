package com.yzl.springbootsecurity.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author admin
 * @date 2020-07-02 16:33
 */
public class MyException extends AuthenticationException {

    public MyException(String msg, Throwable t) {
        super(msg, t);
    }

    public MyException(String msg) {
        super(msg);
    }
}
