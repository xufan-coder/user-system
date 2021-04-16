package com.zerody.user.enums;

/**
 * @author PengQiang
 * @ClassName UserLoginStatusEnum
 * @DateTime 2020/12/18_16:28
 * @Deacription TODO
 */
public enum UserLoginStatusEnum {
    ENABLE(1, "启用"),
    COMPANY_DEPARTMENT_BLOCK_UP(2, "企业或部门停用");

    private Integer code;

    private String desc;

    UserLoginStatusEnum(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
