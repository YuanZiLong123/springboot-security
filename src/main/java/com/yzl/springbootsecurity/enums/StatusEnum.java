package com.yzl.springbootsecurity.enums;

/**
 * 状态枚举
 */
public enum StatusEnum {

    EXIST(1,"正常"),DELETE(0,"删除");

    private Integer id;

    private String name;

    StatusEnum(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
