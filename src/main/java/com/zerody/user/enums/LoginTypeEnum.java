package com.zerody.user.enums;

/**
 * @author  DaBai
 * @date  2021/1/8 16:24
 */

public enum LoginTypeEnum {
    CRM(1, "用户登录"),
    BACK_PC(2, "管理员登录");

    private Integer code;

    private String desc;

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

    LoginTypeEnum(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static LoginTypeEnum getCodeByDesc(String desc){
        for (LoginTypeEnum status : LoginTypeEnum.values()){
            if(desc.equals(status.getDesc())){
                return status;
            }
        }
        return null;
    }
}
