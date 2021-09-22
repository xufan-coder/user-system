package com.zerody.user.enums;

/**
 * @author  DaBai
 * @date  2021/9/22 10:35
 */

public enum MenuSetTypeEnum {
    APP(1, "APP端菜单"),
    MINI(2, "小程序端");

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

    MenuSetTypeEnum(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static MenuSetTypeEnum getCodeByDesc(String desc){
        for (MenuSetTypeEnum status : MenuSetTypeEnum.values()){
            if(desc.equals(status.getDesc())){
                return status;
            }
        }
        return null;
    }
}
