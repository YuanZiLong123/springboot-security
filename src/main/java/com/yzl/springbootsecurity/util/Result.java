package com.yzl.springbootsecurity.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description = "响应model")
public class Result<T> implements Serializable {


    private static final long serialVersionUID = -3988374787465769657L;

    /**
    *成功
    */
    public final static Integer RESULT_SUCCESS = 0;
    /**
     * 失败
     */
    public final static Integer RESULT_ERROR = 1;

    /**
     * 登录失效
     */
    public final static Integer RESULT_LOGINOUT = 2;



    /**
     * 权限验证失败
     */
    public final static Integer RESULT_NO_AUTH = 3;


    /**
     * 返回代码
     */
    @ApiModelProperty(name = "code",value = "响应代码 0 成功  1 失败   2 登录失效  3 权限验证失败")
    private Integer code = RESULT_SUCCESS;

    /**
     * 返回提示语
     */
    @ApiModelProperty(name = "message",value = "返回提示语")
    private String message;

    /**
     * 返回数据
     */
    @ApiModelProperty(name = "data",value = "返回数据")
    private T data;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
