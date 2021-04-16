package com.zerody.user.enums;

/**
 * @author PengQiang
 * @ClassName LoginStatusEnum
 * @DateTime 2020/12/18_16:14
 * @Deacription TODO
 */
public enum CompanyLoginStatusEnum {
    ENABLE(1, "启用"),
    BLOCK_UP(2, "停用");

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

    CompanyLoginStatusEnum(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static CompanyLoginStatusEnum getCodeByDesc(String desc){
        for (CompanyLoginStatusEnum status : CompanyLoginStatusEnum.values()){
            if(desc.equals(status.getDesc())){
                return status;
            }
        }
        return null;
    }
}
