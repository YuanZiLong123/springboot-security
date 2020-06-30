package com.yzl.springbootsecurity.exception;

import lombok.Data;

/**
 * @author admin
 * @date 2020-06-24 16:55
 */
@Data
public class SystemException extends Exception {

    private Integer code;

    private String message;

    public SystemException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
